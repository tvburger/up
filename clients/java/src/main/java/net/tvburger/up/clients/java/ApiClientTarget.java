package net.tvburger.up.clients.java;

import net.tvburger.up.client.UpClientTarget;

public final class ApiClientTarget implements UpClientTarget {

    private final String url;

    public ApiClientTarget(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
