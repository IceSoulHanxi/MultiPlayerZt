package com.ixnah.zerotier;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.zerotier.sockets.ZeroTierNode;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static com.zerotier.sockets.ZeroTierNative.*;

public class ZeroTierUtil {

    public static final int ZTS_IP_ADD_MEMBERSHIP = 3;
    public static final int ZTS_IP_DROP_MEMBERSHIP = 4;
    public static final int ZTS_IP_MULTICAST_IF = 6;
    public static final int ZTS_IP_MULTICAST_LOOP = 7;
    public static ZeroTierLibrary libzt;
    public static ZeroTierNode node;
    public static Long networkId;

    public static void setBroadcast(int fd, boolean on) {
        IntByReference optValue = new IntByReference(on ? 1 : 0);
        int i = libzt.zts_bsd_setsockopt(fd, ZTS_SOL_SOCKET, ZTS_SO_BROADCAST, optValue.getPointer(), 4);
        System.out.println(i);
    }

    public static boolean getSocketOption(int fd) {
        IntByReference optValue = new IntByReference();
        IntByReference optLenAddr = new IntByReference();
        libzt.zts_bsd_getsockopt(fd, ZTS_SOL_SOCKET , ZTS_SO_BROADCAST, optValue.getPointer(), optLenAddr);
        return optValue.getValue() == 1;
    }

    public static void setTrafficClass(int fd, int tc) {
        IntByReference optValue = new IntByReference(tc);
        int i = libzt.zts_bsd_setsockopt(fd, ZTS_IPPROTO_IP, ZTS_IP_TOS, optValue.getPointer(), 4);
        System.out.println(i);
    }

    public static int getTrafficClass(int fd) {
        IntByReference optValue = new IntByReference();
        IntByReference optLenAddr = new IntByReference();
        libzt.zts_bsd_getsockopt(fd, ZTS_IPPROTO_IP , ZTS_IP_TOS, optValue.getPointer(), optLenAddr);
        return optValue.getValue();
    }

    public static void setLoopbackMode(int fd, boolean disable) {
        IntByReference optValue = new IntByReference(disable ? 1 : 0);
        int i = libzt.zts_bsd_setsockopt(fd, ZTS_IPPROTO_IP, ZTS_IP_MULTICAST_LOOP, optValue.getPointer(), 4);
        System.out.println(i);
    }

    public static boolean getLoopbackMode(int fd) {
        IntByReference optValue = new IntByReference();
        IntByReference optLenAddr = new IntByReference();
        libzt.zts_bsd_getsockopt(fd, ZTS_IPPROTO_IP , ZTS_IP_MULTICAST_LOOP, optValue.getPointer(), optLenAddr);
        return optValue.getValue() == 1;
    }

    public static void setInterface(int fd, InetAddress inf) throws SocketException {
        if (!(inf instanceof Inet4Address)) throw new SocketException("Only IPv4 addresses are supported");
        Memory memory = new Memory(4);
        byte[] address = inf.getAddress();
        for (int i = 0; i < address.length; i++) {
            memory.setByte(i, address[i]);
        }
        int i = libzt.zts_bsd_setsockopt(fd, ZTS_IPPROTO_IP, ZTS_IP_MULTICAST_IF, memory, 4);
        System.out.println(i);
    }

    public static InetAddress getInterface(int fd) throws SocketException {
        PointerByReference optValue = new PointerByReference();
        IntByReference optLenAddr = new IntByReference();
        libzt.zts_bsd_getsockopt(fd, ZTS_IPPROTO_IP , ZTS_IP_MULTICAST_IF, optValue.getPointer(), optLenAddr);
        if (optLenAddr.getValue() != 4) {
            throw new SocketException("Unsupported length for IP_MULTICAST_IF " + optLenAddr.getValue());
        }
        try {
            byte[] bytes = new byte[4];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = optValue.getValue().getByte(i);
            }
            return Inet4Address.getByAddress(bytes);
        } catch (UnknownHostException e) {
            SocketException exception = new SocketException(e.getMessage());
            exception.addSuppressed(e);
            throw exception;
        }
    }

    public static void addMembership(int fd, InetAddress group) throws IOException {
        if (!(group instanceof Inet4Address)) throw new SocketException("Only IPv4 addresses are supported");
        if (ZeroTierUtil.node == null) throw new SocketException("ZeroTier node not set");
        InetAddress listenAddress = ZeroTierUtil.node.getIPv4Address(ZeroTierUtil.networkId);
        Memory memory = new Memory(8);
        byte[] groupAddress = group.getAddress();
        for (int i = 0; i < groupAddress.length; i++) {
            memory.setByte(i, groupAddress[i]);
        }
        byte[] listenAddressBytes = listenAddress.getAddress();
        for (int i = 0; i < listenAddressBytes.length; i++) {
            memory.setByte(i + 4, listenAddressBytes[i]);
        }
        int i = libzt.zts_bsd_setsockopt(fd, ZTS_IPPROTO_IP, ZTS_IP_ADD_MEMBERSHIP, memory, 8);
        System.out.println(i);
        if (i == -1) {
            System.out.println(Native.getLastError());
        }
        i = libzt.zts_get_last_socket_error(fd);
        System.out.println(i);
    }

    public static void dropMembership(int fd, InetAddress group) throws IOException {
        if (!(group instanceof Inet4Address)) throw new SocketException("Only IPv4 addresses are supported");
        if (ZeroTierUtil.node == null) throw new SocketException("ZeroTier node not set");
        InetAddress listenAddress = ZeroTierUtil.node.getIPv4Address(ZeroTierUtil.networkId);
        Memory memory = new Memory(8);
        byte[] groupAddress = group.getAddress();
        for (int i = 0; i < groupAddress.length; i++) {
            memory.setByte(i, groupAddress[i]);
        }
        byte[] listenAddressBytes = listenAddress.getAddress();
        for (int i = 0; i < listenAddressBytes.length; i++) {
            memory.setByte(i + 4, listenAddressBytes[i]);
        }
        int i = libzt.zts_bsd_setsockopt(fd, ZTS_IPPROTO_IP, ZTS_IP_DROP_MEMBERSHIP, memory, 8);
        System.out.println(i);
        if (i == -1) {
            System.out.println(Native.getLastError());
        }
        i = libzt.zts_get_last_socket_error(fd);
        System.out.println(i);
    }
}
