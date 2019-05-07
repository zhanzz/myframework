package com.framework.common.manager;

import android.content.ComponentCallbacks2;

import com.facebook.common.disk.DiskTrimmable;
import com.facebook.common.disk.DiskTrimmableRegistry;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzhiqiang on 2018/2/10.
 */

public class FrescoMemoryManager implements DiskTrimmableRegistry,MemoryTrimmableRegistry {
    //private List<DiskTrimmable> trimmables = new ArrayList<>();
    private List<MemoryTrimmable> memoryTrimmables = new ArrayList<>();

    private static FrescoMemoryManager memoryManager;
    private FrescoMemoryManager(){

    }

    public static FrescoMemoryManager getInstance(){
        if (memoryManager == null) {
            synchronized (FrescoMemoryManager.class) {
                if (memoryManager == null) {
                    memoryManager = new FrescoMemoryManager();
                }
            }
        }
        return memoryManager;
    }

    @Override
    public void registerDiskTrimmable(DiskTrimmable trimmable) {
        //trimmables.add(trimmable);
    }

    @Override
    public void unregisterDiskTrimmable(DiskTrimmable trimmable) {
        //trimmables.remove(trimmable);
    }

    @Override
    public void registerMemoryTrimmable(MemoryTrimmable trimmable) {
        memoryTrimmables.add(trimmable);
    }

    @Override
    public void unregisterMemoryTrimmable(MemoryTrimmable trimmable) {
        memoryTrimmables.remove(trimmable);
    }

    public void trim(int level){
        if(level>= ComponentCallbacks2.TRIM_MEMORY_COMPLETE){
            for(MemoryTrimmable memoryTrimmable:memoryTrimmables){
                memoryTrimmable.trim(MemoryTrimType.OnSystemLowMemoryWhileAppInBackground);
            }
        }
    }
}
