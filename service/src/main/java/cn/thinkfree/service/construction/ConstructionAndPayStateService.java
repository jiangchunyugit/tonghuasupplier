package cn.thinkfree.service.construction;


import cn.thinkfree.core.bundle.MyRespBundle;

public interface ConstructionAndPayStateService {

    MyRespBundle<Boolean> isFirstPay(String orderNo);

    MyRespBundle<Boolean> isStagePay(String orderNo);

    MyRespBundle<Boolean> isEndPay(String orderNo);

    /**
     * 开工报告
     */
    boolean isStartReport(String projectNo);

    /**
     * 阶段验收通过
     */
    boolean isStageComplete(String projectNo);

    /**
     * 竣工验收通过
     */
    boolean isBeComplete(String projectNo, String sort, String isEnd);
}
