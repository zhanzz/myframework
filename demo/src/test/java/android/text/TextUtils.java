package android.text;

/**
 * @author zhangzhiqiang
 * @date 2019/12/11.
 * description：
 */
public class TextUtils {
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.equals("")) {
            return true;
        }
        return false;
    }
}
