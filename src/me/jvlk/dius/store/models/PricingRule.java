package me.jvlk.dius.store.models;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static me.jvlk.dius.store.Utils.zip;

public abstract class PricingRule {
    private Date startsOn;
    private Date endsOn;
    protected Product target;

    public PricingRule(Date startsOn, Date endsOn, Product target) {
        this.target = Objects.requireNonNull(target);
        Objects.requireNonNull(startsOn);
        Objects.requireNonNull(endsOn);
        switch (endsOn.compareTo(startsOn)) {
            default:
            case 0:
                throw new IllegalArgumentException(String.format("PricingRule has empty date range: [%s, %s)", startsOn, endsOn));
            case 1:

                this.startsOn = startsOn;
                this.endsOn = endsOn;
                break;
            case -1:
                // convenience to swap out of order dates
                this.startsOn = endsOn;
                this.endsOn = startsOn;
                break;
        }

    }

    public Date getStartsOn() {
        return startsOn;
    }

    public Date getEndsOn() {
        return endsOn;
    }

    public Product getTarget() {
        return target;
    }

    public boolean isActive(Date purchaseDate) {
        return (startsOn.before(purchaseDate) || startsOn.equals(purchaseDate)) &&
                endsOn.after(purchaseDate);
    }

    public abstract List<Priced> apply(List<Priced> cart);


    // FUTURE: authorisedBy - link to a user with the requisite authority to make pricing decisions
    // FUTURE: customerConstraints - only apply to customers matching the given characteristics (eg. referred from some affiiate, has a history of a certain volume, etc)
    // FUTURE: voucherCode - the pricing rule only applies if a particular code is provided at checkout

    public static List<Priced> applyAll(Collection<PricingRule> rules, Date purchaseDate, List<Priced> cart) {
        return rules.stream()
                .filter(rule -> rule.isActive(purchaseDate))
                .reduce(cart,
                        (modifiedCart, rule) -> rule.apply(modifiedCart),
                        (cartA, cartB) -> zip(cartA.stream(), cartB.stream(), Priced::pick).collect(toList())
                );
    }

    public static PricingRule fromMap(Map<String, String> fields) {
        return null;
    }

    @Override
    public String toString() {
        return String.format("valid %1$td-%1$tm-%1$tY to %2$td-%2$tm-%2$tY", startsOn, endsOn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PricingRule that = (PricingRule) o;
        return startsOn.equals(that.startsOn) &&
                endsOn.equals(that.endsOn) &&
                target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startsOn, endsOn, target);
    }

}
