package com.example.study_gradle2;

import android.util.Log;
/**
 * @author zhangzhiqiang
 * @date 2019/8/9.
 * description：
 */
public class TestGlideLoad {
    public void doTest(){
        if(isClassExists("com.example.study_gradle.TestHas")){
            try {
                Class clazz = Class.forName("com.example.study_gradle.TestHas");
                Object object = clazz.newInstance();
                String xx= object.toString();
                Log.e("zhang",xx);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private static final boolean isClassExists(String classFullName) {
        try {
            Class.forName(classFullName);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
