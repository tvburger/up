package net.tvburger.up.spi;

import net.tvburger.up.context.CallerInfo;
import net.tvburger.up.context.UpServiceContext;

public interface UpContextProvider {

    UpServiceContext getServiceContext();

    CallerInfo getCallerInfo();

}
