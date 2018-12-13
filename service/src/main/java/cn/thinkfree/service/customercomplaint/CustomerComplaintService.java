package cn.thinkfree.service.customercomplaint;

import cn.thinkfree.database.model.ComplaintOrderInfo;
import cn.thinkfree.database.model.ContractSignature;
import cn.thinkfree.database.vo.ContractSignatureVO;
import cn.thinkfree.service.customercomplaint.vo.CreateComplaintVO;

import java.util.List;
import java.util.Map;

public interface CustomerComplaintService {

    /**
     * 创建投诉订单
     * @param complaintOrderInfo
     * @return
     */
    CreateComplaintVO saveCustomerComplaint(ComplaintOrderInfo complaintOrderInfo);

    /**
     * 输入被投诉项目编号
     *     则业主姓名、业主手机号、设计师姓名、所属设计公司名称、项目详细地址将自动同步出来
     * @param projectNo
     * @return
     */
    Map<String,String> getCustomerComplaint(String projectNo);
}
