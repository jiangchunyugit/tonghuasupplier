package cn.thinkfree.service.construction;


import cn.thinkfree.core.bundle.MyRespBundle;

public interface ConstructionAndPayStateService {


    MyRespBundle<String> isStagePay(String orderNo,Integer sort);


    boolean isBeComplete(String projectNo, Integer sort);

    Object notifyPay(String orderNo,Integer sort);
}
