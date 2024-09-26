package com.shubham.lightbill.lightbill_backend.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ThreadPoolService {
    private static final int THREAD_POOL_SIZE = 2;
    private final ExecutorService executorService;

    public ThreadPoolService(){
        this.executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    public synchronized void submitTask(Runnable task){
        executorService.submit(task);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
