package me.jvlk.dius.store.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

class PricingRuleTest {

    private PricingRule systemUnderTest;
    private Collection<PricingRule> rulesApplied;

    private static MonetaryAmount ONE = new MonetaryAmount(1);

    void configure(Date startAt, Date finishAt) {
        List<PricingRule> applied = new LinkedList<>();
        this.rulesApplied = applied;

        this.systemUnderTest = new PricingRule(startAt, finishAt) {
            @Override
            public MonetaryAmount apply(Product target, Collection<Product> cart) {
                applied.add(this);
                return ONE;
            }
        };
    }

    @Test
    void isActive() {

        Assertions.fail("FIXME - implement test");
    }

    @Test
    void apply() {

        Assertions.fail("FIXME - implement test");
    }

    @Test
    void applyAll() {

        Assertions.fail("FIXME - implement test");
    }
}