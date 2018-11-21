package net.tvburger.up.applications.api.types;

import net.tvburger.up.deploy.UpPackageDefinition;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ApiPackageDefinition implements UpPackageDefinition {

    private final File file;

    public ApiPackageDefinition(File file) {
        this.file = file;
    }

    public InputStream open() throws IOException {
        return new FileInputStream(file);
    }

}
