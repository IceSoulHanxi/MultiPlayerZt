package com.ixnah.hmcl.mpzt.ui;

import com.ixnah.hmcl.mpzt.MpztPlugin;
import com.zerotier.sockets.ZeroTierNative;
import org.jackhuang.hmcl.util.platform.Architecture;
import org.jackhuang.hmcl.util.platform.OperatingSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.zerotier.sockets.ZeroTierNative.zts_init;

public class MultiplayerManager {

    public static void initialize() {
        String dllSuffix = OperatingSystem.WINDOWS.equals(OperatingSystem.CURRENT_OS) ? ".dll" : ".so";
        String os = OperatingSystem.CURRENT_OS.getCheckedName();
        String arch = Architecture.CURRENT_ARCH.getCheckedName();
        String fileName = "libzt-" + os + "-" + arch + dllSuffix;
        Path dllPath = MpztPlugin.getInstance().getPluginDir().resolve(fileName);
        if (!Files.exists(dllPath) || !Files.isRegularFile(dllPath)) {
            throw new RuntimeException("libzt-" + os + "-" + arch + dllSuffix + " not found");
        }
        try {
            System.load(dllPath.toRealPath().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (zts_init() != ZeroTierNative.ZTS_ERR_OK) {
            throw new ExceptionInInitializerError("JNI init() failed (see GetJavaVM())");
        }
    }

    public static void shutdown() {

    }
}
