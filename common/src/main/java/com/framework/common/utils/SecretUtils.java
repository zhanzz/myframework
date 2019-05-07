package com.framework.common.utils;

import java.io.UnsupportedEncodingException;
import static com.framework.common.utils.MD5Util.MD5Encode;
/**
 * Created by Long on 2016/4/15.
 */
public class SecretUtils {
    /**
     * 登录密码md5
     * @param str
     * @return
     */
    public static String md5(String str){
       return MD5Encode(str, "");
    }

    /**
     * 登录密码 前缀加md5
     * @param str
     * @return
     */
    public static String getBase64(String str) {
        return base64("zhidianlife_2016" + MD5Encode(str, ""));
    }

    private static String base64(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = Base64.encode(b);
        }
        return s;
    }

}
