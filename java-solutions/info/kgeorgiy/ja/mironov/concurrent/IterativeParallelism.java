package info.kgeorgiy.ja.mironov.concurrent;

import info.kgeorgiy.java.advanced.concurrent.ListIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;


import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Implementation of th {@code ListIP} interface, providing methods for parallel data processing
 */
public class IterativeParallelism implements ListIP {

    private final ParallelMapper mapper;

    /**
     * Mapper constructor.
     *
     * @param mapper {@link ParallelMapper} instance
     */
    public IterativeParallelism(ParallelMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Default constructor.
     */
    public IterativeParallelism() {
        this.mapper = null;
    }


    /**
     * return maximum of list
     *
     * @param i          number of concurrent threads.
     * @param list       values to get maximum of.
     * @param comparator value comparator.
     * @param <T>        value of type
     * @return maximum <T> of list
     * @throws InterruptedException If a single stream is interrupted
     */
    @Override
    public <T> T maximum(int i, List<? extends T> list, Comparator<? super T> comparator) throws InterruptedException {
        return multiThreading(i, list,
                stream -> stream.max(comparator).orElseThrow(),
                stream -> stream.max(comparator).orElseThrow());
    }

    /**
     * return minimum of list
     *
     * @param i          number of concurrent threads.
     * @param list       values to get minimum of.
     * @param comparator value comparator.
     * @param <T>        value of type
     * @return minimum <T> of list
     * @throws InterruptedException If a single stream is interrupted
     */
    @Override
    public <T> T minimum(int i, List<? extends T> list, Comparator<? super T> comparator) throws InterruptedException {
        return maximum(i, list, Collections.reverseOrder(comparator));
    }


    /**
     * returns all values that satisfy the predicate
     *
     * @param i         number of concurrent threads.
     * @param list      values to test.
     * @param predicate test predicate.
     * @param <T>       value type
     * @return all values that satisfy the predicate
     * @throws InterruptedException If a single stream is interrupted
     */
    @Override
    public <T> boolean all(int i, List<? extends T> list, Predicate<? super T> predicate) throws InterruptedException {
        return multiThreading(i, list,
                stream -> stream.allMatch(predicate),
                stream -> stream.allMatch(Boolean::booleanValue));
    }

    /**
     * return any value that satisfy the predicate
     *
     * @param i         number of concurrent threads.
     * @param list      values to test.
     * @param predicate test predicate.
     * @param <T>       value type
     * @return any values that satisfy the predicate
     * @throws InterruptedException If a single stream is interrupted
     */

    @Override
    public <T> boolean any(int i, List<? extends T> list, Predicate<? super T> predicate) throws InterruptedException {
        return !all(i, list, predicate.negate());
    }

    /**
     * return count of value that satisfy the predicate
     *
     * @param threads   number of concurrent threads.
     * @param values    values to test.
     * @param predicate test predicate.
     * @param <T>       value type
     * @return count of value that satisfy the predicate
     * @throws InterruptedException If a single stream is interrupted
     */
    @Override
    public <T> int count(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return multiThreading(threads, values,
                stream -> (int) stream.filter(predicate).count(),
                stream -> (Integer) stream.mapToInt(Integer::intValue).sum());
    }

    /**
     * concat value to string
     *
     * @param i    number of concurrent threads.
     * @param list values to join.
     * @return list of concat result the {@link String}
     * @throws InterruptedException If a single stream is interrupted
     */
    @Override
    public String join(int i, List<?> list) throws InterruptedException {
        return multiThreading(i, list,
                stream -> stream.map(Object::toString).collect(Collectors.joining()),
                stream -> stream.collect(Collectors.joining()));
    }


    /**
     * filter value by predicate
     *
     * @param i         number of concurrent threads.
     * @param list      values to filter.
     * @param predicate filter predicate.
     * @param <T>       type of value
     * @return filtered list of value
     * @throws InterruptedException If a single stream is interrupted
     */
    @Override
    public <T> List<T> filter(int i, List<? extends T> list, Predicate<? super T> predicate) throws InterruptedException {
        return multiThreading(i, list,
                stream -> stream.filter(predicate).collect(Collectors.toList()),
                stream -> stream.flatMap(Collection::stream).collect(Collectors.toList()));
    }

    /**
     * Mapping values
     *
     * @param i        number of concurrent threads.
     * @param list     values to map.
     * @param function mapper function.
     * @param <T>      type of value
     * @param <U>      function
     * @return list of value mapped by function
     * @throws InterruptedException If a single stream is interrupted
     */
    @Override
    public <T, U> List<U> map(int i, List<? extends T> list, Function<? super T, ? extends U> function) throws InterruptedException {
        return multiThreading(i, list,
                stream -> stream.map(function).collect(Collectors.toList()),
                stream -> stream.flatMap(Collection::stream).collect(Collectors.toList()));
    }


    /**
     * Splits the execution of this function into threads
     *
     * @param numOfThreads number of concurrent threads
     * @param list         list of value
     * @param task         function to perform
     * @param ansCollector function to collect the answer
     * @param <T>          value of type
     * @param <R>          resulting type
     * @return returns the sheet after executing the function
     * @throws InterruptedException If a single stream is interrupted
     */
   /* private <T, R> R multiThreading(int numOfThreads,
                                    List<? extends T> list,
                                    Function<Stream<? extends T>, R> task,
                                    Function<Stream<? extends R>, R> ansCollector) throws InterruptedException {

        if (list == null) {
            throw new IllegalArgumentException("the list must not be null.");
        }

        if (numOfThreads <= 0) {
            throw new IllegalArgumentException("the number of threads must be greater than zero.");
        }

        numOfThreads = Math.max(1, Math.min(numOfThreads, list.size()));
        List<Stream<? extends T>> parts = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        List<R> ansThreads = new ArrayList<>(Collections.nCopies(numOfThreads, null));


        final int partSize = list.size() / numOfThreads;
        final int rest = list.size() % numOfThreads;

        IntStream.range(0, numOfThreads).forEach(i -> {
            final int left = i * partSize + Math.min(i, rest);
            final int right = left + partSize + (i < rest ? 1 : 0);
            if (mapper != null) {
                setMapper(parts, list.subList(left, right).stream());
            } else {
                Thread partThread = new Thread(() -> {
                    ansThreads.set(i, task.apply(list.subList(left, right).stream()));
                });
                partThread.start();
                threads.add(partThread);

            }
        });

        return collectAns(threads, ansThreads, task, parts, ansCollector);
    }*/
    private <T, R> R multiThreading(int numOfThreads,
                                    List<? extends T> list,
                                    Function<Stream<? extends T>, R> task,
                                    Function<Stream<? extends R>, R> ansCollector) throws InterruptedException {

        if (list == null) {
            throw new IllegalArgumentException("the list must not be null.");
        }

        if (numOfThreads <= 0) {
            throw new IllegalArgumentException("the number of threads must be greater than zero.");
        }

        numOfThreads = Math.max(1, Math.min(numOfThreads, list.size()));
        List<Stream<? extends T>> parts = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        List<R> ansThreads = new ArrayList<>(Collections.nCopies(numOfThreads, null));

        final int partSize = list.size() / numOfThreads;
        final int rest = list.size() % numOfThreads;

        if (mapper != null) {
            IntStream.range(0, numOfThreads).forEach(i -> {
                final int left = i * partSize + Math.min(i, rest);
                final int right = left + partSize + (i < rest ? 1 : 0);
                setMapper(parts, list.subList(left, right).stream());
            });
        } else {

            IntStream.range(0, numOfThreads).forEach(i -> {
                final int left = i * partSize + Math.min(i, rest);
                final int right = left + partSize + (i < rest ? 1 : 0);

                Thread partThread = new Thread(() -> {
                    ansThreads.set(i, task.apply(list.subList(left, right).stream()));
                });
                partThread.start();
                threads.add(partThread);


            });
        }

        return collectAns(threads, ansThreads, task, parts, ansCollector);
    }

    /**
     * collect threads to ansCollector
     *
     * @param threads      list of current threads
     * @param ansThreads   list of thread after exposure to the function
     * @param task         function to perform
     * @param parts        list for map threads
     * @param ansCollector function to collect the answer
     * @param <R>          value of type
     * @param <T>          resulting type
     * @return returns the sheet after executing the function
     * @throws InterruptedException If a single stream is interrupted
     */
    private <R, T> R collectAns(List<Thread> threads, List<R> ansThreads, Function<Stream<? extends T>, R> task,
                                List<Stream<? extends T>> parts, Function<Stream<? extends R>, R> ansCollector) throws InterruptedException {
        if (mapper == null) {
            threads.forEach(thread -> {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    thread.interrupt();
                }
            });
        } else {
            ansThreads = mapper.map(task, parts);
        }
        return ansCollector.apply(ansThreads.stream());
    }

    private <T> void setMapper(List<Stream<? extends T>> parts, Stream<? extends T> stream) {
        parts.add(stream);
    }

}



