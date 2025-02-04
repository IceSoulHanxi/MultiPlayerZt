package com.ixnah.mpzt.network;

import org.jackhuang.hmcl.util.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalServerDetector implements NetworkHandler {
    private static final Logger logger = LoggerFactory.getLogger(LocalServerDetector.class);

    private final AtomicBoolean running = new AtomicBoolean();
    private CompletableFuture<String> future = new CompletableFuture<>();
    private Thread localServerListener;

    @Override
    public void close() {
        running.set(false);
        localServerListener.interrupt();
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void run() {
        running.set(true);
        future = new CompletableFuture<>();
        localServerListener = Lang.thread(() -> {
            try (MulticastSocket socket = new MulticastSocket(4445)) {
                socket.joinGroup(new InetSocketAddress("224.0.2.60", 0), null);
//                socket.joinGroup(new InetSocketAddress("FF75:230::60", 0), null);
                Pattern portRegex = Pattern.compile("\\[AD](\\d+)\\[/AD]");
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                while (isRunning()) {
                    try {
                        socket.receive(packet);
                    } catch (SocketTimeoutException timeout) {
                        continue;
                    }
                    String dataContent = new String(packet.getData(), 0, packet.getLength());
                    Matcher matcher = portRegex.matcher(dataContent);
                    if (!future.isDone() && matcher.find()) {
                        future.complete(matcher.group(1));
                    }
                }
            } catch (IOException e) {
                logger.error("Failed to listen local server", e);
                future.completeExceptionally(e);
            }
        }, "LocalServerDetector", true);
    }

    public CompletableFuture<String> getFuture() {
        return future;
    }
}
