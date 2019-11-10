package me.jvlk.dius.store;

import me.jvlk.dius.store.models.MonetaryAmount;
import me.jvlk.dius.store.models.PricingRule;
import me.jvlk.dius.store.models.Product;
import me.jvlk.dius.store.persistence.CsvLoader;
import me.jvlk.dius.store.persistence.Loader;

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

}
