package com.ixnah.hmcl.mpzt.zerotier;

import com.zerotier.sockets.ZeroTierNative;
import com.zerotier.sockets.ZeroTierSocket;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.net.*;
import java.util.Objects;

public class ZeroTierDatagramSocketImpl extends DatagramSocketImpl {

    ZeroTierSocket _socket;

    @Override
    protected void create() throws SocketException {
        try {
            _socket = new ZeroTierSocket(ZeroTierNative.ZTS_AF_INET, ZeroTierNative.ZTS_SOCK_DGRAM, 0);
        } catch (IOException e) {
            throw toSocketException(e);
        }
    }

    @Override
    protected void bind(int lport, InetAddress laddr) throws SocketException {
        try {
            _socket.bind(laddr, lport);
        } catch (IOException e) {
            throw toSocketException(e);
        }
    }

    @Override
    protected void send(DatagramPacket packet) throws IOException {
        int bytesWritten = ZeroTierNative.zts_bsd_write_offset(
                _socket.getNativeFileDescriptor(),
                packet.getData(),
                0,
                packet.getLength());
        if (bytesWritten < 0) {
            throw new IOException("send(DatagramPacket), errno=" + bytesWritten);
        }
    }

    @Override
    protected int peek(InetAddress i) {
        throw new UnsupportedOperationException("peek(InetAddress) not supported");
    }

    @Override
    protected int peekData(DatagramPacket p) {
        throw new UnsupportedOperationException("peekData(DatagramPacket) not supported");
    }

    @Override
    protected void receive(DatagramPacket packet) throws IOException {
        int bytesRead = ZeroTierNative.zts_bsd_read_offset(
                _socket.getNativeFileDescriptor(),
                packet.getData(),
                0,
                packet.getLength());
        if ((bytesRead <= 0) | (bytesRead == -104) /* EINTR, from SO_RCVTIMEO */) {
            throw new IOException("read(DatagramPacket), errno=" + bytesRead);
        }
    }

    @Override
    protected void setTTL(byte ttl) {
        setTimeToLive(ttl);
    }

    @Override
    protected byte getTTL() {
        return (byte) getTimeToLive();
    }

    @Override
    protected void setTimeToLive(int ttl) {
        ZeroTierNative.zts_set_ttl(_socket.getNativeFileDescriptor(), ttl);
    }

    @Override
    protected int getTimeToLive() {
        return ZeroTierNative.zts_get_ttl(_socket.getNativeFileDescriptor());
    }

    @Override
    protected void join(InetAddress group) throws IOException {
        Objects.requireNonNull(group);
        try {
            joinGroup(new InetSocketAddress(group, 0), null);
        } catch (IllegalArgumentException iae) {
            // 1-arg joinGroup does not specify IllegalArgumentException
            throw new SocketException("joinGroup failed", iae);
        }
    }

    @Override
    protected void leave(InetAddress group) throws IOException {
        Objects.requireNonNull(group);
        try {
            leaveGroup(new InetSocketAddress(group, 0), null);
        } catch (IllegalArgumentException iae) {
            // 1-arg leaveGroup does not specify IllegalArgumentException
            throw new SocketException("leaveGroup failed", iae);
        }
    }

    @Override
    protected void joinGroup(SocketAddress mcastaddr, NetworkInterface netIf) throws IOException {
        InetAddress group = checkGroup(mcastaddr);
        if (_socket.isClosed()) throw new SocketException("Socket is closed");
        ZeroTier.addMembership(_socket.getNativeFileDescriptor(), group);
    }

    @Override
    protected void leaveGroup(SocketAddress mcastaddr, NetworkInterface netIf) throws IOException {
        InetAddress group = checkGroup(mcastaddr);
        if (_socket.isClosed()) throw new SocketException("Socket is closed");
        ZeroTier.dropMembership(_socket.getNativeFileDescriptor(), group);
    }

