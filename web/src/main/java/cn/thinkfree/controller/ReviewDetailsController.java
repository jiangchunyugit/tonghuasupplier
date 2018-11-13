package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
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

import java.util.List;

/**
 * @Auther: jiang
 * @Date: 2018/11/13 10:24
 * @Description: 审核详情页
 */
@Api(tags = "审核详情页")
@RestController
@RequestMapping(value = "reviewDetails")
public class ReviewDetailsController extends AbsBaseController {
    @Autowired
    private ReviewDetailsService reviewDetailsService;
    /**
     * @return
     * @Author jiang
     * @Description 基础施工详情
     * @Date
     * @Param
     **/
    @RequestMapping(value = "getBasisConstruction", method = RequestMethod.POST)
    @ApiOperation(value = "基础施工详情", notes = "")
    public MyRespBundle<BasisConstructionVO> getBasisConstruction(@RequestParam(name = "projectNo") @ApiParam(value = "项目编号", name = "projectNo") String projectNo,
                                                                  @RequestParam(name = "roomType") @ApiParam(value = "房间类型", name = "roomType") String roomType) {
        if (null == projectNo || "".equals(projectNo)) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        if (null == roomType || "".equals(roomType)) {
            return sendJsonData(ResultMessage.ERROR, "房间类型为空");
        }
        List<BasisConstructionVO> basisConstructionVOList = reviewDetailsService.getBasisConstruction(projectNo,roomType);
        return sendJsonData(ResultMessage.SUCCESS, basisConstructionVOList);
    }

    /**
     * @return
     * @Author jiang
     * @Description 硬装保价详情
     * @Date
     * @Param
     **/
    @RequestMapping(value = "getHardQuote", method = RequestMethod.POST)
    @ApiOperation(value = "硬装保价详情", notes = "")
    public MyRespBundle<HardQuoteVO> getHardQuote(@RequestParam(name = "projectNo") @ApiParam(value = "项目编号", name = "projectNo") String projectNo,
                                                  @RequestParam(name = "roomType") @ApiParam(value = "房间类型", name = "roomType") String roomType) {
        if (null == projectNo || "".equals(projectNo)) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        if (null == roomType || "".equals(roomType)) {
            return sendJsonData(ResultMessage.ERROR, "房间类型为空");
        }
        List<HardQuoteVO> hardQuoteVOList = reviewDetailsService.getHardQuote(projectNo,roomType);
        return sendJsonData(ResultMessage.SUCCESS, hardQuoteVOList);
    }

    /**
     * @return
     * @Author jiang
     * @Description 软装保价详情
     * @Date
     * @Param
     **/
    @RequestMapping(value = "getSoftQuote", method = RequestMethod.POST)
    @ApiOperation(value = "软装保价详情", notes = "")
    public MyRespBundle<SoftQuoteVO> getSoftQuote(@RequestParam(name = "projectNo") @ApiParam(value = "项目编号", name = "projectNo") String projectNo,
                                                  @RequestParam(name = "roomType") @ApiParam(value = "房间类型", name = "roomType") String roomType) {
        if (null == projectNo || "".equals(projectNo)) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        if (null == roomType || "".equals(roomType)) {
            return sendJsonData(ResultMessage.ERROR, "房间类型为空");
        }
        List<SoftQuoteVO> softQuoteVOList = reviewDetailsService.getSoftQuote(projectNo,roomType);
        return sendJsonData(ResultMessage.SUCCESS, softQuoteVOList);
    }

    /**
     * @return
     * @Author jiang
     * @Description 软装保价详情
     * @Date
     * @Param
     **/
    @RequestMapping(value = "saveSoftQuote", method = RequestMethod.POST)
    @ApiOperation(value = "新增软装保价", notes = "")
    public MyRespBundle<SoftQuoteVO> saveSoftQuote(@RequestParam(name = "projectNo") @ApiParam(value = "项目编号", name = "projectNo") String projectNo,
                                                   @RequestParam(name = "roomType") @ApiParam(value = "房间类型", name = "roomType") String roomType,
                                                   @RequestParam(name = "brand") @ApiParam(value = "品牌", name = "brand") String brand,
                                                   @RequestParam(name = "model") @ApiParam(value = "型号", name = "model") String model,
                                                   @RequestParam(name = "spec") @ApiParam(value = "规格", name = "spec") String spec,
                                                   @RequestParam(name = "unitPrice") @ApiParam(value = "单价", name = "unitPrice") Integer unitPrice,
                                                   @RequestParam(name = "usedQuantity") @ApiParam(value = "数量", name = "usedQuantity") Integer usedQuantity,
                                                   @RequestParam(name = "totalPrice") @ApiParam(value = "房间类型", name = "totalPrice") Integer totalPrice){
        if (null == projectNo || "".equals(projectNo)) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        if (null == roomType || "".equals(roomType)) {
            return sendJsonData(ResultMessage.ERROR, "房间类型为空");
        }
        SoftQuoteVO softQuoteVO = new SoftQuoteVO();
        softQuoteVO.setProjectNo(projectNo);
        softQuoteVO.setRoomType(roomType);
        softQuoteVO.setBrand(brand);
        softQuoteVO.setModel(model);
        softQuoteVO.setSpec(spec);
        softQuoteVO.setUnitPrice(unitPrice);
        softQuoteVO.setUsedQuantity(usedQuantity);
        softQuoteVO.setTotalPrice(totalPrice);
        List<SoftQuoteVO> softQuoteVOList = reviewDetailsService.saveSoftQuote(softQuoteVO);
        return sendJsonData(ResultMessage.SUCCESS, softQuoteVOList);
    }


