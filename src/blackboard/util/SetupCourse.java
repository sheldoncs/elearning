package blackboard.util;

public class SetupCourse {
 private String crseCode;
 private String crseNumb;
 private boolean upload;
 private String seqNumb;
 
/**
 * @return the seqNumb
 */
public String getSeqNumb() {
	return seqNumb;
}
/**
 * @param seqNumb the seqNumb to set
 */
public void setSeqNumb(String seqNumb) {
	this.seqNumb = seqNumb;
}
public String getCrseCode() {
	return crseCode;
}
public void setCrseCode(String crseCode) {
	this.crseCode = crseCode;
}
public String getCrseNumb() {
	return crseNumb;
}
public void setCrseNumb(String crseNumb) {
	this.crseNumb = crseNumb;
}
public boolean isUpload() {
	return upload;
}
public void setUpload(boolean upload) {
	this.upload = upload;
}
 
 
 
}
