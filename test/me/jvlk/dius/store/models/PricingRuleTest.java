package me.jvlk.dius.store.models;


import me.jvlk.dius.store.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_LIST;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

class PricingRuleTest implements Constants  {

    private PricingRule systemUnderTest;
    private Collection<PricingRule> rulesApplied;


    @BeforeEach
    void setUp() {
        this.rulesApplied = new LinkedList<>();

        this.systemUnderTest = create(ONE_WEEK_AGO, ONE_WEEK_LATER, ONE_DOLLAR_BILL);
    }

    private PricingRule create(Date startsOn, Date endsOn, Product target) {

        return new PricingRule(startsOn, endsOn, target) {
            @Override
            public List<Priced> apply(List<Priced> cart) {
                rulesApplied.add(this);
                return cart;
            }
        };
    }

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
    void constructor_rejectsNull() {
        assertThrows(NullPointerException.class, () -> create(null, ONE_WEEK_LATER, ONE_DOLLAR_BILL));
        assertThrows(NullPointerException.class, () -> create(ONE_WEEK_AGO, null, ONE_DOLLAR_BILL));
        assertThrows(NullPointerException.class, () -> create(ONE_WEEK_AGO, ONE_WEEK_LATER, null));
    }

    @Test
    void constructor_rejectsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> create(ONE_WEEK_LATER, ONE_WEEK_LATER, ONE_DOLLAR_BILL));
    }

    @Test
    void constructor_reordersDates() {
        systemUnderTest = create(ONE_WEEK_LATER, ONE_WEEK_AGO, ONE_DOLLAR_BILL);
        assertTrue(systemUnderTest.isActive(TODAY));
    }

    @Test
    void applyAll() {
        PricingRule otherActiveTest = create(BEFORE, AFTER, ONE_DOLLAR_BILL);
        PricingRule.applyAll(asList(otherActiveTest, systemUnderTest), TODAY, EMPTY_LIST);
        assertThat(rulesApplied).containsExactlyInAnyOrder(systemUnderTest, otherActiveTest)
            .as("Both systemUnderTest and otherActiveTest should be applied");
    }

    @Test
    void applyAll_filtersInactive() {
        PricingRule inactiveBefore = create(BEFORE, ONE_WEEK_AGO, ONE_DOLLAR_BILL);
        PricingRule inactiveAfter = create(AFTER, ONE_WEEK_LATER, ONE_DOLLAR_BILL);
        PricingRule.applyAll(asList(systemUnderTest, inactiveAfter, inactiveBefore), TODAY, EMPTY_LIST);
        assertThat(rulesApplied).containsExactly(systemUnderTest)
                .as("PricingRule inactiveBefore and inactiveAfter must be filtered out becasuse they are not active TODAY.");

    }

    @Test
    void applyAll_choosesLowestPrice() {
        PricingRule lowerPrice = new PricingRule(ONE_WEEK_AGO, ONE_WEEK_LATER, ONE_DOLLAR_BILL) {
            @Override
            public List<Priced> apply(List<Priced> cart) {
                return cart.stream().map(p -> new PricedProduct(p.getProduct(), p.getPrice().vary(0.5))).collect(toList());
            }
        };

        List<Priced> initial = asList(ONE_DOLLAR_BILL);
        List<Priced> actual = PricingRule.applyAll(asList(lowerPrice, systemUnderTest), TODAY, initial);
        assertThat(actual)
                .extracting(Priced::getProduct, Priced::getPrice)
                .containsExactly(tuple(ONE_DOLLAR_BILL, new MonetaryAmount(0.5)));


    }


}