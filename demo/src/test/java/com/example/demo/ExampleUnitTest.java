package com.example.demo;

import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.util.TypeUtils;
import com.framework.common.data.ActivityLifeCycleEvent;
import com.framework.common.exception.ApiException;
import com.framework.common.exception.CustomException;
import com.framework.common.retrofit.ApiSubscriber;
import com.framework.common.utils.GsonUtils;
import com.framework.model.VersionInfo;
import com.google.gson.Gson;
import com.google.zxing.common.StringUtils;

import org.json.JSONException;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import io.reactivex.subjects.BehaviorSubject;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testGson(){
        User o = testInvoke(User.class);
        //JSON.parseObject(content,Result<User>.class);
        System.out.println(o.age);
        System.out.println(o.name==null);
        System.out.println(JSON.toJSONString(o));
        String aa = "{'data':[1,2,3],'code':1}";
        Result<List<Integer>> xx = JSON.parseObject(aa,new TypeReference<Result<List<Integer>>>(){}, Feature.InitStringFieldAsEmpty);
        System.out.println(xx.data);
        //int[] arry = new int[-1];
    }

    @Test
    public void testUser(){
        String aa = "{'data':[1,2,3],'isGiveaway':1}";
        Result<Object> xx = JSON.parseObject(aa,new TypeReference<Result<Object>>(){}, Feature.InitStringFieldAsEmpty);
        System.out.println(xx.data instanceof Integer[]);
        System.out.println(xx.data.getClass().isArray());
        System.out.println(xx.data.getClass());//com.alibaba.fastjson.JSONArray
        System.out.println(MessageFormat.format("{0}{1}", "箱规：","4"));
        System.out.println(xx.isGiveaway());
    }

    @Test
    public void testSetGet(){
        String content = "{'isGiveaway':{'name':'是','value':1}}";
        Wraper xx = JSON.parseObject(content,new TypeReference<Wraper>(){}, Feature.InitStringFieldAsEmpty);
        System.out.println(xx.isGiveaway());
    }

    @Test
    public void testEnum(){
        System.out.println(State.ONE instanceof Serializable);
    }

    @Test
    public void testFiled(){
        Son fater = new Son();
        //fater.setAge(40);
        System.out.println(fater.getFAge());
    }

    private <T> T testInvoke(Class<T> clazz){
        String content = "{'data':{'age':2.5,'daate':1568267088000},'code':1}";
        JSONObject object = JSON.parseObject(content);
        System.out.println(object.get("code"));
        Result<T> o = JSON.parseObject(content,new TypeReference<Result<T>>(clazz){}, Feature.InitStringFieldAsEmpty);
        //Gson gson = new Gson();
        //Result<T> o = gson.fromJson(content,new TypeReference<Result<T>>(clazz){}.getType());
        return o.data;
    }

    @Test
    public void testCase(){
        int x = 0;
        switch (5){
            case 5:
                x = 5;
            case 6:
                x = 7;
        }
        System.out.println(x);//7
        List<Student> students = new ArrayList<>();
        //testList(this.<User>getList());
        if(students instanceof List){

        }
    }

    @Test
    public void testGsonv(){
        String[] xx = new String[]{"44","45"};
        String conent =  GsonUtils.parseToString(xx);
        System.out.println(conent);
        System.out.println(String.format(Locale.CHINA,"剩余%ss",2000/1000));
    }

    public <T> List<T> getList(){
        return new ArrayList<T>();
    }

    @Test
    public void testFormart(){
        System.out.println(String.format("aa%s",""));
    }

    @Test
    public void testList(){
        //tUser((List<User>) list);
        Class c;
        User[] x = new Student[5];
        if(x instanceof Student[]){

        }
        List<? extends User> xx = new ArrayList<>(); //? extends User 为“任何类型”，编译器无法验证“任何类型”的安全型
        //xx.add(new User());
        //xx.add(new Student());
        //x[0] = new User();//java.lang.ArrayStoreException: com.example.demo.User
        List<String> aa = new ArrayList<>();
        List<? extends Object> xxx = aa;//父类转子类（逆变），子类转父类（协变）
        //xxx.add(new Student());
        //tUser(new ArrayList());
        List<? extends User> u = new ArrayList<>();
//        u.add(new Student());
        //u.add(new User());
        List<Student> userList = new ArrayList<>();
        userList.add(new Student());
        //userList.add(new User());
        u = userList;
        //List<User> us = u;
        //tUser(userList);
    }

    List<? extends Object> yy; //? extends Object 作为一种类型解理    T extends Object 表示泛型参数的边界
    public  void tUser(List<User> list){
        //yy = list;
        //List<Object> xx = list;
        //Source<? extends Object> objects = list;
        list.add(new Student());
    }

    public class TwoTuple{
        public final String x;

        public TwoTuple(String x){
            this.x = x;
        }
    }

    @Test
    public void testEnumEquel(){
        //枚举是单实例的
        ActivityLifeCycleEvent aa = ActivityLifeCycleEvent.valueOf("DESTROY");
        System.out.println(aa==ActivityLifeCycleEvent.DESTROY);
        System.out.println(System.identityHashCode(ActivityLifeCycleEvent.DESTROY));
        System.out.println(System.identityHashCode(aa));//打印地址 十进制格式
    }

    @Test
    public void testThread(){
        ThreadGroup threadGroup1 = new ThreadGroup("group1");
        Thread thread = new Thread(threadGroup1,new Runnable() {
            @Override
            public void run() {
                while (true){

                }
            }
        }) ;
        thread.start();


        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    throw new NullPointerException();
                }
            }
        }) ;
        thread2.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEcode(){
        String str = "马中国";
        byte[] bs = str.getBytes();
        printHex(bs);//[-28, -72, -83, -27, -101, -67]
        try {
            String new_str=new String(str.getBytes("utf-8"),"gbk");
            System.out.println(new_str);//涓浗
            byte[] newStr = new_str.getBytes("gbk");
            printHex(newStr);//[-28, -72, -83, -27, -101, -67]
            String final_str=new String(new_str.getBytes("gbk"),"utf-8");
            System.out.println(final_str);//中国

            printHex(str.getBytes("UNICODE"));
            printHex(str.getBytes("UTF-16"));
            printHex(str.getBytes("UTF-16BE"));
            printHex(str.getBytes("UTF-16LE"));
            printHex(str.getBytes("utf-8"));
            printHex(str.getBytes("gbk"));//4e2d 56fd
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void printHex(byte[] byteArray) {
        StringBuffer sb = new StringBuffer();
        for (byte b : byteArray) {
            sb.append(Integer.toHexString((b >> 4) & 0xF));
            sb.append(Integer.toHexString(b & 0xF));
            sb.append(" ");
        }
        System.out.println(sb.toString());
    }

    @Test //测试泛型
    public void testClass(){
        ApiSubscriber apiSubscriber = new ApiSubscriber<String>(){

            @Override
            protected void onFail(ApiException ex) {
                System.out.println("onFail="+ex.getDisplayMessage());
            }

            @Override
            public void onSuccess(String data, int code, String msg) {
                System.out.println("onSuccess="+msg);
            }
        };
        com.framework.common.data.Result<String> result = new com.framework.common.data.Result<>();
        apiSubscriber.onNext(result);
        System.out.println(apiSubscriber.getClass().getSuperclass());
//        request(new CallBack<User>(){
//            @Override
//            public void haha() {
//
//            }
//        });
    }

    public <T> void request(CallBack<T> callBack){
        //Type entityClass =  ((ParameterizedType) callBack.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Type type = callBack.getClass().getGenericInterfaces()[0];
        System.out.println(type instanceof ParameterizedType);//true
        Type type1 = ((ParameterizedType)type).getRawType();
        Type type2 = ((ParameterizedType)type).getOwnerType();
        Type type3 = ((ParameterizedType)type).getActualTypeArguments()[0];
        //System.out.println(entityClass);
        System.out.println(type);//com.example.demo.CallBack<com.example.demo.User>
        System.out.println(type1);//interface com.example.demo.CallBack
        System.out.println(type2);//null
        System.out.println(type3);//class com.example.demo.User
    }

    @Test
    public void testListCast(){
        List<String> strings = new ArrayList<>();
        strings.add("11");strings.add("22");

        List<Object> objects = new ArrayList<>();
        for(String str:strings){
            objects.add(str);
        }
        for (Object object:objects){
            System.out.println(object);
        }
        Son son = new Son();
        son.setAge(15);
        System.out.println(son.getAge());

        Son cson = son.clone();
        System.out.println(cson instanceof Son);
    }

    @Test
    public void testSerisize(){
//        UUID uuid = UUID.randomUUID();
//        String str = GsonUtils.parseToString(uuid);
//        System.out.println(str);
//        UUID uuid1 = GsonUtils.parseFromString(str,UUID.class);
//        System.out.println(uuid1);
        File file = new File("cc/dd","aa.txt");
        System.out.println(file.getName());//aa.txt
        System.out.println(file.getParent());//


        BehaviorSubject<ActivityLifeCycleEvent> lifecycleSubject = BehaviorSubject.create();
        lifecycleSubject.onNext(ActivityLifeCycleEvent.DESTROY);
        System.out.println(lifecycleSubject.getValue()==ActivityLifeCycleEvent.DESTROY);//true
    }
}