package com.example.demo;

import android.content.Intent;
import android.net.Uri;
import android.util.ArraySet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.StringCodec;
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

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;

import io.reactivex.subjects.BehaviorSubject;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

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
//        User o = testInvoke(User.class);
//        //JSON.parseObject(content,Result<User>.class);
//        System.out.println(o.age);
//        System.out.println(o.name==null);
//        System.out.println(JSON.toJSONString(o));
//        String aa = "{'data':[1,2,3],'code':1}";
//        Result<List<Integer>> xx = JSON.parseObject(aa,new TypeReference<Result<List<Integer>>>(){}, Feature.InitStringFieldAsEmpty);
//        System.out.println(xx.data);
        //int[] arry = new int[-1];

        //Result bean = new Result();
        //bean.setGiveaway(true);
        //序列化输出的字符串会把is去掉
        //Result bean = GsonUtils.parseFromString("{\"code\":0,\"giveaway\":true,\"result\":\"\"}",Result.class);
        //System.out.println(bean.isGiveaway());
    }

    @Test
    public void testUser(){
        String aa = "{'data':[1,2,3],'isGiveaway':1,'code':''}";
        Result<Object> xx = JSON.parseObject(aa,new TypeReference<Result<Object>>(){}, Feature.InitStringFieldAsEmpty);
        System.out.println(xx.data instanceof Integer[]);
        System.out.println(xx.data.getClass().isArray());
        System.out.println(xx.data.getClass());//com.alibaba.fastjson.JSONArray
        System.out.println(MessageFormat.format("{0}{1}", "箱规：","4"));
        System.out.println(xx.isGiveaway());
        System.out.println(xx.code);
    }

    @Test
    public void testSetGet(){
        String content = "{'isGiveaway':{'name':'是','value':1}}";
        Wraper xx = JSON.parseObject(content,new TypeReference<Wraper>(){}, Feature.InitStringFieldAsEmpty);
        System.out.println(xx.isGiveaway());
    }

    @Test
    public void testEnum(){
        ReportFormType.Management_total.setContent("有问题");
        System.out.println(ReportFormType.Management_total.getContent());
    }

    @Test
    public void testFiled(){
        Son fater = new Son();
        Fater fater1 = fater;
        fater1.setAge(40);
        System.out.println(fater1.age);//this会先从本类找，找不到在在父类找
    }

    private <T> T testInvoke(Class<T> clazz){
        String content = "{'isGiveaway':true}";
        //JSONObject object = JSON.parseObject(content);
        //System.out.println(object.get("code"));
        Result<T> o = JSON.parseObject(content,new TypeReference<Result<T>>(clazz){}, Feature.InitStringFieldAsEmpty);
        //Gson gson = new Gson();
//        Result<T> o = gson.fromJson(content,new TypeReference<Result<T>>(clazz){}.getType());
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
        //clipRect不影响坐标
//        String[] xx = new String[]{"44","45"};
//        String conent =  GsonUtils.parseToString(xx);
//        System.out.println(conent);
//        System.out.println(String.format(Locale.CHINA,"剩余%ss",2000/1000));
        //获取系统默认编码
        System.out.println(System.getProperty("file.encoding"));

        //获取系统默认的字符编码
        System.out.println(Charset.defaultCharset());

        //获取系统默认语言

        System.out.println(System.getProperty("user.language"));
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
        List<? extends Son> xx = Arrays.asList(new Son()); //? extends User 为“任何类型”，编译器无法验证“任何类型”的安全型
        //xx.add(new User());
        //xx.add(new Student());
        Fater s = xx.get(0);
        //x[0] = new User();//java.lang.ArrayStoreException: com.example.demo.User
        List<String> aa = new ArrayList<>();
        List<? extends Object> xxx = aa;//父类转子类（逆变），子类转父类（协变）
        //xxx.add(new Student());
        //tUser(new ArrayList());
        ArrayList<User> u = new ArrayList<>();
//        u.add(new Student());
        //u.add(new User());
//        List<Student> userList = new ArrayList<>();
//        userList.add(new Student());
        //u.add(new User());
        //u = userList;
        //List<User> us = u;
        tUser(u);
        LinkedHashSet<String> aaa = new LinkedHashSet<>();
        List<Integer> ee= Arrays.asList(4,5);
    }

    List<? extends Object> yy; //? extends Object 作为一种类型解理    T extends Object 表示泛型参数的边界
    public  void tUser(List<User> list){
        //yy = list;
        List<? extends Object> xx = list;
        //xx.add(new User());
        //User x = xx.get(4);
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

            printHex(str.getBytes());
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
        ApiSubscriber apiSubscriber = new ApiSubscriber(){

            @Override
            protected void onFail(ApiException ex) {
                System.out.println("onFail="+ex.getDisplayMessage());
            }

            @Override
            public void onSuccess(Object data, int code, String msg) {
                System.out.println("onSuccess="+msg);
            }
        };
        com.framework.common.data.Result<String> result = new com.framework.common.data.Result<>();
        //apiSubscriber.onNext(result);
        System.out.println(apiSubscriber.getClass());
        System.out.println(apiSubscriber.getClass().getGenericSuperclass() instanceof ParameterizedType);
        System.out.println(apiSubscriber.getClass().getGenericSuperclass().getTypeName());
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
        Fater son = new Son();
        son.setAge(15);
        System.out.println(son.getAge());

        //Son cson = son.clone();
        System.out.println(UUID.randomUUID().toString().replaceAll("-",""));
        double value = 0.30;
        DecimalFormat format = new DecimalFormat("退款金额:#.##");
        System.out.println(format.format(value));
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

        byte a = -2;
        int x = a;
        System.out.println(a);
        System.out.println(Integer.toBinaryString(x));
        f(map());
    }

    public void f(List<? extends Fater> list){
        Generic<Integer>[] aa = (Generic<Integer>[])new Generic[10];
    }

    public static <T> List<T> map(){
        return new ArrayList<>();
    }

    public static class Generic<T>{}

    @Test
    public void testClassInfo(){
        List<Integer> list = new ArrayList<Integer>();
        Map<Integer, String> map = new HashMap<Integer, String>(){};
        System.out.println(Arrays.toString(list.getClass().getTypeParameters()));
        System.out.println(Arrays.toString(map.getClass().getSuperclass().getTypeParameters()));


        Type type = map.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
//ParameterizedType parameterizedType = (ParameterizedType)map.getClass().getGenericSuperclass();
        for (Type typeArgument : parameterizedType.getActualTypeArguments()) {
            System.out.println(typeArgument.getTypeName());
        }

        String packageName = "zhang";
        String sub = packageName.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
        //String sub = packageName.replaceAll("h",File.separator);
        System.out.println(sub);
    }

    interface A<T, ID> {
    }

    class B implements A<String, Integer> {
    }

    @Test
    public void testReturnType(){
        Method method = null;
        try {
            method = Result.class.getDeclaredMethod("getData", null);
            //返回类型解析
            Type returnType = method.getGenericReturnType();
            if(returnType instanceof TypeVariable){
                System.out.println (((TypeVariable) returnType).getName());
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBoolean(){
        String content = "{'isGiveaway':1,'result':null}";
        ParserConfig.getGlobalInstance().putDeserializer(String.class, MyStringCodec.instance);
        Result o = JSON.parseObject(content,Result.class);
        System.out.println(o.isGiveaway());
        String a = null;
        System.out.println(a+"haha");

        Map<String,Integer> map = new HashMap<>();
        Set<String> keys = map.keySet();
        List<String> list = new ArrayList<>(keys);
        System.out.println(String.format(Locale.CHINA,"选择\"%s\"","aa"));
    }

    @Test
    public void testUri() throws UnsupportedEncodingException {
//        Uri uri = Uri.parse("content://media/external/images/media/294");
//        uri = uri.buildUpon().appendQueryParameter("fileName","haha").build();
//        System.out.println(uri.getPath());
        //int x = 5/2;
        //int realPosition = 5/2;
//        String x = "格子小西装外套女220新款春季韩版修身0短款休闲网红西服上衣套装";
//        int lenght=0;
//        for(int y=0,count=x.length();y<count;y++){
//            String string = new String(x.charAt(y)+"");
//            lenght += string.getBytes("GBK").length;
//        }
//        System.out.println("slengh="+x.getBytes("GBK").length);
//        System.out.println("lengh="+lenght);
//        String[] strs = SubByteString.getSubedStrings(x,12);
//        System.out.println(strs.length);
//        for(int i=0,count=strs.length;i<count;i++){
//            System.out.println(String.format("%d=",i)+strs[i].length());
//            System.out.println(String.format("%d=",i)+strs[i]);
//        }
//        System.out.println(String.format("%s",1f));
//        String value = "steps:1/8x";
//        String result = value.replaceAll("[^\\d:]","");
//        System.out.println(result);
//        String test = "zz yy";
//        String result2 = test.replaceAll("[^.]","");
//        System.out.println(result2);
//        //cleanSecondsString("steps:1/8x");
//        System.out.println(String.format("%.100f", 0.0000001));
//        Boolean xx = null;
//        if(xx==Boolean.FALSE){
//            System.out.println("false");
//        }
    }

    @Test
    public void cleanSecondsString() {
//        CharSequence var2 = (CharSequence) seconds;
//        Regex var3 = new Regex("[^\\d:.]");
//        String var4 = "";
//        boolean var5 = false;
//        String filteredValue = var3.replace(var2, var4);
//        System.out.println(filteredValue);
//        String[] values = filteredValue.split(":");
//        System.out.println(Integer.parseInt(values[0]));
//        Son son = new Son();
        //System.out.println(son.sex);
//        String path = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2653344692,3688499604&fm=26&gp=0.jpg?xx=4";
//        File file = new File(path);
//        System.out.println(file.getName());
//            List<String> xx = new ArrayList<>();
//            //xx.add("11");
//            xx.add("22");
//            //xx.add("33");
//        Iterator<String> it = xx.iterator();
//            while(it.hasNext()){
//                String runnable = it.next();
//                System.out.println(runnable);
//                if(runnable.equalsIgnoreCase("22")){
//                    it.remove();
//                }
//
//            }
//        ArrayList<Integer> list = new ArrayList<Integer>();
//        list.add(2);
//        Iterator<Integer> iterator = list.iterator();
//        while(iterator.hasNext()){
//            Integer integer = iterator.next();
//            if(integer==2)
//                iterator.remove();
//        }
//        ArrayList<Integer> list = new ArrayList<Integer>();
//        list.add(2);
//        Iterator<Integer> iterator = list.iterator();
//        while(iterator.hasNext()){
//            Integer integer = iterator.next();
//            if(integer==2)
//                list.remove(integer);
//        }
//        System.out.println(xx.size());
//        float x = 0.01f;
//        x++;
//        System.out.println(x);
        Integer a=5;
        //System.out.println((Boolean)a);
    }
}