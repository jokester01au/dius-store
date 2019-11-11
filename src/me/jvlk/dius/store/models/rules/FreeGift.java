package me.jvlk.dius.store.models.rules;

import me.jvlk.dius.store.models.Priced;
import me.jvlk.dius.store.models.PricingRule;
import me.jvlk.dius.store.models.Product;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.EMPTY_LIST;

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

        Objects.requireNonNull(gift);
        Objects.requireNonNull(purchasedProduct);
        if (giftQuantity <= 0 || purchasedQuantity <= 0) throw new IllegalArgumentException(String.format("Positive integer required for both giftQuantity (%d) and purchasedQuantity (%d)", giftQuantity, purchasedQuantity));
        this.gift = gift;
        this.giftQuantity = giftQuantity;
        this.purchasedProduct = purchasedProduct;
        this.purchasedQuantity = purchasedQuantity;
    }

    @Override
    public List<Priced> apply(List<Priced> cart) {
        return EMPTY_LIST;
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
