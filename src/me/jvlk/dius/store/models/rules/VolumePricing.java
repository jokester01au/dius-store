package me.jvlk.dius.store.models.rules;

import me.jvlk.dius.store.models.MonetaryAmount;
import me.jvlk.dius.store.models.Priced;
import me.jvlk.dius.store.models.PricedProduct;
import me.jvlk.dius.store.models.PricingRule;
import me.jvlk.dius.store.models.Product;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class VolumePricing extends PricingRule {
    private final int minQuantity;
    private final MonetaryAmount discountedPrice;

    public VolumePricing(Date startsOn, Date endsOn, Product target, int minQuantity, MonetaryAmount discountedPrice) {
        super(startsOn, endsOn, target);
        this.minQuantity = minQuantity;
        this.discountedPrice = Objects.requireNonNull(discountedPrice);
    }

    public VolumePricing(Date startsOn, Date endsOn, Product target, int minQuantity, double discountRatio) {
        this(startsOn, endsOn, target, minQuantity, target.getRrp().vary(discountRatio));
    }

    @Override
    public List<Priced> apply(List<Priced> cart) {
        long productCount = cart.stream().filter(p -> p.getProduct().equals(target)).count();
        boolean meetsRequirement = productCount >= minQuantity;
        return cart.stream().map(p ->
                p.getProduct().equals(target) && meetsRequirement && p.getPrice().compareTo(discountedPrice) > 0 ?
                        new PricedProduct(p.getProduct(), discountedPrice) :
                        p
        ).collect(toList());
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
