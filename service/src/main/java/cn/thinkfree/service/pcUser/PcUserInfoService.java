package cn.thinkfree.service.pcUser;

import cn.thinkfree.database.vo.account.AccountSEO;
import cn.thinkfree.database.vo.account.AccountVO;
import com.github.pagehelper.PageInfo;

public interface PcUserInfoService {

    /**
     * 修改密码
     */
    String updatePassWord(String oldPassWord, String newPassWord);


    /**
     * 新增用户账号
     * @param accountVO
     * @return
     */
    AccountVO saveUserAccount(AccountVO accountVO);

    /**
     * 查询账号详情
     * @param id
     * @return
     */
    AccountVO findAccountVOByID(String id);

    /**
     * 删除账号信息
     * @param id
     * @return
     */
    String delAccountByID(String id);

    /**
     * 更新账号信息
     * @param id
     * @param accountVO
     * @return
     */
    String updateAccountVO(String id, AccountVO accountVO);

    /**
     * 重置密码
     * @param id
     * @return
     */
    String updateForResetPassWord(String id);

    /**
     * 账号启停
     * @param id
     * @param state
     * @return
     */
    String updateAccountState(String id, Integer state);

    /**
     * 更新密码 - 初次登录重置
     * @param id
     * @param passWord
     * @return
     */
    String updatePassWordForInit(String id, String passWord);

    /**
     * 分页查询账号
     * @param accountSEO
     * @return
     */
    PageInfo pageAccountVO(AccountSEO accountSEO);

    /**
     * 判断输入的账号是否已经注册过
     * @param name
     * @return
     */
    boolean isEnable(String name);
}
