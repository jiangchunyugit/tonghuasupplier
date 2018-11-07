package cn.thinkfree.database.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.thinkfree.database.model.ContractTermsChild;

public class ContractClauseVO {

    private Map<String,String> paramMap;
    private Map<String,List<ContractTermsChild>> childparamMap;

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, List<ContractTermsChild>> getChildparamMap() {
        return childparamMap;
    }

    public void setChildparamMap(Map<String, List<ContractTermsChild>> childparamMap) {
        this.childparamMap = childparamMap;
    }

    public static void main(String arg[]){
        Map<String,String> paramMap = new HashMap<>();
        Map<String,List<ContractTermsChild>> childparamMap = new HashMap<>();
        paramMap.put("11","222");
        paramMap.put("121", "333");

        List<ContractTermsChild> s =new ArrayList<>();
        ContractTermsChild sq = new ContractTermsChild();
        sq.setCostType("产品服务费code");
        sq.setCostName("我是一段文字。。。。。。");
        ContractTermsChild sq1 = new ContractTermsChild();
        sq1.setCostType("产品服务费code");
        sq1.setCostName("我是一段文字。。。。。。");
        s.add(sq);
        s.add(sq1);

        List<ContractTermsChild> s0 =new ArrayList<>();
        ContractTermsChild sq0 = new ContractTermsChild();
        sq0.setCostType("产品服务费code");
        sq0.setCostName("我是一段文字。。。。。。");
        ContractTermsChild sq10 = new ContractTermsChild();
        sq10.setCostType("产品服务费code");
        sq10.setCostName("我是一段文字。。。。。。");
        s0.add(sq0);
        s0.add(sq10);

        childparamMap.put("设计费code", s);
        childparamMap.put("设计费code0", s0);
        ContractClauseVO contractClauseVO = new ContractClauseVO();
        contractClauseVO.setChildparamMap(childparamMap);
        contractClauseVO.setParamMap(paramMap);


        JSONObject itemJSONObj = JSONObject.parseObject(JSON.toJSONString(contractClauseVO));
        System.out.println(itemJSONObj);

    }
}
