package net.tvburger.up.clients.java.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpRuntimeInfo;
import net.tvburger.up.security.Identification;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ClientEnvironmentInfo implements UpEnvironment.Info {

    private String name;
    private ClientRuntimeInfo runtimeInfo;
    private ClientIdentification identification;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UpRuntimeInfo getRuntimeInfo() {
        return runtimeInfo;
    }

    @Override
    public Identification getIdentification() {
        return identification;
    }

    @Override
    public String toString() {
        return String.format("ClientEnvironmentInfo{%s, %s, %s}", name, runtimeInfo, identification);
    }

    @Override
    public boolean equals(Object object) {
        return this == object
                || null != object &&
                (object instanceof UpEnvironment.Info
                        && Objects.equals(getName(), ((UpEnvironment.Info) object).getName())
                        && Objects.equals(getRuntimeInfo(), ((UpEnvironment.Info) object).getRuntimeInfo())
                        && Objects.equals(getIdentification(), ((UpEnvironment.Info) object).getIdentification()));
    }

    @Override
    public int hashCode() {
        return 53 + Objects.hashCode(name) * 31
                + Objects.hashCode(runtimeInfo) * 91
                + Objects.hashCode(identification) * 47;
    }

}
