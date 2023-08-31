package com.heeexy.example.util;

import java.util.List;
import java.util.UUID;

/**
 * @author: heeexy
 * @date: 2017/10/24 10:16
 */
public class StringTools {

    public static boolean isNullOrEmpty(String str) {
        return null == str || "".equals(str) || "null".equals(str);
    }

    public static boolean isNullOrEmpty(Object obj) {
        return null == obj || "".equals(obj);
    }

    public static String getUUid(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    public static String arrToString(List<String> str) {
        String ret = "";
        if(null!=str && str.size()>=0){
            for (String s : str) {
                ret += s;
                ret += ",";
            }
            return ret.substring(0, ret.length()-1);
        }
        return ret;
    }

}
