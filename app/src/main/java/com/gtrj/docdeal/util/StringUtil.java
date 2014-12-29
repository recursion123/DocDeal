package com.gtrj.docdeal.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhang77555 on 2014/12/26.
 */
public class StringUtil {
    public static String replaceBlank(String str) {
        String result = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            result = m.replaceAll("");
        }
        return result;
    }
}
