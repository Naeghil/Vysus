package vysusWeb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import storage.*;
import util.Conv;

@Named("qualifications")
@ConversationScoped
public class Qualifications extends VysusBean implements Serializable {
	static List<String> subjects = new ArrayList<String>(Arrays.asList("English Literature","English Language","Maths","Chemistry","Biology","Physics","History","Geography","French","German","Spanish","Mandarin","Resistant Materials","Art","Music","PE","Pastoral Studies","Computing and IT","Religious Studies"));
	static List<String> subjectsNull = new ArrayList<String>(Arrays.asList("", "English Literature","English Language","Maths","Chemistry","Biology","Physics","History","Geography","French","German","Spanish","Mandarin","Resistant Materials","Art","Music","PE","Pastoral Studies","Computing and IT","Religious Studies"));
	static List<String> types = new ArrayList<String>(Arrays.asList("Undergraduate", "Postgraduate", "PHD", "Work experience"));
	
	List<Map<String, String>> qualifications = null;//new ArrayList<Map<String, String>>();
	Map<String, Object> newQual = new HashMap<String, Object>();
	
	@Inject
	protected @Named("actor") Actor actor;
	
	@PostConstruct
	void onLoad() {
		System.out.println(actor.account);
		try (Connection connection = getConnection()){
			qualifications = new ArrayList<Map<String, String>>();
			for(Qualification q : Qualification.allQualifications(actor.account, connection)) {
				qualifications.add(q.show());
				System.out.println(qualifications.toString());
			}
		} catch (DBProblemException | InvalidDataException | SQLException e) {
			actor.handleException(e, true);
		}
	}
	
	public void addNew() {
		try(Connection connection = getConnection()) {
			Date sDate = Conv.stringToDate((String)newQual.remove("sYear")+"-"+(String)newQual.remove("sMonth")+"-"+(String)newQual.remove("sDay"));
			Date eDate = Conv.stringToDate((String)newQual.remove("eYear")+"-"+(String)newQual.remove("eMonth")+"-"+(String)newQual.remove("eDay"));

			newQual.put("startDate", sDate);
			newQual.put("endDate", eDate);
			System.out.println(actor.account);
			System.out.println(actor.actor);
			newQual.put("id", "0miles");
			
			System.out.println("newQual: " + newQual);
			new Qualification(newQual, connection);
			
		} catch (DBProblemException | InvalidDataException | SQLException e) {
			actor.handleException(e, false);
		}
	}
	
	public void delete(String stringID) {
		try(Connection connection = getConnection()) {
			new Qualification(Integer.parseInt(stringID), null).deleteQualification(connection);
		} catch (DBProblemException | InvalidDataException | SQLException e) {
			actor.handleException(e, false);
		}
	}
	
//Renderer:	
	public boolean noQualifications() {
		if(qualifications==null) onLoad();
		return qualifications.size()==0;
	}
	
	public List<Map<String, String>> getQualifications(){
		if(qualifications==null) onLoad();
		return qualifications;
	}
	
//For dropdowns:
	public List<String> getSubjects (){
		return subjects;
	}
	public List<String> getSubjectsNull() {
		return subjectsNull;
	}
	public List<String> getTypes(){
		return types;
	}
//Getters & Setters
	public String getTitle() {
		return newQual.containsKey("title") ? (String)newQual.get("title") : "";
	}
	public void setTitle(String title) {
		newQual.put("title", title);
	}
	
	public String getsDay() {
		return newQual.containsKey("sDay") ? (String)newQual.get("sDay") : "";
	}
	public void setsDay(String sDay) {
		newQual.put("sDay", sDay);
	}
	public String getsMonth() {
		return newQual.containsKey("sMonth") ? (String)newQual.get("sDay") : "";
	}
	public void setsMonth(String sMonth) {
		newQual.put("sMonth", sMonth);
	}
	public String getsYear() {
		return newQual.containsKey("sYear") ? (String) newQual.get("sYear") : "";
	}
	public void setsYear(String sYear) {
		newQual.put("sYear", sYear);
	}
	
