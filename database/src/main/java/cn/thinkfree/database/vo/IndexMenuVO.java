package cn.thinkfree.database.vo;

import cn.thinkfree.core.model.BaseModel;
import cn.thinkfree.core.utils.SpringBeanUtil;
import cn.thinkfree.database.model.Menu;

import java.util.List;

/**
 * 首页使用菜单树形
 */
public class IndexMenuVO extends Menu {


    private List<IndexMenuVO> child;

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
