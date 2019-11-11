package me.jvlk.dius.store.models.rules;

import me.jvlk.dius.store.Constants;
import me.jvlk.dius.store.models.Priced;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class VolumePricingTest implements Constants {


    private VolumePricing systemUnderTest;

    private static Stream<Arguments> testCases() {

        return Stream.of(
                Arguments.of(asList(ELECTRICITY_BILL), asList(ELECTRICITY_BILL), 2, "If we dont find enough of the matching item, the list should not change"),
                Arguments.of(asList(ELECTRICITY_BILL, ELECTRICITY_BILL), asList(DISCOUNTED_ELECTRICITY_BILL, DISCOUNTED_ELECTRICITY_BILL), 2, "If we find enough of the matching item, we should discount the prices"),
                Arguments.of(asList(ELECTRICITY_BILL, SUPER_DISCOUNTED_ELECTRICITY_BILL), asList(DISCOUNTED_ELECTRICITY_BILL, SUPER_DISCOUNTED_ELECTRICITY_BILL), 2, "Don't discount it if its already cheaper"),
                Arguments.of(asList(ELECTRICITY_BILL, BILL_BAILEY, ELECTRICITY_BILL), asList(DISCOUNTED_ELECTRICITY_BILL, BILL_BAILEY, DISCOUNTED_ELECTRICITY_BILL), 2, "We shouldn't change the price of unrelated items"),
                Arguments.of(asList(ELECTRICITY_BILL, ELECTRICITY_BILL, ELECTRICITY_BILL), asList(DISCOUNTED_ELECTRICITY_BILL, DISCOUNTED_ELECTRICITY_BILL, DISCOUNTED_ELECTRICITY_BILL),  2, "If we find MORE than enough of the matching item, we should discount ALL the prices")
                // FUTURE - Further corner cases
        );
    }

    @ParameterizedTest
    @MethodSource("testCases")
    void apply(List<Priced> cart, List<Priced> expected, int minQuantity, String description) {
        systemUnderTest = new VolumePricing(ONE_WEEK_AGO, ONE_WEEK_LATER, ELECTRICITY_BILL, minQuantity, ONE);
        List<Priced> actual = systemUnderTest.apply(cart);
        assertThat(actual).containsExactlyElementsOf(expected).as(description);
    }

}