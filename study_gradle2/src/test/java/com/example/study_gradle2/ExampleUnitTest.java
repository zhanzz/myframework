package com.example.study_gradle2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

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
        String content = "{'code':'7','user':null}";
        JSON.DEFAULT_PARSER_FEATURE|= Feature.InitStringFieldAsEmpty.getMask();
        Result result = JSON.parseObject(content,Result.class);
        System.out.println(Result.class.getName());
        System.out.println(result.getMsg()!=null);
        System.out.println(result.getMsg());
        Assert.assertTrue("无值",result!=null);
    }


    @Test
    public void testFloat(){
        float eFloat = 2.7182816f; // Float，实际值为 2.7182815
        System.out.println(1_000_000==eFloat);
        Integer a = 8; Long x = 8L;
        System.out.println(String.format("%s",7));

    }
}