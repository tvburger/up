package net.tvburger.up.runtimes.local.client;

import net.tvburger.up.UpEnvironment;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.client.UpClientBuilder;
import net.tvburger.up.client.UpClientException;
import net.tvburger.up.client.impl.UpClientInfoImpl;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.runtime.UpRuntimeException;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.util.Identities;

public final class LocalClientBuilder implements UpClientBuilder {

    private final LocalClientTarget target;
    private String environmentName = "default";
    private Identity identity = Identities.ANONYMOUS;

    public LocalClientBuilder(LocalClientTarget target) {
        this.target = target;
    }

    @Override
    public String getEnvironment() {
        return environmentName;
    }

    @Override
    public UpClientBuilder withEnvironment(String environmentName) {
        if (environmentName == null || environmentName.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.environmentName = environmentName;
        return this;
    }

    @Override
    public Identity getIdentity() {
        return identity;
    }

    @Override
    public UpClientBuilder withIdentity(Identity identity) {
        this.identity = identity;
        return this;
    }

    private boolean isValid() {
        return environmentName != null && !environmentName.isEmpty() &&
                identity != null;
    }

    @Override
    public UpClient build() throws AccessDeniedException, UpClientException {
        if (!isValid()) {
            throw new IllegalStateException();
        }
        try {
            UpRuntime runtime = target.getInstance().getRuntime();
            UpEnvironment environment;
            if (!runtime.hasEnvironment(environmentName)) {
                environment = runtime.getManager().createEnvironment(environmentName);
            } else {
                environment = runtime.getEnvironment(environmentName);
            }
            UpClient.Info clientInfo = UpClientInfoImpl.Factory.create(environment.getInfo(), Identities.getSafeIdentification(identity));
            LocalClientManager clientManager = new LocalClientManager(clientInfo);
            LocalClient client = new LocalClient(target, clientManager, identity, environment);
            return LocalClientProxy.Factory.create(target, client);
        } catch (UpRuntimeException cause) {
            throw new UpClientException(cause);
        }
    }

}
