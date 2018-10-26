package cn.thinkfree.service.event;

import cn.thinkfree.database.model.ContractInfo;

public class AuditEvent extends BaseEvent<ContractInfo> {
    
	private static final long serialVersionUID = 1L;

	public AuditEvent(ContractInfo user) {
        super(user);
    }

    public AuditEvent(Object source, ContractInfo user){
        super(source,user);
    }

}
