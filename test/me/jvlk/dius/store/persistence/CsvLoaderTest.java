package me.jvlk.dius.store.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * FIXME - this is quite a complex operation with a bunch of edge cases that are not currently tested. Ran out of time for full treatment.
 */
class CsvLoaderTest {
    private CsvLoader<TestObject> systemUnderTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void loadAll() throws IOException {
        BufferedReader csvData = new BufferedReader(new StringReader(
                "integer,decimal,string,quotedString\n" +
                        "1,2.5,blah,\"quoted blah,\"\n" +
                        "5, 3.2345,goobers, \"asaas,sdfg sdfg,\""
        ));
        List<TestObject> expected = asList(
                new TestObject(1, 2.5, "blah", "quoted blah,"),
                new TestObject(5, 3.2345, "goobers", "asaas,sdfg sdfg,")
        );
        systemUnderTest = new CsvLoader<>(TestObject.class, csvData);
        assertThat(systemUnderTest.loadAll()).containsExactlyElementsOf(expected)
                .as("CsvLoader should read the string into the objects as expected");
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

        @Override
        public String toString() {
            return "TestObject{" +
                    "integer=" + integer +
                    ", decimal=" + decimal +
                    ", string='" + string + '\'' +
                    ", quotedString='" + quotedString + '\'' +
                    '}';
        }
    }

    private static class TestObjectBuilder implements Builder<TestObject> {

        private Integer integer = null;
        private Double decimal = null;
        private String string = null;
        private String quotedString = null;

        @Override
        public TestObject build() {
            Objects.requireNonNull(integer);
            Objects.requireNonNull(decimal);
            Objects.requireNonNull(string);
            Objects.requireNonNull(quotedString);
            return new TestObject(integer, decimal, string, quotedString);
        }

        @Override
        public Builder<TestObject> set(String name, String value) {
            switch(name) {
                case "integer":
                    integer = Integer.parseInt(value);
                    break;
                case "decimal":
                    decimal = Double.parseDouble(value);
                    break;
                case "string":
                    string = value;
                    break;
                case "quotedString":
                    quotedString = value;
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Unknown field name (%s) with value %s", name, value));

            };
            return this;
        }

        @Override
        public Builder<TestObject> set(Builder<TestObject> other) {
            TestObjectBuilder o = (TestObjectBuilder) other; // FIXME - what if there are other builders?
            if (o.integer != null) this.integer = o.integer;
            if (o.decimal != null) this.decimal = o.decimal;
            if (o.string != null) this.string = o.string;
            if (o.quotedString != null) this.quotedString = o.quotedString;
            return this;
        }
    }

    static {
        Builders.getInstance().register(TestObject.class, TestObjectBuilder::new);
    }
}