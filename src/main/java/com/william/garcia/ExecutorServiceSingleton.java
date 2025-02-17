package com.william.garcia;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceSingleton {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(5); // 5 hilos

    private ExecutorServiceSingleton() { }

    public static ExecutorService getExecutorService() {
        return executorService;
    }
}
