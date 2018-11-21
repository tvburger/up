package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpRuntimeInfo;
import net.tvburger.up.security.Identification;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiEnvironmentInfo implements UpEnvironment.Info {

    public static ApiEnvironmentInfo fromUp(UpEnvironment.Info up) {
        ApiEnvironmentInfo api = new ApiEnvironmentInfo();
        api.name = up.getName();
        api.runtimeInfo = ApiRuntimeInfo.fromUp(up.getRuntimeInfo());
        api.identification = ApiIdentification.fromUp(up.getIdentification());
        return api;
    }

    public UpEnvironment.Info toUp() {
        return this;
    }

    private String name;
    private ApiRuntimeInfo runtimeInfo;
    private ApiIdentification identification;

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
        return String.format("ApiEnvironmentInfo{%s, %s, %s}", name, runtimeInfo, identification);
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