    /**
     * @return
     * @Author jiang
     * @Description 硬装保价详情
     * @Date
     * @Param
     **/
    @RequestMapping(value = "saveHardQuote", method = RequestMethod.POST)
    @ApiOperation(value = "新增硬装保价", notes = "")
    public MyRespBundle<HardQuoteVO> saveHardQuote(@RequestParam(name = "projectNo") @ApiParam(value = "项目编号", name = "projectNo") String projectNo,
                                                   @RequestParam(name = "roomType") @ApiParam(value = "房间类型", name = "roomType") String roomType,
                                                   @RequestParam(name = "brand") @ApiParam(value = "品牌", name = "brand") String brand,
                                                   @RequestParam(name = "model") @ApiParam(value = "型号", name = "model") String model,
                                                   @RequestParam(name = "spec") @ApiParam(value = "规格", name = "spec") String spec,
                                                   @RequestParam(name = "unitPrice") @ApiParam(value = "单价", name = "unitPrice") Integer unitPrice,
                                                   @RequestParam(name = "usedQuantity") @ApiParam(value = "数量", name = "usedQuantity") Integer usedQuantity,
                                                   @RequestParam(name = "totalPrice") @ApiParam(value = "房间类型", name = "totalPrice") Integer totalPrice){
        if (null == projectNo || "".equals(projectNo)) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        if (null == roomType || "".equals(roomType)) {
            return sendJsonData(ResultMessage.ERROR, "房间类型为空");
        }
        HardQuoteVO hardQuoteVO = new HardQuoteVO();
        hardQuoteVO.setProjectNo(projectNo);
        hardQuoteVO.setRoomType(roomType);
        hardQuoteVO.setBrand(brand);
        hardQuoteVO.setModel(model);
        hardQuoteVO.setSpec(spec);
        hardQuoteVO.setUnitPrice(unitPrice);
        hardQuoteVO.setUsedQuantity(usedQuantity);
        hardQuoteVO.setTotalPrice(totalPrice);
        List<HardQuoteVO> hardQuoteVOList = reviewDetailsService.saveHardQuote(hardQuoteVO);
        return sendJsonData(ResultMessage.SUCCESS, hardQuoteVOList);
    }


    /**
     * @return
     * @Author jiang
     * @Description 新增施工保价
     * @Date
     * @Param
     **/
    @RequestMapping(value = "saveBasisConstruction", method = RequestMethod.POST)
    @ApiOperation(value = "新增施工保价", notes = "")
    public MyRespBundle<BasisConstructionVO> saveBasisConstruction(@RequestParam(name = "projectNo") @ApiParam(value = "项目编号", name = "projectNo") String projectNo,
                                                   @RequestParam(name = "roomType") @ApiParam(value = "房间类型", name = "roomType") String roomType,
                                                   @RequestParam(name = "constructCode") @ApiParam(value = "项目名称", name = "constructCode") String constructCode,
                                                   @RequestParam(name = "constructName") @ApiParam(value = "项目说明", name = "constructName") String constructName,
                                                   @RequestParam(name = "unitPrice") @ApiParam(value = "单价", name = "unitPrice") Integer unitPrice,
                                                   @RequestParam(name = "usedQuantity") @ApiParam(value = "数量", name = "usedQuantity") Integer usedQuantity,
                                                   @RequestParam(name = "totalPrice") @ApiParam(value = "房间类型", name = "totalPrice") Integer totalPrice){
        if (null == projectNo || "".equals(projectNo)) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        if (null == roomType || "".equals(roomType)) {
            return sendJsonData(ResultMessage.ERROR, "房间类型为空");
        }
        BasisConstructionVO basisConstructionVO = new BasisConstructionVO();
        basisConstructionVO.setProjectNo(projectNo);
        basisConstructionVO.setRoomType(roomType);

        basisConstructionVO.setUnitPrice(unitPrice);
        basisConstructionVO.setUsedQuantity(usedQuantity);
        basisConstructionVO.setTotalPrice(totalPrice);
        List<BasisConstructionVO> hardQuoteVOList = reviewDetailsService.saveBasisConstructionVO(basisConstructionVO);
        return sendJsonData(ResultMessage.SUCCESS, hardQuoteVOList);
    }

}
