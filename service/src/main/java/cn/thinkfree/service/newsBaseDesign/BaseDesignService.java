package cn.thinkfree.service.newsBaseDesign;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.model.DesignGrade;
import cn.thinkfree.database.model.DesignLabel;
import cn.thinkfree.database.model.GrowthValueIntegral;
import cn.thinkfree.database.pcvo.DesignGradeVo;
import cn.thinkfree.database.pcvo.DesignLabelVo;
import cn.thinkfree.database.pcvo.GrowthValueIntegralVo;

import java.util.List;

public interface BaseDesignService {
    /**
     * 获取设计师等级列表
     * @param userId
     * @return
     */
    MyRespBundle<List<DesignGradeVo>> getDesignGrade(String userId);

    /**
     * 添加设计师等级列表
     * @param designGradeVo
     * @return
     */
    MyRespBundle<String> addDesignGrade(DesignGradeVo designGradeVo);

    /**
     * 获取设计师标签
     * @param userId
     * @return
     */
    MyRespBundle<List<DesignLabelVo>> getDesignLabel(String userId);

    /**
     * 添加设计师标签
     * @param designLabelVo
     * @return
     */
    MyRespBundle<String> addDesignLabel(DesignLabelVo designLabelVo);

    /**
     * 添加长值与积分
     * @param growthValueIntegralVo
     * @return
     */
    MyRespBundle<String> addDesignIntegral(GrowthValueIntegralVo growthValueIntegralVo);

    /**
     * 获取长值与积分列表
     * @param userId
     * @return
     */
    MyRespBundle<List<GrowthValueIntegralVo>> getDesignIntegral(String userId);
}
