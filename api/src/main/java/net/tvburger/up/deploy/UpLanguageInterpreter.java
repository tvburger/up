package net.tvburger.up.deploy;

import java.util.Set;

public interface UpLanguageInterpreter {

    Language getLanguage();

    Set<EndpointTechnology> getEndpointTechnologies();

}
