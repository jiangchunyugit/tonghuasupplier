package cn.thinkfree.service.basedic;

import java.util.List;

import cn.thinkfree.database.model.ConstructionBaseDic;
import cn.thinkfree.database.vo.MybaseDic;

public interface BaseDicService {

	/**
	 * 0户型结构字典 1 房屋类型字典 2房屋属性 3计费项目类型（设计）4计费项目设置（施工）5施工阶段设置 6项目阶段设置 
	 * @param type
	 * @return
	 */
    List<MybaseDic> getDicListByType(String type);
    
    /**
     * 插入 base 字典
     * @param type
     * @param dicValue
     * @return
     */
    boolean insertDic(String type,String dicValue,String remarks);
    
    
    /**
     * 根据id 修改 base 字典
     * 
     */
    boolean updateDicName(String id,String dicValue,String remarks);
    /**
     * 
     * 查询 施工项目字典列表
     * @return
     */
    List<ConstructionBaseDic> getConstructionDicList();
    
    /**
     * 
     * @param name
     * @param constructionCode
     * @param projectCode
     * @param remarks
     * @param material
     * @return
     */
    boolean insertConstructionBaseDic(ConstructionBaseDic vo);
    
    
    /**
     * 根据id 修改 base 字典
     * 
     */
    boolean updateConstructionBaseDicName(ConstructionBaseDic vo);
}
