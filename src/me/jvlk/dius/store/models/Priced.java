package me.jvlk.dius.store.models;

import java.util.Objects;

public interface Priced extends Comparable<Priced> {
    MonetaryAmount getPrice();
    Product getProduct();

}
