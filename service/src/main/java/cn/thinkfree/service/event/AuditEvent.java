package cn.thinkfree.service.event;

import cn.thinkfree.database.model.ContractInfo;

public class AuditEvent extends BaseEvent<ContractInfo> {
    
    public AuditEvent(ContractInfo user) {
        super(user);
    }

    public AuditEvent(Object source, ContractInfo user){
        super(source,user);
    }

}
