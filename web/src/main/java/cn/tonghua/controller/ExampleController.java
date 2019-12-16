package cn.tonghua.controller;


import cn.tonghua.core.annotation.MyRespBody;
import cn.tonghua.core.base.AbsBaseController;
import cn.tonghua.core.bundle.MyRespBundle;
import cn.tonghua.core.constants.ResultMessage;
import cn.tonghua.database.recogwall.*;
import cn.tonghua.database.utils.BeanValidator;
import cn.tonghua.database.vo.Severitys;
import cn.tonghua.service.example.ExampleService;
import cn.tonghua.service.facerecognition.FaceRecognitionService;
import cn.tonghua.service.utils.Base64Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 测试类
 */
@RestController
@RequestMapping(value = "/recogWall")
@Api(value = "前端使用---测试类---蒋春雨",description = "前端使用---测试类---蒋春雨")
public class ExampleController extends AbsBaseController {

    @Autowired
    ExampleService exampleService;

    @Autowired
    FaceRecognitionService faceRecognitionService;

    /**
     * 测试类
     */
    @RequestMapping(value = "/faceRecognition", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="人脸识别接口")
    public FaceRecognition ebsBranchCompanylist(@RequestBody FaceRecognitionSEO faceRecognitionSEO){

        BeanValidator.validate(faceRecognitionSEO,Severitys.Insert.class);

        return faceRecognitionService.faceRecognition(faceRecognitionSEO);
    }

