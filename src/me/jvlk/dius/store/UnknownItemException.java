package me.jvlk.dius.store;

public class UnknownItemException extends Exception {
    public UnknownItemException(String unknownSku) {

    }

    // FUTURE - provide a constructor that allows efficient input of other sku's to improve the user experience along the lines of "Did you mean...?"
}
