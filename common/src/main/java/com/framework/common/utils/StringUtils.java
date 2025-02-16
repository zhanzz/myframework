package com.framework.common.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.framework.common.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String Utils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2011-7-22
 */
public class StringUtils {

    private StringUtils() {
        throw new AssertionError();
    }

    /**
     * is null or its length is 0 or it is made by space
     * 
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     * 
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * is null or its length is 0
     * 
     * <pre>
     * isEmpty(null) = true;
     * isEmpty(&quot;&quot;) = true;
     * isEmpty(&quot;  &quot;) = false;
     * </pre>
     * 
     * @param str
     * @return if string is null or its size is 0, return true, else return false.
     */
    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }

    /**
     * 只能包含数字中文字母和下划线
     * @param sequence
     * @return
     */
    public static   boolean checkMatchString(String sequence) {
        final String format = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w-_]";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(sequence);
        return !matcher.find();
    }
    /**
     * compare two string
     * 
     * @param actual
     * @param expected
     * @return
     * @see ObjectUtils#isEquals(Object, Object)
     */
    public static boolean isEquals(String actual, String expected) {
        return ObjectUtils.isEquals(actual, expected);
    }

    /**
     * get length of CharSequence
     * 
     * <pre>
     * length(null) = 0;
     * length(\"\") = 0;
     * length(\"abc\") = 3;
     * </pre>
     * 
     * @param str
     * @return if str is null or empty, return 0, else return {@link CharSequence#length()}.
     */
    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    /**
     * null Object to empty string
     * 
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     * 
     * @param str
     * @return
     */
    public static String nullStrToEmpty(Object str) {
        return (str == null ? "" : (str instanceof String ? (String)str : str.toString()));
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str.charAt(0) + "");
        return isNum.matches();
    }

    /**
     * capitalize first letter
     * 
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     * 
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length())
                .append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }

    /**
     * encoded in utf-8
     * 
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     * 
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * encoded in utf-8, if exception, return defultReturn
     * 
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    /**
     * get innerHtml from href
     * 
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     * 
     * @param href
     * @return <ul>
     *         <li>if href is null, return ""</li>
     *         <li>if not match regx, return source</li>
     *         <li>return the last string that match regx</li>
     *         </ul>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

/**
     * process special char in html
     * 
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     * 
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return StringUtils.isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }

    /**
     * transform half width char to full width char
     * 
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     * </pre>
     * 
     * @param s
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char)(source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * transform full width char to half width char
     * 
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
     * </pre>
     * 
     * @param s
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char)12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char)(source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * @author zhenggl
     * @date 2016/4/25 19:13
     * @desc 统一的价格处理动作
    */
    public static SpannableString convertPrice(String priceLabel, String appendTarget){
        DecimalFormat format = new DecimalFormat("#0.00");
        float price = 0;
        try{
            if(!TextUtils.isEmpty(priceLabel)){
                price = Float.parseFloat(priceLabel);
            }else{
                price = 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            price=0;
        }
        priceLabel = format.format(price);
        priceLabel = appendTarget +" "+priceLabel;
        SpannableString result = new SpannableString(priceLabel);
        try{
            if(!TextUtils.isEmpty(priceLabel)){
                if(priceLabel.contains(".")){
                    result.setSpan(new RelativeSizeSpan(1.0f), 0, priceLabel.indexOf(".") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    result.setSpan(new RelativeSizeSpan(0.8f), priceLabel.indexOf("."), priceLabel.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @author zhenggl
     * @date 2016/4/25 19:13
     * @desc 统一的价格处理动作
     */
    public static SpannableString convertPrice(double priceLabel, String appendTarget){
        DecimalFormat format = new DecimalFormat("#0.00");
        String  strPriceLabel = format.format(priceLabel);
        strPriceLabel = appendTarget +" "+ strPriceLabel;
        SpannableString result = new SpannableString(strPriceLabel);
        try{
            if(!TextUtils.isEmpty(strPriceLabel)){
                if(strPriceLabel.contains(".")){
                    result.setSpan(new RelativeSizeSpan(1.0f), 0, strPriceLabel.indexOf(".") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    result.setSpan(new RelativeSizeSpan(0.8f), strPriceLabel.indexOf("."), strPriceLabel.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static SpannableString convertPrice2(double priceLabel, String appendTarget){
        DecimalFormat format = new DecimalFormat("#0.00");
        String  strPriceLabel = format.format(priceLabel);
        strPriceLabel = appendTarget +" "+ strPriceLabel;
        SpannableString result = new SpannableString(strPriceLabel);
        try{
            if(!TextUtils.isEmpty(strPriceLabel)){
                if(strPriceLabel.contains(".")){

                    result.setSpan(new RelativeSizeSpan(0.5f), 0, appendTarget.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    result.setSpan(new RelativeSizeSpan(1.0f), appendTarget.length() + 1, strPriceLabel.indexOf(".") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    result.setSpan(new RelativeSizeSpan(0.8f), strPriceLabel.indexOf("."), strPriceLabel.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 对手机号加 *
     * @param userPhone
     * @return
     */
    public static String getSecretPhone(String userPhone) {
        if(TextUtils.isEmpty(userPhone)&&userPhone.length()==11){
            return "";
        }
        return userPhone.substring(0,3)+"****"+userPhone.substring(7);
    }

    public static String getSecretBankCard(String cardNo) {
        if (StringUtils.isBlank(cardNo)) {
            return cardNo;
        }

        int length = cardNo.length();
        int beforeLength = 4;
        int afterLength = 4;
        String replaceSymbol = "*";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            if (i < beforeLength || i >= (length - afterLength)) {
                sb.append(cardNo.charAt(i));
            } else {
                if (printStr(sb.toString(), "*") % 4 == 0){
                    sb.append(" " + replaceSymbol);
                } else {
                    sb.append(replaceSymbol);
                }
            }
        }

        return sb.toString();
    }

    public static int printStr(String str, String select) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        Map<String, Integer> pMap = new HashMap<String, Integer>();
        String[] split = str.split("");
        for (int i = 0; i < split.length; i++) {
            if (!"".equals(split[i]) && pMap.containsKey(split[i])) {
                pMap.put(split[i], pMap.get(split[i]) + 1);
            } else if (!"".equals(split[i])) {
                pMap.put(split[i], 1);
            }
        }
        int num = pMap.get(select) == null ? 0 : pMap.get(select);
        return num;
    }

    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {
//        StringBuffer unicode = new StringBuffer();
//        for (int i = 0; i < string.length(); i++) {
//            // 取出每一个字符
//            char c = string.charAt(i);
//            // 转换为unicode
//            unicode.append("\\u" + Integer.toHexString(c));
//        }
        try {
            return URLDecoder.decode(string,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return string;
        }
    }

    public static Map<String,String> getUrlParams(String url){
        Map<String,String> params = new HashMap<>();

        if(url!=null&&url.trim().length()!=0){
            int index = url.lastIndexOf("?");
            if(index!=-1&&(url.length()>index+2)){
                String url_params = url.substring(index+1);
                String[] key_values=url_params.split("&");
                for(int i=0;i<key_values.length;i++){
                    String[] param = key_values[i].split("=");
                    if(param.length>1){
                        params.put(param[0],param[1]);
                    }else if(param.length==1){
                        params.put(param[0],"");
                    }
                }
            }
        }
        return params;
    }

    public static String convertProvinceCityArea(String province, String city, String area){
        StringBuilder result = new StringBuilder();
        if(!TextUtils.isEmpty(province)){
            result.append(province);
        }
        if(!result.toString().contains(city)){
            result.append(city);
        }
        if(!result.toString().contains(area)){
            result.append(area);
        }
        return result.toString();
    }

    public static String formatData(long milliseconds){
        SimpleDateFormat sdf =   new SimpleDateFormat("-yyyy年MM月dd日-");
        return sdf.format(new Date(milliseconds));
    }

    public static void setListener(TextView tv, String content, final int color, String[] need_clicks,boolean isNeedUnderline,final View.OnClickListener listener) {
        if (tv == null || TextUtils.isEmpty(content) || ListUtils.isEmpty(need_clicks)) {
            return;
        }
        SpannableString builder = new SpannableString(content);
        for (int i = 0, count = need_clicks.length; i < count; i++) {
            String need_click = need_clicks[i];
            int startIndex = content.indexOf(need_click);
            int endIndex = startIndex + need_click.length();

            builder.setSpan(new ForegroundColorSpan(color), startIndex,
                    endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            //设置文本点击事件
            final int finalI = i;
            builder.setSpan(new ClickableSpan() {
                                @Override
                                public void updateDrawState(@NonNull TextPaint ds) {
                                    super.updateDrawState(ds);
                                    ds.setColor(color);//设置颜色
                                    ds.setUnderlineText(false);//去掉下划线
                                }

                                @Override
                                public void onClick(View widget) {
                                    widget.setTag(R.id.click_position, finalI);
                                    listener.onClick(widget);
                                }
                            }, startIndex, endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv.setHighlightColor(tv.getContext().getResources().getColor(android.R.color.transparent));//方法重新设置点击文字背景为透明色。
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setText(builder);
    }
}
