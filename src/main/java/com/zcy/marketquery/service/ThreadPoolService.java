package com.zcy.marketquery.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ThreadPoolService {
    public static ExecutorService receiveToFileExecutor = Executors.newFixedThreadPool(30);

    public static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    public static ExecutorService writeExecutor = Executors.newFixedThreadPool(2);

}
