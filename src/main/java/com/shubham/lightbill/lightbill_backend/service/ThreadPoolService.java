package com.shubham.lightbill.lightbill_backend.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class ThreadPoolService {
    private static final int CORE_POOL_SIZE = 2;  // Minimum number of threads
    private static final int MAX_POOL_SIZE = 4;   // Maximum number of threads
    private static final int KEEP_ALIVE_TIME = 60; // Keep alive time for excess threads
    private static final int QUEUE_CAPACITY = 10; // Capacity of the waiting queue
    private final ThreadPoolExecutor executorService;

    public ThreadPoolService(){
        this.executorService = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(QUEUE_CAPACITY)
        );
    }

    public synchronized void submitTask(Runnable task){
        executorService.submit(task);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
