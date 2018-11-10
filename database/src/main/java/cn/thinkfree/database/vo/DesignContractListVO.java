package cn.thinkfree.database.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Auther: jiang
 * @Date: 2018/11/9 18:01
 * @Description:
 */
@Getter
@Setter
public class DesignContractListVO {
    private List<DesignContractVO> designContractVOList;
    private Integer pageNum;
    private Integer pageSize;
    private Integer pageCount;


}
