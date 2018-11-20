package net.tvburger.up.runtime.impl;

import net.tvburger.up.UpApplication;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpPackage;
import net.tvburger.up.security.Identification;

import java.util.Objects;

public class UpApplicationInfoImpl implements UpApplication.Info {

    private final String name;
    private final UpPackage.Info packageInfo;
    private final UpEnvironment.Info environmentInfo;
    private final Identification identification;

    public UpApplicationInfoImpl(String name, UpPackage.Info packageInfo, UpEnvironment.Info environmentInfo, Identification identification) {
        this.name = name;
        this.packageInfo = packageInfo;
        this.environmentInfo = environmentInfo;
        this.identification = identification;
    }

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
        return 73 + Objects.hashCode(name) * 31
                + Objects.hashCode(packageInfo) * 341
                + Objects.hashCode(environmentInfo) * 13
                + Objects.hashCode(identification) * 11;
    }
}
