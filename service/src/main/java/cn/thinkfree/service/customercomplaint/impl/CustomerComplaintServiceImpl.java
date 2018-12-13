package cn.thinkfree.service.customercomplaint.impl;

import cn.thinkfree.database.mapper.ComplaintOrderInfoMapper;
import cn.thinkfree.database.model.ComplaintOrderInfo;
import cn.thinkfree.database.model.ComplaintOrderInfoExample;
import cn.thinkfree.service.customercomplaint.CustomerComplaintService;
import cn.thinkfree.service.customercomplaint.vo.CreateComplaintVO;
import cn.thinkfree.service.event.EventService;
import cn.thinkfree.service.pcthirdpartdate.ThirdPartDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerComplaintServiceImpl implements CustomerComplaintService {

    @Autowired
    ComplaintOrderInfoMapper complaintOrderInfoMapper;

    /**
     * 创建投诉订单
     *
     * @param complaintOrderInfo
     * @return
     */
    @Override
    public CreateComplaintVO saveCustomerComplaint(ComplaintOrderInfo complaintOrderInfo) {
        CreateComplaintVO createComplaintVO = new CreateComplaintVO();
        if (complaintOrderInfo != null) {
            // 投诉订单数据入库
            int id = complaintOrderInfoMapper.insertSelective(complaintOrderInfo);
            if (id > 0) {
                // 投诉订单数据查询
                ComplaintOrderInfoExample example = new ComplaintOrderInfoExample();
                example.createCriteria().andWorkOrderNoEqualTo(complaintOrderInfo.getWorkOrderNo());
                List<ComplaintOrderInfo> list = complaintOrderInfoMapper.selectByExample(example);
                if (list == null || list.size() < 0) {
                   return null;
                }
                // 拼装返回参数
                createComplaintVO.setWorkOrderNo(list.get(0).getWorkOrderNo());
                createComplaintVO.setComplaintInfo(list.get(0).getComplaintInfo());
                createComplaintVO.setComplaintTime(list.get(0).getComplaintTime());
                createComplaintVO.setComplaintType(list.get(0).getComplaintType());
                createComplaintVO.setConstructionOrderNo(list.get(0).getConstructionOrderNo());
                createComplaintVO.setHandleUserId(list.get(0).getHandleUserId());
                createComplaintVO.setPriority(list.get(0).getPriority());
                createComplaintVO.setImgList(list.get(0).getImglist());
                createComplaintVO.setProcessState(list.get(0).getProcessState());
                createComplaintVO.setProjectNo(list.get(0).getProjectNo());
                //createComplaintVO.setEndTime(list.get(0).getEndTime());
                // todo
                createComplaintVO.setCompanyName("");
                createComplaintVO.setAddress("");
                createComplaintVO.setDesignerName("");
                createComplaintVO.setName("");
                createComplaintVO.setPhone("");

            }
        }
        return createComplaintVO;
    }

    /**
     * 输入被投诉项目编号
     * 则业主姓名、业主手机号、设计师姓名、所属设计公司名称、项目详细地址将自动同步出来
     *
     * @param projectNo
     * @return
     */
    @Override
    public Map<String, String> getCustomerComplaint(String projectNo) {
        return null;
    }

}
