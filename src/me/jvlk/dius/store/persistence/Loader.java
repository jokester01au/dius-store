package me.jvlk.dius.store.persistence;

import java.util.Set;

public interface Loader<P> {
    Set<P> loadAll();

    // FUTURE load(sku) - an interface for retrieving a single product based on an sku will no
    // doubt eventually be needed, but is superfluous for this specific exercise.
}
