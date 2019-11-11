package me.jvlk.dius.store.persistence;

import java.io.BufferedReader;
import java.util.List;

/**
 *
 * FUTURE - cope with nested quotes
 * @param <P>
 */
public class CsvLoader<P> implements Loader<P> {

    public CsvLoader(Class<P> forClass, BufferedReader csvData) {
    }

    @Override
    public List<P> loadAll() {
        return null;
    }

}
