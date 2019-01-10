package lanhai.entity;

public class Company {

	private String id;
	private String companyName;
	private String companyNumber;
	private String companyAreaName;
	
	
	
	public String getCompanyAreaName() {
		return companyAreaName;
	}
	public void setCompanyAreaName(String companyAreaName) {
		this.companyAreaName = companyAreaName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyNumber() {
		return companyNumber;
	}
	public void setCompanyNumber(String companyNumber) {
		this.companyNumber = companyNumber;
	}
	@Override
	public String toString() {
		return "Company [id=" + id + ", companyName=" + companyName + ", companyNumber=" + companyNumber
				+ ", companyAreaName=" + companyAreaName + "]";
	}
	
	
	
}
