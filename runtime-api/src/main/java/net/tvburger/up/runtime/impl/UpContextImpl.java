package net.tvburger.up.runtime.impl;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpService;
import net.tvburger.up.behaviors.MutableComposition;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.runtime.context.CallerInfo;
import net.tvburger.up.runtime.context.Locality;
import net.tvburger.up.runtime.context.TransactionInfo;
import net.tvburger.up.runtime.context.UpContext;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;
import net.tvburger.up.topology.TopologyException;

import java.util.Objects;

public class UpContextImpl implements UpContext, MutableComposition {

    public static final class Factory {

        public static UpContextImpl createEndpointContext(UpEndpoint<?, ?> endpoint, Identity identity, UpContext engineContext) throws TopologyException, AccessDeniedException {
            Objects.requireNonNull(endpoint);
            Objects.requireNonNull(identity);
            Objects.requireNonNull(engineContext);
            UpContextImpl endpointContext = createEnvironmentContext(endpoint.getInfo().getEnvironmentInfo().getName(), identity, engineContext);
            endpointContext.setTransactionInfo(TransactionInfo.Factory.create(endpoint.getInfo().getEndpointUri()));
            endpointContext.setCallerInfo(CallerInfo.Factory.create(endpoint.getInfo()));
            endpointContext.setEndpoint(endpoint);
            return endpointContext;
        }

        public static UpContextImpl createServiceContext(UpService<?> service, Identity identity, UpContext callerContext) throws TopologyException, AccessDeniedException {
            Objects.requireNonNull(service);
            Objects.requireNonNull(identity);
            Objects.requireNonNull(callerContext);
            UpContextImpl serviceContext = createEnvironmentContext(service.getInfo().getEnvironmentInfo().getName(), identity, callerContext);
            serviceContext.setTransactionInfo(callerContext.getTransactionInfo());
            serviceContext.setCallerInfo(CallerInfo.Factory.create(callerContext));
            serviceContext.setService(service);
            return serviceContext;
        }

        private static UpContextImpl createEnvironmentContext(String environmentName, Identity identity, UpContext parentContext) throws TopologyException, AccessDeniedException {
            UpRuntime runtime = parentContext.getRuntime();
            if (!runtime.hasEnvironment(environmentName)) {
                throw new TopologyException("Unknown environment: " + environmentName);
            }
            UpContextImpl context = createContext(identity, parentContext);
            context.setEnvironment(runtime.getEnvironment(environmentName));
            return context;
        }

        private static UpContextImpl createContext(Identity identity, UpContext parentContext) {
            Objects.requireNonNull(parentContext);
            UpContextImpl context = new UpContextImpl();
            context.setIdentity(identity);
            context.setEngine(parentContext.getEngine());
            context.setRuntime(parentContext.getRuntime());
            context.setLocality(Locality.Factory.create(parentContext.getEngine()));
            return context;
        }

        public static UpContextImpl createEngineContext(UpEngine engine, Identity engineIdentity) {
            Objects.requireNonNull(engine);
            Objects.requireNonNull(engineIdentity);
            UpContextImpl context = new UpContextImpl();
            context.setIdentity(engineIdentity);
            context.setEngine(engine);
            context.setRuntime(engine.getRuntime());
            context.setLocality(Locality.Factory.create(engine));
            return context;
        }

        private Factory() {
        }

    }

    private TransactionInfo transactionInfo;
    private CallerInfo callerInfo;
    private UpEnvironment environment;
    private Identity identity;
    private UpService<?> service;
    private UpEndpoint<?, ?> endpoint;
    private UpEngine engine;
    private UpRuntime runtime;
    private Locality locality;

    @Override
    public TransactionInfo getTransactionInfo() {
        return transactionInfo;
    }

    public void setTransactionInfo(TransactionInfo transactionInfo) {
        this.transactionInfo = transactionInfo;
    }

    @Override
    public CallerInfo getCallerInfo() {
        return callerInfo;
    }

    public void setCallerInfo(CallerInfo callerInfo) {
        this.callerInfo = callerInfo;
    }

    @Override
    public UpEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment(UpEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    @Override
    public UpService<?> getService() {
        return service;
    }

    public void setService(UpService<?> service) {
        this.service = service;
    }

    @Override
    public UpEndpoint<?, ?> getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(UpEndpoint<?, ?> endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public UpEngine getEngine() {
        return engine;
    }

    public void setEngine(UpEngine engine) {
        this.engine = engine;
    }

    @Override
    public UpRuntime getRuntime() {
        return runtime;
    }

    public void setRuntime(UpRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    @Override
    public String toString() {
        return String.format("UpContext{%s, %s, %s, %s, %s, %s, %s, %s, %s}",
                transactionInfo,
                callerInfo,
                environment == null ? null : environment.getInfo(),
                identity,
                service == null ? null : service.getInfo(),
                endpoint == null ? null : endpoint.getInfo(),
                engine == null ? null : engine.getInfo(),
                runtime == null ? null : runtime.getInfo(),
                locality);
    }

}
