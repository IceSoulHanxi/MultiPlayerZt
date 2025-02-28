package com.ixnah.zerotier;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public interface ZeroTierLibrary extends Library {
    int zts_bsd_setsockopt(int fd, int level, int optname, Pointer optval, int optlen);

    int zts_bsd_getsockopt(int fd, int level, int optname, Pointer optval, IntByReference optlen);

    int zts_get_last_socket_error(int fd);
}
