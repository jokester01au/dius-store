package me.jvlk.dius.store.persistence.rules;

import me.jvlk.dius.store.ProductStore;
import me.jvlk.dius.store.models.PricingRule;
import me.jvlk.dius.store.models.Product;
import me.jvlk.dius.store.models.rules.FreeGift;
import me.jvlk.dius.store.persistence.PricingRuleBuilder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FreeGiftBuilder extends PricingRuleBuilder.SubBuilder {
    private Product gift = null;
    private Integer giftQuantity = null;
    private Integer purchasedQuantity = null;

    @Override
    public PricingRule build(Date startsOn, Date endsOn, Product target) {
        return new FreeGift(startsOn, endsOn,
                Objects.requireNonNull(gift),
                Objects.requireNonNull(giftQuantity),
                target,
                Objects.requireNonNull(purchasedQuantity)
        );
    }


    @Override
    public PricingRuleBuilder.SubBuilder setArgs(String... args) {
        if (args.length != 3) throw new IllegalArgumentException(String.format("FreeGift(giftSku, giftQuantity, purchasedQuantity) expects 3 arguments: %s", Arrays.toString(args)));

        gift = ProductStore.getInstance().findProduct(args[0]);
        giftQuantity = Integer.parseInt(args[1]);
        purchasedQuantity = Integer.parseInt(args[2]);
        return this;
    }


    public static void init() {
        PricingRuleBuilder.registerRuleType("FreeGift", FreeGiftBuilder::new);
    }
}
