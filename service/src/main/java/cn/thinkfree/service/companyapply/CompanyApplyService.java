package cn.thinkfree.service.companyapply;

import cn.thinkfree.database.model.PcApplyInfo;
import cn.thinkfree.database.vo.CompanyApplySEO;
import cn.thinkfree.database.vo.PcApplyInfoVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author ying007
 * 公司申请事项接口：入驻申请，资质变更。。。
 */
public interface CompanyApplyService {
    /**
     * 公司申请事项（运营人员添加入驻信息）
     * @param pcApplyInfo
     * @return
     */
    boolean addApplyInfo(PcApplyInfo pcApplyInfo);

    /**
     * 删除申请记录（is_delete)
     * @param id
     * @return
     */
    boolean updateApply(Integer id);

    /**
     * 查询申请记录
     * @param id
     * @return
     */
    PcApplyInfoVo findById(Integer id);

    /**
     * 根据参数查询申请信息
     * @param companyApplySEO
     * @return
     */
    PageInfo<PcApplyInfoVo> findByParam(CompanyApplySEO companyApplySEO);
}
