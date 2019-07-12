package cn.tonghua.service.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ProjectQuotationItemSortUtils {

    static Map<String,Integer> SerialNum = Maps.newHashMap();
    static {
        SerialNum.put("",0);
        SerialNum.put("一",1);
        SerialNum.put("二",2);
        SerialNum.put("三",3);
        SerialNum.put("四",4);
        SerialNum.put("五",5);
        SerialNum.put("六",6);
        SerialNum.put("七",7);
        SerialNum.put("八",8);
        SerialNum.put("九",9);
        SerialNum.put("十",10);
        SerialNum.put("十一",11);
        SerialNum.put("十二",12);
        SerialNum.put("十三",13);
        SerialNum.put("十四",14);
        SerialNum.put("十五",15);
        SerialNum.put("十六",16);
        SerialNum.put("十七",17);
        SerialNum.put("十八",18);
        SerialNum.put("十九",19);
        SerialNum.put("二十",20);
    }


    public static Integer sortNo(String val){
        String[] tmp = val.split("、");
        if(tmp.length == 1){
            return 0;
        }
        return SerialNum.getOrDefault(tmp[0],0);
    }

}
