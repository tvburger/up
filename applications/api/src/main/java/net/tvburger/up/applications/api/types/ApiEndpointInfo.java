package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEndpointTechnologyInfo;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.security.Identification;

import java.net.URI;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiEndpointInfo implements UpEndpoint.Info {

    private URI endpointUri;
    private ApiEndpointTechnologyInfo endpointTechnologyInfo;
    private ApiEnvironmentInfo environmentInfo;
    private ApiIdentification identification;

    @Override
    public URI getEndpointUri() {
        return endpointUri;
    }

    @Override
    public UpEndpointTechnologyInfo getEndpointTechnologyInfo() {
        return endpointTechnologyInfo;
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
        return String.format("ApiEndpointInfo{%s, %s, %s, %s}", endpointUri, endpointTechnologyInfo, environmentInfo, identification);
    }

    @Override
    public boolean equals(Object object) {
        return this == object
                || null != object &&
                (object instanceof UpEndpoint.Info
                        && Objects.equals(getEndpointUri(), ((UpEndpoint.Info) object).getEndpointUri())
                        && Objects.equals(getEndpointTechnologyInfo(), ((UpEndpoint.Info) object).getEndpointTechnologyInfo())
                        && Objects.equals(getEnvironmentInfo(), ((UpEndpoint.Info) object).getEnvironmentInfo())
                        && Objects.equals(getIdentification(), ((UpEndpoint.Info) object).getIdentification()));
    }

    @Override
    public int hashCode() {
        return 182 + Objects.hashCode(endpointUri) * 51
                + Objects.hashCode(endpointTechnologyInfo) * 397
                + Objects.hashCode(environmentInfo) * 197
                + Objects.hashCode(identification) * 92;
    }

}
