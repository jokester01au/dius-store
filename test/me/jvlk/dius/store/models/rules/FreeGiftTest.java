package me.jvlk.dius.store.models.rules;

import me.jvlk.dius.store.Constants;
import me.jvlk.dius.store.models.Priced;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class FreeGiftTest implements Constants {
    private FreeGift systemUnderTest;

    @BeforeEach
    void setUp() {

    }


    private static Stream<Arguments> quantity1TestCases() {
        return Stream.of(
            Arguments.of(asList(BILL_BAILEY), asList(BILL_BAILEY), "If we dont find the matching item, the list should not change"),
            Arguments.of(asList(ONE_DOLLAR_BILL, BILL_BAILEY), asList(ONE_DOLLAR_BILL, BILL_BAILEY), "If we dont find the matching item, the list should not change, even if we find the gift"),
            Arguments.of(asList(BILL_BAILEY, ELECTRICITY_BILL), asList(BILL_BAILEY, ELECTRICITY_BILL, FREE_ONE_DOLLAR_BILL), "If we find the matching item, we should add the gift"),
            Arguments.of(asList(ONE_DOLLAR_BILL, BILL_BAILEY, ELECTRICITY_BILL), asList(FREE_ONE_DOLLAR_BILL, BILL_BAILEY, ELECTRICITY_BILL), "If we find the matching item, an existing gift should have its price set to ZERO"),
            Arguments.of(asList(ONE_DOLLAR_BILL, ELECTRICITY_BILL, BILL_BAILEY, ELECTRICITY_BILL), asList(FREE_ONE_DOLLAR_BILL, ELECTRICITY_BILL, BILL_BAILEY, ELECTRICITY_BILL, FREE_ONE_DOLLAR_BILL), "If we find the matching item, an existing gift should have its price set to ZERO, and another gift added")
                // ... there are numerous other edge cases worth testing here, but in the interests of time we'll leave it at that
        );
    }

    @ParameterizedTest
    @MethodSource("quantity1TestCases")
    void apply_quantity1(List<Priced> cart, List<Priced> expected, String description) {
        apply_quantityMany(cart, expected, 1, 1, description);
    }

private static Stream<Arguments> quantityManyTestCases() {
        return Stream.of(
            Arguments.of(asList(ELECTRICITY_BILL), asList(ELECTRICITY_BILL), 1, 2, "If we dont find enough of the matching item, the list should not change"),
            Arguments.of(asList(ELECTRICITY_BILL, ELECTRICITY_BILL), asList(ELECTRICITY_BILL, ELECTRICITY_BILL, FREE_ONE_DOLLAR_BILL), 1, 2, "If we find enough of the matching item, we should add the gift to the list"),
            Arguments.of(asList(ELECTRICITY_BILL, ELECTRICITY_BILL), asList(ELECTRICITY_BILL, ELECTRICITY_BILL, FREE_ONE_DOLLAR_BILL, FREE_ONE_DOLLAR_BILL), 2, 2, "If we find enough of the matching item, we should add the gifts to the list")
                // FUTURE - Further corner cases
                );
}

    @ParameterizedTest
    @MethodSource("quantityManyTestCases")
    void apply_quantityMany(List<Priced> cart, List<Priced> expected, int giftQuantity, int purchasedQuantity, String description) {
        systemUnderTest = new FreeGift(ONE_WEEK_AGO, ONE_WEEK_LATER, ONE_DOLLAR_BILL, giftQuantity, ELECTRICITY_BILL, purchasedQuantity);
        List<Priced> actual = systemUnderTest.apply(cart);
        assertThat(actual).containsExactlyElementsOf(expected).as(description);
    }

}