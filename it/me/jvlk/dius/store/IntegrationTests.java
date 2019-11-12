package me.jvlk.dius.store;

import me.jvlk.dius.store.models.PricingRule;
import me.jvlk.dius.store.persistence.CsvLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.fail;

public class IntegrationTests {
    public static final String PRICING_RULES_FILE = "pricing-rules.csv";

    Checkout systemUnderTest;

    void scanAndFailOnUnknownItem(String sku) {
        try {
            systemUnderTest.scan(sku);
        } catch (UnknownItemException e) {
            fail(e);
        }
    }

    void testHarness(String expectedAmount, String...skus) {
        try {
            Checkout.init(); // FIXME - chicken and egg init problem
            Collection<PricingRule> pricingRules = new CsvLoader<>(PricingRule.class, new BufferedReader(new FileReader(PRICING_RULES_FILE))).loadAll();
        systemUnderTest = new Checkout(pricingRules);
        Arrays.stream(skus).forEach(this::scanAndFailOnUnknownItem);
        Assertions.assertEquals(expectedAmount, systemUnderTest.total().toString());
        } catch (Exception e) {
            fail(e);
        }
    }

    // SKUs Scanned: atv, atv, atv, vga Total expected: $249.00
    @Test
    void test1() {
        testHarness("$249.00", "atv", "atv", "atv", "vga");
    }

    // SKUs Scanned: atv, ipd, ipd, atv, ipd, ipd, ipd Total expected: $2718.95
    @Test
    void test2() {
        testHarness("$2,718.95", "atv", "ipd", "ipd", "atv", "ipd", "ipd", "ipd");
    }

    // SKUs Scanned: mbp, vga, ipd Total expected: $1949.98
    @Test
    void test3() {
        testHarness("$1,949.98", "mbp", "vga", "ipd");
    }
}
