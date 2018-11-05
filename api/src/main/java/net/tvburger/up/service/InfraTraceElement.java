package net.tvburger.up.service;

import java.io.Serializable;
import java.net.InetAddress;

public class InfraTraceElement implements Serializable {

    private final InetAddress hostName;
    private final String threadName;

    public InfraTraceElement(InetAddress hostName, String threadName) {
        this.hostName = hostName;
        this.threadName = threadName;
    }

    public InetAddress getHostName() {
        return hostName;
    }

    public String getThreadName() {
        return threadName;
    }

}
