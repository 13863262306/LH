package lanhai.entity;

public class ProducePaper {

	private String id;
	private String produceCompanyId;
	private String produceCompanyName;
	private String wlbm;
	private String yield;
	private String bizDate;
	private String subState;
	
	
	public String getSubState() {
		return subState;
	}
	public void setSubState(String subState) {
		this.subState = subState;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProduceCompanyId() {
		return produceCompanyId;
	}
	public void setProduceCompanyId(String produceCompanyId) {
		this.produceCompanyId = produceCompanyId;
	}
	public String getProduceCompanyName() {
		return produceCompanyName;
	}
	public void setProduceCompanyName(String produceCompanyName) {
		this.produceCompanyName = produceCompanyName;
	}
	public String getWlbm() {
		return wlbm;
	}
	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}
	public String getYield() {
		return yield;
	}
	public void setYield(String yield) {
		this.yield = yield;
	}
	public String getBizDate() {
		return bizDate;
	}
	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
	}
	@Override
	public String toString() {
		return "ProducePaper [produceCompanyId=" + produceCompanyId + ", produceCompanyName=" + produceCompanyName
				+ ", wlbm=" + wlbm + ", yield=" + yield + ", bizDate=" + bizDate + "]";
	}
	
	
	
}
