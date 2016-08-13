package com.jixianxueyuan.widget.alex.util;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

/**
 * 作者：alex
 * 时间：2016/8/12 11:20
 * 博客地址：http://www.jianshu.com/users/c3c4ea133871/subscriptions
 */
public class SpannableUtil {
    private static int n = 0;// 原Str拥有的字符个数
    private static SpannableString spannableString;
    private static double multiple = 0;// 放大倍数

    /**
     * 对显示的字符串进行格式化 比如输入：出生年月 输出结果：出正生正年正月
     */
    private static String formatStr(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        n = str.length();
        if (n >= 6) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        for (int i = n - 1; i > 0; i--) {
            sb.insert(i, "　");
        }
        return sb.toString();
    }

    /**
     * 对显示字符串进行格式化 比如输入：安正卓正机正器正人 输出结果：安 卓 机 器 人
     *
     * @param str
     * @return
     */
    public static SpannableString formatText(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        str = formatStr(str);
        if (str.length() <= 6) {
            return null;
        }
        spannableString = new SpannableString(str);
        switch (n) {
            case 2:
                multiple = 4;
                break;
            case 3:
                multiple = 1.5;
                break;
            case 4:
                multiple = 0.66666666666666666666666666666666667;
                break;
            case 5:
                multiple = 0.25;
                break;
            default:
                break;
        }
        for (int i = 1; i < str.length(); i = i + 2) {
            spannableString.setSpan(new RelativeSizeSpan((float) multiple), i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }
}
