package com.mastercom.bigdata.logic.service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kwong on 2018/1/24.
 */
public class ScheduledExecutorServiceHolder {

    private static ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(5);

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                scheduler.shutdown();
                scheduler = null;
            }
        }));
    }

    public static ScheduledFuture<?> submit(Runnable command,
                                         long initialDelay,
                                         long period,
                                         TimeUnit unit){
        return scheduler.scheduleAtFixedRate(command, initialDelay, period, unit);
    }
}
