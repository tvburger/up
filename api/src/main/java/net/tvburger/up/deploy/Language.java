package net.tvburger.up.deploy;

import java.io.Serializable;

public final class Language implements Serializable {

    private final String name;
    private final String version;

    public Language(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

}
