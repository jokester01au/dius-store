package me.jvlk.dius.store.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class MonetaryAmountTest {
    private MonetaryAmount systemUnderTest;

    @BeforeEach
    void setUp() {
        systemUnderTest = new MonetaryAmount(1);
    }

    @Test
    void testToString() {
        assertEquals("$1.00", systemUnderTest.toString());
    }

    @Test
    void vary() {
        assertEquals(new MonetaryAmount(0.5), systemUnderTest.vary(0.5));
    }

    @Test
    void vary_roundsDown() {
        assertEquals(new MonetaryAmount(0.66), systemUnderTest.vary(2/3D));
    }
    @Test
    void sum() {
        assertEquals(new MonetaryAmount(1.66), MonetaryAmount.sum(asList(systemUnderTest, new MonetaryAmount(0.66))));
    }
}