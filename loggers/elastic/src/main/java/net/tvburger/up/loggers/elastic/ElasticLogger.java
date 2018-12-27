package net.tvburger.up.loggers.elastic;

import net.tvburger.up.logbindings.LogBindings;
import net.tvburger.up.runtime.logger.LogStatement;
import net.tvburger.up.runtime.logger.UpLogger;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;

// TODO: add buffer for log statements and use bulk request
public final class ElasticLogger implements UpLogger {

    private final Client client;
    private final String index;
    private final String type;
    private final String loggerName;
    private final ElasticLogFormatter formatter;

    public ElasticLogger(Client client, String index, String type, String loggerName, ElasticLogFormatter formatter) {
        this.client = client;
        this.index = index;
        this.type = type;
        this.loggerName = loggerName;
        this.formatter = formatter;
    }

    @Override
    public void log(LogStatement logStatement) {
        try {
            client.index(new IndexRequest(index, type).source(formatter.formatLog(logStatement, loggerName)));
        } catch (Exception cause) {
            LogBindings.getStderr().println("Failed to log message: " + cause.getMessage());
            LogBindings.getStderr().println(formatter.formatLog(logStatement, loggerName));
        }
    }

}
