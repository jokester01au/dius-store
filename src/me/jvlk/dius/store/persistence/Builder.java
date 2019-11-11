package me.jvlk.dius.store.persistence;

public interface Builder<P> {
    P build();
    Builder<P> set(String name, String value);

    Builder<P> set(Builder<P> other);
}
