package net.tvburger.up.clients.java.impl;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public interface ApiResponseType {

    final class MapList implements ApiResponseType {

        private final ApiResponseType keyType;
        private final ApiResponseType valueType;

        public MapList(ApiResponseType keyType, ApiResponseType valueType) {
            this.keyType = keyType;
            this.valueType = valueType;
        }

        public ApiResponseType getKeyType() {
            return keyType;
        }

        public ApiResponseType getValueType() {
            return valueType;
        }

        @Override
        public Class<?> getType() {
            return LinkedList.class;
        }

    }

    final class Map implements ApiResponseType {

        private final ApiResponseType keyType;
        private final ApiResponseType valueType;

        public Map(ApiResponseType keyType, ApiResponseType valueType) {
            this.keyType = keyType;
            this.valueType = valueType;
        }

        public ApiResponseType getKeyType() {
            return keyType;
        }

        public ApiResponseType getValueType() {
            return valueType;
        }

        @Override
        public Class<?> getType() {
            return LinkedHashMap.class;
        }

    }

    final class Set implements ApiResponseType {

        private final ApiResponseType innerType;

        public Set(ApiResponseType innerType) {
            this.innerType = innerType;
        }

        public ApiResponseType getInnerType() {
            return innerType;
        }

        @Override
        public Class<?> getType() {
            return LinkedHashSet.class;
        }

    }

    final class Value implements ApiResponseType {

        private final Class<?> valueType;

        public Value(Class<?> valueType) {
            this.valueType = valueType;
        }

        @Override
        public Class<?> getType() {
            return valueType;
        }

    }

    Class<?> getType();

}
