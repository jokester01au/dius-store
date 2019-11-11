package me.jvlk.dius.store.persistence;

import me.jvlk.dius.store.models.Product;

public class ProductBuilder implements Builder<Product> {

    @Override
    public Product build() {
        return null;
    }

    @Override
    public Builder<Product> set(String name, String value) {
        return this;
    }

    @Override
    public Builder<Product> set(Builder<Product> other) {
        return this;
    }
}
