package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.security.PublicKey;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiPublicKey implements PublicKey {

    private String algorithm;
    private String format;
    private byte[] encoded;

    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public byte[] getEncoded() {
        return encoded;
    }

    @Override
    public String toString() {
        return String.format("ApiPublicKey{%s, %s}", algorithm, format);
    }

}
