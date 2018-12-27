package net.tvburger.up.loggers.elastic;

import net.tvburger.up.UpEndpoint;
import net.tvburger.up.UpService;
import net.tvburger.up.behaviors.ManagedEntity;
import net.tvburger.up.runtime.context.CallerInfo;
import net.tvburger.up.runtime.context.Locality;
import net.tvburger.up.runtime.context.TransactionInfo;
import net.tvburger.up.runtime.logger.LogStatement;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static net.tvburger.up.loggers.elastic.ElasticMappings.*;

public final class ElasticLogFormatter {

    public static final String FORMAT_VERSION = "1";

    public String getFormatVersion() {
        return FORMAT_VERSION;
    }

    private Map<String, Object> getTransactionMapping() {
        return object("id", keyword(),
                "requester", keyword(),
                "request_uri", keyword(),
                "request_uri_scheme", keyword(),
                "request_uri_domain", keyword(),
                "request_uri_path", keyword());
    }

    private Map<String, Object> getCallerMapping() {
        return object("invocation_time", timestamp(),
                "operation_id", keyword(),
                "engine_id", keyword(),
                "application_id", keyword(),
                "application_name", keyword(),
                "service_type", keyword(),
                "service_type_package", keyword(),
                "service_type_short", keyword(),
                "service_id", keyword(),
                "service_instance", keyword(),
                "endpoint_technology", keyword(),
                "endpoint_technology_version", keyword(),
                "endpoint_id", keyword(),
                "endpoint_uri", keyword());
    }

    private Map<String, Object> getLocalityMapping() {
        return object("runtime_id", keyword(),
                "engine_id", keyword(),
                "thread_name", keyword());
    }

    private Map<String, Object> getSourceMapping() {
        return object("class_name", keyword(),
                "class_name_package", keyword(),
                "class_name_short", keyword(),
                "file_name", keyword(),
                "line_nr", number(),
                "method_name", keyword());
    }

    public Map<String, Object> getMapping() {
        return object(
                "logger", keyword(),
                "log_time", timestamp(),
                "log_level", keyword(),
                "operation_id", keyword(),
                "transaction", getTransactionMapping(),
                "entity_id", keyword(),
                "application_name", keyword(),
                "application_id", keyword(),
                "endpoint_technology", keyword(),
                "caller", getCallerMapping(),
                "locality", getLocalityMapping(),
                "source", getSourceMapping(),
                "message", text());
    }

