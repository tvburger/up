package net.tvburger.up.loggers.elastic;

import net.tvburger.up.runtime.logger.UpLogger;
import net.tvburger.up.runtime.spi.UpLoggerProvider;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public final class ElasticLoggerProvider implements UpLoggerProvider {

    private static final String clusterName = "docker-cluster";
    private static final TransportAddress[] clusterAddresses = new TransportAddress[]{new TransportAddress(InetAddress.getLoopbackAddress(), 9300)};
    private static final String indexName = "up";
    private static final String typeBaseName = "logs";
    private static final boolean ensureExists = true;

    private final Map<String, ElasticLogger> loggers = new HashMap<>();
    private final ElasticLogFormatter formatter = new ElasticLogFormatter();

    private final Client client;

    private static Client createClient() {
        PreBuiltTransportClient client = new PreBuiltTransportClient(Settings.builder()
                .put("cluster.name", clusterName)
                .build());
        for (TransportAddress address : clusterAddresses) {
            client.addTransportAddress(address);
        }
        return client;
    }

    public ElasticLoggerProvider() throws ExecutionException, InterruptedException {
        this(createClient());
        init();
    }

    public ElasticLoggerProvider(Client client) {
        this.client = client;
    }

    private void init() throws ExecutionException, InterruptedException {
        if (ensureExists) {
            if (!indexExists()) {
                createIndexAndMapping();
            } else if (!typeExists()) {
                createMapping();
            }
        }
    }

    private boolean indexExists() throws ExecutionException, InterruptedException {
        IndicesExistsRequest request = new IndicesExistsRequest();
        request.indices(indexName);
        return client.admin().indices().exists(request).get().isExists();
    }

    private boolean typeExists() throws ExecutionException, InterruptedException {
        TypesExistsRequest request = new TypesExistsRequest();
        request.indices(indexName);
        request.types(new String[]{getTypeName()});
        return client.admin().indices().typesExists(request).get().isExists();
    }

    private void createIndexAndMapping() throws ExecutionException, InterruptedException {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.mapping(getTypeName(), formatter.getMapping());
        client.admin().indices().create(request).get();
    }

    private void createMapping() throws ExecutionException, InterruptedException {
        PutMappingRequest request = new PutMappingRequest();
        request.source(formatter.getMapping());
        request.type("_doc");
        client.admin().indices().putMapping(request).get();
    }

    private String getTypeName() {
        return typeBaseName + "_" + formatter.getFormatVersion();
    }

    @Override
    public UpLogger getLogger(String loggerName) {
        return loggers.computeIfAbsent(loggerName, (key) -> new ElasticLogger(client, indexName, getTypeName(), key, formatter));
    }

}
