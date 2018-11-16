package net.tvburger.up.runtime.context;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpService;
import net.tvburger.up.runtime.UpEngine;
import net.tvburger.up.runtime.UpRuntime;
import net.tvburger.up.runtime.impl.UpContextHolder;
import net.tvburger.up.security.Identity;

public interface UpContext {

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
