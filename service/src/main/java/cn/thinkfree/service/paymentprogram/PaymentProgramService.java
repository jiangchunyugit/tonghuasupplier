package cn.thinkfree.service.paymentprogram;

import cn.thinkfree.database.model.PaymentProgram;
import cn.thinkfree.database.vo.PaymentProgramVO;

public interface PaymentProgramService {

    /**
     * 获取支付方案信息
     * @return
     */
    PaymentProgramVO getPaymentPrograms(PaymentProgramVO paymentProgramVO);

    /**
     * 新增或者编辑支付方案信息
     * @param paymentProgram
     * @return
     */
    boolean addOrUpdatePaymentProgram(PaymentProgram paymentProgram);

}
