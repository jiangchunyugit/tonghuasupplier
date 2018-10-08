package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.vo.ProjectBigSchedulingVO;
import cn.thinkfree.database.vo.ProjectSEO;
import cn.thinkfree.database.vo.ProjectVO;
import cn.thinkfree.service.scheduling.SchedulingService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author gejiaming
 */
@Api(tags = "排期相关")
@RestController
@RequestMapping(value = "scheduling")
public class SchedulingController extends AbsBaseController {

    @Autowired
    private SchedulingService schedulingService;

    /**
     * 项目列表
     *
     * @param companyId
     * @return
     */
    @ApiOperation(value = "施工配置列表", notes = "")
    @PostMapping("/list")
    @MyRespBody
    public MyRespBundle<ProjectBigSchedulingVO> list(@RequestParam(name = "companyId") @ApiParam(value = "公司编号", name = "companyId") String companyId) {
        if ("".equals(companyId) || null == companyId) {
            sendJsonData(ResultMessage.ERROR, "公司编号为空");
        }
        ProjectBigSchedulingVO projectBigSchedulingVO = schedulingService.selectProjectBigSchedulingByCompanyId(companyId);
        return sendJsonData(ResultMessage.SUCCESS, projectBigSchedulingVO);
    }

    @RequestMapping(value = "saveProjectScheduling",method = RequestMethod.POST)
    @ApiOperation(value = "添加公司施工节点")
    public MyRespBundle saveProjectScheduling(@ApiParam(value = "施工节点信息") ProjectBigSchedulingVO projectBigSchedulingVO){
        String result = schedulingService.saveProjectScheduling(projectBigSchedulingVO);
        return sendSuccessMessage(result);
    }

    @RequestMapping(value = "deleteProjectScheduling",method = RequestMethod.POST)
    @ApiOperation(value = "删除公司施工节点")
    public MyRespBundle deleteProjectScheduling(@ApiParam(value = "施工节点信息") ProjectBigSchedulingVO projectBigSchedulingVO){
        String result = schedulingService.deleteProjectScheduling(projectBigSchedulingVO);
        return sendSuccessMessage(result);
    }

}
