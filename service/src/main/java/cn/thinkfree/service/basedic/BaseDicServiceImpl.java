package cn.thinkfree.service.basedic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.database.mapper.BaseDicMapper;
import cn.thinkfree.database.mapper.ConstructionBaseDicMapper;
import cn.thinkfree.database.model.BaseDic;
import cn.thinkfree.database.model.BaseDicExample;
import cn.thinkfree.database.model.ConstructionBaseDic;
import cn.thinkfree.database.model.ConstructionBaseDicExample;
import cn.thinkfree.database.vo.MybaseDic;

@Service
public class BaseDicServiceImpl extends AbsLogPrinter implements BaseDicService {

	@Autowired
	BaseDicMapper baseDicMapper;
	
	@Autowired
	ConstructionBaseDicMapper ConstructionBaseDicMapper ;

	
	
	@Override
	public List<MybaseDic> getDicListByType(String type) {

		List<MybaseDic> resList = new ArrayList<>();
		BaseDicExample example = new BaseDicExample();
		example.createCriteria().andDicCategoryEqualTo(type);
		example.setOrderByClause("create_time desc");//创建时间排序
		List<BaseDic> list = baseDicMapper.selectByExample(example);
		printInfoMes("查询list集合 baseList size = "+list.size());
		list.forEach((BaseDic doc) -> {
			MybaseDic c = new MybaseDic();
			c.setDicCode(doc.getId()+"");
			c.setDicName(doc.getDicValue());
			c.setDicSort(doc.getId()+"");
			c.setRemarks(doc.getRemarks());
			resList.add(c);
		});
		printInfoMes("返回list集合 resList size{} = "+list.size());
		return resList;
	}

	@Override
	public boolean insertDic(String type, String dicValue,String remarks) {
		BaseDic record = new BaseDic();
		record.setDicCategory(type);
		record.setDicValue(dicValue);
		record.setRemarks(remarks);
		record.setCreateTime(new Date());
		record.setUpdateTime(new Date());
		int falg = baseDicMapper.insertSelective(record);
		printInfoMes("新增字典 type={},dicValue={},remarks={}",type,dicValue,remarks);
		if(falg > 0 ){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public List<ConstructionBaseDic> getConstructionDicList() {
		//把数据内存化
		Map<String,String> map = new HashMap<>();
		List<BaseDic> listMap = baseDicMapper.selectByExample(null);
		listMap.forEach((BaseDic doc) -> {
			map.put(doc.getId()+"",doc.getDicValue());
		});
		List<ConstructionBaseDic> list = ConstructionBaseDicMapper.selectByExample(null);
		list.forEach((ConstructionBaseDic doc) -> {
			doc.setConstructionCode(map.get(doc.getConstructionCode()));//翻译
			doc.setProjectCode(map.get(doc.getProjectCode()));//翻译
		});
		return list;
	}

	@Override
	public boolean insertConstructionBaseDic(ConstructionBaseDic record) {
		
		int  falg = ConstructionBaseDicMapper.insertSelective(record);
		if(falg > 0){
			return true;
		}
		return false;
	}

	@Override
	public boolean updateDicName(String id, String dicValue,String remarks) {
		BaseDic record = new BaseDic();
		record.setId(Integer.valueOf(id));
		record.setDicValue(dicValue);
		record.setRemarks(remarks);
		record.setUpdateTime(new Date());
		
		int falg = baseDicMapper.updateByExampleSelective(record, new BaseDicExample());
		printInfoMes("新增字典 id={},dicValue={},remarks={}",id,dicValue,remarks);
		if(falg > 0 ){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean updateConstructionBaseDicName(ConstructionBaseDic record) {
		int  falg = ConstructionBaseDicMapper.updateByExample(record, new ConstructionBaseDicExample());
		if(falg > 0){
			return true;
		}
		return false;
	}
	
	
   
}
