package net.tvburger.up.applications.api.application.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.tvburger.up.applications.api.application.UpApplicationTopologyReader;
import net.tvburger.up.applications.api.types.ApiApplicationTopology;
import net.tvburger.up.topology.UpApplicationTopology;

import java.io.IOException;
import java.io.Reader;

// NOTE: not sure if this can actually works if after the topology anything else follows...
public final class JsonTopologyReader extends UpApplicationTopologyReader {

    private static final ObjectMapper mapper = new ObjectMapper();

    public JsonTopologyReader(Reader reader) {
        super(reader);
    }

    public UpApplicationTopology readTopology() throws IOException {
        return mapper.readValue(this, ApiApplicationTopology.class).toUp();
    }

}
