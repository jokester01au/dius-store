package me.jvlk.dius.store;

import me.jvlk.dius.store.models.MonetaryAmount;
import me.jvlk.dius.store.models.PricingRule;
import me.jvlk.dius.store.models.Product;
import me.jvlk.dius.store.persistence.PricingRuleBuilder;
import me.jvlk.dius.store.persistence.ProductBuilder;
import me.jvlk.dius.store.persistence.rules.FreeGiftBuilder;
import me.jvlk.dius.store.persistence.rules.VolumePricingBuilder;

import java.util.Collection;

public class Checkout {
    /**
     * This is not at all a robust mechanism for instantiating products, it is done this way only to comply with the required interface
     */
    private static final Collection<Product> DEFAULT_PRODUCTS = null;

    public Checkout(Collection<PricingRule> pricingRules) {
        this(pricingRules, DEFAULT_PRODUCTS);
    }

    public Checkout(Collection<PricingRule> pricingRules, Collection<Product> products) {

    }

    void scan(String sku) throws UnknownItemException {

    }

    public MonetaryAmount total() {
        return null;
    }

    /**
     * FIXME - implement a dynamic discovery mechanism
     */
    public static void init() {
        PricingRuleBuilder.init();
        ProductBuilder.init();
        VolumePricingBuilder.init();
        FreeGiftBuilder.init();
    }

    static {
        init();
    }
}
