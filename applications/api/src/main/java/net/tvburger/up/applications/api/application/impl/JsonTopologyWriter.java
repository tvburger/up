package net.tvburger.up.applications.api.application.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.tvburger.up.applications.api.application.UpApplicationTopologyWriter;
import net.tvburger.up.applications.api.types.ApiApplicationTopology;
import net.tvburger.up.topology.UpApplicationTopology;

import java.io.IOException;
import java.io.Writer;

public final class JsonTopologyWriter extends UpApplicationTopologyWriter {

    private static final ObjectMapper mapper = new ObjectMapper();

    public JsonTopologyWriter(Writer writer) {
        super(writer);
    }

    public void writeTopology(UpApplicationTopology topology) throws IOException {
        mapper.writeValue(this, ApiApplicationTopology.fromUp(topology));
    }

}
