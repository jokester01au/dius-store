package me.jvlk.dius.store.models;

import java.util.Map;
import java.util.Objects;

public class Product implements Priced{
    private String sku;
    private MonetaryAmount rrp;
    private String name;

    public Product(String sku, MonetaryAmount rrp, String name) {
        this.sku = sku;
        this.rrp = rrp;
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public MonetaryAmount getRrp() {
        return rrp;
    }

    public String getName() {
        return name;
    }

    public static Product fromMap(Map<String, String> fields) {
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s RRP)", name, sku, rrp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return sku.equals(product.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku);
    }

    @Override
    public MonetaryAmount getPrice() {
        return rrp;
    }

    @Override
    public Product getProduct() {
        return this;
    }
}
