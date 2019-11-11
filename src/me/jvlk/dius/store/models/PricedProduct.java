package me.jvlk.dius.store.models;

import java.util.Objects;

public class PricedProduct implements Priced {
    private final Product product;
    private final MonetaryAmount actualPrice;

    public PricedProduct(Product product, MonetaryAmount actualPrice) {
        this.product = Objects.requireNonNull(product);
        this.actualPrice = Objects.requireNonNull(actualPrice);
    }

    public Product getProduct() {
        return product;
    }

    public MonetaryAmount getPrice() {
        return actualPrice;
    }


    @Override
    public String toString() {
        double ratio = actualPrice.asRatioOf(product.getRrp());
        int percent = (int) Math.abs(1 - ratio) * 100;
        return String.format("%s @ %s (%s%% %s)", product, actualPrice, percent,
                ratio > 1.0D ? "premium" : "discount"
        );
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
