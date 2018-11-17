package net.tvburger.up.clients.java;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.client.UpClientBuilder;
import net.tvburger.up.client.UpClientException;
import net.tvburger.up.client.impl.UpClientInfoImpl;
import net.tvburger.up.clients.java.types.ClientEnvironmentInfo;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public final class ApiClientBuilder implements UpClientBuilder {

    private String environmentName;
    private Identity identity;
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public ApiClientBuilder withBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    @Override
    public String getEnvironment() {
        return environmentName;
    }

    @Override
    public ApiClientBuilder withEnvironment(String environmentName) {
        this.environmentName = environmentName;
        return this;
    }

    @Override
    public Identity getIdentity() {
        return identity;
    }

    @Override
    public ApiClientBuilder withIdentity(Identity identity) {
        this.identity = identity;
        return this;
    }

    @Override
    public ApiClient build() throws AccessDeniedException, UpClientException {
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new UpClientException("No baseUrl set!");
        }
        if (environmentName == null || environmentName.isEmpty()) {
            throw new UpClientException("No environment set!");
        }
        try {
            Client client = ClientBuilder.newClient();
            ApiRequester requester = ApiRequester.Factory.create(client, baseUrl + "/" + environmentName, identity);
            UpEnvironment.Info environmentInfo = requester.request("info", ClientEnvironmentInfo.class);
            UpClient.Info clientInfo = UpClientInfoImpl.Factory.create(environmentInfo, identity);
            return new ApiClient(requester, identity, new ApiClientManager(clientInfo, client));
        } catch (ApiException cause) {
            throw new UpClientException("Failed to connect to api: " + cause.getMessage(), cause);
        }
    }

}
