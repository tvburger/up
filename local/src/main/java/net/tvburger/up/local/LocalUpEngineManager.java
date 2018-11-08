package net.tvburger.up.local;

import net.tvburger.up.EndpointTechnology;
import net.tvburger.up.EndpointTechnologyInfo;
import net.tvburger.up.Up;
import net.tvburger.up.behaviors.Implementation;
import net.tvburger.up.behaviors.LifecycleException;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.context.Locality;
import net.tvburger.up.context.UpServiceContext;
import net.tvburger.up.deploy.*;
import net.tvburger.up.impl.*;
import net.tvburger.up.security.Identity;
import net.tvburger.up.util.EndpointTechnologyLoader;
import net.tvburger.up.util.Identities;
import net.tvburger.up.util.ThreadBasedContextProvider;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class LocalUpEngineManager implements UpEngineManager {

    public static final class Factory {

        public static LocalUpEngineManager create(UpRuntime runtime, Identity identity) throws UnknownHostException, LifecycleException {
            LocalUpEngineManager manager = new LocalUpEngineManager(
                    UpEngineInfoImpl.Factory.create(
                            UUID.randomUUID(),
                            InetAddress.getLocalHost(),
                            -1,
                            Identities.ANONYMOUS,
                            implementation.getSpecification()), identity);
            manager.init(runtime);
            return manager;
        }

        private Factory() {
        }

    }

    // TODO: this must be determined from actual running JVM!
    private static final Implementation implementation = ImplementationImpl.Factory.create(
            SpecificationImpl.Factory.create("java", "8"),
            "Oracle Java SE Runtime Environment",
            "1.8.0_131");

    public static Implementation getImplementation() {
        return implementation;
    }

    private final UpEngineInfo info;
    private final Identity identity;
    private final Map<EndpointTechnologyInfo<?>, EndpointTechnology<?>> endpointTechnologies = new HashMap<>();
    private UpEngine engine;

    private LocalUpEngineManager(UpEngineInfo info, Identity identity) {
        this.info = info;
        this.identity = identity;
    }

    private void init(UpRuntime runtime) throws LifecycleException {
        UpServiceContext serviceContext = Up.getServiceContext();
        try {
            engine = UpEngineImpl.Factory.create(this, endpointTechnologies, runtime);
            UpServiceContextImpl engineContext = new UpServiceContextImpl();
            engineContext.setIdentity(identity);
            engineContext.setLocality(Locality.Factory.create(engine));
            engineContext.setEngine(getEngine());
            ThreadBasedContextProvider.set(engineContext);
            endpointTechnologies.putAll(EndpointTechnologyLoader.load(engine));
        } catch (DeployException cause) {
            throw new LifecycleException(cause);
        } finally {
            ThreadBasedContextProvider.set(serviceContext);
        }
    }

    public UpEngine getEngine() {
        return engine;
    }

    @Override
    public UpEngineInfo getInfo() {
        return info;
    }

    @Override
    public String getImplementationName() {
        return implementation.getImplementationName();
    }

    @Override
    public String getImplementationVersion() {
        return implementation.getImplementationVersion();
    }

    @Override
    public Specification getSpecification() {
        return implementation.getSpecification();
    }

    @Override
    public void init() {
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException();
    }

}
