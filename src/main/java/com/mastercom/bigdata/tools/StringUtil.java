package com.mastercom.bigdata.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
/**
 * Created by Kwong on 2017/9/22.
 */
public class StringUtil {

    public static String parseCamelCase(String propertyName){
        if(propertyName == null){
            return null;
        }
        char[] f = propertyName.toCharArray();
        char[] t = new char[f.length*2];
        int i = 0;
        for(char c : f){
            if(c >= 65 && c<=90){
                if (i != 0){
                    t[i++] = '_';
                }
                t[i++] = c;
            } else if(c >= 97 && c<=122){
                t[i++] = (char) (c - 32);
            }else{
                t[i++] = c;
            }
        }
        return String.valueOf(t, 0, i);

    }

    public interface StrMatchCallBack{

        String handle(String oriStr, String matchWord);
    }

    /**
     * 按指定的模式匹配，匹配上词回调处理
     * @param str
     * @param patternStr
     * @param callBack
     * @return
     */
    public static String match(String str, String patternStr, StrMatchCallBack callBack){

        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            String matchWord = matcher.group(0);
            str = callBack.handle(str, matchWord);
        }
        return str;
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

}
