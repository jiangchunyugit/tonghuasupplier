package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.constants.OneTrue;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.model.BusinessEntity;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.utils.BeanValidator;
import cn.thinkfree.database.vo.BusinessEntitySEO;
import cn.thinkfree.database.vo.BusinessEntityVO;
import cn.thinkfree.database.vo.CompanyInfoVo;
import cn.thinkfree.database.vo.Severitys;
import cn.thinkfree.service.businessentity.BusinessEntityService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/businessEntity")
@Api(value = "经营主体",description = "经营主体")
public class BusinessEntityController extends AbsBaseController{

    @Autowired
    BusinessEntityService businessEntityService;

    /**
     * 创建经营主体
     */
    @RequestMapping(value = "/saveBusinessEntity", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="经营主体管理：新增")
    public MyRespBundle<String> saveBusinessEntity(@ApiParam("经营主体信息") BusinessEntity businessEntity){
        BeanValidator.validate(businessEntity, Severitys.Insert.class);
        int line = businessEntityService.addBusinessEntity(businessEntity);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, line);
        }
        return sendJsonData(ResultMessage.FAIL, line);
    }

    /**
     * 编辑经营主体信息
     */
    @RequestMapping(value = "/updateBusinessEntity", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="经营主体管理：编辑")
    public MyRespBundle<String> updateBusinessEntity(@ApiParam("经营主体信息")BusinessEntity businessEntity){
        BeanValidator.validate(businessEntity, Severitys.Update.class);
        int line = businessEntityService.updateBusinessEntity(businessEntity);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, line);
        }
        return sendJsonData(ResultMessage.FAIL, line);
    }

    /**
     * 查询经营主体信息
     */
    @RequestMapping(value = "/businessEntitylist", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="经营主体管理：经营主体分页查询")
    public MyRespBundle<PageInfo<CompanyInfo>> businessEntitylist(@ApiParam("查询经营主体参数")BusinessEntitySEO businessEntitySEO){

        PageInfo<BusinessEntity> pageInfo = businessEntityService.businessEntityList(businessEntitySEO);

        return sendJsonData(ResultMessage.SUCCESS, pageInfo);
    }

    /**
     * 经营主体详情
     */
    @RequestMapping(value = "/businessEntityDetails", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="经营主体管理：查看")
    public MyRespBundle<BusinessEntityVO> businessEntityDetails(@ApiParam("经营主体id")@RequestParam(value = "businessEntityId") Integer businessEntityId){

        if(businessEntityId ==null) {
            return sendJsonData(ResultMessage.FAIL, businessEntityId);
        }
        BusinessEntityVO businessEntityVO = businessEntityService.businessEntityDetails(businessEntityId);
        return sendJsonData(ResultMessage.SUCCESS, businessEntityVO);
    }

    /**
     * 经营主体
     */
    @RequestMapping(value = "/businessEntitys", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="经营主体管理：经营主体信息")
    public MyRespBundle<List<BusinessEntity>> businessEntitys(){

        List<BusinessEntity> businessEntitys = businessEntityService.businessEntitys();

        return sendJsonData(ResultMessage.SUCCESS, businessEntitys);
    }

    /**
     * 经营主体
     */
    @RequestMapping(value = "/businessEntityDelete", method = RequestMethod.DELETE)
    @MyRespBody
    @ApiOperation(value="经营主体管理：删除")
    public MyRespBundle<PageInfo<CompanyInfoVo>> businessEntityDelete(@ApiParam("经营主体id")@RequestParam(value = "businessEntityId") Integer businessEntityId){

        if(businessEntityId ==null) {
            return sendJsonData(ResultMessage.FAIL,businessEntityId);
        }
        BeanValidator.validate(businessEntityId, Severitys.Update.class);
        BusinessEntity businessEntity = new BusinessEntity();
        businessEntity.setId(businessEntityId);
        businessEntity.setIsDel(OneTrue.YesOrNo.YES.val.shortValue());
        int line = businessEntityService.updateBusinessEntity(businessEntity);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, line);
        }
        return sendJsonData(ResultMessage.FAIL, line);
    }

    /**
     * 经营主体
     */
    @RequestMapping(value = "/businessEntityEnable", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="经营主体管理：启用")
    public MyRespBundle<PageInfo<CompanyInfoVo>> businessEntityEnable(@ApiParam("经营主体id")@RequestParam(value = "businessEntityId") Integer businessEntityId){

        if(businessEntityId ==null) {
            return sendJsonData(ResultMessage.FAIL,businessEntityId);
        }
        BeanValidator.validate(businessEntityId, Severitys.Update.class);
        BusinessEntity businessEntity = new BusinessEntity();
        businessEntity.setId(businessEntityId);
        businessEntity.setIsEnable(UserEnabled.Enabled_false.shortVal().shortValue());
        int line = businessEntityService.updateBusinessEntity(businessEntity);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, line);
        }
        return sendJsonData(ResultMessage.FAIL, line);
    }

    /**
     * 经营主体
     */
    @RequestMapping(value = "/businessEntityDelete", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="经营主体管理：禁用")
    public MyRespBundle<PageInfo<CompanyInfoVo>> businessEntityDisable(@ApiParam("经营主体id")@RequestParam(value = "businessEntityId") Integer businessEntityId){

        if(businessEntityId ==null) {
            return sendJsonData(ResultMessage.FAIL,businessEntityId);
        }
        BeanValidator.validate(businessEntityId, Severitys.Update.class);
        BusinessEntity businessEntity = new BusinessEntity();
        businessEntity.setId(businessEntityId);
        businessEntity.setIsDel(UserEnabled.Disable.shortVal());
        int line = businessEntityService.updateBusinessEntity(businessEntity);
        if(line > 0){
            return sendJsonData(ResultMessage.SUCCESS, line);
        }
        return sendJsonData(ResultMessage.FAIL, line);
    }
}

