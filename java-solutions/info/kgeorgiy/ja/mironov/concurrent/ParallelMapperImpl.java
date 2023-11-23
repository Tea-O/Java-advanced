package info.kgeorgiy.ja.mironov.concurrent;


import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;

/**
 * Class implements ParallelMapper
 */
public class ParallelMapperImpl implements ParallelMapper {

    private final List<Thread> workers;
    private final Queue<Runnable> tasks;

    private static class TaskCollector<R> {
        private final List<R> list;
        private int count;

        TaskCollector(int size) {
            count = 0;
            list = new ArrayList<>(Collections.nCopies(size, null));
        }

        synchronized void setToList(final int position, R element) {
            list.set(position, element);
            count++;
            if (count == list.size()) {
                notify();
            }
        }

        synchronized List<R> getList() throws InterruptedException {
            while (count < list.size()) {
                wait();
            }
            return list;
        }
    }

    /**
     * Thread constructor
     * Creates a ParallelMapperImpl instance operating with maximum of threads
     *
     * @param listOfThreadsCount maximum count of operable threads
     */
    public ParallelMapperImpl(final int listOfThreadsCount) {
        tasks = new ArrayDeque<>();
        workers = new ArrayList<>();
        for (int i = 0; i < listOfThreadsCount; i++) {
            final Thread thread = new Thread(() -> {
                try {
                    while (!Thread.interrupted()) {
                        pollTask().run();
                    }
                } catch (final InterruptedException ignored) {
                } finally {
                    Thread.currentThread().interrupt();
                }
            });
            workers.add(thread);
            thread.start();
        }
    }

    private Runnable pollTask() throws InterruptedException {
        synchronized (tasks) {
            while (tasks.isEmpty()) {
                tasks.wait();
            }
            return tasks.poll();
        }
    }

    private void addTask(final Runnable task) throws InterruptedException {
        synchronized (tasks) {
            tasks.add(task);
            tasks.notifyAll();
        }
    }

    /**
     * Maps function {@code function} over specified {@code list}.
     * Mapping for each element performs in parallel.
     *
     * @param f    function to map
     * @param args arguments
     * @param <T>  value type
     * @param <R>  result type
     * @return mapped function
     * @throws InterruptedException InterruptedException if calling thread was interrupted
     */
    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> f, List<? extends T> args) throws InterruptedException {
        final TaskCollector<R> collector = new TaskCollector<>(args.size());
        for (int i = 0; i < args.size(); i++) {
            final int pos = i;
            addTask(() -> {
                collector.setToList(pos, f.apply(args.get(pos)));
            });
        }
        return collector.getList();
    }

    /**
     * Stops all threads. All unfinished mappings leave in undefined state.
     */
    @Override
    public void close() {
        for (Thread worker : workers) {
            worker.interrupt();
        }
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                worker.interrupt();
            }
        }
    }
}