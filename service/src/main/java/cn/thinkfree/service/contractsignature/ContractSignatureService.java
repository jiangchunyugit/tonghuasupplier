package cn.thinkfree.service.contractsignature;

import cn.thinkfree.database.model.ContractSignature;
import cn.thinkfree.database.vo.ContractSignatureVO;

import java.util.List;

public interface ContractSignatureService {

    /**
     * 上传合同签名
     * @param contractSignatureVO
     * @return
     */
    boolean saveContractSignature (ContractSignatureVO contractSignatureVO);

    /**
     * 获取合同签名
     * @param contractNumber
     * @return
     */
    List<ContractSignature> getContraSignature(String contractNumber);
}
