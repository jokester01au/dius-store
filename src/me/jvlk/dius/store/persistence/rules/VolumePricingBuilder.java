package me.jvlk.dius.store.persistence.rules;

import me.jvlk.dius.store.models.MonetaryAmount;
import me.jvlk.dius.store.models.PricingRule;
import me.jvlk.dius.store.models.Product;
import me.jvlk.dius.store.models.rules.VolumePricing;
import me.jvlk.dius.store.persistence.PricingRuleBuilder;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class VolumePricingBuilder extends PricingRuleBuilder.SubBuilder {
    private Integer minQuantity = null;
    private MonetaryAmount discountedPrice = null;

    @Override
    public PricingRule build(Date startsOn, Date endsOn, Product target) {
        return new VolumePricing(startsOn, endsOn, target,
                Objects.requireNonNull(minQuantity),
                Objects.requireNonNull(discountedPrice));
    }


    @Override
    public PricingRuleBuilder.SubBuilder setArgs(String... args) {
        if (args.length != 2) throw new IllegalArgumentException(String.format("VolumePricing(minQuantity, discountedPrice) expects 2 arguments: %s", Arrays.toString(args)));

        minQuantity = Integer.parseInt(args[0]);
        discountedPrice = new MonetaryAmount(Double.parseDouble(args[1]));
        return this;
    }


    public static void init() {
        PricingRuleBuilder.registerRuleType("VolumePricing", VolumePricingBuilder::new);
    }
}
