package com.example.study_gradle2;

import com.alibaba.fastjson.JSON;

import org.junit.Assert;
import org.junit.Test;

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
    public void transString(){
        String content = "{'code':'ab'}";
        Result result = JSON.parseObject(content,Result.class);
        System.out.println(result.getCode());
        Assert.assertTrue("无值",result!=null);
    }
}