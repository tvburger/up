package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.tvburger.up.UpApplication;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpPackage;
import net.tvburger.up.runtime.impl.UpPackageInfoImpl;
import net.tvburger.up.security.Identification;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiApplicationInfo implements UpApplication.Info {

    private String name;
    private UpPackageInfoImpl packageInfo;
    private ApiEnvironmentInfo environmentInfo;
    private ApiIdentification identification;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UpPackage.Info getPackageInfo() {
        return packageInfo;
    }

    @Override
    public UpEnvironment.Info getEnvironmentInfo() {
        return environmentInfo;
    }

    @Override
    public Identification getIdentification() {
        return identification;
    }

}
