package cn.thinkfree.service.paymentprogram;

import cn.thinkfree.database.vo.PaymentProgramVO;

public interface PaymentProgramService {

    /**
     * 获取支付方案信息
     * @return
     */
    PaymentProgramVO getPaymentPrograms(PaymentProgramVO paymentProgramVO);

    /**
     * 新增支付方案信息
     * @param paymentProgramVO
     * @return
     */
    boolean addPaymentProgram(PaymentProgramVO paymentProgramVO);
}
