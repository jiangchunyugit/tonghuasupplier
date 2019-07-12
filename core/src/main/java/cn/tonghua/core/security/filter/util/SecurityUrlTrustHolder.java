package cn.tonghua.core.security.filter.util;

import com.google.common.collect.Lists;

import java.util.List;

public class SecurityUrlTrustHolder {
    private static List<String> urls = Lists.newArrayList();

    public static void setUrlTrustList(List<String> trustList){
        urls.clear();
        for (String s : trustList) {
            urls.add(s);
        }
    }

    public static Boolean isTrust(String url){
        return urls.contains(url);
    }
}
