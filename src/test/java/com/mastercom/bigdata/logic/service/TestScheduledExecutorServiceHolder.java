package com.mastercom.bigdata.logic.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by Kwong on 2018/1/25.
 */
public class TestScheduledExecutorServiceHolder {


    @Test
    public void run() throws InterruptedException {
        final IntRef intRef = new IntRef();
        ScheduledExecutorServiceHolder.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("add:" + intRef.value);
                intRef.value += 1;

            }
        },0L, 200L, TimeUnit.MILLISECONDS);


        Thread.currentThread().sleep(2000);
        Assert.assertTrue(intRef.value == 10);
    }

    static class IntRef{

        public int value = 0;
    }
}
