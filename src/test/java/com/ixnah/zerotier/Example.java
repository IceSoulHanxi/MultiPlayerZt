package com.ixnah.zerotier;

import com.ixnah.hmcl.mpzt.zerotier.ZeroTier;
import com.ixnah.hmcl.mpzt.zerotier.ZeroTierDatagramSocketImplFactory;
import com.ixnah.hmcl.mpzt.zerotier.ZeroTierLibrary;
import com.sun.jna.Native;
import com.zerotier.sockets.*;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

import static com.zerotier.sockets.ZeroTierNative.zts_init;

public class Example {
    public static void main(String[] args) {
        if (args.length != 4) {
            System.err.println("Invalid arguments");
            System.err.println(" Usage: <server|client> <id_path> <network> <port>");
            System.err.println("  example server ./ 0123456789abcdef 8080");
            System.err.println("  example client ./ 0123456789abcdef 8080\n");
            System.exit(1);
        }

        String storagePath;
        int port;
        String mode = args[0];
        storagePath = args[1];
        BigInteger nwid = new BigInteger(args[2], 16);
        long networkId = nwid.longValue();

        port = Integer.parseInt(args[3]);
        System.out.println("mode        = " + mode);
        System.out.println("networkId   = " + Long.toHexString(networkId));
        System.out.println("storagePath = " + storagePath);
        System.out.println("port        = " + port);

        // ZeroTier setup

        // Loads dynamic library at initialization time
        System.loadLibrary("libzt");
        ZeroTier.libzt = Native.loadLibrary("libzt", ZeroTierLibrary.class);
        if (zts_init() != ZeroTierNative.ZTS_ERR_OK) {
            throw new ExceptionInInitializerError("JNI init() failed (see GetJavaVM())");
        }

        ZeroTierNode node = new ZeroTierNode();
        ZeroTier.node = node;
        node.initFromStorage(storagePath);
        node.initSetEventHandler(new MyZeroTierEventListener());
        // node.initSetPort(9994);
        node.start();

        System.out.println("Waiting for node to come online...");
        while (!node.isOnline()) {
            ZeroTierNative.zts_util_delay(50);
        }
        System.out.println("Node ID: " + Long.toHexString(node.getId()));
        System.out.println("Joining network...");
        node.join(networkId);
        System.out.println("Waiting for network...");
        while (!node.isNetworkTransportReady(networkId)) {
            ZeroTierNative.zts_util_delay(50);
        }
        System.out.println("Joined");
        ZeroTier.networkId = networkId;

        // IPv4

        InetAddress addr4 = node.getIPv4Address(networkId);
        System.out.println("IPv4 address = " + addr4.getHostAddress());

        // IPv6

        InetAddress addr6 = node.getIPv6Address(networkId);
        System.out.println("IPv6 address = " + addr6.getHostAddress());

        // MAC address

        System.out.println("MAC address = " + node.getMACAddress(networkId));

        // Socket logic
        JmDNS jmdns;
        try {
            DatagramSocket.setDatagramSocketImplFactory(ZeroTierDatagramSocketImplFactory.INSTANCE);
            jmdns = JmDNS.create(addr4);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                DatagramSocket.setDatagramSocketImplFactory(null);
            } catch (IOException ignored) {
            }
        }

