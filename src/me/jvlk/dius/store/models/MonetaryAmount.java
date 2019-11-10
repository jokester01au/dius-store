package me.jvlk.dius.store.models;

import java.util.Collection;
import java.util.Objects;

/**
 * Immutable value object representing a
 */
public class MonetaryAmount {
    private int cents;

    public MonetaryAmount(double value) {

    }

    public MonetaryAmount vary(double ratio) {
        return null;
    }

    // FUTURE - gst settings
    // FUTURE - support multiple currencies - issues: conversion, validation, time differences?

    public static MonetaryAmount sum(Collection<MonetaryAmount> amounts) {
        return null;
    }

    public static final MonetaryAmount FREE = new MonetaryAmount(0);

    @Override
    public String toString() {
        return "$" + (cents / 100);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonetaryAmount that = (MonetaryAmount) o;
        return cents == that.cents;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cents);
    }
}
