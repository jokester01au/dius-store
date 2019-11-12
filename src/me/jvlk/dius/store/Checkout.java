package me.jvlk.dius.store;

import me.jvlk.dius.store.models.MonetaryAmount;
import me.jvlk.dius.store.models.Priced;
import me.jvlk.dius.store.models.PricingRule;
import me.jvlk.dius.store.models.Product;
import me.jvlk.dius.store.persistence.PricingRuleBuilder;
import me.jvlk.dius.store.persistence.ProductBuilder;
import me.jvlk.dius.store.persistence.rules.FreeGiftBuilder;
import me.jvlk.dius.store.persistence.rules.VolumePricingBuilder;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static me.jvlk.dius.store.Utils.strToDate;

public class Checkout {
    private static final Date OPENING_DAY = strToDate("12-11-2019");

    private final Collection<PricingRule> pricingRules;
    private final List<Priced> cart;

    public Checkout(Collection<PricingRule> pricingRules) {

        this.pricingRules = pricingRules;
        this.cart = new LinkedList<>();
    }

    public void scan(String sku) throws UnknownItemException {
        Product product = ProductStore.getInstance().findProduct(sku);
        if (product == null) throw new UnknownItemException(sku);
        cart.add(product);
    }

    public MonetaryAmount total() {
        List<Priced> priceds = PricingRule.applyAll(pricingRules, OPENING_DAY, cart);
        List<MonetaryAmount> amounts = priceds.stream().map(Priced::getPrice).collect(toList());//FIXME -should do this streaming
        return MonetaryAmount.sum(amounts);
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
