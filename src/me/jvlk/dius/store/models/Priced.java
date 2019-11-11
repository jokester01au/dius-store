package me.jvlk.dius.store.models;

import java.util.Objects;

public interface Priced extends Comparable<Priced> {
    MonetaryAmount getPrice();
    Product getProduct();

    default Priced pick(Priced other) {
        if ( ! this.getProduct().equals(other.getProduct()) ) {
            throw new IllegalArgumentException(String.format("Cannot pick between two unrelated products (%s, %s)", this, other));
        }
        if (other.getPrice().compareTo(this.getPrice()) < 0) {
            return other;
        }
        return this;
    }

    @Override
    default int compareTo(Priced o) {
        Objects.requireNonNull(this.getProduct());
        Objects.requireNonNull(o.getProduct());
        Objects.requireNonNull(this.getPrice());
        Objects.requireNonNull(o.getPrice());
        if (this.getProduct().equals(o.getProduct())) {
            return -1;
        }
        return this.getPrice().compareTo(o.getPrice());
    }
}
