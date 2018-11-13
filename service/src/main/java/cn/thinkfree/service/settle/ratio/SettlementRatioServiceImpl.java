package cn.thinkfree.service.settle.ratio;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.fabric.xmlrpc.base.Data;

import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.core.utils.DateUtils;
import cn.thinkfree.database.mapper.PcAuditInfoMapper;
import cn.thinkfree.database.mapper.SettlementRatioInfoMapper;
import cn.thinkfree.database.model.PcAuditInfo;
import cn.thinkfree.database.model.PcAuditInfoExample;
import cn.thinkfree.database.model.SettlementRatioInfo;
import cn.thinkfree.database.model.SettlementRatioInfoExample;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.database.vo.settle.SettlementRatioAudit;
import cn.thinkfree.database.vo.settle.SettlementRatioSEO;
import cn.thinkfree.service.constants.SettlementStatus;
import cn.thinkfree.service.utils.ExcelData;
import cn.thinkfree.service.utils.ExcelUtils;

@Service
public  class SettlementRatioServiceImpl extends AbsLogPrinter implements SettlementRatioService {
	
	@Autowired
	SettlementRatioInfoMapper  settlementRatioInfoMapper;

	@Autowired
	PcAuditInfoMapper pcAuditInfoMapper;

	@Override
	public PageInfo<SettlementRatioInfo> pageSettlementRatioBySEO(SettlementRatioSEO ratio) {
		PageHelper.startPage(ratio.getPage(), ratio.getRows());
		SettlementRatioInfoExample example = new SettlementRatioInfoExample();
		this.searchRef(example,ratio);
		List<SettlementRatioInfo> list = settlementRatioInfoMapper.selectByExample(example);
		printInfoMes("查询 结算比例数量 {}", list.size());
		return new PageInfo<>(list);
	}

	private Integer datecompare(Date date) {

		Date date1 = new Date();
		int compareTo = date1.compareTo(date);
		return compareTo;
	}
	
	@Override
	public void exportList(SettlementRatioSEO ratio, HttpServletResponse response) {
		ExcelData data = new ExcelData();
		String title = "合同信息数据";
        data.setName(title);
        //添加表头
        List<String> titles = new ArrayList<>();
        titles.add("序号");
        titles.add("比列编号");
        titles.add("比列名称");
        titles.add("比列有效期");
        titles.add("创建时间");
        titles.add("创建人");
        titles.add("比列状态");
        titles.add("备注");
        titles.add("比例(%)");
        titles.add("金额（元）");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList<>();
        List<Object> row = null;
		PageHelper.startPage(ratio.getPage(), ratio.getRows());
		SettlementRatioInfoExample example = new SettlementRatioInfoExample();
		this.searchRef(example,ratio);
		List<SettlementRatioInfo> list = settlementRatioInfoMapper.selectByExample(example);
       for(int i=0; i<list.size();i++){
           row=new ArrayList<>();
           row.add(i+1);
           row.add(list.get(i).getRatioNumber());
           row.add(list.get(i).getFeeName());
           row.add(DateUtils.dateToDateTime(list.get(i).getEffectStartTime())+"———"+DateUtils.dateToDateTime(list.get(i).getEffectStartTime()));
           row.add(DateUtils.dateToDateTime(list.get(i).getCreateTime()));
           row.add(list.get(i).getCreateUser());
           row.add(SettlementStatus.getDesc(list.get(i).getStatus()));
           row.add(list.get(i).getRemark());
           row.add(list.get(i).getRatio());
           row.add(list.get(i).getAmount());
           rows.add(row);
       }
        data.setRows(rows);
        SimpleDateFormat fdate=new SimpleDateFormat("yyyy-MM-dd-HHmmss");
        String fileName=title+"_"+fdate.format(new Date())+".xls";
        try {
			ExcelUtils.exportExcel(response, fileName, data);
		} catch (Exception e) {
			printErrorMes("结算比列导出异常",e.getMessage());
		}		
	}

