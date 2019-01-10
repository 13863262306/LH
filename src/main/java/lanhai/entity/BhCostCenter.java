package lanhai.entity;

public class BhCostCenter {

	private String id;
	private String centerName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCenterName() {
		return centerName;
	}
	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}
	@Override
	public String toString() {
		return "BhCostCenter [id=" + id + ", centerName=" + centerName + "]";
	}
	
}
