package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.constants.OneTrue;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.model.BusinessEntity;
import cn.thinkfree.database.utils.BeanValidator;
import cn.thinkfree.database.vo.BusinessEntitySEO;
import cn.thinkfree.database.vo.BusinessEntityVO;
import cn.thinkfree.database.vo.Severitys;
import cn.thinkfree.service.businessentity.BusinessEntityService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 经营主体
 */
@RestController
@RequestMapping(value = "/businessEntity")
@Api(value = "前端使用---经营主体数据---蒋春雨",description = "前端使用---经营主体数据---蒋春雨")
public class BusinessEntityController extends AbsBaseController{

    @Autowired
    BusinessEntityService businessEntityService;

    /**
     * 创建经营主体
     */
    @PostMapping(value = "/saveBusinessEntity")
    @MyRespBody
    @ApiOperation(value="经营主体数据：创建主体")
    public MyRespBundle<String> saveBusinessEntity(@ApiParam("经营主体信息") BusinessEntityVO businessEntityVO){
        BeanValidator.validate(businessEntityVO, Severitys.Insert.class);
        int line = businessEntityService.addBusinessEntity(businessEntityVO);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, line);
        }
        return sendJsonData(ResultMessage.FAIL, line);
    }

    /**
     * 编辑经营主体信息
     */
    @PostMapping(value = "/updateBusinessEntity")
    @MyRespBody
    @ApiOperation(value="经营主体数据：编辑主体")
    public MyRespBundle<String> updateBusinessEntity(@ApiParam("经营主体信息")BusinessEntityVO businessEntityVO){
        BeanValidator.validate(businessEntityVO, Severitys.Update.class);
        int line = businessEntityService.updateBusinessEntity(businessEntityVO);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, line);
        }
        return sendJsonData(ResultMessage.FAIL, line);
    }

    /**
     * 查询经营主体信息
     */
    @GetMapping(value = "/businessEntitylist")
    @MyRespBody
    @ApiOperation(value="经营主体数据：经营主体数据维护（经营主体分页查询）")
    public MyRespBundle<PageInfo<BusinessEntityVO>> businessEntitylist(@ApiParam("查询经营主体参数")BusinessEntitySEO businessEntitySEO){

        PageInfo<BusinessEntityVO> pageInfo = businessEntityService.businessEntityList(businessEntitySEO);

        return sendJsonData(ResultMessage.SUCCESS, pageInfo);
    }

    /**
     * 经营主体详情
     */
    @GetMapping(value = "/businessEntityDetails")
    @MyRespBody
    @ApiOperation(value="经营主体数据：查看主体")
    public MyRespBundle<BusinessEntityVO> businessEntityDetails(@ApiParam("经营主体id")@RequestParam(value = "id") Integer id){

        BusinessEntityVO businessEntityVO = businessEntityService.businessEntityDetails(id);
        return sendJsonData(ResultMessage.SUCCESS, businessEntityVO);
    }

    /**
     * 经营主体
     */
    @GetMapping(value = "/businessEntitys")
    @MyRespBody
    @ApiOperation(value="经营主体数据：经营主体信息")
    public MyRespBundle<List<BusinessEntity>> businessEntitys(){

        List<BusinessEntity> businessEntitys = businessEntityService.businessEntices();

        return sendJsonData(ResultMessage.SUCCESS, businessEntitys);
    }

    /**
     * 经营主体
     */
    @PostMapping(value = "/businessEntityDelete")
    @MyRespBody
    @ApiOperation(value="经营主体数据：删除")
    public MyRespBundle<String> businessEntityDelete(@ApiParam("经营主体id")@RequestParam(value = "id") Integer id){

        BusinessEntityVO businessEntityVO = new BusinessEntityVO();
        businessEntityVO.setId(id);
        businessEntityVO.setIsDel(OneTrue.YesOrNo.YES.val.shortValue());
        int line = businessEntityService.updateBusinessEntity(businessEntityVO);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

    /**
     * 经营主体
     */
    @PostMapping(value = "/businessEntityEnable")
    @MyRespBody
    @ApiOperation(value="经营主体数据：启用")
    public MyRespBundle<String> businessEntityEnable(@ApiParam("经营主体id")@RequestParam(value = "id") Integer id){

        BusinessEntityVO businessEntity = new BusinessEntityVO();
        businessEntity.setId(id);
        businessEntity.setIsEnable(UserEnabled.Enabled_true.shortVal().shortValue());
        int line = businessEntityService.enableBusinessEntity(businessEntity);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

    /**
     * 经营主体
     */
    @RequestMapping(value = "/businessEntityDisable", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="经营主体数据：禁用")
    public MyRespBundle<String> businessEntityDisable(@ApiParam("经营主体id")@RequestParam(value = "id") Integer id){

        BusinessEntityVO businessEntity = new BusinessEntityVO();
        businessEntity.setId(id);
        businessEntity.setIsEnable(UserEnabled.Disable.shortVal());
        int line = businessEntityService.enableBusinessEntity(businessEntity);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }
}