	public String geteDay() {
		return newQual.containsKey("eDay") ? (String)newQual.get("eDay") : "";
	}
	public void seteDay(String eDay) {
		newQual.put("eDay", eDay);
	}
	public String geteMonth() {
		return newQual.containsKey("eMonth") ? (String)newQual.get("eDay") : "";
	}
	public void seteMonth(String eMonth) {
		newQual.put("eMonth", eMonth);
	}
	public String geteYear() {
		return newQual.containsKey("eYear") ? (String) newQual.get("eYear") : "";
	}
	public void seteYear(String eYear) {
		newQual.put("eYear", eYear);
	}
	
	public String getComment() {
		return newQual.containsKey("comment") ? (String) newQual.get("comment") : "";
	}
	public void setComment(String comment) {
		newQual.put("comment", comment);
	}
	
	public String getWhere() {
		return newQual.containsKey("institution") ? (String) newQual.get("institution") : "";
	}
	public void setWhere(String where) {
		newQual.put("institution", where);
	}
	
	public String getType() {
		return newQual.containsKey("level") ? (String) newQual.get("level") : "";
	}
	public void setType(String type) {
		newQual.put("level", type);
	}
	
	public String getEmail() {
		return newQual.containsKey("institutionEmail") ? (String) newQual.get("institutionEmail") : "";
	}
	public void setEmail(String rEmail) {
		newQual.put("institutionEmail", rEmail);
	}
	
	public String getPhoneNo() {
		return newQual.containsKey("institutionPhoneNo") ? (String) newQual.get("institutionPhoneNo") : "";
	}
	public void setPhoneNo(String rPhoneNo) {
		newQual.put("institutionPhoneNo", rPhoneNo);
	}
	
	public String getReferee() {
		return newQual.containsKey("refree") ? (String) newQual.get("referee") : "";
	}
	public void setReferee(String referee) {
		newQual.put("referee", referee);
	}
	
	public String getmainSubj() {
		return "";
	}
	public void setmainSubj(String mainSubj) {
		newQual.put("mainSubj", mainSubj);
	}
	public String getSubj1() {
		return "";
	}
	public void setSubj1(String subj1) {
		newQual.put("subj1", subj1);
	}
	public String getSubj2() {
		return "";
	}
	public void setSubj2(String subj2) {
		newQual.put("subj2", subj2);
	}
	public String getSubj3() {
		return "";
	}
	public void setSubj3(String subj3) {
		newQual.put("subj3", subj3);
	}
}

class showQualifications {

	public String accountID;
	//qualification info
	public String title;
	public String level;
	public String startDate;
	public String endDate;
	public String comment;
	//institution they got it from
	public String institution;
	public String institutionEmail;
	public String institutionPhoneNo;
	public String referee;
	//subjects it allows them to teach
	public String mainSubj;
	public String subj1;
	public String subj2;
	public String subj3;

	showQualifications() {

	}

	public void setDetails(Map<String,String> qualification) {
		this.accountID = qualification.get("accountID");
		this.title = qualification.get("title");
		this.startDate = qualification.get("startDate");
		this.endDate = qualification.get("endDate");
		this.comment = qualification.get("comment");
		this.institution = qualification.get("institution");
		this.institutionEmail = qualification.get("institutionEmail");
		this.institutionPhoneNo = qualification.get("institutionPhoneNo");
		this.referee = qualification.get("referee");
		this.mainSubj = qualification.get("mainSubj");
		this.subj1 = qualification.get("subj1");
		this.subj2 = qualification.get("subj2");
		this.subj3 = qualification.get("subj3");
		
	}
	
	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getInstitutionEmail() {
		return institutionEmail;
	}

	public void setInstitutionEmail(String institutionEmail) {
		this.institutionEmail = institutionEmail;
	}

	public String getInstitutionPhoneNo() {
		return institutionPhoneNo;
	}

	public void setInstitutionPhoneNo(String institutionPhoneNo) {
		this.institutionPhoneNo = institutionPhoneNo;
	}

	public String getReferee() {
		return referee;
	}

	public void setReferee(String referee) {
		this.referee = referee;
	}

	public String getMainSubj() {
		return mainSubj;
	}

	public void setMainSubj(String mainSubj) {
		this.mainSubj = mainSubj;
	}

	public String getSubj1() {
		return subj1;
	}

	public void setSubj1(String subj1) {
		this.subj1 = subj1;
	}

	public String getSubj2() {
		return subj2;
	}

	public void setSubj2(String subj2) {
		this.subj2 = subj2;
	}

	public String getSubj3() {
		return subj3;
	}

	public void setSubj3(String subj3) {
		this.subj3 = subj3;
	}

}