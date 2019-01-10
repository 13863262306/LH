package lanhai.entity;

public class BhWl {
	private int id;
	private String djbh;
	private int wlid;
	private int wlsq;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDjbh() {
		return djbh;
	}
	public void setDjbh(String djbh) {
		this.djbh = djbh;
	}
	public int getWlid() {
		return wlid;
	}
	public void setWlid(int wlid) {
		this.wlid = wlid;
	}
	public int getWlsq() {
		return wlsq;
	}
	public void setWlsq(int wlsq) {
		this.wlsq = wlsq;
	}
	@Override
	public String toString() {
		return "BhWl [id=" + id + ", djbh=" + djbh + ", wlid=" + wlid
				+ ", wlsq=" + wlsq + "]";
	}
	
}
