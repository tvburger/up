package net.tvburger.up.logger;

import java.io.Serializable;

public class InfraTraceElement implements Serializable {

    private final String hostName;
    private final String threadName;

    public InfraTraceElement(String hostName, String threadName) {
        this.hostName = hostName;
        this.threadName = threadName;
    }

    public String getHostName() {
        return hostName;
    }

    public String getThreadName() {
        return threadName;
    }
}
