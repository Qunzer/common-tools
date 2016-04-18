package common.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by rq on 2016/4/18.
 */
public class BatchExecutor<T> implements Runnable {
    private final int batchSize;
    private final Closure<T> closure;
    private static final int DEFAULT_QUEUE_SIZE = 1000;
    private static final int DEFAULT_PROCESS_THREADS = Runtime.getRuntime().availableProcessors() + 1;
    private int queueSize = DEFAULT_QUEUE_SIZE;
    private int threads = DEFAULT_PROCESS_THREADS;
    private BlockingQueue<T> queue;
    private ThreadPoolExecutor executor;

    public BatchExecutor(String name, int batchSize, int queueSize, Closure<T> closure) {
        Preconditions.checkNotNull(closure);
        this.batchSize = batchSize;
        this.closure = closure;
        this.queueSize = queueSize;
    }

    @PostConstruct
    public void init() {
        this.queue = new LinkedBlockingQueue<T>(this.queueSize);
        if (this.executor == null) {
            this.executor = new ThreadPoolExecutor(1, threads, 1L, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(1), new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    return new Thread();
                }
            });
            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        }
    }

    public boolean addItem(T t) {
        boolean offer = this.queue.offer(t);
        if (!offer) {
            this.executor.execute(this);
        }
        return offer;
    }

    public boolean addItem(T t, long timeout, TimeUnit unit) throws InterruptedException {
        boolean offer = this.queue.offer(t, timeout, unit);
        if (offer) {
            this.executor.execute(this);
        }
        return offer;
    }

    public void run() {
        if (!this.queue.isEmpty()) {
            List<T> list = Lists.newArrayListWithCapacity(batchSize);
            int size = this.queue.drainTo(list, batchSize);
            if (size > 0) {
                this.closure.process(list);
            }
        }
    }

    @PreDestroy
    public void destroy() {
        if (executor != null) {
            executor.shutdown();
        }
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public void setExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
    }
}
