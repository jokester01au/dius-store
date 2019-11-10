package me.jvlk.dius.store.models.rules;

import me.jvlk.dius.store.models.MonetaryAmount;
import me.jvlk.dius.store.models.PricingRule;
import me.jvlk.dius.store.models.Product;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

public class VolumePricing extends PricingRule {
    private final Product target;
    private final int minQuantity;
    private final MonetaryAmount discountedPrice;

    public VolumePricing(Date startsOn, Date endsOn, Product target, int minQuantity, MonetaryAmount discountedPrice) {
        super(startsOn, endsOn);
        this.target = target;
        this.minQuantity = minQuantity;
        this.discountedPrice = discountedPrice;
    }

    public VolumePricing(Date startsOn, Date endsOn, Product target, int minQuantity, double discountRatio) {
        this(startsOn, endsOn, target, minQuantity, null); //target.getRrp().vary(discountRatio));
    }

    @Override
    public MonetaryAmount apply(Product target, Collection<Product> cart) {
        return null;
    }

    @Override
    public String toString() {
        return String.format("Price of >= %d %s: %s (%s)", minQuantity, target, discountedPrice, super.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VolumePricing that = (VolumePricing) o;
        return minQuantity == that.minQuantity &&
                target.equals(that.target) &&
                discountedPrice.equals(that.discountedPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(target, minQuantity, discountedPrice);
    }
}