    public Map<String, Object> formatLog(LogStatement logStatement, String loggerName) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("logger", loggerName);
        map.put("log_time", logStatement.getTimestamp());
        map.put("log_level", logStatement.getLogLevel().name());
        map.put("operation_id", Objects.toString(logStatement.getOperationId()));
        map.put("transaction", createTransactionMap(logStatement.getTransactionInfo()));
        if (logStatement.getEntityInfo() != null) {
            ManagedEntity.Info info = logStatement.getEntityInfo();
            map.put("entity_id", Objects.toString(info.getIdentification().getUuid()));
            if (info instanceof UpService.Info) {
                UpService.Info<?> serviceInfo = (UpService.Info<?>) info;
                map.put("application_name", serviceInfo.getApplicationInfo().getName());
                map.put("application_id", Objects.toString(serviceInfo.getApplicationInfo().getIdentification().getUuid()));
            } else if (info instanceof UpEndpoint.Info) {
                UpEndpoint.Info endpointInfo = (UpEndpoint.Info) info;
                map.put("application_name", endpointInfo.getApplicationInfo().getName());
                map.put("application_id", Objects.toString(endpointInfo.getApplicationInfo().getIdentification().getUuid()));
                map.put("endpoint_technology", endpointInfo.getEndpointTechnologyInfo().getSpecificationName() + ":" + endpointInfo.getEndpointTechnologyInfo().getSpecificationVersion());
            }
        }
        map.put("caller", createCallerMap(logStatement.getCallerInfo()));
        map.put("locality", createLocalityMap(logStatement.getLocality()));
        map.put("source", createSourceMap(logStatement.getSource()));
        map.put("message", logStatement.getMessage());
        return map;
    }

    private Map<String, Object> createTransactionMap(TransactionInfo transactionInfo) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (transactionInfo != null) {
            if (transactionInfo.getId() != null) {
                map.put("id", Objects.toString(transactionInfo.getId()));
            }
            if (transactionInfo.getRequester() != null) {
                map.put("requester", transactionInfo.getRequester().getName());
            }
            if (transactionInfo.getRequestUri() != null) {
                map.put("request_uri", Objects.toString(transactionInfo.getRequestUri()));
                map.put("request_uri_scheme", transactionInfo.getRequestUri().getScheme());
                map.put("request_uri_domain", transactionInfo.getRequestUri().getHost() + ":" + transactionInfo.getRequestUri().getPort());
                map.put("request_uri_path", transactionInfo.getRequestUri().getPath());
            }
        }
        return map;
    }

    private Map<String, Object> createCallerMap(CallerInfo callerInfo) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (callerInfo != null) {
            map.put("invocation_time", callerInfo.getTimestamp());
            if (callerInfo.getOperationId() != null) {
                map.put("operation_id", Objects.toString(callerInfo.getOperationId()));
            }
            if (callerInfo.getEngineInfo() != null) {
                map.put("engine_id", Objects.toString(callerInfo.getEngineInfo().getUuid()));
            }
            if (callerInfo.getApplicationInfo() != null) {
                map.put("application_name", callerInfo.getApplicationInfo().getName());
                map.put("application_id", Objects.toString(callerInfo.getApplicationInfo().getIdentification().getUuid()));
            }
            if (callerInfo.getServiceInfo() != null) {
                map.put("service_type", callerInfo.getServiceInfo().getSpecificationName());
                map.put("service_type_package", getPackageName(callerInfo.getServiceInfo().getSpecificationName()));
                map.put("service_type_short", getShortName(callerInfo.getServiceInfo().getSpecificationName()));
                map.put("service_id", Objects.toString(callerInfo.getServiceInfo().getIdentification().getUuid()));
                map.put("service_instance", Objects.toString(callerInfo.getServiceInfo().getServiceInstanceId()));
            }
            if (callerInfo.getEndpointInfo() != null) {
                map.put("endpoint_technology", callerInfo.getEndpointInfo().getEndpointTechnologyInfo().getSpecificationName());
                map.put("endpoint_technology_version", callerInfo.getEndpointInfo().getEndpointTechnologyInfo().getSpecificationVersion());
                map.put("endpoint_id", Objects.toString(callerInfo.getEndpointInfo().getIdentification().getUuid()));
                map.put("endpoint_uri", Objects.toString(callerInfo.getEndpointInfo().getEndpointUri()));
            }
        }
        return map;
    }

    private Map<String, Object> createLocalityMap(Locality locality) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (locality != null) {
            map.put("runtime_id", Objects.toString(locality.getRuntimeInfo().getIdentification().getUuid()));
            map.put("engine_id", Objects.toString(locality.getEngineInfo().getIdentification().getUuid()));
            map.put("thread_name", locality.getThreadName());
        }
        return map;
    }

    private Map<String, Object> createSourceMap(StackTraceElement source) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (source != null) {
            map.put("class_name", source.getClassName());
            map.put("class_name_package", getPackageName(source.getClassName()));
            map.put("class_name_short", getShortName(source.getClassName()));
            map.put("file_name", source.getFileName());
            map.put("line_nr", source.getLineNumber());
            map.put("method_name", source.getMethodName());
        }
        return map;
    }

    private static String getPackageName(String className) {
        int index = className.lastIndexOf('.');
        return index > 0 ? className.substring(0, index) : "";
    }

    public static String getShortName(String className) {
        int index = className.lastIndexOf('.');
        return index > 0 ? className.substring(index + 1) : className;
    }

}
