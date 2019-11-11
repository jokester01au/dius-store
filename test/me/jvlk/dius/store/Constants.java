package me.jvlk.dius.store;

import me.jvlk.dius.store.models.MonetaryAmount;
import me.jvlk.dius.store.models.Priced;
import me.jvlk.dius.store.models.PricedProduct;
import me.jvlk.dius.store.models.PricingRule;
import me.jvlk.dius.store.models.Product;

import java.util.Date;

import static me.jvlk.dius.store.models.MonetaryAmount.FREE;

/**
 * FUTURE - these may be better as mocks so as not to pollute test logic
 */
public interface Constants {

    MonetaryAmount HALF = new MonetaryAmount(0.5);
    MonetaryAmount ONE = new MonetaryAmount(1);
    MonetaryAmount TWO = new MonetaryAmount(2);
    MonetaryAmount TWO_MILLION = new MonetaryAmount(2000000);

    Date BEFORE = new Date(100, 1, 1);
    Date ONE_WEEK_AGO = new Date(119, 11, 3);
    Date TODAY = new Date(119, 11, 10);
    Date ONE_WEEK_LATER = new Date(119, 11, 17);
    Date AFTER = new Date(199, 1, 1);

    Product ONE_DOLLAR_BILL = new Product("odb", ONE, "One Dollar Bill");
    Product ELECTRICITY_BILL = new Product("eb", TWO, "Commemorative Electricity Bill");
    Product BILL_BAILEY = new Product("bb", TWO_MILLION, "Mark Robert Bailey");

    Priced FREE_ONE_DOLLAR_BILL = new PricedProduct(ONE_DOLLAR_BILL, FREE);
    Priced DISCOUNTED_ELECTRICITY_BILL = new PricedProduct(ELECTRICITY_BILL, ONE);
    Priced SUPER_DISCOUNTED_ELECTRICITY_BILL = new PricedProduct(ELECTRICITY_BILL, HALF);

    String sku = "scoobyDoo";
    String name = "Scooby";
    double price = 2.34;

    Product SCOOBY_DOO = new Product(sku, new MonetaryAmount(price), name);

    String ONE_WEEK_AGO_STRING = String.format("%1$td-%1$tm-%1$tY", ONE_WEEK_AGO);
    String ONE_WEEK_LATER_STRING = String.format("%1$td-%1$tm-%1$tY", ONE_WEEK_LATER);

}
