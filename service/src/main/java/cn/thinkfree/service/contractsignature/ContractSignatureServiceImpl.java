package cn.thinkfree.service.contractsignature;

import cn.thinkfree.database.mapper.ContractSignatureMapper;
import cn.thinkfree.database.model.ContractSignature;
import cn.thinkfree.database.model.ContractSignatureExample;
import cn.thinkfree.database.vo.ContractSignatureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractSignatureServiceImpl implements ContractSignatureService{

    @Autowired
    ContractSignatureMapper contractSignatureMapper;

    @Override
    public boolean saveContractSignature(ContractSignatureVO contractSignatureVO) {

        if (contractSignatureVO.getContractSignatures().size()>0) {

            contractSignatureVO.getContractSignatures().forEach(e->{
                e.setContractNumber(contractSignatureVO.getContractNo());
                contractSignatureMapper.insertSelective(e);
            });
            return true;
        }
        return false;
    }

    @Override
    public List<ContractSignature> getContraSignature(String contractNumber) {

        ContractSignatureExample contractSignatureExample = new ContractSignatureExample();
        contractSignatureExample.createCriteria().andContractNumberEqualTo(contractNumber);
        return contractSignatureMapper.selectByExample(contractSignatureExample);
    }
}
