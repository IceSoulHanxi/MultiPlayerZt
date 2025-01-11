package com.ixnah.hmcl.mpzt.zerotier;

import java.net.DatagramSocketImpl;
import java.net.DatagramSocketImplFactory;

public class ZeroTierDatagramSocketImplFactory implements DatagramSocketImplFactory {

    public static final DatagramSocketImplFactory INSTANCE = new ZeroTierDatagramSocketImplFactory();

    @Override
    public DatagramSocketImpl createDatagramSocketImpl() {
        return new ZeroTierDatagramSocketImpl();
    }
}
