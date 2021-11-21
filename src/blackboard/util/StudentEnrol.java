package blackboard.util;

public class StudentEnrol {

	/*term code, subj_code, crse_numb, seq_numb, id*/
	private String termCode;
	private String subjCode;
	private String crseNumb;
	private String seqNumb;
	private String id;
	private String action;
	private String firstname;
	private String lastname;
	private String crn;
	private String userType;
	private String courseId;
	private String email;
	private String campusCode;
	
	public String getCampusCode() {
		return campusCode;
	}
	public void setCampusCode(String campusCode) {
		this.campusCode = campusCode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the courseId
	 */
	public String getCourseId() {
		return courseId;
	}
	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public void setTermCode(String s){
		termCode = s;
	}
	public String getTermCode(){
		return termCode;
	}
	public void setSubjCode(String s){
		subjCode = s;
	}
	public String getSubjCode(){
		return subjCode;
	}
	public void setCrseCode(String s){
		crseNumb = s;
	}
	public String getCrseCode(){
		return crseNumb;
	}
	public void setSeqNumb(String s){
		seqNumb = s;
	}
	public String getSeqNumb(){
		return seqNumb;
	}
	public void setId(String s){
		id = s;
	}
	public String getId(){
		return id;
	}
	public void setAction(String s){
		action = s;
	}
	public String getAction(){
		return action;
	}
	public void setFirstname(String s){
		firstname = s;
	}
	public String getFirstname(){
		return firstname;
	}
	public void setLastname(String s){
		lastname = s;
	}
	public String getLastname(){
		return lastname;
	}
	public void setCrn(String s){
		crn = s;
	}
	public String getCrn(){
		return crn;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
}
