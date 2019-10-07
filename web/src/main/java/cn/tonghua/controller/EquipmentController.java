package cn.tonghua.controller;

import cn.tonghua.core.annotation.MyRespBody;
import cn.tonghua.core.annotation.MySysLog;
import cn.tonghua.core.base.AbsBaseController;
import cn.tonghua.core.bundle.MyRespBundle;
import cn.tonghua.core.constants.SysLogAction;
import cn.tonghua.core.constants.SysLogModule;
import cn.tonghua.service.endMachine.EndMachineService;
import cn.tonghua.service.program.ProgramService;
import cn.tonghua.service.projector.ProjectorService;
import cn.tonghua.service.startmachine.StartMachineService;
import cn.tonghua.service.volume.VolumeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/equipment")
@Api(value = "前端使用---操作---蒋春雨",description = "前端使用---操作---蒋春雨")
public class EquipmentController extends AbsBaseController {

    @Autowired
    StartMachineService startMachineService;

    @Autowired
    EndMachineService endMachineService;

    @Autowired
    ProjectorService projectorService;

    @Autowired
    ProgramService programService;

    @Autowired
    VolumeService volumeService;

    /**
     * 查询静态资源信息
     */
    @RequestMapping(value = "/startmachine", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="单个机器启动/关闭")
    @MySysLog(action = SysLogAction.SAVE,module = SysLogModule.PC_PROJECT,desc = "启动")
    public MyRespBundle<String> startMachine(@ApiParam("机器名称")@RequestParam(value = "serviceNm") String serviceNm, @ApiParam("操作类型，1开启，0关闭")@RequestParam(value = "type") int type){

        if (type == 0){
            return endMachineService.endMachine(serviceNm);
        }
        return startMachineService.startMachine(serviceNm);
    }

    /**
     * 查询静态资源信息
     */
    @RequestMapping(value = "/startall", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="全部开启/关闭")
    @MySysLog(action = SysLogAction.SAVE,module = SysLogModule.PC_PROJECT,desc = "全部开启")
    public MyRespBundle<String> startall(@ApiParam("操作类型，1开启，0关闭")@RequestParam(value = "type") int type){

        if (type == 0){
            return endMachineService.endMachine();
        }
        return startMachineService.startMachine();
    }

    /**
     * 程序
     */
    @RequestMapping(value = "/program", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="程序")
    @MySysLog(action = SysLogAction.SAVE,module = SysLogModule.PC_PROJECT,desc = "程序")
    public MyRespBundle<String> fileResourceCaseList(@ApiParam("机器名称")@RequestParam(value = "serviceNm") String serviceNm, @ApiParam("操作类型，1开启，0关闭")@RequestParam(value = "type") int type){

        return programService.programOperation(serviceNm,type);
    }

    /**
     * 投影仪
     */
    @RequestMapping(value = "/projector", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="投影仪")
    @MySysLog(action = SysLogAction.SAVE,module = SysLogModule.PC_PROJECT,desc = "投影仪")
    public MyRespBundle<String> fileResourceCaseInfo(@ApiParam("机器名称")@RequestParam(value = "serviceNm") String serviceNm, @ApiParam("操作类型，1开启，0关闭")@RequestParam(value = "type") int type){

        return projectorService.projectorOperation(serviceNm,type)? sendSuccessMessage("操作成功"):sendFailMessage("操作失败");
    }

    /**
     * 声音
     */
    @RequestMapping(value = "/volume", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="声音")
    @MySysLog(action = SysLogAction.SAVE,module = SysLogModule.PC_PROJECT,desc = "声音")
    public MyRespBundle<String> volume(@ApiParam("操作类型，0静音，1增加，2减少")@RequestParam(value = "type") int type){

        return volumeService.volume(type)? sendSuccessMessage("操作成功"):sendFailMessage("操作失败");
    }
}
