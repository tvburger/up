package net.tvburger.up;

import net.tvburger.up.behaviors.ManagedObject;
import net.tvburger.up.behaviors.Specification;
import net.tvburger.up.deploy.UpApplicationDefinition;
import net.tvburger.up.deploy.UpClassLoader;
import net.tvburger.up.deploy.UpResourceLoader;

import java.util.Set;
import java.util.UUID;

public interface UpPackage extends ManagedObject<UpPackage.Manager, UpPackage.Info> {

    interface Manager extends ManagedObject.Manager<Info> {
    }

    interface Info extends Specification, ManagedObject.Info {

        UUID getPackageId();

    }

    UpResourceLoader getResourceLoader();

    UpClassLoader getClassLoader();

    Set<UpApplicationDefinition> getApplicationDefinitions();

}
