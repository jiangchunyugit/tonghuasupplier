package cn.thinkfree.service.companyapply;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.database.mapper.PcApplyInfoMapper;
import cn.thinkfree.database.model.PcApplyInfo;
import cn.thinkfree.database.model.PcApplyInfoExample;
import cn.thinkfree.database.model.SystemMessage;
import cn.thinkfree.database.vo.CompanyApplySEO;
import cn.thinkfree.database.vo.PcApplyInfoVo;
import cn.thinkfree.database.vo.PcUserInfoVo;
import cn.thinkfree.service.constants.CompanyApply;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * @author ying007
 * 公司申请
 */
@Service
public class CompanyApplyServiceImpl implements CompanyApplyService {
    @Autowired
    PcApplyInfoMapper pcApplyInfoMapper;

    /**
     * 添加申请记录
     * @param pcApplyInfo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addApplyInfo(PcApplyInfo pcApplyInfo) {
        Date date = new Date();
        pcApplyInfo.setApplyDate(date);
        pcApplyInfo.setTransactType(SysConstants.YesOrNo.NO.shortVal());
        pcApplyInfo.setIsDelete(SysConstants.YesOrNo.NO.shortVal());
        int line = pcApplyInfoMapper.insertSelective(pcApplyInfo);
        if(line > 0){
            return true;
        }
        return false;
    }

    /**
     * 删除申请记录（修改is_delete=1) 申请类型是1：后台运营申请才可以删除
     * @param id
     * @return
     */
    @Override
    public boolean updateApply(Integer id) {
        PcApplyInfo pcApplyInfo = new PcApplyInfo();
//        pcApplyInfo.setId(id);
        pcApplyInfo.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
//        pcApplyInfo.setApplyType(1);
        PcApplyInfoExample pcApplyInfoExample = new PcApplyInfoExample();
        pcApplyInfoExample.createCriteria().andIdEqualTo(id).
                andApplyTypeEqualTo(CompanyApply.applyTpye.PCAPPLY.code.shortValue());
        int line = pcApplyInfoMapper.updateByExampleSelective(pcApplyInfo, pcApplyInfoExample);
        if(line > 0){
            return true;
        }
        return false;
    }

    /**
     * 根据id查询申请记录
     * @param id
     * @return
     */
    @Override
    public PcApplyInfoVo findById(Integer id) {
        return pcApplyInfoMapper.findById(id);
    }

    /**
     * 根据参数查询申请信息
     * @param companyApplySEO
     * @return
     */
    @Override
    public PageInfo<PcApplyInfoVo> findByParam(CompanyApplySEO companyApplySEO) {
        String param = companyApplySEO.getParam();
        if(StringUtils.isNotBlank(param)){
            companyApplySEO.setParam("%" + param + "%");
        }
        PageHelper.startPage(companyApplySEO.getPage(), companyApplySEO.getRows());
        List<PcApplyInfoVo> pcUserInfoVos = pcApplyInfoMapper.findByParam(companyApplySEO);
        PageInfo<PcApplyInfoVo> pageInfo = new PageInfo<>(pcUserInfoVos);
        return pageInfo;
    }

}
