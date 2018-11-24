package cn.thinkfree.service.construction;


import cn.thinkfree.core.bundle.MyRespBundle;

public interface ConstructionAndPayStateService {

    MyRespBundle<Boolean> isFirstPay(String orderNo);

    MyRespBundle<Boolean> isStagePay(String orderNo);


    boolean isBeComplete(String projectNo, Integer sort);
}
