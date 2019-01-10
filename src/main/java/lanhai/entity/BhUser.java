package lanhai.entity;

public class BhUser {

	private String userNumber;
	private String name;
	private String userName;
	private String password;
	//档口组织ID
	private String costCenterId;
	//档口组织名称
	private String costCenterName;
	private String companyId;
	private String companyName;
	private String companyAreaName;
	private String companyAndCostCenterName;
	//职务ID
	private String positionId;
	//职务名称
	private String position;
	private String userType;
	private String longNumber;
	private String pmId;
	
	private CustomerCom cc;
	
	
	
	
	public String getCompanyAreaName() {
		return companyAreaName;
	}
	public void setCompanyAreaName(String companyAreaName) {
		this.companyAreaName = companyAreaName;
	}
	public CustomerCom getCc() {
		return cc;
	}
	public void setCc(CustomerCom cc) {
		this.cc = cc;
	}
	public String getPmId() {
		return pmId;
	}
	public void setPmId(String pmId) {
		this.pmId = pmId;
	}
	public String getLongNumber() {
		return longNumber;
	}
	public void setLongNumber(String longNumber) {
		this.longNumber = longNumber;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getCompanyAndCostCenterName() {
		return companyAndCostCenterName;
	}
	public void setCompanyAndCostCenterName(String companyAndCostCenterName) {
		this.companyAndCostCenterName = companyAndCostCenterName;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserNumber() {
		return userNumber;
	}
	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCostCenterId() {
		return costCenterId;
	}
	public void setCostCenterId(String costCenterId) {
		this.costCenterId = costCenterId;
	}
	public String getCostCenterName() {
		return costCenterName;
	}
	public void setCostCenterName(String costCenterName) {
		this.costCenterName = costCenterName;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	@Override
	public String toString() {
		return "BhUser [userNumber=" + userNumber + ", name=" + name + ", userName=" + userName + ", password="
				+ password + ", costCenterId=" + costCenterId + ", costCenterName=" + costCenterName + ", companyId="
				+ companyId + ", companyName=" + companyName + ", companyAreaName=" + companyAreaName
				+ ", companyAndCostCenterName=" + companyAndCostCenterName + ", positionId=" + positionId
				+ ", position=" + position + ", userType=" + userType + ", longNumber=" + longNumber + ", pmId=" + pmId
				+ ", cc=" + cc + "]";
	}
	
	
	
	
	
	
}
