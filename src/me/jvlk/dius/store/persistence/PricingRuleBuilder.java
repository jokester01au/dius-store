package me.jvlk.dius.store.persistence;

import me.jvlk.dius.store.models.PricingRule;
import me.jvlk.dius.store.models.Product;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static me.jvlk.dius.store.Utils.strToDate;

public class PricingRuleBuilder implements Builder<PricingRule> {
    private final List<Product> products;

    private Date startsOn;
    private Date endsOn;
    private Product target;
    private SubBuilder ruleBuilder;

    public PricingRuleBuilder(List<Product> products) {

        this.products = products;
    }

    @Override
    public final PricingRule build() {
        return ruleBuilder.build(
                Objects.requireNonNull(startsOn),
                Objects.requireNonNull(endsOn),
                Objects.requireNonNull(target)
        );
    }

    @Override
    public Builder<PricingRule> set(String name, String value) {
        switch (name) {
            case "startsOn":
                this.startsOn = strToDate(value);
                break;
            case "endsOn":
                this.endsOn = strToDate(value);
                break;
            case "target":
                this.target = findProduct(value);
                break;
            case "rule":
                this.ruleBuilder = this.parseRule(value);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown field name: %s (value %s)", name, value));
        }
        return this;
    }

    @Override
    public Builder<PricingRule> set(Builder<PricingRule> other) {
        PricingRuleBuilder o = (PricingRuleBuilder) other; // FIXME - what if there are other implementations??
        if (o.ruleBuilder != null) this.ruleBuilder = o.ruleBuilder;
        if (o.target != null) this.target = o.target;
        if (o.startsOn != null) this.startsOn = o.startsOn;
        if (o.endsOn != null) this.endsOn = o.endsOn;
        return this;
    }

    private static Map<String, Function<PricingRuleBuilder, SubBuilder>> INSTANTIATORS = new ConcurrentHashMap<>();

    public static void registerRuleType(String name, Function<PricingRuleBuilder, SubBuilder> instantiator) {
        INSTANTIATORS.put(name, instantiator);
    }

    private SubBuilder parseRule(String spec) {
        String[] nameAndArguiments = spec.split("[()]", 3);
        if (nameAndArguiments.length != 3) throw new IllegalArgumentException(String.format("rule expected of form ruleName([param1, [...]]): %s", spec));

        Function<PricingRuleBuilder, SubBuilder> subBuilder = INSTANTIATORS.get(nameAndArguiments[0]);
        if (subBuilder == null) throw new IllegalArgumentException(String.format("Unknown rule type: %s", spec));

        SubBuilder builder = subBuilder.apply(this);

        String[] arguments = nameAndArguiments[1].split(",");
        builder.setArgs(arguments);

        return builder;
    }

    public static abstract class SubBuilder {
        private final PricingRuleBuilder parent;

        public SubBuilder(PricingRuleBuilder parent) {
            this.parent = parent;
        }

        protected Product findProduct(String sku) {
            return this.parent.findProduct(sku);
        }

        public abstract SubBuilder setArgs(String...args);

        public abstract PricingRule build(Date startAt, Date endOn, Product target);

    }

    private Product findProduct(String sku) {
        try {
            return products.stream()
                    .filter(p -> p.getSku().equals(sku))
                    .findFirst()
                    .orElseThrow();
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException(String.format("No product found with sku %s", sku));
        }
    }
}
