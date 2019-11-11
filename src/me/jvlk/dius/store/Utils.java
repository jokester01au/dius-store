package me.jvlk.dius.store;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.IMMUTABLE;

public class Utils {
    /**
     * Adapted from JDK 1.8b93 code with reference to https://stackoverflow.com/questions/17640754/zipping-streams-using-jdk8-with-lambda-java-util-stream-streams-zip
     *
     * @param a the first stream
     * @param b the second stream
     * @param zipper a function to combine the two streams into a single object
     * @param <A> the type of the first stream
     * @param <B> the type of the second stream
     * @param <C> the type of the zipped object
     * @return a stream of zipped objects
     */
    public static<A, B, C> Stream<C> zip(Stream<? extends A> a,
                                         Stream<? extends B> b,
                                         BiFunction<? super A, ? super B, ? extends C> zipper) {
        Objects.requireNonNull(zipper);
        Spliterator<? extends A> aSpliterator = Objects.requireNonNull(a).spliterator();
        Spliterator<? extends B> bSpliterator = Objects.requireNonNull(b).spliterator();

        // Zipping looses DISTINCT and SORTED characteristics
        int characteristics = aSpliterator.characteristics() & bSpliterator.characteristics() &
                ~(Spliterator.DISTINCT | Spliterator.SORTED);

        long zipSize = ((characteristics & Spliterator.SIZED) != 0)
                ? Math.min(aSpliterator.getExactSizeIfKnown(), bSpliterator.getExactSizeIfKnown())
                : -1;

        Iterator<A> aIterator = Spliterators.iterator(aSpliterator);
        Iterator<B> bIterator = Spliterators.iterator(bSpliterator);
        Iterator<C> cIterator = new Iterator<C>() {
            @Override
            public boolean hasNext() {
                return aIterator.hasNext() && bIterator.hasNext();
            }

            @Override
            public C next() {
                return zipper.apply(aIterator.next(), bIterator.next());
            }
        };

        Spliterator<C> split = Spliterators.spliterator(cIterator, zipSize, characteristics);
        return (a.isParallel() || b.isParallel())
               ? StreamSupport.stream(split, true)
               : StreamSupport.stream(split, false);
    }

    public static<A> Stream<A> stream(Iterator<A> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, IMMUTABLE), false);

    }
    public static<A> Stream<A> stream(Supplier<Boolean> hasNext, Supplier<A> next) {
        return stream(new Iterator<A>() {
            @Override
            public boolean hasNext() {
                return hasNext.get();
            }

            @Override
            public A next() {
                return next.get();
            }
        });
    }


    public static class Tuple2<A, B> {
        public final A a;
        public final B b;


        public Tuple2(A a, B b) {
            this.a = a;
            this.b = b;
        }
    }

    /**
     * Hacky class to propagate an exception through a stream.
     * Java doesnt support generic exception clauses so we lose typesafety here
     */
    public static class WrappedException extends RuntimeException {
        public WrappedException(Exception wrapped) {
            super(wrapped);
        }

        public <T extends Throwable> T unwrap(Class<T> exceptionType) {
            return (T) getCause();
        }
    }

}
