package net.tvburger.up.clients.java.impl;

import net.tvburger.up.UpApplication;
import net.tvburger.up.UpEnvironment;
import net.tvburger.up.UpException;
import net.tvburger.up.UpPackage;
import net.tvburger.up.applications.api.types.ApiEnvironmentInfo;
import net.tvburger.up.applications.api.types.ApiPackageDefinition;
import net.tvburger.up.applications.api.types.ApiPackageInfo;
import net.tvburger.up.clients.java.ApiException;
import net.tvburger.up.deploy.DeployException;
import net.tvburger.up.deploy.UpApplicationDefinition;
import net.tvburger.up.deploy.UpPackageDefinition;
import net.tvburger.up.runtime.impl.UpPackageManagerImpl;

import java.io.IOException;
import java.io.InputStream;

public final class ClientEnvironmentManager extends ClientEntityManager implements UpEnvironment.Manager {

    public ClientEnvironmentManager(ApiRequester requester) {
        super(requester, "manager");
    }

    @Override
    public UpEnvironment.Info getInfo() {
        try {
            return apiRead("info", ApiEnvironmentInfo.class);
        } catch (ApiException | UpException cause) {
            throw new ApiException("Failed to read environment info: " + cause.getMessage(), cause);
        }
    }

    @Override
    public boolean supportsPackageDefinitionType(Class<? extends UpPackageDefinition> packageDefinitionType) {
        return ApiPackageDefinition.class.equals(packageDefinitionType);
    }

    @Override
    public UpPackage.Manager deployPackage(UpPackageDefinition packageDefinition) throws DeployException {
        try (InputStream bytes = ((ApiPackageDefinition) packageDefinition).open()) {
            ApiPackageInfo packageInfo = apiWrite("deploy/package", bytes, new ApiResponseType.Value(ApiPackageInfo.class));
            return new UpPackageManagerImpl(packageInfo);
        } catch (DeployException cause) {
            throw cause;
        } catch (IOException cause) {
            throw new DeployException(cause);
        } catch (UpException cause) {
            throw new ApiException(cause);
        }
    }

    @Override
    public UpApplication.Manager createApplication(String name, UpPackage.Info packageInfo) throws DeployException {
        return null;
    }

    @Override
    public UpApplication.Manager deployApplication(UpApplicationDefinition applicationDefinition, UpPackage.Info packageInfo) throws DeployException {
        return null;
    }

}
