package net.tvburger.up.local;

import net.tvburger.up.deploy.EndpointTechnology;
import net.tvburger.up.deploy.Language;
import net.tvburger.up.deploy.UpLanguageInterpreter;
import net.tvburger.up.impl.ProtocolLifecycleManagerProvider;

import java.util.Set;

public final class Java8Interpreter implements UpLanguageInterpreter {

    public static class Factory {

        public static Java8Interpreter create() {
            return new Java8Interpreter(new ProtocolLifecycleManagerProvider());
        }

        private Factory() {
        }

    }

    private final Language java8 = new Language("java", "8");
    private final ProtocolLifecycleManagerProvider protocolProvider;

    private Java8Interpreter(ProtocolLifecycleManagerProvider protocolProvider) {
        this.protocolProvider = protocolProvider;
    }

    @Override
    public Language getLanguage() {
        return java8;
    }

    @Override
    public Set<EndpointTechnology> getEndpointTechnologies() {
        return protocolProvider.getEndpointTechnologies();
    }

}
