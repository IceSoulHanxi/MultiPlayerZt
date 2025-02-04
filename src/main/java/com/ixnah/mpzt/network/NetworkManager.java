package com.ixnah.mpzt.network;

import com.zerotier.sockets.ZeroTierEventListener;
import com.zerotier.sockets.ZeroTierNative;
import com.zerotier.sockets.ZeroTierNode;
import org.jackhuang.hmcl.util.platform.Architecture;
import org.jackhuang.hmcl.util.platform.OperatingSystem;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static com.zerotier.sockets.ZeroTierNative.*;
import static org.jackhuang.hmcl.util.Lang.wrap;

public class NetworkManager {

    private static Path initDir;

    public static void initialize(Path initDir) {
        String dllSuffix = OperatingSystem.WINDOWS.equals(OperatingSystem.CURRENT_OS) ? ".dll" : ".so";
        String os = OperatingSystem.CURRENT_OS.getCheckedName();
        String arch = Architecture.CURRENT_ARCH.getCheckedName();
        String fileName = "libzt-" + os + "-" + arch + dllSuffix;
        Path dllPath = initDir.resolve(fileName);
        try {
            System.load(dllPath.toRealPath().toString());
        } catch (IOException e) {
            throw new RuntimeException("libzt-" + os + "-" + arch + dllSuffix + " init fail", e);
        }
        if (zts_init() != ZeroTierNative.ZTS_ERR_OK) {
            throw new ExceptionInInitializerError("JNI init() failed (see GetJavaVM())");
        }
        NetworkManager.initDir = initDir;
    }

    public static CompletableFuture<ZeroTierNode> startNode(String networkId) {
        return CompletableFuture.supplyAsync(wrap(() -> {
            ZeroTierNode node = new ZeroTierNode();
            node.initFromStorage(initDir.toString());
//            node.initSetEventHandler()
            node.start();
            long maxWaitTime = TimeUnit.MINUTES.toNanos(1);
            long delayTime = TimeUnit.MILLISECONDS.toNanos(50);
            for (int i = (int) (maxWaitTime / delayTime); i > 0; i--) {
                if (node.isOnline()) break;
                LockSupport.parkNanos(delayTime);
            }
            if (!node.isOnline()) {
                node.stop();
                throw new ExceptionInInitializerError("Node start timeout!");
            }
            long parsedId = Long.parseUnsignedLong(networkId, 16);
            int statusCode = node.join(parsedId);
            if (statusCode != ZTS_ERR_OK) {
                throw new ExceptionInInitializerError("Node join network fail: " + statusCode);
            }
            for (int i = (int) (maxWaitTime / delayTime); i > 0; i--) {
                if (node.isNetworkTransportReady(parsedId)) break;
                LockSupport.parkNanos(delayTime);
            }
            if (!node.isNetworkTransportReady(parsedId)) {
                node.leave(parsedId);
                node.stop();
                throw new ExceptionInInitializerError("Node setup network transport timeout!");
            }
            final int ASSIGNED = 1;
            for (int i = (int) (maxWaitTime / delayTime); i > 0; i--) {
                statusCode = zts_addr_is_assigned(parsedId, ZTS_AF_INET);
                if (statusCode == ASSIGNED) break;
                LockSupport.parkNanos(delayTime);
            }
            if (statusCode != ASSIGNED) {
                node.leave(parsedId);
                node.stop();
                throw new ExceptionInInitializerError("Node assign ip timeout!");
            }
            return node;
        }));
    }

    private static final ZeroTierEventListener multiplayerZtEventListener = (id, eventCode) -> {
        switch (eventCode) {
            case ZeroTierNative.ZTS_EVENT_NODE_UP: {

            }
        }
    };
}
