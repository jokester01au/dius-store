package me.jvlk.dius.store.persistence;

import me.jvlk.dius.store.models.PricingRule;
import me.jvlk.dius.store.models.Product;

import java.util.List;

public class PricingRuleBuilder implements Builder<PricingRule> {
    public PricingRuleBuilder(List<Product> products) {

    }

    @Override
    public PricingRule build() {
        return null;
    }

    @Override
    public Builder<PricingRule> set(String name, String value) {
        return this;
    }

    @Override
    public Builder<PricingRule> set(Builder<PricingRule> other) {
        return this;
    }
}
