package me.jvlk.dius.store.persistence;

import me.jvlk.dius.store.Constants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductBuilderTest implements Constants {

    private ProductBuilder systemUnderTest;

    @BeforeEach
    void setUp() {
        systemUnderTest = new ProductBuilder();
    }

    @Test
    void set_oneAtATime() {
        systemUnderTest.set("sku", sku);
        systemUnderTest.set("rrp", Double.toString(price));
        systemUnderTest.set("name", name);
        assertThat(systemUnderTest.build())
                .isEqualTo(SCOOBY_DOO);
    }

    @Test
    void build_failsOnNull() {
        assertThrows(NullPointerException.class, systemUnderTest::build);
        systemUnderTest.set("sku", sku);
        assertThrows(NullPointerException.class, systemUnderTest::build);
        systemUnderTest.set("rrp", Double.toString(price));
        assertThrows(NullPointerException.class, systemUnderTest::build);
        systemUnderTest.set("name", name);
        assertDoesNotThrow(systemUnderTest::build);
    }

    @Test
    void set_allAtOnce() {
        ProductBuilder other = new ProductBuilder();
        other.set("sku", sku);
        other.set("rrp", Double.toString(price));
        other.set("name", name);
        systemUnderTest.set(other);

        assertThat(systemUnderTest.build())
                .isEqualTo(SCOOBY_DOO);
    }

}
