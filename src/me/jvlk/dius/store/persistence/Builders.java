package me.jvlk.dius.store.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class Builders {
    private static final Builders _INSTANCE = new Builders();
    private Map<Class<?>, Supplier<Builder<?>>> builders = new HashMap<>();

    private Builders() {}

    public static Builders getInstance() {
        return _INSTANCE;
    }

    public <P> void register(Class<P> forClass, Supplier<Builder<P>> builder) {
        this.builders.put(forClass, (Supplier<Builder<?>>)(Supplier) builder);
    }

    public <P> Builder<P> get(Class<P> forClass) {
        Supplier<Builder<?>> supplier = builders.get(forClass);
        if (supplier == null) throw new IllegalArgumentException(String.format("No builder registered for class %s", forClass));
        Builder<P> builder = (Builder<P>) supplier.get();
        Objects.requireNonNull(builder);
        return builder;
    }
}
