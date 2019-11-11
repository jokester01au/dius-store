package me.jvlk.dius.store.persistence;

import me.jvlk.dius.store.Utils;
import me.jvlk.dius.store.Utils.Tuple2;
import me.jvlk.dius.store.Utils.WrappedException;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static me.jvlk.dius.store.Utils.stream;
import static me.jvlk.dius.store.Utils.zip;

/**
 *
 * FUTURE - cope with nested quotes
 * @param <P>
 */
public class CsvLoader<P> implements Loader<P> {

    private final Class<P> forClass;
    private final BufferedReader csvData;
    private List<String> fieldNames;
    private boolean complete = false;

    public CsvLoader(Class<P> forClass, BufferedReader csvData) {
        this.forClass = forClass;

        this.csvData = csvData;
    }

    private class LineReader implements Iterator<String> {
        private boolean complete = false;

        public boolean hasNext() {
            return ! complete;
        }

        public String next() {
            StringBuilder sb = new StringBuilder();
            boolean inQuote = false;

            while (true) {
                try {
                    int chr = csvData.read();
                    switch (chr) {
                        case '"':
                            inQuote = ! inQuote;
                            continue;
                        case ',':
                        case '\n':
                            if (! inQuote) break;
                            /* fall through */
                        default:
                            sb.append((char) chr);
                            continue;
                        case -1:
                            CsvLoader.this.complete = true;
                            break;
                    }

                    if (chr != ',') this.complete = true;
                } catch (EOFException e) {
                    CsvLoader.this.complete = true;
                    this.complete = true;
                } catch (IOException e) {
                    throw new WrappedException(e);
                }
                return sb.toString().strip();
            }
        }
    }

    private Stream<String> readLine() {
        return stream(new LineReader());
    }

    @Override
    public List<P> loadAll() throws IOException {
        try {
            this.fieldNames = readLine().collect(toUnmodifiableList());
            return stream(
                    () -> ! this.complete,
                    () -> zip(this.fieldNames.stream(), readLine(), Tuple2::new)
                        .reduce(Builders.getInstance().get(this.forClass),
                                (builder, tuple) -> builder.set(tuple.a, tuple.b),
                                Builder::set
                        ).build()
            ).collect(toUnmodifiableList());
        } catch (WrappedException e) {
            throw e.unwrap(IOException.class);
        }
    }

}
