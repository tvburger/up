package net.tvburger.up.example.code;

public class ExampleServiceImpl implements ExampleService {

    private final String prefix;
    private final DependencyService dependencyService;

    public ExampleServiceImpl(String prefix, DependencyService dependencyService) {
        this.prefix = prefix;
        this.dependencyService = dependencyService;
    }

    @Override
    public String sayHelloTo(String name) {
        return String.format("%s %s %s", dependencyService.getPrompt(true), prefix, name);
    }

}
