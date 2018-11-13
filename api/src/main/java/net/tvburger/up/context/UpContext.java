package net.tvburger.up.context;

import net.tvburger.up.Environment;
import net.tvburger.up.ServiceInfo;
import net.tvburger.up.security.Identity;

public interface UpContext {

    /**
     * Returns the information of the transaction, if any
     *
     * @return
     */
    TransactionInfo getTransactionInfo();

    /**
     * Returns the information of the caller, if any
     *
     * @return
     */
    CallerInfo getCallerInfo();

    /**
     * Returns the serviceInfo when called from inside a Service
     *
     * @return
     */
    ServiceInfo<?> getServiceInfo();

    /**
     * Returns the identity of the Service when called inside a Service, otherwise Identity of the applicable Engine
     *
     * @return
     */
    Identity getIdentity();

    /**
     * Returns the environment that is applicable, or null when endpoints changes their lifecycle state
     *
     * @return
     */
    Environment getEnvironment();

    /**
     * Returns the locality of the current code execution
     *
     * @return
     */
    Locality getLocality();

}
