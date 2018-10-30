package cn.thinkfree.service.settle.ratio;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.utils.SpringBeanUtil;
import cn.thinkfree.database.mapper.SettlementRatioInfoMapper;
import cn.thinkfree.database.model.SettlementRatioInfo;
import cn.thinkfree.database.model.SettlementRatioInfoExample;
import cn.thinkfree.database.vo.settle.SettlementRatioSEO;

@Service
public class SettlementRatioServiceImpl extends AbsLogPrinter implements SettlementRatioService {
	
	@Autowired
	SettlementRatioInfoMapper  settlementRatioInfoMapper;
	

	@Override
	public PageInfo<SettlementRatioInfo> pageSettlementRatioBySEO(SettlementRatioSEO ratio) {
		PageHelper.startPage(ratio.getPage(), ratio.getRows());
		SettlementRatioInfoExample example = new SettlementRatioInfoExample();
		example.createCriteria().andRatioNumberLike("%"+ratio.getRatioNumber()+"%")
		.andFeeNameLike("%"+ratio.getRatioName()+"%")
		.andCreateUserLike("%"+ratio.getCreateUser()+"%")
		.andEffectStartTimeGreaterThanOrEqualTo(ratio.getStartTime())
		.andEffectEndTimeLessThanOrEqualTo(ratio.getEndTime())
		.andStatusEqualTo(ratio.getAuditStatus());
		if(!StringUtils.isEmpty(ratio.getRatioStatus())){
			if(ratio.getRatioStatus().equals("0")){//0生效 1失效 2作废 3未生效
				example.createCriteria()
				.andEffectStartTimeGreaterThanOrEqualTo(new Date())
				.andEffectEndTimeLessThanOrEqualTo(new Date());
			}else if(ratio.getRatioStatus().equals("1")){
				 example.createCriteria()
				.andEffectStartTimeLessThan(new Date())
				.andEffectEndTimeGreaterThan(new Date());
			}else if(ratio.getRatioStatus().equals("2")){
				 example.createCriteria().andStatusEqualTo("4");
			}else if(ratio.getRatioStatus().equals("3")){
				 example.createCriteria().andStatusEqualTo("1");
			}
		}
		List<SettlementRatioInfo> list = settlementRatioInfoMapper.selectByExample(example);
		printInfoMes("查询 结算比例数量 {}", list.size());
		return new PageInfo<>(list);
	}

	@Override
	public boolean insertOrupdateSettlementRatio(SettlementRatioInfo settlementRatioSEO) {
		int flag = 0;
		if (settlementRatioSEO != null) {
			if (StringUtils.isEmpty(settlementRatioSEO.getRatioNumber())) {
				settlementRatioSEO.setCreateTime(new Date());
				settlementRatioSEO.setUpdateTime(new Date());
				settlementRatioSEO.setCreateUser("从登陆中获取小b");
				settlementRatioSEO.setStatus("1");// 新增未待审核
				settlementRatioSEO.setRatioNumber("111111");
				try {
					flag = settlementRatioInfoMapper.insertSelective(settlementRatioSEO);
				} catch (Exception e) {
					printErrorMes("添加结算比例{}" + e.getMessage());
				}
			} else {
				settlementRatioSEO.setCreateTime(new Date());
				settlementRatioSEO.setUpdateTime(new Date());
				settlementRatioSEO.setCreateUser("从登陆中获取小b");
				try {
					SettlementRatioInfoExample example = new SettlementRatioInfoExample();
					example.createCriteria().andRatioNumberNotEqualTo(settlementRatioSEO.getRatioNumber());
					flag = settlementRatioInfoMapper.updateByExampleSelective(settlementRatioSEO, example);
				} catch (Exception e) {
					printErrorMes("添加结算比例{}" + e.getMessage());
				}
			}

			if (flag > 0) {
				return true;
			}
		}

		return false;
	}
	
	

	@Override
	public boolean copySettlementRatio(String ratioNumber) {
		if(!StringUtils.isEmpty(ratioNumber)){
			SettlementRatioInfoExample example = new SettlementRatioInfoExample();
			example.createCriteria().andRatioNumberNotEqualTo(ratioNumber);
			List<SettlementRatioInfo>  list = settlementRatioInfoMapper.selectByExample(example);
			SettlementRatioInfo ralio = list.get(0);
			ralio.setRatioNumber(ratioNumber);
			ralio.setCreateTime(new Date());
			ralio.setUpdateTime(new Date());
			ralio.setCreateUser("从登陆中获取小b");
			int  falg = settlementRatioInfoMapper.insertSelective(ralio);
			if(falg > 0 ){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	
	@Override
	public SettlementRatioInfo getSettlementRatio(String ratioNumber) {
		SettlementRatioInfoExample example = new SettlementRatioInfoExample();
		example.createCriteria().andRatioNumberNotEqualTo(ratioNumber);
		List<SettlementRatioInfo>  list = settlementRatioInfoMapper.selectByExample(example);
		SettlementRatioInfo ratio = (list!=null && list.size() > 0)?null:list.get(0);
		if(ratio != null && StringUtils.isEmpty(ratio.getFeeDicId())){
			Map<String, String> paream = getCostNames();
			ratio.setFeeDicId(paream.get(ratio.getFeeDicId()));//翻译
		}
		return ratio;
	}
	
	
	
	
	@Override
	public boolean cancellatSettlementRatio(String ratioNumber) {
		SettlementRatioInfoExample example = new SettlementRatioInfoExample();
		example.createCriteria().andRatioNumberNotEqualTo(ratioNumber);
		SettlementRatioInfo record = new SettlementRatioInfo();
		record.setStatus("4");//作废
		settlementRatioInfoMapper.updateByExampleSelective(record, example);
		return false;
	}

	@Override
	public Map<String, String> getCostNames() {
		Map<String, String> map = new HashMap<>();
		map.put("01", "设计费");
		map.put("02", "施工费");
		map.put("03", "测试费");
		return map;
	}

	
}
