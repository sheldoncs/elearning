package blackboard.util;

public class Dropped {
 
	private String termCode;
    private String crn;
    private boolean dropped;
    private String id;
	public String getTermCode() {
		return termCode;
	}
	public void setTermCode(String termCode) {
		this.termCode = termCode;
	}
	public String getCrn() {
		return crn;
	}
	public void setCrn(String crn) {
		this.crn = crn;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isDropped() {
		return dropped;
	}
	public void setDropped(boolean dropped) {
		this.dropped = dropped;
	}
}
