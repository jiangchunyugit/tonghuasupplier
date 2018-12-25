package cn.thinkfree.database.vo;

import cn.thinkfree.core.model.BaseModel;
import cn.thinkfree.core.utils.SpringBeanUtil;
import cn.thinkfree.database.model.Menu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 首页使用菜单树形
 */
@ApiModel("首页菜单")
public class IndexMenuVO extends Menu {

    @ApiModelProperty("子集")
    private List<IndexMenuVO> child;

    public IndexMenuVO(Menu menu, List<Menu> menus) {
        SpringBeanUtil.copy(menu,this);
        setChild(menus.stream()
                .filter(m -> menu.getId().equals(m.getPid()))
                .map(m->new IndexMenuVO(m,menus))
                .collect(Collectors.toList()));
    }

    public List<IndexMenuVO> getChild() {
        return child;
    }

    public void setChild(List<IndexMenuVO> child) {
        this.child = child;
    }

    public IndexMenuVO(){}
    public IndexMenuVO(Menu menu){
        SpringBeanUtil.copy(menu,this);
    }


}
