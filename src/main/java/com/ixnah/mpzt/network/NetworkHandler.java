package com.ixnah.mpzt.network;

import java.util.regex.Pattern;

public interface NetworkHandler extends AutoCloseable, Runnable {
    public static final Pattern ADDRESS_PATTERN = Pattern.compile("^\\s*(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):(\\d{1,5})\\s*$");

    @Override
    void close();

    boolean isRunning();
}
