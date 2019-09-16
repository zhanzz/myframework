package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.util.TypeUtils;
import com.framework.common.data.ActivityLifeCycleEvent;
import com.google.gson.Gson;

import org.json.JSONException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
    }

    public <T> List<T> getList(){
        return new ArrayList<T>();
    }

    @Test
    public void testFormart(){
        System.out.println(String.format("aa%s",null));
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
        List<? extends Object> xxx = new ArrayList<>();//父类转子类（逆变），子类转父类（协变）
        //xxx.add(new Student());
        tUser(new ArrayList());
    }

    List<? extends Object> yy; //? extends Object 作为一种类型解理    T extends Object 表示泛型参数的边界
    public void tUser(List list){
        yy = list;
    }

    public class TwoTuple{
        public final String x;

        public TwoTuple(String x){
            this.x = x;
        }
    }

    @Test
    public void testEnumEquel(){
        ActivityLifeCycleEvent aa = ActivityLifeCycleEvent.valueOf("DESTROY");
        System.out.println(ActivityLifeCycleEvent.DESTROY.equals(aa));
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
}