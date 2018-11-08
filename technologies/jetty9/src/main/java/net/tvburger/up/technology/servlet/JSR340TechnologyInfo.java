package net.tvburger.up.technology.servlet;

import net.tvburger.up.EndpointTechnologyInfo;

public final class JSR340TechnologyInfo implements EndpointTechnologyInfo<JSR340Manager> {

    private static final JSR340TechnologyInfo info = new JSR340TechnologyInfo();

    public static JSR340TechnologyInfo get() {
        return info;
    }

    private JSR340TechnologyInfo() {
    }

    @Override
    public Class<JSR340Manager> getEndpointManagerType() {
        return JSR340Manager.class;
    }

    @Override
    public String getSpecificationName() {
        return JSR340Specification.get().getSpecificationName();
    }

    @Override
    public String getSpecificationVersion() {
        return JSR340Specification.get().getSpecificationVersion();
    }

}
