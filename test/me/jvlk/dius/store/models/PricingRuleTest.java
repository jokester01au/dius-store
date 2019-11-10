package me.jvlk.dius.store.models;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_LIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PricingRuleTest {

    private PricingRule systemUnderTest;
    private Collection<PricingRule> rulesApplied;

    private static MonetaryAmount ONE = new MonetaryAmount(1);

    @BeforeEach
    void setUp() {
        this.rulesApplied = new LinkedList<>();

        this.systemUnderTest = create(ONE_WEEK_AGO, ONE_WEEK_LATER);
    }

    private PricingRule create(Date startsOn, Date endsOn) {

        return new PricingRule(startsOn, endsOn) {
            @Override
            public MonetaryAmount apply(Product target, Collection<Product> cart) {
                rulesApplied.add(this);
                return ONE;
            }
        };
    }

    private static Date BEFORE = new Date(2000, 1, 1);
    private static Date ONE_WEEK_AGO = new Date(2019, 11, 3);
    private static Date TODAY = new Date(2019, 11, 10);
    private static Date ONE_WEEK_LATER = new Date(2019, 11, 17);
    private static Date AFTER = new Date(2099, 1, 1);

    @Test
    void isActive_before() {
        assertFalse(systemUnderTest.isActive(BEFORE));
    }

    @Test
    void isActive_during() {
        assertTrue(systemUnderTest.isActive(TODAY));
    }

    @Test
    void isActive_after() {
        assertFalse(systemUnderTest.isActive(AFTER));
    }

    @Test
    void isActive_includesStartDate() {
        assertTrue(systemUnderTest.isActive(ONE_WEEK_AGO));
    }

    @Test
    void isActive_excludesEndDate() {
        assertFalse(systemUnderTest.isActive(ONE_WEEK_LATER));
    }

    @Test
    void constructor_reordersDates() {
        systemUnderTest = create(ONE_WEEK_LATER, ONE_WEEK_AGO);
        assertTrue(systemUnderTest.isActive(TODAY));
    }

    @Test
    void applyAll() {
        PricingRule otherActiveTest = create(BEFORE, AFTER);
        assertThat(rulesApplied).containsExactlyInAnyOrder(systemUnderTest, otherActiveTest)
            .as("Both systemUnderTest and otherActiveTest should be applied");
    }

    @Test
    void applyAll_filtersInactive() {
        PricingRule inactiveBefore = create(BEFORE, ONE_WEEK_AGO);
        PricingRule inactiveAfter = create(AFTER, ONE_WEEK_LATER);
        PricingRule.applyAll(asList(systemUnderTest, inactiveAfter, inactiveBefore), TODAY, EMPTY_LIST);
        assertThat(rulesApplied).containsExactly(systemUnderTest)
                .as("PricingRule inactiveBefore and inactiveAfter must be filtered out becasuse they are not active TODAY.");

    }
}