package com.ixnah.zerotier;

import com.ixnah.mpzt.network.LocalServerDetector;

import java.util.concurrent.ExecutionException;

public class LocalServerListenerTest {
    public static void main(String[] args) {
        try (LocalServerDetector listener = new LocalServerDetector()) {
            listener.run();
            System.out.println(listener.getFuture().get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
