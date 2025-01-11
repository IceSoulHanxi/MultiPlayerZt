package com.ixnah.hmcl.mpzt.zerotier;

import com.sun.jna.Memory;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.zerotier.sockets.ZeroTierNode;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static com.zerotier.sockets.ZeroTierNative.*;

public class ZeroTier {

    public static final int ZTS_IP_ADD_MEMBERSHIP = 3;
    public static final int ZTS_IP_DROP_MEMBERSHIP = 4;
    public static final int ZTS_IP_MULTICAST_IF = 6;
    public static final int ZTS_IP_MULTICAST_LOOP = 7;
    public static ZeroTierLibrary libzt;
    public static ZeroTierNode node;

    public static void setBroadcast(int fd, boolean on) {
        IntByReference optValue = new IntByReference(on ? 1 : 0);
        libzt.zts_bsd_setsockopt(fd, ZTS_SOL_SOCKET , ZTS_SO_BROADCAST, optValue.getPointer(), 4);
    }

    public static boolean getSocketOption(int fd) {
        IntByReference optValue = new IntByReference();
        IntByReference optLenAddr = new IntByReference();
        libzt.zts_bsd_getsockopt(fd, ZTS_SOL_SOCKET , ZTS_SO_BROADCAST, optValue.getPointer(), optLenAddr);
        return optValue.getValue() == 1;
    }

    public static void setTrafficClass(int fd, int tc) {
        IntByReference optValue = new IntByReference(tc);
        libzt.zts_bsd_setsockopt(fd, ZTS_IPPROTO_IP , ZTS_IP_TOS, optValue.getPointer(), 4);
    }

    public static int getTrafficClass(int fd) {
        IntByReference optValue = new IntByReference();
        IntByReference optLenAddr = new IntByReference();
        libzt.zts_bsd_getsockopt(fd, ZTS_IPPROTO_IP , ZTS_IP_TOS, optValue.getPointer(), optLenAddr);
        return optValue.getValue();
    }

    public static void setLoopbackMode(int fd, boolean disable) {
        IntByReference optValue = new IntByReference(disable ? 1 : 0);
        libzt.zts_bsd_setsockopt(fd, ZTS_IPPROTO_IP , ZTS_IP_MULTICAST_LOOP, optValue.getPointer(), 4);
    }

    public static boolean getLoopbackMode(int fd) {
        IntByReference optValue = new IntByReference();
        IntByReference optLenAddr = new IntByReference();
        libzt.zts_bsd_getsockopt(fd, ZTS_IPPROTO_IP , ZTS_IP_MULTICAST_LOOP, optValue.getPointer(), optLenAddr);
        return optValue.getValue() == 1;
    }

    public static void setInterface(int fd, InetAddress inf) throws SocketException {
        if (!(inf instanceof Inet4Address)) throw new SocketException("Only IPv4 addresses are supported");
        IntByReference memory = new IntByReference(bytesToInt(inf.getAddress()));
        libzt.zts_bsd_setsockopt(fd, ZTS_IPPROTO_IP , ZTS_IP_MULTICAST_IF, memory.getPointer(), 4);
    }

    public static InetAddress getInterface(int fd) throws SocketException {
        PointerByReference optValue = new PointerByReference();
        IntByReference optLenAddr = new IntByReference();
        libzt.zts_bsd_getsockopt(fd, ZTS_IPPROTO_IP , ZTS_IP_MULTICAST_IF, optValue.getPointer(), optLenAddr);
        if (optLenAddr.getValue() != 4) {
            throw new SocketException("Unsupported length for IP_MULTICAST_IF " + optLenAddr.getValue());
        }
        try {
            return Inet4Address.getByAddress(intToBytes(optValue.getValue().getInt(0)));
        } catch (UnknownHostException e) {
            throw new SocketException(e);
        }
    }

    public static void addMembership(int fd, InetAddress group) throws IOException {
        if (!(group instanceof Inet4Address)) throw new SocketException("Only IPv4 addresses are supported");
        if (ZeroTier.node == null) throw new SocketException("ZeroTier node not set");
        InetAddress listenAddress = ZeroTier.node.getIPv4Address(ZeroTier.node.getId());
        Memory ipMreq = new Memory(8);
        ipMreq.setInt(0, bytesToInt(group.getAddress()));
        ipMreq.setInt(4, bytesToInt(listenAddress.getAddress()));
        libzt.zts_bsd_setsockopt(fd, ZTS_IPPROTO_IP , ZTS_IP_ADD_MEMBERSHIP, ipMreq, 8);
    }

    public static void dropMembership(int fd, InetAddress group) throws IOException {
        if (!(group instanceof Inet4Address)) throw new SocketException("Only IPv4 addresses are supported");
        if (ZeroTier.node == null) throw new SocketException("ZeroTier node not set");
        InetAddress listenAddress = ZeroTier.node.getIPv4Address(ZeroTier.node.getId());
        Memory ipMreq = new Memory(8);
        ipMreq.setInt(0, bytesToInt(group.getAddress()));
        ipMreq.setInt(4, bytesToInt(listenAddress.getAddress()));
        libzt.zts_bsd_setsockopt(fd, ZTS_IPPROTO_IP , ZTS_IP_DROP_MEMBERSHIP, ipMreq, 8);
    }

    public static int bytesToInt(byte[] bytes) {
        int addr = bytes[3] & 0xFF;
        addr |= ((bytes[2] << 8) & 0xFF00);
        addr |= ((bytes[1] << 16) & 0xFF0000);
        addr |= ((bytes[0] << 24) & 0xFF000000);
        return addr;
    }

    public static byte[] intToBytes(int ipInt) {
        byte[] ipAddr = new byte[4];
        ipAddr[0] = (byte) ((ipInt >>> 24) & 0xFF);
        ipAddr[1] = (byte) ((ipInt >>> 16) & 0xFF);
        ipAddr[2] = (byte) ((ipInt >>> 8) & 0xFF);
        ipAddr[3] = (byte) (ipInt & 0xFF);
        return ipAddr;
    }
}
