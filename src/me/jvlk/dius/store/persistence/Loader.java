package me.jvlk.dius.store.persistence;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface Loader<P> {
    List<P> loadAll() throws IOException;

    // FUTURE load(sku) - an interface for retrieving a single product based on an sku will no
    // doubt eventually be needed, but is superfluous for this specific exercise.
}
