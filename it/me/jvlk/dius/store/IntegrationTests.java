package me.jvlk.dius.store;

import me.jvlk.dius.store.models.PricingRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

public class IntegrationTests {

    Checkout systemUnderTest;

    void scanAndFailOnUnknownItem(String sku) {
        try {
            systemUnderTest.scan(sku);
        } catch (UnknownItemException e) {
            Assertions.fail(e);
        }
    }

    void testHarness(String expectedAmount, String...skus) {
        Collection<PricingRule> pricingRules = Collections.EMPTY_LIST;
        systemUnderTest = new Checkout(pricingRules);
        Arrays.stream(skus).forEach(this::scanAndFailOnUnknownItem);
        Assertions.assertEquals(expectedAmount, systemUnderTest.total().toString());
    }

    // SKUs Scanned: atv, atv, atv, vga Total expected: $249.00
    @Test
    void test1() {
        testHarness("$249.00", "atv", "atv", "atv", "vga");
    }

    // SKUs Scanned: atv, ipd, ipd, atv, ipd, ipd, ipd Total expected: $2718.95
    @Test
    void test2() {
        testHarness("$2718.95", "atv", "ipd", "ipd", "atv", "ipd", "ipd", "ipd");
    }

    // SKUs Scanned: mbp, vga, ipd Total expected: $1949.98
    @Test
    void test3() {
        testHarness("1949.98", "mbp", "vga", "ipd");
    }
}
