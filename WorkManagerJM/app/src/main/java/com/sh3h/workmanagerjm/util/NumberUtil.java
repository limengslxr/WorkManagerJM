package com.sh3h.workmanagerjm.util;

import com.sh3h.mobileutil.util.TextUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LiMeng on 2018/2/7.
 */

public class NumberUtil {

    public static boolean isNumber(String value) {
        if (TextUtil.isNullOrEmpty(value)) {
            return false;
        }

        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(value);
        return isNum.matches();
    }

    public static int transformNumber(String value) {
        if (TextUtil.isNullOrEmpty(value)) {
            return 0;
        }

        value = value.replaceAll("[^(0-9)]", TextUtil.EMPTY);
        if (TextUtil.isNullOrEmpty(value)) {
            return 0;
        }

        if (value.length() > 10) {
            return 0;
        }

        return Integer.parseInt(value);
    }

}
