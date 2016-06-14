package com.myapplication.cache;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by admin on 2016/5/17.
 */
public class TaskCache {

    private static final ThreadFactory downloadThreadFactory = new ThreadFactory() {

        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return null;
        }
    };
}