        if (mode.equals("server")) {
            System.out.println("Starting server...");
            try (JmDNS ignored = jmdns;
                 ZeroTierServerSocket listener = new ZeroTierServerSocket(port);
                 ZeroTierSocket conn = listener.accept();
                 ZeroTierInputStream inputStream = conn.getInputStream();
                 DataInputStream dataInputStream = new DataInputStream(inputStream)) {
                // Register a service
                ServiceInfo serviceInfo = ServiceInfo.create("_http._tcp.local.", "example", port, "path=index.html");
                jmdns.registerService(serviceInfo);
                String message;
                while ((message = dataInputStream.readUTF()) != null && !"exit".equals(message)) {
                    System.out.println("recv: " + message);
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

        if (mode.equals("client")) {
            System.out.println("Starting client...");
            final CompletableFuture<Inet4Address> future = new CompletableFuture<>();
            jmdns.addServiceListener("_http._tcp.local.", new ServiceListener() {
                @Override
                public void serviceAdded(ServiceEvent event) {
                    System.out.println("serviceResolved: " + event.getName() + ", " + Arrays.toString(event.getInfo().getInet4Addresses()));
                }

                @Override
                public void serviceRemoved(ServiceEvent event) {
                    System.out.println("serviceResolved: " + event.getName() + ", " + Arrays.toString(event.getInfo().getInet4Addresses()));
                }

                @Override
                public void serviceResolved(ServiceEvent event) {
                    System.out.println("serviceResolved: " + event.getName() + ", " + Arrays.toString(event.getInfo().getInet4Addresses()));
                    Arrays.stream(event.getInfo().getInet4Addresses()).findFirst().ifPresent(future::complete);
                }
            });
            future.thenAccept(addr -> {
                String remoteAddr = addr.getHostAddress();
                System.out.println("remoteAddr  = " + remoteAddr);
                try (ZeroTierSocket socket = new ZeroTierSocket(remoteAddr, port);
                     ZeroTierOutputStream outputStream = socket.getOutputStream();
                     DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                     Scanner scanner = new Scanner(System.in)
                ) {
                    String message;
                    while ((message = scanner.nextLine()) != null) {
                        dataOutputStream.writeUTF(message);
                        if ("exit".equals(message)) {
                            break;
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            });
        }
    }
}

/**
 * (OPTIONAL) event handler
 */
class MyZeroTierEventListener implements com.zerotier.sockets.ZeroTierEventListener {
    public void onZeroTierEvent(long id, int eventCode) {
        if (eventCode == ZeroTierNative.ZTS_EVENT_NODE_UP) {
            System.out.println("EVENT_NODE_UP");
        }
        if (eventCode == ZeroTierNative.ZTS_EVENT_NODE_ONLINE) {
            System.out.println("EVENT_NODE_ONLINE: " + Long.toHexString(id));
        }
        if (eventCode == ZeroTierNative.ZTS_EVENT_NODE_OFFLINE) {
            System.out.println("EVENT_NODE_OFFLINE");
        }
        if (eventCode == ZeroTierNative.ZTS_EVENT_NODE_DOWN) {
            System.out.println("EVENT_NODE_DOWN");
        }
        if (eventCode == ZeroTierNative.ZTS_EVENT_NETWORK_READY_IP4) {
            System.out.println("ZTS_EVENT_NETWORK_READY_IP4: " + Long.toHexString(id));
        }
        if (eventCode == ZeroTierNative.ZTS_EVENT_NETWORK_READY_IP6) {
            System.out.println("ZTS_EVENT_NETWORK_READY_IP6: " + Long.toHexString(id));
        }
        if (eventCode == ZeroTierNative.ZTS_EVENT_NETWORK_DOWN) {
            System.out.println("EVENT_NETWORK_DOWN: " + Long.toHexString(id));
        }
        if (eventCode == ZeroTierNative.ZTS_EVENT_NETWORK_OK) {
            System.out.println("EVENT_NETWORK_OK: " + Long.toHexString(id));
        }
        if (eventCode == ZeroTierNative.ZTS_EVENT_NETWORK_ACCESS_DENIED) {
            System.out.println("EVENT_NETWORK_ACCESS_DENIED: " + Long.toHexString(id));
        }
        if (eventCode == ZeroTierNative.ZTS_EVENT_NETWORK_NOT_FOUND) {
            System.out.println("EVENT_NETWORK_NOT_FOUND: " + Long.toHexString(id));
        }
        if (eventCode == ZeroTierNative.ZTS_EVENT_PEER_PATH_DISCOVERED) {
            System.out.println("EVENT_PEER_PATH_DISCOVERED: " + Long.toHexString(id));
        }
    }
}
