package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.tvburger.up.UpApplication;
import net.tvburger.up.security.Identification;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiApplicationInfo implements UpApplication.Info {

    public static ApiApplicationInfo fromUp(UpApplication.Info up) {
        ApiApplicationInfo api = new ApiApplicationInfo();
        api.name = up.getName();
        api.packageInfo = ApiPackageInfo.fromUp(up.getPackageInfo());
        api.environmentInfo = ApiEnvironmentInfo.fromUp(up.getEnvironmentInfo());
        api.identification = ApiIdentification.fromUp(up.getIdentification());
        return api;
    }

    public UpApplication.Info toUp() {
        return this;
    }

    private String name;
    private ApiPackageInfo packageInfo;
    private ApiEnvironmentInfo environmentInfo;
    private ApiIdentification identification;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ApiPackageInfo getPackageInfo() {
        return packageInfo;
    }

    @Override
    public ApiEnvironmentInfo getEnvironmentInfo() {
        return environmentInfo;
    }

    @Override
    public Identification getIdentification() {
        return identification;
    }

    @Override
    public String toString() {
        return String.format("UpApplication.Info{%s, %s, %s, %s}",
                name,
                packageInfo,
                environmentInfo,
                identification);
    }

    @Override
    public boolean equals(Object object) {
        return object == this
                || null != object &&
                (object instanceof UpApplication.Info
                        && Objects.equals(getName(), ((UpApplication.Info) object).getName())
                        && Objects.equals(getPackageInfo(), ((UpApplication.Info) object).getPackageInfo())
                        && Objects.equals(getEnvironmentInfo(), ((UpApplication.Info) object).getEnvironmentInfo())
                        && Objects.equals(getIdentification(), ((UpApplication.Info) object).getIdentification()));
    }

    @Override
    public int hashCode() {
        return 173 + Objects.hashCode(name) * 31
                + Objects.hashCode(packageInfo) * 341
                + Objects.hashCode(environmentInfo) * 13
                + Objects.hashCode(identification) * 11;
    }
}
