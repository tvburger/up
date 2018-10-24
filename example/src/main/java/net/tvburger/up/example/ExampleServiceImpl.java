package net.tvburger.up.example;

public class ExampleServiceImpl implements ExampleService {

    private final String format;

    public ExampleServiceImpl(String format) {
        this.format = format;
    }

    @Override
    public String sayHelloTo(String name) {
        return String.format(format, name);
    }

}
