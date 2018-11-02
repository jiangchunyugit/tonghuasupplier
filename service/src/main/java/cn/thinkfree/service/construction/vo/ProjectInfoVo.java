package cn.thinkfree.service.construction.vo;

import cn.thinkfree.database.model.PreProjectCompanySet;
import cn.thinkfree.database.model.PreProjectGuide;
import cn.thinkfree.database.model.PreProjectInfo;
import cn.thinkfree.database.model.PreProjectUserRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 项目详情
 */
@Getter
@Setter
@ApiModel("项目交互详情")
public class ProjectInfoVo  {

    @ApiModelProperty("公司信息")
    private PreProjectCompanySet preProjectCompanySet;
    @ApiModelProperty("项目信息")
    private PreProjectInfo preProjectInfo;

    @ApiModelProperty("项目员工")
    private PreProjectUserRole preProjectUserRole;

    /**
     * 管家姓名
     */
    @ApiModelProperty("管家姓名")
    private String stewardName;

    /**
     * 项目经理名称
     */
    @ApiModelProperty("项目经理姓名")
    private String projectManagerName;

    /**
     * 所属公司
     * @return
     */
    @ApiModelProperty("所属公司名称")
    public String companyName;

    /**
     * 户型名称
     */
    @ApiModelProperty("户型")
    private String houseTypeName;

}
