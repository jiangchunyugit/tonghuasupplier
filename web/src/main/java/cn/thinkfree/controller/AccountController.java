package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.vo.account.PermissionSEO;
import cn.thinkfree.database.vo.account.PermissionVO;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账号相关
 */
@RestController
@RequestMapping("/account")
public class AccountController extends AbsBaseController {



    /**
     * 创建权限
     * @param name 权限名称
     * @param desc 权限描述
     * @return
     */
    @PostMapping("/permission")
    @MyRespBody
    public MyRespBundle<PermissionVO> create(String name,String desc){



        return sendJsonData(ResultMessage.SUCCESS,null);
    }

    /**
     * 权限列表
     * @param permissionSEO
     * @return
     */
    @GetMapping("/permission")
    @MyRespBody
    public MyRespBundle<PageInfo<PermissionVO>> permissions(PermissionSEO permissionSEO){

        return null;
    }

    /**
     * 权限详情
     */

    /**
     * 权限资源
     */

    /**
     * 编辑权限
     */

    /**
     * 新建角色
     */


    /**
     * 资源列表
     */





}
