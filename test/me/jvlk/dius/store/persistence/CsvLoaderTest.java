package me.jvlk.dius.store.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class CsvLoaderTest {
    private CsvLoader<TestObject> systemUnderTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void loadAll() {

        fail("FIXME - implement test");
    }

    @Test
    void fromFile() {

        fail("FIXME - implement test");
    }

    private static class TestObject {
        public final int integer;
        public final double decimal;

        public final String string;
        public final String quotedString;

        private TestObject(int integer, double decimal, String string, String quotedString) {
            this.integer = integer;
            this.decimal = decimal;
            this.string = string;
            this.quotedString = quotedString;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestObject that = (TestObject) o;
            return integer == that.integer &&
                    Double.compare(that.decimal, decimal) == 0 &&
                    string.equals(that.string) &&
                    quotedString.equals(that.quotedString);
        }

        @Override
        public int hashCode() {
            return Objects.hash(integer, decimal, string, quotedString);
        }
    }
}