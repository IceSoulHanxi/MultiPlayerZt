package com.ixnah.zerotier;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class MdnsExample {
    public static void main(String[] args) {
        try (JmDNS jmDNS = JmDNS.create(InetAddress.getByName("192.168.196.61"), "example", TimeUnit.SECONDS.toMillis(1))) {
            jmDNS.registerService(ServiceInfo.create("_http._tcp.local.", "example", 8086, "path=index.html"));
            LockSupport.parkNanos(TimeUnit.MINUTES.toNanos(10));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
