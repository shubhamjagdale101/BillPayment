package com.shubham.lightbill.lightbill_backend.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ThreadPoolService {
    private static final int THREAD_POOL_SIZE = 2;
    private static ThreadPoolService instance;
    private final ExecutorService executorService;

    private ThreadPoolService(){
        this.executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    public static synchronized ThreadPoolService getInstance(){
        if(instance == null) instance = new ThreadPoolService();
        return instance;
    }

    public void submitTask(Runnable task){
        executorService.submit(task);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
