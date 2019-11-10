package me.jvlk.dius.store.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @param <P>
 */
public class CsvLoader<P> implements Loader<P> {

    public CsvLoader(Function<Map<String, String>, P> mapFunction, BufferedReader csvData) {

    }

    @Override
    public Set<P> loadAll() {
        return null;
    }

    public static <P> CsvLoader<P> fromFile(Function<Map<String, String>, P> mapFunction, File csvFile) {
        return null;
    }

}