    @Override
    protected void close() {
        try {
            _socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setOption(int optID, Object value) throws SocketException {
        switch (optID) {
            case SO_REUSEADDR:
                if (value instanceof Boolean) {
                    _socket.setReuseAddress((Boolean) value);
                } else if (value instanceof Number
                        && ((Number) value).intValue() >= 0 && ((Number) value).intValue() <= 1) {
                    _socket.setReuseAddress(((Number) value).intValue() == 1);
                }
                break;
            case SO_BROADCAST:
                if (value instanceof Boolean) {
                    ZeroTier.setBroadcast(_socket.getNativeFileDescriptor(), (Boolean) value);
                } else if (value instanceof Number
                        && ((Number) value).intValue() >= 0 && ((Number) value).intValue() <= 1) {
                    ZeroTier.setBroadcast(_socket.getNativeFileDescriptor(), ((Number) value).intValue() == 1);
                }
                break;
            case IP_MULTICAST_IF:
            case IP_MULTICAST_IF2:
                InetAddress address = null;
                if (value instanceof NetworkInterface) {
                    if (ZeroTier.node == null) throw new SocketException("ZeroTier node not set");
                    address = ZeroTier.node.getIPv4Address(ZeroTier.node.getId());
                } else if (value instanceof InetAddress) {
                    address = (InetAddress) value;
                }
                if (address == null) throw new SocketException("Unsupported value for IP_MULTICAST_IF: " + value);
                ZeroTier.setInterface(_socket.getNativeFileDescriptor(), address);
                break;
            case IP_MULTICAST_LOOP:
                if (value instanceof Boolean) {
                    ZeroTier.setLoopbackMode(_socket.getNativeFileDescriptor(), (Boolean) value);
                } else if (value instanceof Number
                        && ((Number) value).intValue() >= 0 && ((Number) value).intValue() <= 1) {
                    ZeroTier.setLoopbackMode(_socket.getNativeFileDescriptor(), ((Number) value).intValue() == 1);
                }
                break;
            case IP_TOS:
                if (value instanceof Number)
                    ZeroTier.setTrafficClass(_socket.getNativeFileDescriptor(), ((Number) value).intValue());
                break;
            case SO_TIMEOUT:
                if (value instanceof Number)
                    _socket.setSoTimeout(((Number) value).intValue());
                break;
            case SO_SNDBUF:
                if (value instanceof Number)
                    _socket.setSendBufferSize(((Number) value).intValue());
                break;
            case SO_RCVBUF:
                if (value instanceof Number)
                    _socket.setReceiveBufferSize(((Number) value).intValue());
                break;
            default:
                throw new SocketException("Unsupported option " + optID + " with value: " + value);
        }
    }

    @Override
    public Object getOption(int optID) throws SocketException {
        switch (optID) {
            case SO_BINDADDR:
                InetAddress localAddress = _socket.getLocalAddress();
                if (localAddress == null) throw new SocketException("Local address not bound");
                return localAddress;
            case SO_REUSEADDR:
                return _socket.getReuseAddress();
            case SO_BROADCAST:
                return ZeroTier.getSocketOption(_socket.getNativeFileDescriptor());
            case IP_MULTICAST_IF:
                return ZeroTier.getInterface(_socket.getNativeFileDescriptor());
            case IP_MULTICAST_IF2:
                try {
                    return getDefaultNetworkInterface.invoke();
                } catch (Throwable e) {
                    throw toSocketException(e);
                }
            case IP_MULTICAST_LOOP:
                return ZeroTier.getLoopbackMode(_socket.getNativeFileDescriptor());
            case IP_TOS:
                return ZeroTier.getTrafficClass(_socket.getNativeFileDescriptor());
            case SO_TIMEOUT:
                return _socket.getSoTimeout();
            case SO_SNDBUF:
                return _socket.getSendBufferSize();
            case SO_RCVBUF:
                return _socket.getReceiveBufferSize();
            default:
                throw new SocketException("Unsupported option: " + optID);
        }
    }

    private static final MethodHandle getDefaultNetworkInterface;

    static {
        try {
            getDefaultNetworkInterface = MethodHandles.lookup()
                    .unreflect(NetworkInterface.class.getDeclaredMethod("getDefault"));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static InetAddress checkGroup(SocketAddress mcastaddr) throws SocketException {
        if (!(mcastaddr instanceof InetSocketAddress)) throw new IllegalArgumentException("Unsupported address type");
        InetAddress group = ((InetSocketAddress) mcastaddr).getAddress();
        if (group == null) throw new IllegalArgumentException("Unresolved address");
        if (!group.isMulticastAddress()) throw new SocketException("Not a multicast address");
        return group;
    }

    public static SocketException toSocketException(Throwable e) {
        if (e instanceof SocketException)
            return (SocketException) e;
        Throwable cause = e.getCause();
        if (cause instanceof SocketException)
            return (SocketException) cause;
        return new SocketException(e.getMessage(), e);
    }
}
