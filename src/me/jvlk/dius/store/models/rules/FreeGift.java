package me.jvlk.dius.store.models.rules;

import me.jvlk.dius.store.models.MonetaryAmount;
import me.jvlk.dius.store.models.Priced;
import me.jvlk.dius.store.models.PricedProduct;
import me.jvlk.dius.store.models.PricingRule;
import me.jvlk.dius.store.models.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * FUTURE - dont apply other rules to free gifts
 */
public class FreeGift extends PricingRule {
    private final Product gift;
    private final int giftQuantity;
    private final int purchasedQuantity;

    public FreeGift(Date startsOn, Date endsOn, Product gift, Product purchasedProduct) {
        this(startsOn, endsOn, gift, 1, purchasedProduct, 1);
    }

    public FreeGift(Date startsOn, Date endsOn, Product gift, int giftQuantity, Product purchasedProduct, int purchasedQuantity) {
        super(startsOn, endsOn, purchasedProduct);

        if (giftQuantity <= 0 || purchasedQuantity <= 0) throw new IllegalArgumentException(String.format("Positive integer required for both giftQuantity (%d) and purchasedQuantity (%d)", giftQuantity, purchasedQuantity));
        this.gift = Objects.requireNonNull(gift);
        this.giftQuantity = giftQuantity;
        this.purchasedQuantity = purchasedQuantity;
    }

    @Override
    public List<Priced> apply(List<Priced> cart) {
        // FUTURE - could do this with fewer iterations
        long purchaseCount = cart.stream().filter(p -> p.equals(target)).count();
        long initialGiftCount = cart.stream().filter(p -> p.equals(gift)).count();
        long requiredGiftCount = giftQuantity * purchaseCount / purchasedQuantity;
        long giftsToAdd = Math.max(requiredGiftCount - initialGiftCount, 0) ;


        // FUTURE - do this functionally
        List<Priced> result = new ArrayList<>((int) (cart.size() + giftsToAdd));
        for (Priced p : cart) {
            if (p.getProduct().equals(gift) && ! p.getPrice().isFree() && requiredGiftCount > 0) {
                requiredGiftCount--;
                result.add(new PricedProduct(p.getProduct(), MonetaryAmount.FREE));
            } else {
                result.add(p);
            }
        }
        while (requiredGiftCount-- > 0) result.add(new PricedProduct(gift, MonetaryAmount.FREE));
        return result;
    }

    @Override
    public String toString() {
        return String.format("Purchase %d of %s, get %d %s free (%s)", purchasedQuantity, target, giftQuantity, gift, super.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FreeGift freeGift = (FreeGift) o;
        return super.equals(o) &&
                giftQuantity == freeGift.giftQuantity &&
                purchasedQuantity == freeGift.purchasedQuantity &&
                gift.equals(freeGift.gift);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gift, giftQuantity, purchasedQuantity);
    }
}
