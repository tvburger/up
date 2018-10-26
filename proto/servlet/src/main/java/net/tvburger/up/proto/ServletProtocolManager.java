package net.tvburger.up.proto;

import net.tvburger.up.spi.ProtocolManager;

import javax.servlet.Servlet;

public interface ServletProtocolManager extends ProtocolManager {

    void registerServlet(String mapping, Class<? extends Servlet> servletClass, Object... arguments);

}
