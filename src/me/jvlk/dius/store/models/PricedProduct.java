package me.jvlk.dius.store.models;

import java.util.Objects;

public class PricedProduct implements Priced {
    private final Product product;
    private final MonetaryAmount actualPrice;

    public PricedProduct(Product product) {
        this(product, product.getRrp());
    }

    public PricedProduct(Product product, MonetaryAmount actualPrice) {
        Objects.requireNonNull(product);
        Objects.requireNonNull(actualPrice);
        this.product = product;
        this.actualPrice = actualPrice;
    }

    public Product getProduct() {
        return product;
    }

    public MonetaryAmount getPrice() {
        return actualPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PricedProduct that = (PricedProduct) o;
        return product.equals(that.product) &&
                actualPrice.equals(that.actualPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, actualPrice);
    }
}
