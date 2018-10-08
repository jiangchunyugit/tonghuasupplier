package cn.thinkfree.database.vo;

/**
 * @Auther: jiang
 * @Date: 2018/9/30 14:29
 * @Description:
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目详情
 */
@ApiModel("项目施工顺序")
public class ProjectBigSchedulingVO {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("公司编号")
    private String companyId;
    @ApiModelProperty("序号")
    private Integer sort;
    @ApiModelProperty("工作模块名称")
    private String name;
    @ApiModelProperty("自定义大排期名字")
    private String rename;
    @ApiModelProperty("状态(1,正常  2,失效)")
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRename() {
        return rename;
    }

    public void setRename(String rename) {
        this.rename = rename;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
