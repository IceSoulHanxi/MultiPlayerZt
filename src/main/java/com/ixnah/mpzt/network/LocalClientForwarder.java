package com.ixnah.mpzt.network;

import com.zerotier.sockets.ZeroTierSocket;
import org.jackhuang.hmcl.event.Event;
import org.jackhuang.hmcl.event.EventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ixnah.mpzt.network.ServiceNetworkHandler.forwardTraffic;
import static com.zerotier.sockets.ZeroTierNative.ZTS_AF_INET;
import static com.zerotier.sockets.ZeroTierNative.ZTS_SOCK_STREAM;
import static org.jackhuang.hmcl.util.Lang.wrap;

public class LocalClientForwarder implements ServiceNetworkHandler {
    private static final Logger LOG = LoggerFactory.getLogger(LocalClientForwarder.class);

    private final ThreadGroup threadGroup = new ThreadGroup("RemoteClients");
    private final List<String> remoteAddresses = new CopyOnWriteArrayList<>();
    private final String localAddress;
    private final int port;
    private final Map<ZeroTierSocket, Socket> socketMap = new WeakHashMap<>();

    private final EventManager<Event> onExit = new EventManager<>();
    private final EventManager<Event> onConnected = new EventManager<>();
    private final EventManager<Event> onDisconnected = new EventManager<>();

    private volatile boolean running = true;

    public LocalClientForwarder(String localAddress, int port) {
        this.localAddress = localAddress;
        this.port = port;
    }

    private Thread newThread(Runnable task, String name) {
        Thread thread = new Thread(threadGroup, task, name);
        thread.setDaemon(true);
        return thread;
    }

    @Override
    public void run() {
        Thread forwardPortThread = newThread(this::acceptor, "ForwardPort");
        forwardPortThread.start();
    }

    @Override
    public void close() {
        running = false;
        threadGroup.interrupt();
    }

    @Override
    public List<String> getRemoteAddresses() {
        return null;
    }

    @Override
    public String getLocalAddress() {
        return localAddress;
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

    void acceptor() {
        try (ZeroTierSocket serverSocket = new ZeroTierSocket(ZTS_AF_INET, ZTS_SOCK_STREAM, 0)) {
            serverSocket.bind(localAddress, 0);
            serverSocket.listen(port);
            while (running) {
                ZeroTierSocket accepted = serverSocket.accept();
                InetAddress remoteAddress = accepted.getRemoteAddress();
                String remoteStr = remoteAddress.getHostAddress() + ":" + accepted.getRemotePort();
                LOG.info("Accepting remote client {}", remoteStr);
                try {
                    Socket forwardSocket = new Socket();
                    forwardSocket.setSoTimeout(30000);
                    forwardSocket.connect(new InetSocketAddress("127.0.0.1", port));
                    newThread(() -> {
                        try {
                            forwardTraffic(wrap(accepted::getInputStream), wrap(forwardSocket::getOutputStream));
                        } finally {
                            onDisconnected.fireEvent(new Event(accepted));
                            socketMap.remove(accepted);
                        }
                    }, "Forward [" + remoteStr + "] C->S ").start();
                    newThread(() -> forwardTraffic(wrap(forwardSocket::getInputStream), wrap(accepted::getOutputStream)), "Forward [" + remoteStr + "] S->C ").start();
                    remoteAddresses.add(remoteStr);
                    socketMap.put(accepted, forwardSocket);
                    onConnected.fireEvent(new Event(accepted));
                } catch (IOException e) {
                    LOG.error("Error in forwarding port " + remoteStr, e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            onExit.fireEvent(new Event(this));
        }
    }
}
