package cn.thinkfree.core.model;

public abstract class AbstractDataAuth {

	
	/**
	 * 
	 * 是否要走数据权限
	 */
	private boolean dataFlag = false;

	public boolean isDataFlag() {
		return dataFlag;
	}

	public void setDataFlag(boolean dataFlag) {
		this.dataFlag = dataFlag;
	}
	
}
