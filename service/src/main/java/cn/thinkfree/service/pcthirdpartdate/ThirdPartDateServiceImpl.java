package cn.thinkfree.service.pcthirdpartdate;

import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.mapper.ContractInfoMapper;
import cn.thinkfree.database.mapper.PcAuditInfoMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.MarginContractVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 20181109
 * @Description 第三方数据接口
 */
@Service
public class ThirdPartDateServiceImpl implements ThirdPartDateService {

    @Autowired
    ContractInfoMapper contractInfoMapper;

    @Autowired
    CompanyInfoMapper companyInfoMapper;

    @Autowired
    PcAuditInfoMapper pcAuditInfoMapper;

    @Value("${optionType}")
    private String type;

    @Override
    public MarginContractVO getMarginContract(String contractNumber) {


        MarginContractVO marginContractVO = new MarginContractVO();
        ContractInfoExample contractInfoExample = new ContractInfoExample();
        contractInfoExample.createCriteria().andContractNumberEqualTo(contractNumber);
        List<ContractInfo> contractInfos = contractInfoMapper.selectByExample(contractInfoExample);

        if (contractInfos.size() > 0) {

            // 合同信息
            ContractInfo contractInfo = contractInfos.get(0);
            marginContractVO.setOptionType(type);
            marginContractVO.setContractNumber(contractInfo.getContractNumber());
            marginContractVO.setTransactionDate(contractInfo.getSignedTime().toString());
            marginContractVO.setVendorId(contractInfo.getCompanyId());

            // 公司信息
            if (StringUtils.isNotBlank(contractInfo.getCompanyId())) {

//                CompanyInfoExample companyInfoExample = new CompanyInfoExample();
//                companyInfoExample.createCriteria().andCompanyIdEqualTo(contractInfo.getCompanyId());
                CompanyInfo companyInfo = companyInfoMapper.selectByCompanyId(contractInfo.getCompanyId());

                if (companyInfo != null) {
                    marginContractVO.setOperatingUnit(companyInfo.getSiteCompanyId());
                    marginContractVO.setVendorName(companyInfo.getCompanyName());
                }
            }

            // 审批人信息
            PcAuditInfoExample pcAuditInfoExample = new PcAuditInfoExample();
            pcAuditInfoExample.createCriteria().andContractNumberEqualTo(contractNumber);
            List<PcAuditInfo> pcAuditInfos = pcAuditInfoMapper.selectByExample(pcAuditInfoExample);
            if (pcAuditInfos.size() > 0) {

                PcAuditInfo pcAuditInfo = pcAuditInfos.get(0);
                marginContractVO.setOperationPerson(pcAuditInfo.getAuditPersion());
            }

            return marginContractVO;
        }
        return null;
    }
}
