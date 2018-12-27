package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.MaterialsRemAgency;
import cn.thinkfree.database.vo.AgencyContractCompanyInfoVo;
import cn.thinkfree.service.materialsremagency.MaterialsRemAgencyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 经销商
 */
@RestController
@RequestMapping(value = "/materialsRemAgency")
@Api(value = "前端使用---经销商---蒋春雨",description = "前端使用---经销商---蒋春雨")
public class MaterialsRemAgencyController extends AbsBaseController{

    @Autowired
    MaterialsRemAgencyService materialsRemAgencyService;

    @GetMapping(value = "/materialsRemAgencyList")
    @MyRespBody
    @ApiOperation(value="经销商：经销商信息")
    public MyRespBundle<List<MaterialsRemAgency>> materialsRemAgencyList(@ApiParam("经销商编码")String code,@ApiParam("经销商名称")String name){

        return sendJsonData(ResultMessage.SUCCESS, materialsRemAgencyService.getMaterialsRemAgencys(code,name));
    }

    @GetMapping(value = "/companyAgencyList")
    @MyRespBody
    @ApiOperation(value="经销商：合同列表入口选择经销商信息")
    public MyRespBundle<List<AgencyContractCompanyInfoVo>> companyAgencyList(@ApiParam("经销商名称")@RequestParam(value = "companyName") String companyName){

        List<AgencyContractCompanyInfoVo> agencyContractCompanyInfoVos = materialsRemAgencyService.getAgencyCompanyInfos("",companyName);
        if (agencyContractCompanyInfoVos.size() ==0) {

            return sendFailMessage("没有查到相关入驻成功的经销商信息。请先去办理经销商");
        }
        return sendJsonData(ResultMessage.SUCCESS, agencyContractCompanyInfoVos);
    }

    @GetMapping(value = "/companyAgency")
    @MyRespBody
    @ApiOperation(value="经销商：品牌入口选择经销商信息")
    public MyRespBundle<AgencyContractCompanyInfoVo> companyAgency(@ApiParam("公司id")@RequestParam(value = "companyId")String companyId){

        if (StringUtils.isBlank(companyId)) {
            return sendJsonData(ResultMessage.FAIL,"操作失败,公司id不可以为空");
        }
        List<AgencyContractCompanyInfoVo> agencyContractCompanyInfoVos = materialsRemAgencyService.getAgencyCompanyInfos(companyId,"");
        return sendJsonData(ResultMessage.SUCCESS,agencyContractCompanyInfoVos.size()>0?agencyContractCompanyInfoVos.get(0):new AgencyContractCompanyInfoVo() );
    }
}

