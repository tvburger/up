package net.tvburger.up.spi;

import net.tvburger.up.context.UpContext;

public interface UpContextProvider {

    UpContext getContext();

    void setContext(UpContext context);

}
