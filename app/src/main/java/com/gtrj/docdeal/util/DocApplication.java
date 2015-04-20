package com.gtrj.docdeal.util;

import android.app.Application;
import android.util.LruCache;

/**
 * Created by zhang77555 on 2015/4/16.
 */
public class DocApplication extends Application{
    public static LruCache lruCache;
    private int cacheSize=4*1024*1024;
    @Override
    public void onCreate()
    {
        lruCache=new LruCache(cacheSize);
        super.onCreate();

    }


}
