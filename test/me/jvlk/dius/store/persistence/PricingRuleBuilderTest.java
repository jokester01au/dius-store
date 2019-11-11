package me.jvlk.dius.store.persistence;

import me.jvlk.dius.store.Constants;
import me.jvlk.dius.store.models.Priced;
import me.jvlk.dius.store.models.PricingRule;
import me.jvlk.dius.store.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PricingRuleBuilderTest implements Constants {

    private PricingRuleBuilder systemUnderTest;

    @BeforeEach
    void setUp() {
        List<Product> products = asList(ONE_DOLLAR_BILL);
        systemUnderTest = new PricingRuleBuilder(products);
    }

    @Test
    void set_oneAtATime() {
        systemUnderTest.set("startsOn", ONE_WEEK_AGO_STRING);
        systemUnderTest.set("endsOn", ONE_WEEK_LATER_STRING);
        systemUnderTest.set("target", ONE_DOLLAR_BILL.getSku());
        systemUnderTest.set("rule", "APricingRule(1)");
        assertThat(systemUnderTest.build())
                .extracting(PricingRule::getStartsOn, PricingRule::getEndsOn, PricingRule::getTarget, Object::getClass)
                .containsExactly(ONE_WEEK_AGO, ONE_WEEK_LATER, ONE_DOLLAR_BILL, APricingRule.class); //FIXME - test parameter
    }

    @Test
    void set_allAtOnce() {
        PricingRuleBuilder other = new PricingRuleBuilder(asList(ONE_DOLLAR_BILL));
        other.set("startsOn", ONE_WEEK_AGO_STRING);
        other.set("endsOn", ONE_WEEK_LATER_STRING);
        other.set("target", ONE_DOLLAR_BILL.getSku());
        other.set("rule", "APricingRule(1)");

        systemUnderTest.set(other);
        assertThat(systemUnderTest.build())
                .extracting(PricingRule::getStartsOn, PricingRule::getEndsOn, PricingRule::getTarget, Object::getClass)
                .containsExactly(ONE_WEEK_AGO, ONE_WEEK_LATER, ONE_DOLLAR_BILL, APricingRule.class); //FIXME - test parameter
    }

    @Test
    void build_failsOnNull() {
        assertThrows(NullPointerException.class, systemUnderTest::build);
        systemUnderTest.set("startsOn", ONE_WEEK_AGO_STRING);
        assertThrows(NullPointerException.class, systemUnderTest::build);
        systemUnderTest.set("endsOn", ONE_WEEK_LATER_STRING);
        assertThrows(NullPointerException.class, systemUnderTest::build);
        systemUnderTest.set("target", ONE_DOLLAR_BILL.getSku());
        assertThrows(NullPointerException.class, systemUnderTest::build);
        systemUnderTest.set("rule", "APricingRule(1)");

        assertDoesNotThrow(systemUnderTest::build);
    }


    private static class APricingRule extends PricingRule {
        public int aParameter;

        public APricingRule(Date startsOn, Date endsOn, Product target, int aParameter) {
            super(startsOn, endsOn, target);
            this.aParameter = aParameter;
        }

        @Override
        public List<Priced> apply(List<Priced> cart) {
            return null;
        }
    }
}
