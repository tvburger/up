package net.tvburger.up.applications.api.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.tvburger.up.deploy.InstanceDefinition;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiInstanceDefinition {

    private final ObjectMapper mapper = new ObjectMapper();

    public static ApiInstanceDefinition fromUp(InstanceDefinition up) {
        ApiInstanceDefinition api = new ApiInstanceDefinition();
        api.classSpecification = ApiSpecification.fromUp(up.getClassSpecification());
        api.arguments = new ApiList<>();
        for (Object argument : up.getArguments()) {
            api.arguments.add(argument == null ? Void.class : argument.getClass(), argument);
        }
        return api;
    }

    @SuppressWarnings("unchecked")
    public InstanceDefinition toUp() throws IOException {
        try {
            InstanceDefinition.Builder builder = new InstanceDefinition.Builder();
            builder.withClassSpecification(classSpecification.toUp());
            for (ApiList.Entry<Class<?>, Object> argument : arguments) {
                Object value;
                if (argument.getValue() instanceof String) {
                    String stringArgument = (String) argument.getValue();
                    Class<?> type = argument.getKey();
                    if (String.class.isAssignableFrom(type)) {
                        value = stringArgument;
                    } else if (Class.class.isAssignableFrom(type)) {
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
        } catch (ClassNotFoundException cause) {
            throw new IOException("Failed to load class: " + cause.getMessage(), cause);
        }
    }

    private ApiSpecification classSpecification;
    private ApiList<Class<?>, Object> arguments;

    public ApiSpecification getClassSpecification() {
        return classSpecification;
    }

    public ApiList<Class<?>, Object> getArguments() {
        return arguments;
    }

}
