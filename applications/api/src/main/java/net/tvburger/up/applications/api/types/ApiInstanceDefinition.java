package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.tvburger.up.topology.InstanceDefinition;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiInstanceDefinition {

    private final ObjectMapper mapper = new ObjectMapper();

    public static ApiInstanceDefinition fromUp(InstanceDefinition up) {
        ApiInstanceDefinition api = new ApiInstanceDefinition();
        api.instanceSpecification = ApiSpecification.fromUp(up.getInstanceSpecification());
        api.arguments = new ApiList<>();
        for (Object argument : up.getArguments()) {
            api.arguments.add(argument == null ? Void.class : argument.getClass(), argument);
        }
        return api;
    }

    @SuppressWarnings("unchecked")
    public InstanceDefinition toUp() throws ClassNotFoundException, IOException {
        InstanceDefinition.Builder builder = new InstanceDefinition.Builder();
        builder.withInstanceSpecification(instanceSpecification.toUp());
        for (ApiList.Entry<Class<?>, Object> argument : arguments) {
            Object value;
            if (argument.getValue() instanceof String) {
                String stringArgument = (String) argument.getValue();
                Class<?> type = argument.getKey();
                if (Class.class.isAssignableFrom(type)) {
                    value = Class.forName(stringArgument);
                } else if (Enum.class.isAssignableFrom(type)) {
                    value = Enum.valueOf((Class) type, stringArgument);
                } else {
                    value = mapper.readValue((String) argument.getValue(), type);
                }
            } else {
                value = argument.getValue();
            }
            builder.withArgument(value);
        }
        return builder.build();
    }

    private ApiSpecification instanceSpecification;
    private ApiList<Class<?>, Object> arguments;

    public ApiSpecification getInstanceSpecification() {
        return instanceSpecification;
    }

    public ApiList<Class<?>, Object> getArguments() {
        return arguments;
    }

}