	@Override
	public boolean insertOrupdateSettlementRatio(SettlementRatioInfo settlementRatioSEO) {

		int flag = 0;
		if (settlementRatioSEO != null) {
			UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
			String auditPersion = userVO == null ? "" : userVO.getUsername();
			
			if (StringUtils.isEmpty(settlementRatioSEO.getRatioNumber())) {
				
				settlementRatioSEO.setCreateTime(new Date());
				settlementRatioSEO.setUpdateTime(new Date());
				settlementRatioSEO.setCreateUser(auditPersion);
				// 新增未待审核
				// 当前时间大于结束时间作废
				if (this.datecompare(settlementRatioSEO.getEffectEndTime())>0) {

					settlementRatioSEO.setStatus(SettlementStatus.AuditCAN.getCode());
				} else {

					settlementRatioSEO.setStatus(SettlementStatus.AuditWait.getCode());
				}
				// 新增未待审核
				settlementRatioSEO.setRatioNumber(getRatioNumber());
				try {
					flag = settlementRatioInfoMapper.insertSelective(settlementRatioSEO);
				} catch (Exception e) {
					printErrorMes("添加结算比例{}" + e.getMessage());
				}
			} else {
				settlementRatioSEO.setUpdateTime(new Date());
				settlementRatioSEO.setCreateUser(auditPersion);
				settlementRatioSEO.setStatus(SettlementStatus.AuditWait.getCode());
				try {
					SettlementRatioInfoExample example = new SettlementRatioInfoExample();
					example.createCriteria().andRatioNumberEqualTo(settlementRatioSEO.getRatioNumber());
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
			UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
			String auditPersion = userVO == null ? "" : userVO.getUsername();
			SettlementRatioInfoExample example = new SettlementRatioInfoExample();
			example.createCriteria().andRatioNumberEqualTo(ratioNumber);
			List<SettlementRatioInfo>  list = settlementRatioInfoMapper.selectByExample(example);
			SettlementRatioInfo ralio = list.get(0);
			ralio.setId(null);
			ralio.setRatioNumber(getRatioNumber());
			ralio.setCreateTime(new Date());
			ralio.setUpdateTime(new Date());
			ralio.setCreateUser(auditPersion);
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
	public SettlementRatioAudit getSettlementRatio(String ratioNumber) {
		
		SettlementRatioAudit res = new SettlementRatioAudit();
		SettlementRatioInfoExample example = new SettlementRatioInfoExample();
		example.createCriteria().andRatioNumberEqualTo(ratioNumber);
		List<SettlementRatioInfo>  list = settlementRatioInfoMapper.selectByExample(example);
		SettlementRatioInfo ratio = (list!=null && list.size() > 0)?list.get(0):null;
		if(ratio != null && StringUtils.isEmpty(ratio.getFeeDicId())){
			Map<String, String> paream = getCostNames();
			ratio.setFeeDicId(paream.get(ratio.getFeeDicId()));//翻译
		}
		res.setInfo(ratio);
		//获取 审批信息
		PcAuditInfoExample autit = new PcAuditInfoExample();
		autit.createCriteria().andContractNumberEqualTo(ratioNumber).andAuditTypeEqualTo("4");
		List<PcAuditInfo>  auList =  pcAuditInfoMapper.selectByExample(autit);
		res.setAuditInfo(auList);
		return res;
	}

	@Override
	public boolean cancelledSettlementRatio(String ratioNumber) {
		SettlementRatioInfoExample example = new SettlementRatioInfoExample();
		example.createCriteria().andRatioNumberEqualTo(ratioNumber);
		SettlementRatioInfo record = new SettlementRatioInfo();
		record.setStatus(SettlementStatus.AuditCAN.getCode());
		//作废
	    int  falg = settlementRatioInfoMapper.updateByExampleSelective(record, example);
	    if(falg >  0 ){
			return true;
		}
		return false;
	}

	@Override
	public Map<String, String> getCostNames() {
		Map<String, String> map = new HashMap<>();
		map.put("01", "设计费");
		map.put("02", "产品服务费");
		map.put("03", "施工管理费");
		map.put("04", "合同保证金");
		return map;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean batchcCheckSettlementRatio(List<String> ratioNumbers,String auditStatus,String auditCase) {

		for (int i = 0; i < ratioNumbers.size(); i++) {
				//添加审核记录表
				UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
				String auditPersion = userVO == null ? "" : userVO.getUsername();
				String auditAccount = userVO ==null?"":userVO.getUserRegister().getPhone();
				String companyId = userVO==null?"":userVO.getCompanyID();
				String ratioNumber = String.valueOf(ratioNumbers.get(i));
				PcAuditInfo record = new PcAuditInfo("4", "2",
						auditPersion, auditStatus, new Date(),
						companyId, auditCase, String.valueOf(ratioNumbers.get(i)), new Date(), auditAccount);
				//插入审核记录
				pcAuditInfoMapper.insertSelective(record);
				//修改状态
				SettlementRatioInfoExample example = new SettlementRatioInfoExample();
				example.createCriteria().andRatioNumberEqualTo(ratioNumber);
				SettlementRatioInfo recordT = new SettlementRatioInfo();

			List<SettlementRatioInfo> settlementRatioInfos = settlementRatioInfoMapper.selectByExample(example);
			SettlementRatioInfo settlementRatioInfo = (settlementRatioInfos!=null && settlementRatioInfos.size() > 0)?settlementRatioInfos.get(0):null;
			if (settlementRatioInfo == null) {
				return false;
			}
			// 待审核，申请作废，审批
			if (settlementRatioInfo.getStatus().equals(SettlementStatus.CANDecline.getCode())) {
				if (auditStatus.equals(SettlementStatus.AuditPass.getCode())) {
					recordT.setStatus(SettlementStatus.AuditCAN.getCode());
				} else {
					recordT.setStatus(SettlementStatus.AuditPass.getCode());
				}
			} else if (settlementRatioInfo.getStatus().equals(SettlementStatus.AuditWait.getCode())){
				if (auditStatus.equals(SettlementStatus.AuditPass.getCode())) {
					// 没到有效期 待生效
					if (this.datecompare(settlementRatioInfo.getEffectStartTime())<0) {
						recordT.setStatus(SettlementStatus.EffectiveWait.getCode());
					}
					// 在有效期 生效
					if (this.datecompare(settlementRatioInfo.getEffectStartTime())>=0 && this.datecompare(settlementRatioInfo.getEffectEndTime())<= 0) {
						recordT.setStatus(SettlementStatus.Effective.getCode());
					}
				} else {
					recordT.setStatus(SettlementStatus.AuditCAN.getCode());
				}
			}
			settlementRatioInfoMapper.updateByExampleSelective(recordT, example);
			return true;
		}
		return false;
	}


	
	/**
	 * 获取6-10 的随机位数数字
	 * @return result
	 */
	public synchronized static String getRatioNumber() {
		
	    Random random = new Random();
	    DecimalFormat df = new DecimalFormat("00");
	    String no = new SimpleDateFormat("yyyyMMddHHmmss")
	                .format(new Date()) + df.format(random.nextInt(100));
		return no;
	}


	/**
	 * 
	 * 查询有效的 的结算比列
	 * 平台服务管理费比列 0
	 *  设计费计算比列
	 */
	@Override
	public List<String>  getRatiloList(String code) {
		List<String> resList = new ArrayList<String>();
		Date now = new Date();
		SettlementRatioInfoExample example = new SettlementRatioInfoExample();
		example.createCriteria().andFeeDicIdEqualTo(code).
		andEffectStartTimeLessThan(now).andEffectEndTimeGreaterThanOrEqualTo(now);
		List<SettlementRatioInfo>  list = settlementRatioInfoMapper.selectByExample(example);
		for (int i = 0; i < list.size(); i++) {
			String amount = list.get(i).getAmount();
			String ratio = list.get(i).getRatio();
			if(!StringUtils.isEmpty(ratio)){
				resList.add(ratio);
			}else{// (!StringUtils.isEmpty(ratio)){
				resList.add(amount);
			}
		}
		return resList;
	}

	private void searchRef (SettlementRatioInfoExample example,SettlementRatioSEO ratio) {

		if(!StringUtils.isBlank(ratio.getRatioNumber())){
			example.createCriteria().andRatioNumberLike("%"+ratio.getRatioNumber()+"%");
		}
		if(!StringUtils.isBlank(ratio.getRatioName())){
			example.createCriteria().andFeeNameLike("%"+ratio.getRatioName()+"%");
		}
		if(!StringUtils.isBlank(ratio.getCreateUser())){
			example.createCriteria().andCreateUserLike("%"+ratio.getCreateUser()+"%");
		}
		if(ratio.getStartTime() !=null && !StringUtils.isBlank(ratio.getStartTime()+"")){
			example.createCriteria().andEffectStartTimeGreaterThanOrEqualTo(ratio.getStartTime());
		}
		if(ratio.getEndTime() != null && !StringUtils.isBlank(ratio.getEndTime()+"")){
			example.createCriteria().andEffectEndTimeLessThanOrEqualTo(ratio.getEndTime());
		}

		if(!StringUtils.isEmpty(ratio.getRatioStatus())){
			//1 待审核 2审核通过 3审核不通过 4作废 5申请作废 7生效 8失效 9未生效
			example.createCriteria().andStatusEqualTo(ratio.getAuditStatus());
		}
	}

	@Override
	public boolean applicationInvalid(String ratioNumber) {
		SettlementRatioInfoExample example = new SettlementRatioInfoExample();
		example.createCriteria().andRatioNumberEqualTo(ratioNumber);
		SettlementRatioInfo record = new SettlementRatioInfo();
		//申请作废
		record.setStatus(SettlementStatus.CANDecline.getCode());
		int  falg = 	settlementRatioInfoMapper.updateByExampleSelective(record, example);
		if(falg >  0 ){
			return true;
		}
		return false;
	}

}
