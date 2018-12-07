package cn.thinkfree.service.contractsignature;

import cn.thinkfree.database.event.sync.CreateOrder;
import cn.thinkfree.database.mapper.ContractSignatureMapper;
import cn.thinkfree.database.model.ContractSignature;
import cn.thinkfree.database.model.ContractSignatureExample;
import cn.thinkfree.database.vo.ContractSignatureVO;
import cn.thinkfree.database.vo.remote.SyncOrderVO;
import cn.thinkfree.service.event.EventService;
import cn.thinkfree.service.pcthirdpartdate.ThirdPartDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractSignatureServiceImpl implements ContractSignatureService{

    @Autowired
    ContractSignatureMapper contractSignatureMapper;
    
    @Autowired
    EventService eventService;
    
    @Autowired
    ThirdPartDateService thirdPartDateService;

    @Override
    public boolean saveContractSignature(ContractSignatureVO contractSignatureVO) {

        if (contractSignatureVO.getContractSignatures().size()>0) {

            contractSignatureVO.getContractSignatures().forEach(e->{
                e.setContractNumber(contractSignatureVO.getContractNo());
                contractSignatureMapper.insertSelective(e);
//                List<SyncOrderVO>  listvo=  thirdPartDateService.getOrderContract(contractSignatureVO.getContractNo());
//                //for (int i = 0; i < listvo.size(); i++) {
//                	  CreateOrder order = new CreateOrder();
//                order.setData(listvo);
//                      eventService.publish(order);
//                //}
              
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



//    public static void main(String[] args) {
//        String jsonSr = "[{'sortNumber':'0','name':'设计3d方案','ratio': '30','costValue': '200000'},{'sortNumber': '1','name':'设计3d方案2','ratio': '40','costValue': '2222222222'}]";
//       JSONArray jsonArray=JSONArray.parseArray(jsonSr);
//        System.out.println(jsonArray.size());
//    }
}
