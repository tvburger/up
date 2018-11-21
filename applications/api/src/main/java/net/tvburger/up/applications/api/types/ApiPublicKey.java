package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.security.PublicKey;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiPublicKey implements PublicKey {

    public static ApiPublicKey fromUp(PublicKey up) {
        if (up == null) {
            return null;
        }
        ApiPublicKey api = new ApiPublicKey();
        api.algorithm = up.getAlgorithm();
        api.format = up.getFormat();
        api.encoded = up.getEncoded();
        return api;
    }

    public PublicKey toUp() {
        return this;
    }

    private String algorithm;
    private String format;
    private byte[] encoded;

    public String getAlgorithm() {
        return algorithm;
    }

    public String getFormat() {
        return format;
    }

    public byte[] getEncoded() {
        return encoded;
    }

    @Override
    public String toString() {
        return String.format("ApiPublicKey{%s, %s, %s}", algorithm, format, encoded);
    }

}
