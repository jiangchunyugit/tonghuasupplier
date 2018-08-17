package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.Province;
import cn.thinkfree.service.dictionary.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("dictionary")
public class DictionaryController extends AbsBaseController {


    @Autowired
    DictionaryService dictionaryService;


    /**
     * 获取省份信息
     * @return
     */
    @GetMapping("/province")
    @MyRespBody
    public MyRespBundle<Province>  province(){

        List<Province> provinceList = dictionaryService.findAllProvince();

        return sendJsonData(ResultMessage.SUCCESS,provinceList);
    }



}
