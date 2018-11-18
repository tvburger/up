package net.tvburger.up.clients.java.impl;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpException;
import net.tvburger.up.applications.api.types.ApiEnvironmentInfo;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.client.UpClientBuilder;
import net.tvburger.up.client.UpClientException;
import net.tvburger.up.client.impl.UpClientInfoImpl;
import net.tvburger.up.clients.java.ApiClientTarget;
import net.tvburger.up.clients.java.ApiException;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.net.MalformedURLException;

public final class ApiClientBuilder implements UpClientBuilder {

    private final ApiClientTarget target;

    private String environmentName;
    private Identity identity;

    public ApiClientBuilder(ApiClientTarget target) {
        this.target = target;
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
        if (target == null || target.getUrl().isEmpty()) {
            throw new UpClientException("No baseUrl set!");
        }
        if (environmentName == null || environmentName.isEmpty()) {
            throw new UpClientException("No environment set!");
        }
        try {
            Client client = ClientBuilder.newClient();
            ApiRequester requester = ApiRequester.Factory.create(client, target.getUrl() + "/" + environmentName, identity);
            UpEnvironment.Info environmentInfo = requester.apiRead("info", ApiEnvironmentInfo.class);
            UpClient.Info clientInfo = UpClientInfoImpl.Factory.create(environmentInfo, identity);
            return new ApiClient(requester, identity, new ApiClientManager(clientInfo, client));
        } catch (AccessDeniedException cause) {
            throw cause;
        } catch (ApiException | UpException | MalformedURLException cause) {
            throw new UpClientException("Failed to connect to api: " + cause.getMessage(), cause);
        }
    }

}
