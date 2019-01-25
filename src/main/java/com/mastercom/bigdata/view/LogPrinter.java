package com.mastercom.bigdata.view;

import com.mastercom.bigdata.logic.service.ScheduledExecutorServiceHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kwong on 2018/1/24.
 */
public class LogPrinter {

    private static final Logger LOG = LoggerFactory.getLogger(LogPrinter.class);

    private LogPrinter(){}

    private static Queue<String> msgQueue;

    private static boolean isInit = true;

    public static void println(String msg){
        if (isInit){
            init();
        }
        msgQueue.add(msg + "\n");
    }

    private static void init(){
        synchronized (LogPrinter.class){
            msgQueue = new LinkedList<>();
            ScheduledExecutorServiceHolder.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        String msg = null;
                        if (MainFrame.getInstance().rightPanel != null && null != (msg = msgQueue.poll())){
                            MainFrame.getInstance().rightPanel.output(msg);
                        }
                    }catch (Throwable e){
                        LOG.error("", e);
                    }
                }
            }, 0L, 500L, TimeUnit.MILLISECONDS);
            isInit = false;
        }
    }
}
