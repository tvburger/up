package net.tvburger.up.clients.java.impl;

import net.tvburger.up.UpException;
import net.tvburger.up.UpPackage;
import net.tvburger.up.applications.api.types.ApiPackageInfo;
import net.tvburger.up.clients.java.ApiException;
import net.tvburger.up.deploy.UpApplicationDefinition;
import net.tvburger.up.deploy.UpClassLoader;
import net.tvburger.up.deploy.UpResourceLoader;
import net.tvburger.up.security.AccessDeniedException;

import java.util.Set;

public final class ClientPackage extends ApiRequester implements UpPackage {

    public ClientPackage(String path, ApiRequester requester) {
        super(requester, path);
    }

    @Override
    public Manager getManager() throws AccessDeniedException {
        return new ClientPackageManager(this);
    }

    @Override
    public Info getInfo() {
        try {
            return apiRead("info", ApiPackageInfo.class);
        } catch (UpException cause) {
            throw new ApiException(cause);
        }
    }

    @Override
    public UpResourceLoader getResourceLoader() {
        throw new ApiException("Not supported!");
    }

    @Override
    public UpClassLoader getClassLoader() {
        throw new ApiException("Not supported!");
    }

    @Override
    public Set<UpApplicationDefinition> getApplicationDefinitions() {
        throw new ApiException("Not supported!");
    }

}
