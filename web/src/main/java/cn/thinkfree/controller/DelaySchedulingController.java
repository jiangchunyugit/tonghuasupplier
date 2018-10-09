package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.service.scheduling.DelaySchedulingService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gejiaming
 */
@Api(tags = "延期控相关")
@RestController
@RequestMapping(value = "delayScheduling")
public class DelaySchedulingController extends AbsBaseController {
    @Autowired
    private DelaySchedulingService delaySchedulingService;


}
