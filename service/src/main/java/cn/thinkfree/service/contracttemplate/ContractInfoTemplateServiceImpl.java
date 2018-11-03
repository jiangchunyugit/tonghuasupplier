package cn.thinkfree.service.contracttemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.thinkfree.core.utils.WebFileUtil;
import cn.thinkfree.database.mapper.ContractTemplateDictMapper;
import cn.thinkfree.database.mapper.ContractTemplateMapper;
import cn.thinkfree.database.model.ContractTemplate;
import cn.thinkfree.database.model.ContractTemplateDict;
import cn.thinkfree.database.model.ContractTemplateDictExample;
import cn.thinkfree.database.model.ContractTemplateExample;

@Service
public class ContractInfoTemplateServiceImpl implements ContractTemplateService {

	
	@Autowired
    ContractTemplateMapper ContractTemplateMapper;
	
	@Autowired
	ContractTemplateDictMapper ContractTemplateDictMapper;
	
	
	@Override
	public List<ContractTemplate> ContractTemplateList(String type) {
		ContractTemplateExample example = new ContractTemplateExample();
		example.createCriteria().andContractTpTypeEqualTo(type);
		return ContractTemplateMapper.selectByExample(example);
	}



	@Override
	public boolean updateContractTemplateInfo(String id,String type,String contractTpName, String contractTpRemark) {
		
		ContractTemplate con = new ContractTemplate();
		con.setContractTpName(contractTpName);
		con.setContractStatus("0");
		con.setContractTpRemark(contractTpRemark);
		con.setContractTpType(type);
		con.setUpdateTime(new Date());
		con.setUpdateTime(new Date());
		
		if(StringUtils.isEmpty(id)){
			int falg = ContractTemplateMapper.insertSelective(con);
			if(falg > 0 ){
				return true;
			}
			return false;
		}else{
			con.setId(Integer.valueOf(id));
			int falg = ContractTemplateMapper.updateByExample(con, new ContractTemplateExample());
			if(falg > 0 ){
				return true;
			}
			return true;
		}
		
	}



	@Override
	public boolean updateContractTemplateStatus(String type, String stauts) {
		ContractTemplate record = new ContractTemplate();
		record.setContractTpType(type);
		record.setContractStatus(stauts);
		ContractTemplateExample example = new ContractTemplateExample();
		example.createCriteria().andContractTpTypeEqualTo(type);
		int flag = ContractTemplateMapper.updateByExampleSelective(record, example);
		if(flag > 0 ){
			
			return true;
		}
		return false;
	}



	@Override
	public String getTemplatePdfUrl(String type) {
		return ContractTemplateMapper.queryListByType(type)==null?"":
			ContractTemplateMapper.queryListByType(type).get(0).getPdfUrl();
	}


	
	@Override
	public boolean uploadFile(String type, MultipartFile file) {
		//生成pdf 返回url 
		String url = WebFileUtil.fileCopy("static/contractTemplate/", file);//上传合同模板
		ContractTemplate record = new ContractTemplate();
		record.setContractTpType(type);
		record.setUploadUrl(url);
		record.setPdfUrl("static/contract/template/pdf"+type+".pdf");
		record.setUpdateTime(new Date());
		ContractTemplateExample example = new ContractTemplateExample();
		example.createCriteria().andContractTpTypeEqualTo(type);
		int flag = ContractTemplateMapper.updateByExampleSelective(record, example);
		if(flag > 0 ){
			
			return true;
		}
		return false;
	}



	@Override
	public Map<String, String> queryContractDic(String type) {
		ContractTemplateDictExample example = new ContractTemplateDictExample();
		example.createCriteria().andTypeEqualTo(type);
		List<ContractTemplateDict> list = ContractTemplateDictMapper.selectByExample(example);
		Map<String,String> resMap = new HashMap<>();
		for (int i = 0; i < list.size(); i++) {
			resMap.put(list.get(i).getCode(), list.get(i).getName());
		}
		return resMap;
	}
}
