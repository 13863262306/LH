package lanhai.entity;

public class WL_list {
	private String wlbm;
	private String wlmc;
	private String wlgg;
	private String wljl;
	private int wlsq;
	private int wlsh;
	private String wlrq;
	private String lastMonthAvg;
	private String sameMonthOfLastYearAvg;
	private String mbId;
	private String inputKey;
	
	
	public String getInputKey() {
		return inputKey;
	}
	public void setInputKey(String inputKey) {
		this.inputKey = inputKey;
	}
	public String getMbId() {
		return mbId;
	}
	public void setMbId(String mbId) {
		this.mbId = mbId;
	}
	public String getLastMonthAvg() {
		return lastMonthAvg;
	}
	public void setLastMonthAvg(String lastMonthAvg) {
		this.lastMonthAvg = lastMonthAvg;
	}
	public String getSameMonthOfLastYearAvg() {
		return sameMonthOfLastYearAvg;
	}
	public void setSameMonthOfLastYearAvg(String sameMonthOfLastYearAvg) {
		this.sameMonthOfLastYearAvg = sameMonthOfLastYearAvg;
	}
	public String getWlbm() {
		return wlbm;
	}
	public void setWlbm(String wlbm) {
		this.wlbm = wlbm;
	}
	public String getWlmc() {
		return wlmc;
	}
	public void setWlmc(String wlmc) {
		this.wlmc = wlmc;
	}
	public String getWlgg() {
		return wlgg;
	}
	public void setWlgg(String wlgg) {
		this.wlgg = wlgg;
	}
	public String getWljl() {
		return wljl;
	}
	public void setWljl(String wljl) {
		this.wljl = wljl;
	}
	public int getWlsq() {
		return wlsq;
	}
	public void setWlsq(int wlsq) {
		this.wlsq = wlsq;
	}
	public int getWlsh() {
		return wlsh;
	}
	public void setWlsh(int wlsh) {
		this.wlsh = wlsh;
	}
	public String getWlrq() {
		return wlrq;
	}
	public void setWlrq(String wlrq) {
		this.wlrq = wlrq;
	}
	@Override
	public String toString() {
		return "WL_list [wlbm=" + wlbm + ", wlmc=" + wlmc + ", wlgg=" + wlgg + ", wljl=" + wljl + ", wlsq=" + wlsq
				+ ", wlsh=" + wlsh + ", wlrq=" + wlrq + ", lastMonthAvg=" + lastMonthAvg + ", sameMonthOfLastYearAvg="
				+ sameMonthOfLastYearAvg + ", mbId=" + mbId + "]";
	}
	
	
	
	
	
}
