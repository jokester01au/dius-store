package me.jvlk.dius.store.persistence;

import me.jvlk.dius.store.ProductStore;
import me.jvlk.dius.store.models.PricingRule;
import me.jvlk.dius.store.models.Product;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static me.jvlk.dius.store.Utils.strToDate;

public class PricingRuleBuilder implements Builder<PricingRule> {

    private Date startsOn;
    private Date endsOn;
    private Product target;
    private SubBuilder ruleBuilder;

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
                this.target = ProductStore.getInstance().findProduct(value);
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

    private static Map<String, Supplier<SubBuilder>> INSTANTIATORS = new ConcurrentHashMap<>();

    public static void registerRuleType(String name, Supplier<SubBuilder> instantiator) {
        INSTANTIATORS.put(name, instantiator);
    }

    private SubBuilder parseRule(String spec) {
        String[] nameAndArguiments = spec.split("[()]", 3);
        if (nameAndArguiments.length != 3) throw new IllegalArgumentException(String.format("rule expected of form ruleName([param1, [...]]): %s", spec));

        Supplier<SubBuilder> subBuilder = INSTANTIATORS.get(nameAndArguiments[0]);
        if (subBuilder == null) throw new IllegalArgumentException(String.format("Unknown rule type: %s", spec));

        SubBuilder builder = subBuilder.get();

        String[] arguments = nameAndArguiments[1].split(",");
        builder.setArgs(arguments);

        return builder;
    }

    public static abstract class SubBuilder {

        public abstract SubBuilder setArgs(String...args);

        public abstract PricingRule build(Date startAt, Date endOn, Product target);

    }

    public static void init() {
        Builders.getInstance().register(PricingRule.class, PricingRuleBuilder::new);
    }

}
