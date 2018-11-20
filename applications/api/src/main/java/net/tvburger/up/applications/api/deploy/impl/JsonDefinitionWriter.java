package net.tvburger.up.applications.api.deploy.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.tvburger.up.applications.api.deploy.UpApplicationDefinitionWriter;
import net.tvburger.up.deploy.UpApplicationDefinition;

import java.io.IOException;
import java.io.Writer;

public final class JsonDefinitionWriter extends UpApplicationDefinitionWriter {

    private final ObjectMapper mapper = new ObjectMapper();

    public JsonDefinitionWriter(Writer writer) {
        super(writer);
    }

    @Override
    public void writeDefinition(UpApplicationDefinition definition) throws IOException {

    }

}
