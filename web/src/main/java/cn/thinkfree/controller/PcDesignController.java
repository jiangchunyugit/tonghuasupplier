package cn.thinkfree.controller;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.model.DesignGrade;
import cn.thinkfree.database.model.DesignLabel;
import cn.thinkfree.database.model.GrowthValueIntegral;
import cn.thinkfree.database.pcvo.DesignGradeVo;
import cn.thinkfree.database.pcvo.DesignLabelVo;
import cn.thinkfree.database.pcvo.GrowthValueIntegralVo;
import cn.thinkfree.service.newsBaseDesign.BaseDesignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author gejiaming
 */
@Api(tags = "PC-设计师等级")
@RestController
@RequestMapping("pcDesign")
public class PcDesignController {

    @Autowired
    BaseDesignService baseDesignService;

    @RequestMapping(value = "getDesignGrade",method = RequestMethod.POST)
    @ApiOperation("获取设计师等级列表")
    public MyRespBundle<List<DesignGradeVo>> getDesignGrade(@RequestParam(name = "userId")@ApiParam(name = "userId",value = "用户id")String userId){
        return baseDesignService.getDesignGrade(userId);
    }

    @RequestMapping(value = "addDesignGrade",method = RequestMethod.POST)
    @ApiOperation("添加设计师等级")
    public MyRespBundle<String> addDesignGrade(@ApiParam(name = "designGrade",value = "设计师等级实体")DesignGradeVo designGradeVo){
        return baseDesignService.addDesignGrade(designGradeVo);
    }

    @RequestMapping(value = "getDesignLabel",method = RequestMethod.POST)
    @ApiOperation("获取设计师标签列表")
    public MyRespBundle<List<DesignLabelVo>> getDesignLabel(@RequestParam(name = "userId")@ApiParam(name = "userId",value = "用户id")String userId){
        return baseDesignService.getDesignLabel(userId);
    }

    @RequestMapping(value = "addDesignLabel",method = RequestMethod.POST)
    @ApiOperation("添加设计师标签")
    public MyRespBundle<String> addDesignLabel(@ApiParam(name = "designLabel",value = "设计师标签实体")DesignLabelVo designLabelVo){
        return baseDesignService.addDesignLabel(designLabelVo);
    }

    @RequestMapping(value = "addDesignIntegral",method = RequestMethod.POST)
    @ApiOperation("添加成长值与积分")
    public MyRespBundle<String> addDesignIntegral(@ApiParam(name = "growthValueIntegral",value = "成长值与积分实体")GrowthValueIntegralVo growthValueIntegralVo){
        return baseDesignService.addDesignIntegral(growthValueIntegralVo);
    }

    @RequestMapping(value = "getDesignIntegral",method = RequestMethod.POST)
    @ApiOperation("获取成长值与积分列表")
    public MyRespBundle<List<GrowthValueIntegralVo>> getDesignIntegral(@RequestParam(name = "userId")@ApiParam(name = "userId",value = "用户id")String userId){
        return baseDesignService.getDesignIntegral(userId);
    }


}
