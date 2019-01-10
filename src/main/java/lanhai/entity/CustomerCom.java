package lanhai.entity;

public class CustomerCom {

	private String id;
	private String userNum;
	private String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserNum() {
		return userNum;
	}
	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "CustomerCom [id=" + id + ", userNum=" + userNum + ", name=" + name + "]";
	}
	
	
	
}
