package info.kgeorgiy.ja.mironov.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.*;

import static java.util.concurrent.Executors.newFixedThreadPool;

public class WebCrawler implements Crawler {
    private final Downloader downloader;
    private final ExecutorService downloaders;
    private final ExecutorService extractors;
    private final int perHost;
    private final Map<String, Semaphore> hosts;


    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
        this.downloader = downloader;
        this.downloaders = newFixedThreadPool(downloaders);
        this.extractors = newFixedThreadPool(extractors);
        this.perHost = perHost;
        this.hosts = new ConcurrentHashMap<>();
    }


    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.err.println("Expected: url [depth [downloads [extractors [perHost]]]]");
        } else {
            int depth, downloaders, extractors, perHost;
            depth = parseArgument(args, 1, 1);
            downloaders = parseArgument(args, 2, 1);
            extractors = parseArgument(args, 3, 1);
            perHost = parseArgument(args, 4, 1);

            try (WebCrawler crawler = new WebCrawler(new CachingDownloader(1), downloaders, extractors, perHost)) {
                crawler.download(args[0], depth);
            } catch (IOException e) {
                System.err.println("Failed to create downloader: " + e.getMessage());
            }
        }
    }


    public static int parseArgument(String[] args, int index, int defaultValue) {
        if (index < args.length) {
            return Integer.parseInt(args[index]);
        }
        return defaultValue;

    }

    /**
     * loads the specified web page, extracts links from it and adds them to the waiting list
     *
     * @param link         link to the web page.
     * @param depth        the depth to which the page should be loaded
     * @param level        phaser level
     * @param result       a set of links to downloaded web pages
     * @param excep        Map of exceptions that occur during the loading of web pages.
     * @param waitingQueue queue of links to web pages that have not yet been loaded.
     */
    private void arrDownload(final String link, final int depth, final Phaser level,
                             final Set<String> result, final ConcurrentMap<String, IOException> excep,
                             final ConcurrentLinkedQueue<String> waitingQueue) {
        String host;
        try {
            host = URLUtils.getHost(link);


            Semaphore semaphore = hosts.computeIfAbsent(host, s -> new Semaphore(perHost));
            try {
                semaphore.acquire();
            } catch (InterruptedException ignored) {

            }

            level.register();
            final Phaser phaser = new Phaser(1);
            downloaders.submit(() -> {
                try {
                    Document document = downloader.download(link);
                    result.add(link);
                    if (depth > 1) {
                        arrExtraction(document, level, waitingQueue);
                    }
                } catch (IOException e) {
                    excep.put(link, e);
                } finally {
                    level.arriveAndDeregister();
                    semaphore.release();
                    phaser.arriveAndDeregister();
                }
            });
        } catch (MalformedURLException e) {
            excep.put(link, e);

        }
    }

    /**
     * retrieves links from a given document and adds them to the waiting list
     *
     * @param document     the document from which you want to extract the links
     * @param level        phaser level
     * @param waitingQueue queue of links to web pages that have not yet been loaded.
     */
    private void arrExtraction(final Document document, final Phaser level,
                               final ConcurrentLinkedQueue<String> waitingQueue) {
        level.register();
        extractors.submit(() -> {
            try {
                List<String> links = document.extractLinks();
                waitingQueue.addAll(links);
            } catch (IOException ignored) {
            } finally {
                level.arrive();
            }
        });
    }

    /**
     * starts the process of downloading and retrieving links on the page at a given URL with a given depth of search
     *
     * @param url   start <a href="http://tools.ietf.org/html/rfc3986">URL</a>.
     * @param depth download depth.
     * @return Result - list of successfully loaded pages and error list
     */
    @Override
    public Result download(String url, int depth) {
        final ConcurrentLinkedQueue<String> waitingQueue = new ConcurrentLinkedQueue<>();
        final Set<String> result = ConcurrentHashMap.newKeySet();
        final ConcurrentMap<String, IOException> excep = new ConcurrentHashMap<>();
        final Phaser lock = new Phaser(0);

        final Set<String> extracted = ConcurrentHashMap.newKeySet();
        waitingQueue.add(url);

        iterateDepth(depth, waitingQueue, result, excep, lock, extracted);

        return new Result(new ArrayList<>(result), excep);
    }

    /**
     * iterated by depth
     *
     * @param depth        download depth.
     * @param waitingQueue queue of links to web pages that have not yet been loaded.
     * @param result       a set of links to downloaded web pages
     * @param excep        Map of exceptions that occur during the loading of web pages.
     * @param lock         current Phaser
     * @param extracted    many references already found in previous iterations
     */
    private void iterateDepth(int depth, ConcurrentLinkedQueue<String> waitingQueue, Set<String> result,
                              ConcurrentMap<String, IOException> excep, Phaser lock, Set<String> extracted) {
        for (int i = 0; i < depth; i++) {
            final Phaser level = new Phaser(1);
            List<String> processing = new ArrayList<>(waitingQueue);
            waitingQueue.clear();
            for (String link : processing) {
                if (extracted.add(link)) {
                    arrDownload(link, depth - i, level, result, excep, waitingQueue);
                }
            }
            lock.register();
            level.arriveAndAwaitAdvance();
        }
    }

    @Override
    public void close() {
        downloaders.shutdownNow();
        extractors.shutdownNow();
    }
}
