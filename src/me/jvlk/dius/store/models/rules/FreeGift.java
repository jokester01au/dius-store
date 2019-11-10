package me.jvlk.dius.store.models.rules;

import me.jvlk.dius.store.models.MonetaryAmount;
import me.jvlk.dius.store.models.PricingRule;
import me.jvlk.dius.store.models.Product;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

public class FreeGift extends PricingRule {
    private final Product gift;
    private final int giftQuantity;
    private final Product purchasedProduct;
    private final int purchasedQuantity;

    public FreeGift(Date startsOn, Date endsOn, Product gift, Product purchasedProduct) {
        this(startsOn, endsOn, gift, 1, purchasedProduct, 1);
    }

    public FreeGift(Date startsOn, Date endsOn, Product gift, int giftQuantity, Product purchasedProduct, int purchasedQuantity) {
        super(startsOn, endsOn);

        this.gift = gift;
        this.giftQuantity = giftQuantity;
        this.purchasedProduct = purchasedProduct;
        this.purchasedQuantity = purchasedQuantity;
    }

    @Override
    public MonetaryAmount apply(Product target, Collection<Product> cart) {
        return null;
    }

    @Override
    public String toString() {
        return String.format("Purchase %d of %s, get %d %s free (%s)", purchasedQuantity, purchasedProduct, giftQuantity, gift, super.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FreeGift freeGift = (FreeGift) o;
        return giftQuantity == freeGift.giftQuantity &&
                purchasedQuantity == freeGift.purchasedQuantity &&
                gift.equals(freeGift.gift) &&
                purchasedProduct.equals(freeGift.purchasedProduct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gift, giftQuantity, purchasedProduct, purchasedQuantity);
    }
}
