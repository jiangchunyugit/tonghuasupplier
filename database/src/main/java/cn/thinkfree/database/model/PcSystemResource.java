package cn.thinkfree.database.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "权限资源表")
public class PcSystemResource {
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("模块")
    private String module;

    @ApiModelProperty("资源名")
    private String name;

    @ApiModelProperty("资源编码")
    private String code;

    @ApiModelProperty("资源路径")
    private String url;

    @ApiModelProperty("资源类型")
    private String type;

    @ApiModelProperty("排序号")
    private Integer sortNum;

    @ApiModelProperty("父节点")
    private Integer pid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module == null ? null : module.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}