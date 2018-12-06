package net.tvburger.up.runtime.context;

import net.tvburger.up.*;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.runtime.util.UpContextHolder;
import net.tvburger.up.security.Identity;

import java.util.UUID;

public interface UpContext {

    /**
     * Returns the current operation id
     *
     * @return
     */
    UUID getOperationId();

    /**
     * Returns the information of the transaction
     *
     * @return
     */
    TransactionInfo getTransactionInfo();

    /**
     * Returns the information of the caller
     *
     * @return
     */
    CallerInfo getCallerInfo();

    /**
     * Returns the environment that is applicable, or null when not executing within an UpEnvironment
     *
     * @return
     */
    UpEnvironment getEnvironment();

    /**
     * Returns the package that is applicable, or null when not executing within an UpPackage
     *
     * @return
     */
    UpPackage getPackage();

    /**
     * Returns the application that is applicable, or null when not executing within an UpApplication
     *
     * @return
     */
    UpApplication getApplication();

    /**
     * Returns the identity of the UpService, UpEndpoint, UpEngine or UpRuntime
     *
     * @return
     */
    Identity getIdentity();

    /**
     * Returns the service when called from a UpService execution, otherwise null
     *
     * @return
     */
    UpService<?> getService();

    /**
     * Returns the endpoint when called from an UpEndpoint execution, otherwise null
     *
     * @return
     */
    UpEndpoint<?, ?> getEndpoint();

    /**
     * Returns the Engine that is executing the current service
     *
     * @return
     */
    UpEngine getEngine();

    /**
     * Returns the Runtime that is executing the current service
     *
     * @return
     */
    UpRuntime getRuntime();

    /**
     * Returns the locality of the current code execution
     *
     * @return
     */
    Locality getLocality();

    static UpContext getContext() {
        return UpContextHolder.getContext();
    }

}
