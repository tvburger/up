package my.company.example.logic;

public class ExampleServiceImpl implements ExampleService {

    private final String prefix;
    private final DependencyService dependencyService;

    public ExampleServiceImpl(String prefix, DependencyService dependencyService) {
        this.prefix = prefix;
        this.dependencyService = dependencyService;
    }

    @Override
    public String sayHelloTo(String name) {
        System.out.println("We are going to say hello to: " + name + "!");
        return String.format("%s %s %s", dependencyService.getPrompt(true), prefix, name);
    }

}
