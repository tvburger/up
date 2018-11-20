package net.tvburger.up.applications.api.deploy.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

// NOTE: not sure if this can actually works if after the topology anything else follows...
public final class JsonTopologyReader { //extends UpApplicationTopologyReader {

    private static final ObjectMapper mapper = new ObjectMapper();

//    public JsonTopologyReader(Reader reader) {
//        super(reader);
//    }
//
//    public UpApplicationTopology readTopology() throws IOException {
//        return mapper.readValue(this, ApiApplicationDefinition.class).toUp();
//    }

}
