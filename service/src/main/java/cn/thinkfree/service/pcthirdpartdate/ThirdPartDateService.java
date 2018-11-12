package cn.thinkfree.service.pcthirdpartdate;

import cn.thinkfree.database.vo.MarginContractVO;

/**
 * @author jiangchunyu(后台)
 * @date 20181109
 * @Description 第三方数据接口
 */
public interface ThirdPartDateService {

    MarginContractVO getMarginContract(String contractCode);
}
