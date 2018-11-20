package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.tvburger.up.UpEndpoint;

import java.net.URI;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiEndpointInfo implements UpEndpoint.Info {

    private URI endpointUri;
    private ApiEndpointTechnologyInfo endpointTechnologyInfo;
    private ApiApplicationInfo applicationInfo;
    private ApiIdentification identification;

    @Override
    public URI getEndpointUri() {
        return endpointUri;
    }

    @Override
    public ApiEndpointTechnologyInfo getEndpointTechnologyInfo() {
        return endpointTechnologyInfo;
    }

    @Override
    public ApiApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    @Override
    public ApiIdentification getIdentification() {
        return identification;
    }

    @Override
    public String toString() {
        return String.format("ApiEndpointInfo{%s, %s, %s, %s}", endpointUri, endpointTechnologyInfo, applicationInfo, identification);
    }

    @Override
    public boolean equals(Object object) {
        return this == object
                || null != object &&
                (object instanceof UpEndpoint.Info
                        && Objects.equals(getEndpointUri(), ((UpEndpoint.Info) object).getEndpointUri())
                        && Objects.equals(getEndpointTechnologyInfo(), ((UpEndpoint.Info) object).getEndpointTechnologyInfo())
                        && Objects.equals(getApplicationInfo(), ((UpEndpoint.Info) object).getApplicationInfo())
                        && Objects.equals(getIdentification(), ((UpEndpoint.Info) object).getIdentification()));
    }

    @Override
    public int hashCode() {
        return 182 + Objects.hashCode(endpointUri) * 51
                + Objects.hashCode(endpointTechnologyInfo) * 397
                + Objects.hashCode(applicationInfo) * 197
                + Objects.hashCode(identification) * 92;
    }

}
