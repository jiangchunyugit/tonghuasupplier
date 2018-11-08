package cn.thinkfree.service.pcUser;

import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.model.UserRegister;
import cn.thinkfree.database.vo.MyPageHelper;
import cn.thinkfree.database.vo.PcUserInfoVo;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.database.vo.account.AccountSEO;
import cn.thinkfree.database.vo.account.AccountVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface PcUserInfoService {
    List<PcUserInfo> selectByParam(UserVO userVO);
    PageInfo<PcUserInfoVo> findByParam(MyPageHelper myPageHelper);
    boolean delPcUserInfo(String userId);
    boolean saveUserInfo(PcUserInfoVo pcUserInfoVo);
    boolean updateUserInfo(PcUserInfoVo pcUserInfoVo);
    PcUserInfoVo findByUserId(String userId);
    /**
     * 修改密码
     */
    String updatePassWord(String oldPassWord, String newPassWord);

    /**
     * 启用账户
     */
    String canEnabled(String id, Integer enabled);

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
}
