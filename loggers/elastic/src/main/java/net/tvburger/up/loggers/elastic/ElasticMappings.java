package net.tvburger.up.loggers.elastic;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ElasticMappings {

    public static Map<String, Object> map(Object... values) {
        if (values == null || values.length == 0) {
            return Collections.emptyMap();
        }
        if ((values.length % 2) != 0) {
            throw new IllegalArgumentException();
        }
        if (values.length == 2) {
            return Collections.singletonMap((String) values[0], values[1]);
        }
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < values.length; i += 2) {
            map.put((String) values[i], values[i + 1]);
        }
        return map;
    }

    public static Map<String, Object> keyword() {
        return map("type", "keyword");
    }

    public static Map<String, Object> text() {
        return map("type", "text");
    }

    public static Map<String, Object> timestamp() {
        return map("type", "date", "format", "epoch_millis");
    }

    public static Map<String, Object> number() {
        return map("type", "integer");
    }

    public static Map<String, Object> object(Object... values) {
        return map("properties", map(values));
    }

    public static Map<String, Object> object(Map<String, Object> properties) {
        return map("properties", properties);
    }

    private ElasticMappings() {
    }

}
