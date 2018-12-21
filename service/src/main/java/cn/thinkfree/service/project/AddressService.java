package cn.thinkfree.service.project;

import cn.thinkfree.database.model.Project;

/**
 * 地址服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/12/21 12:49
 */
public interface AddressService {
    /**
     * 获取项目地址
     * @param project 项目信息
     * @return 项目地址
     */
    String getAddress(Project project);
}
