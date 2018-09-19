package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.SystemMessage;
import cn.thinkfree.database.vo.ProjectQuotationVO;
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.sysMsg.SystemMessageService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 不需要token的部分
 */
@RestController
@RequestMapping("/open")
public class OpenApiController extends AbsBaseController {

    @Autowired
    ProjectService projectService;

    @Autowired
    SystemMessageService systemMessageService;

    /**
     * 报价单
     * @param projectNo
     * @return
     */
    @GetMapping("/quotation")
    @MyRespBody
    public MyRespBundle<ProjectQuotationVO> quotation(@RequestParam String projectNo){
        ProjectQuotationVO projectQuotationVO = projectService.selectProjectQuotationVoByProjectNo(projectNo);
        return sendJsonData(ResultMessage.SUCCESS,projectQuotationVO);
    }

    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    @MyRespBody
    public MyRespBundle<PageInfo<SystemMessage>> findById(@RequestParam(value = "id")Integer id){
        SystemMessage sysMsg = systemMessageService.selectByPrimaryKey(id);
        if(null == sysMsg){
            return sendJsonData(ResultMessage.FAIL, "失败");
        }
        return sendJsonData(ResultMessage.SUCCESS, sysMsg);
    }

}