    /**
     * 测试类
     */
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="测试类")
    public MyRespBundle<String> ebsBranchCompanylist(@RequestParam(value = "path")String path){

        return sendJsonData(ResultMessage.SUCCESS, Base64Util.getImgBase64CodeStr(path));
    }

    /**
     * 测试类
     */
    @RequestMapping(value = "/wealthAna", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="财富分析")
    public WealthAna wealthAna(@ApiParam("客户号（行外客户为-1）")String custNo, @ApiParam("客户评级代码行内客户填写A") String acctypeCode, @ApiParam("客户风险等级代码行内客户为3，行外客户为答题流程最后一个题的结果")@RequestParam(value = "rankCode")String rankCode){

        if (!"-1".equals(custNo)) {

            if (!"A".equals(acctypeCode)||!"3".equals(rankCode)) {

                WealthAna wealthAna = new WealthAna();
                WallHeader wallHeader = new WallHeader();
                wallHeader.setMesFlag("999999");
                wallHeader.setbSerialNo("2018259905367357");
                WallError wallError = new WallError();
                wallError.setErrorCode("AAP005");
                wallError.setErrorInfo("行内客户，客户代码评级A,风险等级3");
                wealthAna.setHeader(wallHeader);
                wealthAna.setError(wallError);
                return wealthAna;
            } else {

                WealthAna wealthAna = new WealthAna();
                WallHeader wallHeader = new WallHeader();
                wallHeader.setMesFlag("000000");
                wallHeader.setbSerialNo("201806251359490006");
                wealthAna.setHeader(wallHeader);

                WallData wallData = new WallData();
                wallData.setCusCode("1608041820");
                wallData.setExamineDt("2019-10-30");
                wallData.setExamineScore("68");
                wallData.setMoreLastexamineScore("2");
                wallData.setExamineTime("15:58:13");
                List<CacuRestAstcatcdetls> cacuRestAstcatcdetls = new ArrayList<>();

                CacuRestAstcatcdetls cacuRestAstcatcdetls1 = new CacuRestAstcatcdetls();
                cacuRestAstcatcdetls1.setAssetcatCode("DCM");
                cacuRestAstcatcdetls1.setAssetcatName("保值的钱");
                cacuRestAstcatcdetls1.setAssetcatPerct("28");
                cacuRestAstcatcdetls1.setCurrentEval("建议增持");
                cacuRestAstcatcdetls1.setCurrentTalks("您的固定收益类资产余额偏低。");
                cacuRestAstcatcdetls1.setProposalPerct("60");
                cacuRestAstcatcdetls1.setProposalTalks("后续可适当增加固定收益类产品的投资比例，提升资产安全性和投资收益。");
                CacuRestAstcatcdetls cacuRestAstcatcdetls2 = new CacuRestAstcatcdetls();
                cacuRestAstcatcdetls2.setAssetcatCode("EQ");
                cacuRestAstcatcdetls2.setAssetcatName("生钱的钱");
                cacuRestAstcatcdetls2.setAssetcatPerct("22");
                cacuRestAstcatcdetls2.setCurrentEval("建议减持");
                cacuRestAstcatcdetls2.setCurrentTalks("您的股票权益类资产余额充裕，茄子君为您过人的投资眼光深深折服。");
                cacuRestAstcatcdetls2.setProposalPerct("20");
                cacuRestAstcatcdetls2.setProposalTalks("后续可参考资产配置建议，均衡配置其他类型产品。");
                CacuRestAstcatcdetls cacuRestAstcatcdetls3 = new CacuRestAstcatcdetls();
                cacuRestAstcatcdetls3.setAssetcatCode("MM");
                cacuRestAstcatcdetls3.setAssetcatName("要花的钱");
                cacuRestAstcatcdetls3.setAssetcatPerct("44");
                cacuRestAstcatcdetls3.setCurrentEval("建议减持");
                cacuRestAstcatcdetls3.setCurrentTalks("您的现金管理类资产余额较多，日常花销费用充足。");
                cacuRestAstcatcdetls3.setProposalPerct("15");
                cacuRestAstcatcdetls3.setProposalTalks("保留家庭月支出4-6倍资金作为日常花销，闲置资金可投资其他产品，赚点零用钱。");
                CacuRestAstcatcdetls cacuRestAstcatcdetls4 = new CacuRestAstcatcdetls();
                cacuRestAstcatcdetls4.setAssetcatCode("OTH");
                cacuRestAstcatcdetls4.setAssetcatName("避险的钱");
                cacuRestAstcatcdetls4.setAssetcatPerct("6");
                cacuRestAstcatcdetls4.setCurrentEval("建议减持");
                cacuRestAstcatcdetls4.setCurrentTalks("您的保险类资产余额较多，真有保障意识，为您点赞。");
                cacuRestAstcatcdetls4.setProposalPerct("5");
                cacuRestAstcatcdetls4.setProposalTalks("在现有保险投资的基础上，尝试投资其他类产品，有可能会给你带来意外惊喜哦。");
                cacuRestAstcatcdetls.add(cacuRestAstcatcdetls1);
                cacuRestAstcatcdetls.add(cacuRestAstcatcdetls2);
                cacuRestAstcatcdetls.add(cacuRestAstcatcdetls3);
                cacuRestAstcatcdetls.add(cacuRestAstcatcdetls4);
                wallData.setCacuRestAstcatcdetls(cacuRestAstcatcdetls);
                List<Propools> propools = new ArrayList<>();

                Propools propools1 = new Propools();
                propools1.setBankProCode("BNQXJS");
                propools1.setProName("百年人寿乾享金生");
                propools1.setExpReturnRate("精彩生活");
                propools1.setExpReturnRateName("财富保值增值");
                propools1.setProTermValue("稳定领取_安枕无忧");
                propools1.setMininvAmt("3000元起购");
                propools1.setCompanyCode("INS00220000");
                propools1.setInsCombineYn("Y");
                propools1.setSourceFlag("3");
                propools1.setAssetcatCode("OTH");
                propools1.setAssetcatName("避险的钱");
                Propools propools2 = new Propools();
                propools2.setBankProCode("BNQXJS");
                propools2.setProName("T百年人寿乾享金生");
                propools2.setExpReturnRate("T精彩生活");
                propools2.setExpReturnRateName("T财富保值增值");
                propools2.setProTermValue("T永久");
                propools2.setMininvAmt("T3T0000元起购");
                propools2.setCompanyCode("TINS00220000");
                propools2.setInsCombineYn("Y");
                propools2.setSourceFlag("T3");
                propools2.setAssetcatCode("OTH");
                propools2.setAssetcatName("避险的钱");
                Propools propools3 = new Propools();
                propools3.setBankProCode("BNQXJS");
                propools3.setProName("R百年人寿乾享金生");
                propools3.setExpReturnRate("R精彩生活");
                propools3.setExpReturnRateName("R财富保值增值");
                propools3.setProTermValue("R永久");
                propools3.setMininvAmt("R30000元起购");
                propools3.setCompanyCode("INS00220000");
                propools3.setInsCombineYn("Y");
                propools3.setSourceFlag("R3");
                propools3.setAssetcatCode("DCM");
                propools3.setAssetcatName("保值的钱");

                Propools propools4 = new Propools();
                propools4.setBankProCode("JBNQXJS");
                propools4.setProName("J百年人寿乾享金生");
                propools4.setExpReturnRate("J精彩生活");
                propools4.setExpReturnRateName("J财富保值增值");
                propools4.setProTermValue("J永久");
                propools4.setMininvAmt("J30000元起购");
                propools4.setCompanyCode("JINS00220000");
                propools4.setInsCombineYn("Y");
                propools4.setSourceFlag("J3");
                propools4.setAssetcatCode("MM");
                propools4.setAssetcatName("要花的钱");

                propools.add(propools1);
                propools.add(propools2);
                propools.add(propools3);
                propools.add(propools4);
                wallData.setPropools(propools);
                wealthAna.setData(wallData);
                WallError wallError = new WallError();
                wealthAna.setError(wallError);
                return wealthAna;
            }

        } else {

            WealthAna wealthAna = new WealthAna();
            WallHeader wallHeader = new WallHeader();
            wallHeader.setMesFlag("000000");
            wallHeader.setbSerialNo("201806251359490006");
            wealthAna.setHeader(wallHeader);

            WallData wallData = new WallData();
            wallData.setCusCode("1608041820");
            wallData.setExamineDt("2019-10-30");
            wallData.setExamineScore("50");
            wallData.setMoreLastexamineScore("2");
            wallData.setExamineTime("15:58:13");

            List<CacuRestAstcatcdetls> cacuRestAstcatcdetls = new ArrayList<>();

            CacuRestAstcatcdetls cacuRestAstcatcdetls1 = new CacuRestAstcatcdetls();
            cacuRestAstcatcdetls1.setAssetcatCode("DCM");
            cacuRestAstcatcdetls1.setAssetcatName("保值的钱");
            cacuRestAstcatcdetls1.setAssetcatPerct("");
            cacuRestAstcatcdetls1.setCurrentEval("建议增持");
            cacuRestAstcatcdetls1.setCurrentTalks("您的固定收益类资产余额偏低。");
            cacuRestAstcatcdetls1.setProposalPerct("60");
            cacuRestAstcatcdetls1.setProposalTalks("后续可适当增加固定收益类产品的投资比例，提升资产安全性和投资收益。");
            CacuRestAstcatcdetls cacuRestAstcatcdetls2 = new CacuRestAstcatcdetls();
            cacuRestAstcatcdetls2.setAssetcatCode("EQ");
            cacuRestAstcatcdetls2.setAssetcatName("生钱的钱");
            cacuRestAstcatcdetls2.setAssetcatPerct("");
            cacuRestAstcatcdetls2.setCurrentEval("建议减持");
            cacuRestAstcatcdetls2.setCurrentTalks("您的股票权益类资产余额充裕，茄子君为您过人的投资眼光深深折服。");
            cacuRestAstcatcdetls2.setProposalPerct("20");
            cacuRestAstcatcdetls2.setProposalTalks("后续可参考资产配置建议，均衡配置其他类型产品。");
            CacuRestAstcatcdetls cacuRestAstcatcdetls3 = new CacuRestAstcatcdetls();
            cacuRestAstcatcdetls3.setAssetcatCode("MM");
            cacuRestAstcatcdetls3.setAssetcatName("要花的钱");
            cacuRestAstcatcdetls3.setAssetcatPerct("");
            cacuRestAstcatcdetls3.setCurrentEval("建议减持");
            cacuRestAstcatcdetls3.setCurrentTalks("您的现金管理类资产余额较多，日常花销费用充足。");
            cacuRestAstcatcdetls3.setProposalPerct("15");
            cacuRestAstcatcdetls3.setProposalTalks("保留家庭月支出4-6倍资金作为日常花销，闲置资金可投资其他产品，赚点零用钱。");
            CacuRestAstcatcdetls cacuRestAstcatcdetls4 = new CacuRestAstcatcdetls();
            cacuRestAstcatcdetls4.setAssetcatCode("OTH");
            cacuRestAstcatcdetls4.setAssetcatName("避险的钱");
            cacuRestAstcatcdetls4.setAssetcatPerct("");
            cacuRestAstcatcdetls4.setCurrentEval("建议减持");
            cacuRestAstcatcdetls4.setCurrentTalks("您的保险类资产余额较多，真有保障意识，为您点赞。");
            cacuRestAstcatcdetls4.setProposalPerct("5");
            cacuRestAstcatcdetls4.setProposalTalks("在现有保险投资的基础上，尝试投资其他类产品，有可能会给你带来意外惊喜哦。");
            cacuRestAstcatcdetls.add(cacuRestAstcatcdetls1);
            cacuRestAstcatcdetls.add(cacuRestAstcatcdetls2);
            cacuRestAstcatcdetls.add(cacuRestAstcatcdetls3);
            cacuRestAstcatcdetls.add(cacuRestAstcatcdetls4);
            wallData.setCacuRestAstcatcdetls(cacuRestAstcatcdetls);
            List<Propools> propools = new ArrayList<>();

            Propools propools1 = new Propools();
            propools1.setBankProCode("BNQXJS");
            propools1.setProName("百年人寿乾享金生");
            propools1.setExpReturnRate("精彩生活");
            propools1.setExpReturnRateName("财富保值增值");
            propools1.setProTermValue("稳定领取_安枕无忧");
            propools1.setMininvAmt("3000元起购");
            propools1.setCompanyCode("INS00220000");
            propools1.setInsCombineYn("Y");
            propools1.setSourceFlag("3");
            propools1.setAssetcatCode("OTH");
            propools1.setAssetcatName("避险的钱");
            Propools propools2 = new Propools();
            propools2.setBankProCode("BNQXJS");
            propools2.setProName("T百年人寿乾享金生");
            propools2.setExpReturnRate("T精彩生活");
            propools2.setExpReturnRateName("T财富保值增值");
            propools2.setProTermValue("T永久");
            propools2.setMininvAmt("T30000元起购");
            propools2.setCompanyCode("TINS00220000");
            propools2.setInsCombineYn("Y");
            propools2.setSourceFlag("3");
            propools2.setAssetcatCode("OTH");
            propools2.setAssetcatName("避险的钱");
            Propools propools3 = new Propools();
            propools3.setBankProCode("YBNQXJS");
            propools3.setProName("Y百年人寿乾享金生");
            propools3.setExpReturnRate("Y精彩生活");
            propools3.setExpReturnRateName("Y财富保值增值");
            propools3.setProTermValue("Y永久");
            propools3.setMininvAmt("Y30000元起购");
            propools3.setCompanyCode("YINS00220000");
            propools3.setInsCombineYn("Y");
            propools3.setSourceFlag("3");
            propools3.setAssetcatCode("DCM");
            propools3.setAssetcatName("保值的钱");

            Propools propools4 = new Propools();
            propools4.setBankProCode("BNQXJS");
            propools4.setProName("J百年人寿乾享金生");
            propools4.setExpReturnRate("J精彩生活");
            propools4.setExpReturnRateName("J财富保值增值");
            propools4.setProTermValue("J永久");
            propools4.setMininvAmt("J30000元起购");
            propools4.setCompanyCode("INS00220000");
            propools4.setInsCombineYn("Y");
            propools4.setSourceFlag("3");
            propools4.setAssetcatCode("MM");
            propools4.setAssetcatName("要花的钱");

            if (rankCode.equals("1")) {
                wallData.setExamineScore("50");
            }
            if (rankCode.equals("2")) {
                wallData.setExamineScore("60");
                propools.add(propools2);
            }
            if (rankCode.equals("3")) {
                wallData.setExamineScore("70");
            }
            propools.add(propools3);
            propools.add(propools1);
            if (rankCode.equals("4")) {
                wallData.setExamineScore("80");
                propools.add(propools4);
            }
            wallData.setPropools(propools);
            wealthAna.setData(wallData);
            WallError wallError = new WallError();
            wealthAna.setError(wallError);
            return wealthAna;

        }
    }

}

