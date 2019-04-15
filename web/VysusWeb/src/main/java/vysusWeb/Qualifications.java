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
import javax.inject.Named;

import storage.DBProblemException;
import storage.InvalidDataException;
import storage.Qualification;
import util.Conv;

@Named("qualifications")
@ConversationScoped
public class Qualifications extends VysusBean implements Serializable {
	static List<String> subjects = new ArrayList<String>(Arrays.asList("English Literature","English Language","Maths","Chemistry","Biology","Physics","History","Geography","French","German","Spanish","Mandarin","Resistant Materials","Art","Music","PE","Pastoral Studies","Computing and IT","Religious Studies"));
	static List<String> subjectsNull = new ArrayList<String>(Arrays.asList("", "English Literature","English Language","Maths","Chemistry","Biology","Physics","History","Geography","French","German","Spanish","Mandarin","Resistant Materials","Art","Music","PE","Pastoral Studies","Computing and IT","Religious Studies"));
	static List<String> types = new ArrayList<String>(Arrays.asList("Undergraduate", "Postgraduate", "PHD", "Work experience"));
	
	List<Map<String, String>> qualifications = new ArrayList<Map<String, String>>();
	Map<String, Object> newQual = new HashMap<String, Object>();
	
	@PostConstruct
	void onInit() {
		System.out.println(actor.account);
		try (Connection connection = getConnection()){
			for(Qualification q : Qualification.allQualifications(actor.account, connection)) {
				qualifications.add(q.show());
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
		return qualifications.size()==0;
	}
	
	public List<Map<String, String>> getQualifications(){
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