package com.example.demo.incremental_updating;

public class BsPatch {
    /**
     * 合并
     * @param oldfile
     * @param newfile
     * @param patchfile
     */
    public native static void patch(String oldfile,String patchfile,String newfile);

    static {
        System.loadLibrary("bspatch");
    }
}