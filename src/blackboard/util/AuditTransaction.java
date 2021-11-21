package blackboard.util;
//"<Full path to msaccess>\msaccess.exe" "<Full path to database>\MyDB.mdb" /x MacroToRun 
public class AuditTransaction {
	private String orderid;
	private String finalStatus;
	private String authDate;
	private String term;
	private String id;
	private String pidm;
	private String cardAmount;
	private String activityDate;
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getFinalStatus() {
		return finalStatus;
	}
	public void setFinalStatus(String finalStatus) {
		this.finalStatus = finalStatus;
	}
	public String getAuthDate() {
		return authDate;
	}
	public void setAuthDate(String authDate) {
		this.authDate = authDate;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPidm() {
		return pidm;
	}
	public void setPidm(String pidm) {
		this.pidm = pidm;
	}
	public String getCardAmount() {
		return cardAmount;
	}
	public void setCardAmount(String cardAmount) {
		this.cardAmount = cardAmount;
	}
	public String getActivityDate() {
		return activityDate;
	}
	public void setActivityDate(String activityDate) {
		this.activityDate = activityDate;
	}
	
}
