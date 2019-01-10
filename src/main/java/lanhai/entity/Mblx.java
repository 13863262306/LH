package lanhai.entity;

public class Mblx {
	private String id;
	private String mbmc;
	private String mbNumber;
	private String baohuoInputKey;
	
	
	
	public String getBaohuoInputKey() {
		return baohuoInputKey;
	}

	public void setBaohuoInputKey(String baohuoInputKey) {
		this.baohuoInputKey = baohuoInputKey;
	}

	public String getMbNumber() {
		return mbNumber;
	}

	public void setMbNumber(String mbNumber) {
		this.mbNumber = mbNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMbmc() {
		return mbmc;
	}

	public void setMbmc(String mbmc) {
		this.mbmc = mbmc;
	}

	@Override
	public String toString() {
		return "Mblx [id=" + id + ", mbmc=" + mbmc + "]";
	}

	
	
}
