package me.jvlk.dius.store.persistence;

import me.jvlk.dius.store.models.MonetaryAmount;
import me.jvlk.dius.store.models.Product;

import java.util.Objects;

public class ProductBuilder implements Builder<Product> {
    private String sku = null;
    private Double rrp = null;
    private String name = null;

    @Override
    public Product build() {
        return new Product(
                Objects.requireNonNull(sku),
                new MonetaryAmount(Objects.requireNonNull(rrp)),
                Objects.requireNonNull(name));
    }

    @Override
    public Builder<Product> set(String name, String value) {
        switch (name) {
            case "sku":
                this.sku = value;
                break;
            case "rrp":
                this.rrp = Double.parseDouble(value);
                break;
            case "name":
                this.name = value;
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown field name: %s (value %s)", name, value));

        }
        return this;
    }

    @Override
    public Builder<Product> set(Builder<Product> other) {
        ProductBuilder o = (ProductBuilder) other; // FIXME - what if there are other implementations??
        if (o.name != null) this.name = o.name;
        if (o.rrp != null) this.rrp = o.rrp;
        if (o.sku != null) this.sku = o.sku;
        return this;
    }
}
