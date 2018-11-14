package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.pcvo.ProjectQuotationCheckVo;
import cn.thinkfree.database.pcvo.QuotationVo;
import cn.thinkfree.database.vo.BasisConstructionVO;
import cn.thinkfree.database.vo.HardQuoteVO;
import cn.thinkfree.database.vo.SoftQuoteVO;
import cn.thinkfree.service.neworder.ReviewDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: jiang
 * @Date: 2018/11/13 10:24
 * @Description: 审核详情页
 */
@Api(tags = "PC-精准报价相关接口")
@RestController
@RequestMapping(value = "reviewDetails")
public class ReviewDetailsController extends AbsBaseController {
    @Autowired
    private ReviewDetailsService reviewDetailsService;

    @RequestMapping(value = "getPriceDetail", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取精准报价")
    public MyRespBundle<List<QuotationVo>> getPriceDetail(
            @RequestParam(name = "projectNo") @ApiParam(value = "项目编号  1223098338391", name = "projectNo") String projectNo) {
        return reviewDetailsService.getPriceDetail(projectNo);
    }

    @RequestMapping(value = "saveSoftQuote", method = RequestMethod.POST)
    @ApiOperation(value = "新增软装保价", notes = "")
    public MyRespBundle<String> saveSoftQuote(
            @RequestParam(name = "projectNo") @ApiParam(value = "项目编号", name = "projectNo") String projectNo,
            @RequestParam(name = "roomType") @ApiParam(value = "房间类型", name = "roomType") String roomType,
            @RequestParam(name = "roomName") @ApiParam(value = "房间名称", name = "roomName") String roomName,
            @RequestParam(name = "materialName") @ApiParam(value = "产品名称", name = "materialName") String materialName,
            @RequestParam(name = "brand") @ApiParam(value = "品牌", name = "brand") String brand,
            @RequestParam(name = "model") @ApiParam(value = "型号", name = "model") String model,
            @RequestParam(name = "spec") @ApiParam(value = "规格", name = "spec") String spec,
            @RequestParam(name = "unitPrice") @ApiParam(value = "单价", name = "unitPrice") BigDecimal unitPrice,
            @RequestParam(name = "usedQuantity") @ApiParam(value = "数量", name = "usedQuantity") Integer usedQuantity,
            @RequestParam(name = "totalPrice") @ApiParam(value = "房间类型", name = "totalPrice") BigDecimal totalPrice,
            @RequestParam(name = "id") @ApiParam(value = "主键ID", name = "id") String id) {
        SoftQuoteVO softQuoteVO = new SoftQuoteVO();
        softQuoteVO.setProjectNo(projectNo);
        softQuoteVO.setRoomType(roomType);
        softQuoteVO.setRoomName(roomName);
        softQuoteVO.setBrand(brand);
        softQuoteVO.setModel(model);
        softQuoteVO.setMaterialName(materialName);
        softQuoteVO.setSpec(spec);
        softQuoteVO.setUnitPrice(unitPrice);
        softQuoteVO.setUsedQuantity(usedQuantity);
        softQuoteVO.setTotalPrice(totalPrice);
        softQuoteVO.setId(id);
        return reviewDetailsService.saveSoftQuote(softQuoteVO);
    }

    @RequestMapping(value = "saveHardQuote", method = RequestMethod.POST)
    @ApiOperation(value = "新增硬装保价", notes = "")
    public MyRespBundle<String> saveHardQuote(
            @RequestParam(name = "projectNo") @ApiParam(value = "项目编号", name = "projectNo") String projectNo,
            @RequestParam(name = "materialName") @ApiParam(value = "产品名称", name = "materialName") String materialName,
            @RequestParam(name = "roomType") @ApiParam(value = "房间类型", name = "roomType") String roomType,
            @RequestParam(name = "roomName") @ApiParam(value = "房间名称", name = "roomName") String roomName,
            @RequestParam(name = "brand") @ApiParam(value = "品牌", name = "brand") String brand,
            @RequestParam(name = "model") @ApiParam(value = "型号", name = "model") String model,
            @RequestParam(name = "spec") @ApiParam(value = "规格", name = "spec") String spec,
            @RequestParam(name = "unitPrice") @ApiParam(value = "单价", name = "unitPrice") BigDecimal unitPrice,
            @RequestParam(name = "usedQuantity") @ApiParam(value = "数量", name = "usedQuantity") Integer usedQuantity,
            @RequestParam(name = "totalPrice") @ApiParam(value = "房间类型", name = "totalPrice") BigDecimal totalPrice,
            @RequestParam(name = "id") @ApiParam(value = "主键ID", name = "id") String id) {
        HardQuoteVO hardQuoteVO = new HardQuoteVO();
        hardQuoteVO.setProjectNo(projectNo);
        hardQuoteVO.setRoomType(roomType);
        hardQuoteVO.setRoomName(roomName);
        hardQuoteVO.setBrand(brand);
        hardQuoteVO.setMaterialName(materialName);
        hardQuoteVO.setModel(model);
        hardQuoteVO.setSpec(spec);
        hardQuoteVO.setUnitPrice(unitPrice);
        hardQuoteVO.setUsedQuantity(usedQuantity);
        hardQuoteVO.setTotalPrice(totalPrice);
        hardQuoteVO.setId(id);
        return reviewDetailsService.saveHardQuote(hardQuoteVO);
    }

    @RequestMapping(value = "saveBasisConstruction", method = RequestMethod.POST)
    @ApiOperation(value = "新增施工保价")
    public MyRespBundle<String> saveBasisConstruction(
            @RequestParam(name = "projectNo") @ApiParam(value = "项目编号", name = "projectNo") String projectNo,
            @RequestParam(name = "roomType") @ApiParam(value = "房间类型", name = "roomType") String roomType,
            @RequestParam(name = "roomName") @ApiParam(value = "房间名称", name = "roomName") String roomName,
            @RequestParam(name = "constructCode") @ApiParam(value = "项目名称", name = "constructCode") String constructCode,
            @RequestParam(name = "constructName") @ApiParam(value = "项目说明", name = "constructName") String constructName,
            @RequestParam(name = "unitPrice") @ApiParam(value = "单价", name = "unitPrice") BigDecimal unitPrice,
            @RequestParam(name = "usedQuantity") @ApiParam(value = "数量", name = "usedQuantity") Integer usedQuantity,
            @RequestParam(name = "totalPrice") @ApiParam(value = "房间类型", name = "totalPrice") BigDecimal totalPrice,
            @RequestParam(name = "id") @ApiParam(value = "主键ID", name = "id") String id) {
        BasisConstructionVO basisConstructionVO = new BasisConstructionVO();
        basisConstructionVO.setProjectNo(projectNo);
        basisConstructionVO.setRoomType(roomType);
        basisConstructionVO.setRoomName(roomName);
        basisConstructionVO.setUnitPrice(unitPrice);
        basisConstructionVO.setUsedQuantity(usedQuantity);
        basisConstructionVO.setTotalPrice(totalPrice);
        basisConstructionVO.setConstructName(constructName);
        basisConstructionVO.setConstructCode(constructCode);
        basisConstructionVO.setId(id);
        return reviewDetailsService.saveBasisConstructionVO(basisConstructionVO);
    }

    @RequestMapping(value = "delSoftQuote", method = RequestMethod.POST)
    @ApiOperation(value = "删除软装保价", notes = "")
    public MyRespBundle<String> delSoftQuote(@RequestParam(name = "id") @ApiParam(value = "主键ID", name = "id") String id) {
        return reviewDetailsService.delSoftQuote(id);
    }

    @RequestMapping(value = "delHardQuote", method = RequestMethod.POST)
    @ApiOperation(value = "删除硬装保价", notes = "")
    public MyRespBundle<String> delHardQuote(@RequestParam(name = "id") @ApiParam(value = "主键ID", name = "id") String id) {
        return reviewDetailsService.delHardQuote(id);
    }

    @RequestMapping(value = "delBasisConstruction", method = RequestMethod.POST)
    @ApiOperation(value = "删除施工保价")
    public MyRespBundle<String> delBasisConstruction(@RequestParam(name = "id") @ApiParam(value = "主键ID", name = "id") String id) {
        return reviewDetailsService.delBasisConstruction(id);
    }

    @RequestMapping(value = "getCheckDetail",method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation("获取精准报价审核信息")
    public MyRespBundle<ProjectQuotationCheckVo> getCheckDetail(
            @RequestParam(name = "projectNo") @ApiParam(value = "项目编号  1223098338391", name = "projectNo") String projectNo){
        return reviewDetailsService.getCheckDetail(projectNo);
    }

    @RequestMapping(value = "addCheckDetail",method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation("提交精准报价审核信息")
    public MyRespBundle<String> addCheckDetail(
            @RequestParam(name = "projectNo") @ApiParam(value = "提交精准报价", name = "projectNo") String projectNo){
        return reviewDetailsService.addCheckDetail(projectNo);
    }

    @RequestMapping(value = "reviewOffer",method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation("审核精准报价")
    public MyRespBundle<String> reviewOffer(
            @RequestParam(name = "projectNo") @ApiParam(value = "提交精准报价", name = "projectNo") String projectNo,
            @RequestParam(name = "result", defaultValue = "-1") @ApiParam(value = "审核结果(1,通过 2,不通过)", name = "result") int result,
            @RequestParam(name = "refuseReason") @ApiParam(value = "不通过原因", name = "refuseReason") String refuseReason){
        return reviewDetailsService.reviewOffer(projectNo, result, refuseReason);
    }

    @RequestMapping(value = "getShangHaiPriceDetail", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation("获取上海报价信息")
    public MyRespBundle getShangHaiPriceDetail(
            @RequestParam(name = "designId") @ApiParam(name = "designId", value = "案例id fc80f1ef-4937-41a4-9443-7ebf95500143",required = true) String designId,
            @RequestParam(name = "projectNo") @ApiParam(name = "projectNo", value = "项目编号 1223098338391",required = true) String projectNo) {
        return reviewDetailsService.getShangHaiPriceDetail(designId,projectNo);
    }


}
