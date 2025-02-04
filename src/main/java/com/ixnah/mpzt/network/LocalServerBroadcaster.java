/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2022  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.ixnah.mpzt.network;

import com.zerotier.sockets.ZeroTierSocket;
import org.jackhuang.hmcl.event.Event;
import org.jackhuang.hmcl.event.EventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.nio.channels.UnresolvedAddressException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

import static com.ixnah.mpzt.network.ServiceNetworkHandler.forwardTraffic;
import static com.zerotier.sockets.ZeroTierNative.ZTS_AF_INET;
import static com.zerotier.sockets.ZeroTierNative.ZTS_SOCK_STREAM;
import static org.jackhuang.hmcl.util.Lang.parseInt;
import static org.jackhuang.hmcl.util.Lang.wrap;
import static org.jackhuang.hmcl.util.i18n.I18n.i18n;

public class LocalServerBroadcaster implements ServiceNetworkHandler {
    private static final Logger LOG = LoggerFactory.getLogger(LocalServerBroadcaster.class);

    private final String remoteAddress;
    private final ThreadGroup threadGroup = new ThreadGroup("JoinSession");

    private final EventManager<Event> onExit = new EventManager<>();
    private final EventManager<Event> onConnected = new EventManager<>();
    private final EventManager<Event> onDisconnected = new EventManager<>();

    private boolean running = true;

    public LocalServerBroadcaster(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    private Thread newThread(Runnable task, String name) {
        Thread thread = new Thread(threadGroup, task, name);
        thread.setDaemon(true);
        return thread;
    }

    @Override
    public void close() {
        running = false;
        threadGroup.interrupt();
    }

    @Override
    public List<String> getRemoteAddresses() {
        return Collections.singletonList(remoteAddress);
    }

    @Override
    public String getLocalAddress() {
        return null;
    }

    @Override
    public EventManager<Event> onExit() {
        return onExit;
    }

    @Override
    public EventManager<Event> onConnected() {
        return onConnected;
    }

    @Override
    public EventManager<Event> onDisconnected() {
        return onDisconnected;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        Thread forwardPortThread = newThread(this::forwardPort, "ForwardPort");
        forwardPortThread.start();
    }

    private void forwardPort() {
        try {
            Matcher matcher = ADDRESS_PATTERN.matcher(remoteAddress);
            if (!matcher.find()) {
                throw new MalformedURLException();
            }
            try (ZeroTierSocket forwardingSocket = new ZeroTierSocket(ZTS_AF_INET, ZTS_SOCK_STREAM, 0);
                 ServerSocket serverSocket = new ServerSocket()) {
                forwardingSocket.setSoTimeout(30000);
                forwardingSocket.connect(new InetSocketAddress(matcher.group(1), parseInt(matcher.group(2), 0)));

                serverSocket.bind(null);

                Thread broadcastMOTDThread = newThread(() -> broadcastMOTD(serverSocket.getLocalPort()), "BroadcastMOTD");
                broadcastMOTDThread.start();

                LOG.info("Listening " + serverSocket.getLocalSocketAddress());

                while (running) {
                    Socket forwardedSocket = serverSocket.accept();
                    LOG.info("Accepting client");
                    newThread(() -> forwardTraffic(wrap(forwardingSocket::getInputStream), wrap(forwardedSocket::getOutputStream)), "Forward S->D").start();
                    newThread(() -> forwardTraffic(wrap(forwardedSocket::getInputStream), wrap(forwardingSocket::getOutputStream)), "Forward D->S").start();
                }
            }
        } catch (IOException | UnresolvedAddressException e) {
            LOG.warn("Error in forwarding port", e);
        } finally {
            close();
            onExit.fireEvent(new Event(this));
        }
    }

    private void broadcastMOTD(int port) {
        DatagramSocket socket;
        InetAddress broadcastAddress;
        try {
            socket = new DatagramSocket();
            broadcastAddress = InetAddress.getByName("224.0.2.60");
        } catch (IOException e) {
            LOG.warn("Failed to create datagram socket", e);
            return;
        }

        while (running) {
            try {
                byte[] data = String.format("[MOTD]%s[/MOTD][AD]%d[/AD]", i18n("multiplayer.session.name.motd"), port).getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket(data, 0, data.length, broadcastAddress, 4445);
                socket.send(packet);
                LOG.trace("Broadcast server 0.0.0.0:" + port);
            } catch (IOException e) {
                LOG.warn("Failed to send motd packet", e);
            }

            try {
                Thread.sleep(1500);
            } catch (InterruptedException ignored) {
                return;
            }
        }

        socket.close();
    }
}
