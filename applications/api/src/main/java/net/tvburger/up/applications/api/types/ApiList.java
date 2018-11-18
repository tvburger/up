package net.tvburger.up.applications.api.types;

import java.util.LinkedList;
import java.util.Map;

public final class ApiList<K, V> extends LinkedList<ApiList.Entry<K, V>> {

    public static final class Entry<K, V> {

        private K key;
        private V value;

        public Entry() {
        }

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

    }

    public ApiList() {
    }

    public ApiList(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            add(new Entry<>(entry.getKey(), entry.getValue()));
        }
    }

    public void add(K key, V value) {
        add(new Entry<>(key, value));
    }

}
