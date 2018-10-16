package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.DesignOrder;
import cn.thinkfree.database.model.LogInfo;
import cn.thinkfree.database.model.PreProjectGuide;
import cn.thinkfree.database.model.PreProjectMaterial;
import cn.thinkfree.database.vo.PreProjectMaterialVO;
import cn.thinkfree.database.vo.ProjectOrderVO;
import cn.thinkfree.service.constants.Scheduling;
import cn.thinkfree.service.scheduling.DelaySchedulingService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.Document;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gejiaming
 */
@Api(tags = "延期控相关")
@RestController
@RequestMapping(value = "delayScheduling")
public class DelaySchedulingController extends AbsBaseController {
    @Autowired
    private DelaySchedulingService delaySchedulingService;


    /**
     * @return
     * @Author jiang
     * @Description
     * @Date
     * @Param
     **/
    @RequestMapping(value = "projectOrderList", method = RequestMethod.POST)
    @ApiOperation(value = "项目派单列表", notes = "")
    public MyRespBundle<DesignOrder> projectOrderList(@RequestBody ProjectOrderVO projectOrderVO,
                                                      @RequestParam(defaultValue = "1") Integer pageNum,
                                                      @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        if (null == projectOrderVO.getProjectNo() || "".equals(projectOrderVO.getProjectNo())) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        Map<String, Object> params = new HashMap<>();
        List<ProjectOrderVO> preProjectGuideList = new ArrayList<>();
        preProjectGuideList = delaySchedulingService.queryProjectOrderByPage(projectOrderVO, (pageNum - 1) * pageSize, pageSize);
        //这里查询的是所有的数据
        params.put("list", preProjectGuideList);
        //这里查询的是总页数
        params.put("totalPage", delaySchedulingService.queryProjectOrderCount(projectOrderVO));
        params.put("pageSize", pageSize);
        params.put("pageNum", pageNum);
        return sendJsonData(ResultMessage.SUCCESS, params);
    }


}
