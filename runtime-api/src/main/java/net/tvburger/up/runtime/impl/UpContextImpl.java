package net.tvburger.up.runtime.impl;

import net.tvburger.up.*;
import net.tvburger.up.behaviors.MutableComposition;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.runtime.context.CallerInfo;
import net.tvburger.up.runtime.context.Locality;
import net.tvburger.up.runtime.context.TransactionInfo;
import net.tvburger.up.runtime.context.UpContext;
import net.tvburger.up.security.AccessDeniedException;
import net.tvburger.up.security.Identity;

import java.util.Objects;
import java.util.UUID;

public class UpContextImpl implements UpContext, MutableComposition {

    public static final class Factory {

        public static UpContextImpl createEndpointContext(UpEndpoint<?, ?> endpoint, UpPackage upPackage, UpApplication application, Identity identity, UpContext engineContext) throws DeployException, AccessDeniedException {
            Objects.requireNonNull(endpoint);
            Objects.requireNonNull(upPackage);
            Objects.requireNonNull(application);
            Objects.requireNonNull(identity);
            Objects.requireNonNull(engineContext);
            UpContextImpl endpointContext = createEnvironmentContext(endpoint.getInfo().getApplicationInfo().getEnvironmentInfo().getName(), identity, engineContext);
            TransactionInfo transactionInfo = TransactionInfo.Factory.create(endpoint.getInfo().getEndpointUri());
            endpointContext.setOperationId(transactionInfo.getId());
            endpointContext.setTransactionInfo(transactionInfo);
            endpointContext.setPackage(upPackage);
            endpointContext.setApplication(application);
            endpointContext.setEndpoint(endpoint);
            return endpointContext;
        }

        public static UpContextImpl createServiceContext(UpService<?> service, UpPackage upPackage, UpApplication application, Identity identity, UpContext callerContext) throws DeployException, AccessDeniedException {
            Objects.requireNonNull(service);
            Objects.requireNonNull(upPackage);
            Objects.requireNonNull(application);
            Objects.requireNonNull(identity);
            Objects.requireNonNull(callerContext);
            UpContextImpl serviceContext = createEnvironmentContext(service.getInfo().getApplicationInfo().getEnvironmentInfo().getName(), identity, callerContext);
            serviceContext.setOperationId(UUID.randomUUID());
            serviceContext.setTransactionInfo(callerContext.getTransactionInfo());
            serviceContext.setCallerInfo(CallerInfo.Factory.create(callerContext));
            serviceContext.setPackage(upPackage);
            serviceContext.setApplication(application);
            serviceContext.setService(service);
            return serviceContext;
        }

        public static UpContextImpl createEngineContext(UpEngine engine, Identity engineIdentity) {
            Objects.requireNonNull(engine);
            Objects.requireNonNull(engineIdentity);
            UpContextImpl context = new UpContextImpl();
            context.setOperationId(UUID.randomUUID());
            context.setIdentity(engineIdentity);
            context.setEngine(engine);
            context.setRuntime(engine.getRuntime());
            context.setLocality(Locality.Factory.create(engine));
            return context;
        }

        private static UpContextImpl createEnvironmentContext(String environmentName, Identity identity, UpContext parentContext) throws DeployException, AccessDeniedException {
            UpRuntime runtime = parentContext.getRuntime();
            if (!runtime.hasEnvironment(environmentName)) {
                throw new DeployException("Unknown environment: " + environmentName);
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

        private Factory() {
        }

    }

    private UUID operationId;
    private TransactionInfo transactionInfo;
    private CallerInfo callerInfo;
    private UpEnvironment environment;
    private UpPackage upPackage;
    private UpApplication application;
    private Identity identity;
    private UpService<?> service;
    private UpEndpoint<?, ?> endpoint;
    private UpEngine engine;
    private UpRuntime runtime;
    private Locality locality;

    @Override
    public UUID getOperationId() {
        return operationId;
    }

    public void setOperationId(UUID operationId) {
        this.operationId = operationId;
    }

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
    public UpPackage getPackage() {
        return upPackage;
    }

    public void setPackage(UpPackage upPackage) {
        this.upPackage = upPackage;
    }

    @Override
    public UpApplication getApplication() {
        return application;
    }

    public void setApplication(UpApplication application) {
        this.application = application;
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
