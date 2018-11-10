package net.tvburger.up.runtimes.local.spi;

import net.tvburger.up.Environment;
import net.tvburger.up.client.UpClient;
import net.tvburger.up.client.UpClientBuilder;
import net.tvburger.up.client.UpClientInfo;
import net.tvburger.up.impl.UpClientInfoImpl;
import net.tvburger.up.runtime.DeployException;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.runtimes.local.impl.LocalClientProxy;
import net.tvburger.up.runtimes.local.impl.LocalUpClient;
import net.tvburger.up.runtimes.local.impl.LocalUpClientManager;
import net.tvburger.up.runtimes.local.impl.LocalUpClientTarget;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.util.Identities;

public final class LocalUpClientBuilder implements UpClientBuilder {

    private final LocalUpClientTarget target;
    private String environmentName = "default";
    private Identity identity = Identities.ANONYMOUS;

    public LocalUpClientBuilder(LocalUpClientTarget target) {
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
    public UpClient build() throws AccessDeniedException, DeployException {
        if (!isValid()) {
            throw new IllegalStateException();
        }
        UpRuntime runtime = target.getInstance().getRuntime();
        Environment environment;
        if (!runtime.hasEnvironment(environmentName)) {
            environment = runtime.getManager().createEnvironment(environmentName);
        } else {
            environment = runtime.getEnvironment(environmentName);
        }
        UpClientInfo clientInfo = UpClientInfoImpl.Factory.create(environment.getInfo(), Identities.getSafeIdentification(identity));
        LocalUpClientManager clientManager = new LocalUpClientManager(clientInfo);
        LocalUpClient client = new LocalUpClient(target, clientManager, identity);
        return (UpClient) LocalClientProxy.Factory.create(target, client);
    }

}
