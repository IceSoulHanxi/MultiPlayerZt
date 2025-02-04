package com.ixnah.mpzt.network;

import org.jackhuang.hmcl.event.Event;
import org.jackhuang.hmcl.event.EventManager;
import org.jackhuang.hmcl.util.logging.Level;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.function.Supplier;

import static org.jackhuang.hmcl.util.logging.Logger.LOG;

public interface ServiceNetworkHandler extends NetworkHandler {

    List<String> getRemoteAddresses();

    String getLocalAddress();

    EventManager<Event> onExit();

    EventManager<Event> onConnected();

    EventManager<Event> onDisconnected();

    static void forwardTraffic(Supplier<InputStream> src, Supplier<OutputStream> dest) {
        try (InputStream is = src.get(); OutputStream os = dest.get()) {
            byte[] buf = new byte[1024];
            while (true) {
                int len = is.read(buf, 0, buf.length);
                if (len < 0) break;
                LOG.log(Level.INFO, "Forwarding buffer " + len);
                os.write(buf, 0, len);
            }
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Disconnected", e);
        }
    }
}
