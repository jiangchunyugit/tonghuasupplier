package cn.thinkfree.database.vo;

import lombok.Data;

import java.util.List;

/**
 * 导出pdf
 *
 * @author song
 * @version 1.0
 * @date 2018/11/13 11:21
 */
@Data
public class AfExportPdfVO {

    private String instanceNo;
    private String createTime;
    private String remark;
    private String createUsername;
    private String createRoleName;
    private List<AfUserVO> approvalUsers;
    /**
     * 项目编号
     */
    private String projectNo;
    private String configNo;
    private String configName;
    private String customerName;
    private String phoneNo;
    private String address;

}
