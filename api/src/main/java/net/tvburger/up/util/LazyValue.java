package net.tvburger.up.util;

import java.util.function.Supplier;

public final class LazyValue<T> implements Supplier<T> {

    private T value;

    @Override
    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("LazyValue{%s}", value);
    }

}
