package cn.thinkfree.service.paymentprogram;

import cn.thinkfree.database.mapper.PaymentProgramMapper;
import cn.thinkfree.database.model.PaymentProgram;
import cn.thinkfree.database.model.PaymentProgramExample;
import cn.thinkfree.database.vo.PaymentProgramVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentProgramServiceImpl implements PaymentProgramService {

    @Autowired
    PaymentProgramMapper paymentProgramMapper;

    @Override
    public boolean addPaymentProgram(PaymentProgramVO paymentProgramVO) {

        if (paymentProgramVO != null && paymentProgramVO.getPaymentProgramList().size() > 0) {

            paymentProgramVO.getPaymentProgramList().forEach(e->{
                e.setProgramCode(paymentProgramVO.getPaymentProgramCode());
                paymentProgramMapper.insertSelective(e);
            });

            return true;
        }
        return false;
    }

    @Override
    public PaymentProgramVO getPaymentPrograms(PaymentProgramVO paymentProgramVO) {

        PaymentProgramExample paymentProgramExample = new PaymentProgramExample();
        paymentProgramExample.createCriteria().andProgramCodeEqualTo(paymentProgramVO.getPaymentProgramCode());
        List<PaymentProgram> paymentPrograms = paymentProgramMapper.selectByExample(paymentProgramExample);
        paymentProgramVO.setPaymentProgramList(paymentPrograms);
        return paymentProgramVO;
    }
}
