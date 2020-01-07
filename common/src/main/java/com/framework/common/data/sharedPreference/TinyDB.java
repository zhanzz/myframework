package com.framework.common.data.sharedPreference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public final class TinyDB {
    SharedPreferences preferences;

    public TinyDB(Context appContext) {
        if(appContext==null){
            throw new IllegalArgumentException("context 不能为null");
        }
        this.preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    public TinyDB(Context appContext, String tinydbname) {
        if(appContext==null){
            throw new IllegalArgumentException("context 不能为null");
        }
        this.preferences = appContext.getSharedPreferences(tinydbname, Context.MODE_PRIVATE);
    }

    public int getInt(String key) {
        return this.preferences.getInt(key, 0);
    }

    public int getInt(String key,int defaultValue) {
        return this.preferences.getInt(key, defaultValue);
    }

    public long getLong(String key) {
        return this.preferences.getLong(key, 0L);
    }

    public long getLongDefaultMaxValue(String key) {
        return this.preferences.getLong(key, 9223372036854775807L);
    }

    public String getString(String key) {
        return this.preferences.getString(key, "");
    }

    public String getString(String key, String defVal) {
        return this.preferences.getString(key, defVal);
    }

    public double getDouble(String key) {
        String number = this.getString(key);
        try {
            double e = Double.parseDouble(number);
            return e;
        } catch (NumberFormatException var5) {
            return 0.0D;
        }
    }

    public void putInt(String key, int value) {
        Editor editor = this.preferences.edit();
        editor.putInt(key, value).commit();
    }

    public void putLong(String key, long value) {
        Editor editor = this.preferences.edit();
        editor.putLong(key, value).commit();
    }

    public void putDouble(String key, double value) {
        this.putString(key, String.valueOf(value));
    }

    public void putString(String key, String value) {
        Editor editor = this.preferences.edit();
        editor.putString(key, value).commit();
    }

    /*1. apply没有返回值而commit返回boolean表明修改是否提交成功
    2. apply是将修改数据原子提交到内存, 而后异步真正提交到硬件磁盘,
    而commit是同步的提交到硬件磁盘，因此，在多个并发的提交commit的时候，
    他们会等待正在处理的commit保存到磁盘后在操作，从而降低了效率。
    而apply只是原子的提交到内容，后面有调用apply的函数的将会直接覆盖前面的内存数据，
    这样从一定程度上提高了很多效率。
    3. apply方法不会提示任何失败的提示。
    由于在一个进程中，sharedPreference是单实例，一般不会出现并发冲突，
    如果对提交的结果不关心的话，建议使用apply，当然需要确保提交成功且有后续操作的话，还是需要用commit的。*/
    //避免在页面过多的调用 https://blog.csdn.net/qq_16188829/article/details/78597427
    public void putStringAsy(String key, String value) {
        Editor editor = this.preferences.edit();
        editor.putString(key, value).apply();
    }

    public void putList(String key, ArrayList<String> marray) {
        Editor editor = this.preferences.edit();
        String[] mystringlist = marray.toArray(new String[marray.size()]);
        editor.putString(key, TextUtils.join("‚‗‚", mystringlist)).commit();
    }

    public ArrayList<String> getList(String key) {
        String[] mylist = TextUtils.split(this.preferences.getString(key, ""), "‚‗‚");
        ArrayList gottenlist = new ArrayList(Arrays.asList(mylist));
        return gottenlist;
    }

    public void putListInt(String key, ArrayList<Integer> marray, Context context) {
        Editor editor = this.preferences.edit();
        Integer[] mystringlist = marray.toArray(new Integer[marray.size()]);
        editor.putString(key, TextUtils.join("‚‗‚", mystringlist)).commit();
    }

    public ArrayList<Integer> getListInt(String key, Context context) {
        String[] mylist = TextUtils.split(this.preferences.getString(key, ""), "‚‗‚");
        ArrayList gottenlist = new ArrayList(Arrays.asList(mylist));
        ArrayList gottenlist2 = new ArrayList();

        for(int i = 0; i < gottenlist.size(); ++i) {
            gottenlist2.add(Integer.valueOf(Integer.parseInt((String)gottenlist.get(i))));
        }

        return gottenlist2;
    }

    public void putListBoolean(String key, ArrayList<Boolean> marray) {
        ArrayList origList = new ArrayList();
        Iterator var4 = marray.iterator();

        while(var4.hasNext()) {
            Boolean b = (Boolean)var4.next();
            if(b.booleanValue()) {
                origList.add("true");
            } else {
                origList.add("false");
            }
        }

        this.putList(key, origList);
    }

    public ArrayList<Boolean> getListBoolean(String key) {
        ArrayList origList = this.getList(key);
        ArrayList<Boolean> mBools = new ArrayList<>();
        Iterator var4 = origList.iterator();

        while(var4.hasNext()) {
            String b = (String)var4.next();
            if(b.equals("true")) {
                mBools.add(true);
            } else {
                mBools.add(false);
            }
        }

        return mBools;
    }

    public void putBoolean(String key, boolean value) {
        Editor editor = this.preferences.edit();
        editor.putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key) {
        return this.preferences.getBoolean(key, false);
    }

    public boolean getBooleanDefaultTrue(String key) {
        return this.preferences.getBoolean(key, true);
    }

    public void putFloat(String key, float value) {
        Editor editor = this.preferences.edit();
        editor.putFloat(key, value).commit();
    }

    public float getFloat(String key) {
        return this.preferences.getFloat(key, 0.0F);
    }

    public void remove(String key) {
        Editor editor = this.preferences.edit();
        editor.remove(key).commit();
    }

    public Boolean deleteImage(String path) {
        File tobedeletedImage = new File(path);
        Boolean isDeleted = Boolean.valueOf(tobedeletedImage.delete());
        return isDeleted;
    }

    public void clear() {
        Editor editor = this.preferences.edit();
        editor.clear().commit();
    }

    public Map<String, ?> getAll() {
        return this.preferences.getAll();
    }

    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        this.preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        this.preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
