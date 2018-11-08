package cn.thinkfree.service.newsBaseDesign;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.mapper.DesignGradeMapper;
import cn.thinkfree.database.mapper.DesignLabelMapper;
import cn.thinkfree.database.mapper.GrowthValueIntegralMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.pcvo.DesignGradeVo;
import cn.thinkfree.database.pcvo.DesignLabelVo;
import cn.thinkfree.database.pcvo.GrowthValueIntegralVo;
import cn.thinkfree.service.constants.ProjectStatus;
import cn.thinkfree.service.constants.Scheduling;
import cn.thinkfree.service.utils.BaseToVoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BaseDesignServiceImpl implements BaseDesignService {
    @Autowired
    DesignGradeMapper designGradeMapper;
    @Autowired
    DesignLabelMapper designLabelMapper;
    @Autowired
    GrowthValueIntegralMapper growthValueIntegralMapper;


    /**
     * 获取设计师等级列表
     *
     * @param userId
     * @return
     */
    @Override
    public MyRespBundle<List<DesignGradeVo>> getDesignGrade(String userId) {
        DesignGradeExample example = new DesignGradeExample();
        DesignGradeExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        List<DesignGrade> designGrades = designGradeMapper.selectByExample(example);
        List<DesignGradeVo> listVo = BaseToVoUtils.getListVo(designGrades, DesignGradeVo.class);
        return RespData.success(listVo);
    }

    /**
     * 添加设计师等级列表
     * @param designGradeVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle<String> addDesignGrade(DesignGradeVo designGradeVo) {
        if(designGradeVo != null){
            if(designGradeVo.getSort()==null){
                DesignGradeExample example = new DesignGradeExample();
                example.setOrderByClause("sort desc");
                List<DesignGrade> designGrades = designGradeMapper.selectByExample(example);
                DesignGrade designGrade = BaseToVoUtils.getVo(designGradeVo, DesignGrade.class);
                designGrade.setStatus(Scheduling.BASE_STATUS.getValue());
                designGrade.setSort(designGrades.get(0).getSort()+1);
                int i = designGradeMapper.insertSelective(designGrade);
                if(i == 0){
                    return RespData.error("添加失败!");
                }
            }else {
                DesignGrade designGrade = BaseToVoUtils.getVo(designGradeVo, DesignGrade.class);
                DesignGradeExample example = new DesignGradeExample();
                DesignGradeExample.Criteria criteria = example.createCriteria();
                criteria.andSortEqualTo(designGrade.getSort());
                int i = designGradeMapper.updateByExampleSelective(designGrade, example);
                if(i == 0){
                    return RespData.error("修改失败!");
                }
            }
        }
        return RespData.success();
    }

    /**
     * 获取设计师标签
     * @param userId
     * @return
     */
    @Override
    public MyRespBundle<List<DesignLabelVo>> getDesignLabel(String userId) {
        DesignLabelExample example = new DesignLabelExample();
        DesignLabelExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        List<DesignLabel> designLabels = designLabelMapper.selectByExample(example);
        List<DesignLabelVo> listVo = BaseToVoUtils.getListVo(designLabels, DesignLabelVo.class);
        return RespData.success(listVo);
    }

    /**
     * 添加设计师标签
     * @param designLabelVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle<String> addDesignLabel(DesignLabelVo designLabelVo) {
        if(designLabelVo != null){
            if (designLabelVo.getSort()==null){
                DesignLabelExample example = new DesignLabelExample();
                example.setOrderByClause("sort desc");
                List<DesignLabel> designLabels = designLabelMapper.selectByExample(example);
                DesignLabel designLabel = BaseToVoUtils.getVo(designLabelVo, DesignLabel.class);
                designLabel.setStatus(Scheduling.BASE_STATUS.getValue());
                designLabel.setSort(designLabels.get(0).getSort()+1);
                int i = designLabelMapper.insertSelective(designLabel);
                if(i == 0){
                    return RespData.error("添加失败!");
                }
            }else {
                DesignLabel designLabel = BaseToVoUtils.getVo(designLabelVo, DesignLabel.class);
                DesignLabelExample example = new DesignLabelExample();
                DesignLabelExample.Criteria criteria = example.createCriteria();
                criteria.andSortEqualTo(designLabel.getSort());
                int i = designLabelMapper.updateByExampleSelective(designLabel, example);
                if(i == 0){
                    return RespData.error("修改失败!");
                }
            }


        }
        return RespData.success();
    }

    /**
     * 添加长值与积分
     * @param growthValueIntegralVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle<String> addDesignIntegral(GrowthValueIntegralVo growthValueIntegralVo) {
        if(growthValueIntegralVo != null){
            if (growthValueIntegralVo.getSort()==null){
                GrowthValueIntegralExample example = new GrowthValueIntegralExample();
                example.setOrderByClause("sort desc");
                List<GrowthValueIntegral> growthValueIntegrals = growthValueIntegralMapper.selectByExample(example);
                GrowthValueIntegral growthValueIntegral = BaseToVoUtils.getVo(growthValueIntegralVo, GrowthValueIntegral.class);
                growthValueIntegral.setStatus(Scheduling.BASE_STATUS.getValue());
                growthValueIntegral.setSort(growthValueIntegrals.get(0).getSort()+1);
                int i = growthValueIntegralMapper.insertSelective(growthValueIntegral);
                if(i == 0){
                    return RespData.error("添加失败!");
                }
            }else {
                GrowthValueIntegral growthValueIntegral = BaseToVoUtils.getVo(growthValueIntegralVo, GrowthValueIntegral.class);
                GrowthValueIntegralExample example = new GrowthValueIntegralExample();
                GrowthValueIntegralExample.Criteria criteria = example.createCriteria();
                criteria.andSortEqualTo(growthValueIntegral.getSort());
                int i = growthValueIntegralMapper.updateByExampleSelective(growthValueIntegral, example);
                if (i == 0) {
                    return RespData.error("修改失败!");
                }
            }
        }
        return RespData.success();
    }

    /**
     * 获取长值与积分列表
     * @param userId
     * @return
     */
    @Override
    public MyRespBundle<List<GrowthValueIntegralVo>> getDesignIntegral(String userId) {
        GrowthValueIntegralExample example = new GrowthValueIntegralExample();
        GrowthValueIntegralExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        List<GrowthValueIntegral> growthValueIntegrals = growthValueIntegralMapper.selectByExample(example);
        List<GrowthValueIntegralVo> listVo = BaseToVoUtils.getListVo(growthValueIntegrals, GrowthValueIntegralVo.class);
        return RespData.success(listVo);
    }
}
