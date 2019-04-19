package vysusWeb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import exceptions.*;
import storage.*;
import util.Conv;

@Named("qualifications")
@ConversationScoped
public class Qualifications extends vysusWeb.bases.SecondaryBean implements Serializable {
	static List<String> subjects = new ArrayList<String>(
			Arrays.asList("English Literature", "English Language", "Maths", "Chemistry", "Biology", "Physics",
					"History", "Geography", "French", "German", "Spanish", "Mandarin", "Resistant Materials", "Art",
					"Music", "PE", "Pastoral Studies", "Computing and IT", "Religious Studies"));
	static List<String> subjectsNull = new ArrayList<String>(
			Arrays.asList("", "English Literature", "English Language", "Maths", "Chemistry", "Biology", "Physics",
					"History", "Geography", "French", "German", "Spanish", "Mandarin", "Resistant Materials", "Art",
					"Music", "PE", "Pastoral Studies", "Computing and IT", "Religious Studies"));
	static List<String> types = new ArrayList<String>(
			Arrays.asList("Undergraduate", "Postgraduate", "PHD", "Work experience"));

	public void onLoad() { 
		onLoad(""); 
	}
	
	protected void loadData(Connection connection) throws DBProblemException, InvalidDataException {
		System.out.println("Qualifications.loadData");
		for (SecondaryStorage q : Qualification.all(actor.account(), connection)) toShow.add(q.show());
	}

	protected void makeNew (Connection connection) throws InvalidDataException, DBProblemException {
		Date sDate = Conv.stringToDate((String) newData.remove("sYear") 
			+ "-" + (String) newData.remove("sMonth")
			+ "-" + (String) newData.remove("sDay"));
		Date eDate = Conv.stringToDate((String) newData.remove("eYear") 
			+ "-" + (String) newData.remove("eMonth")
			+ "-" + (String) newData.remove("eDay"));
		newData.put("startDate", sDate);
		newData.put("endDate", eDate);
		
		new Qualification(actor.account(), newData, connection);
		redirect("qualifications.xhtml");
	}

	public void delete(String id) {
		try (Connection connection = getConnection()) {
			new Qualification(Integer.parseInt(id)).delete(connection);
			redirect("qualifications.xhtml");
		} catch (DBProblemException | InvalidDataException | SQLException e) {
			actor.handleException(e, false);
		}
	}

//For dropdowns:
	public List<String> getSubjects() { return subjects; }
	public List<String> getSubjectsNull() { return subjectsNull; }
	public List<String> getTypes() { return types; }

//Getters & Setters
	public String getTitle() {
		return newData.containsKey("title") ? (String) newData.get("title") : "";
	}
	public void setTitle(String title) {
		newData.put("title", title);
	}

	public String getsDay() {
		return newData.containsKey("sDay") ? (String) newData.get("sDay") : "";
	}
	public void setsDay(String sDay) {
		newData.put("sDay", sDay);
	}
	public String getsMonth() {
		return newData.containsKey("sMonth") ? (String) newData.get("sDay") : "";
	}
	public void setsMonth(String sMonth) {
		newData.put("sMonth", sMonth);
	}
	public String getsYear() {
		return newData.containsKey("sYear") ? (String) newData.get("sYear") : "";
	}
	public void setsYear(String sYear) {
		newData.put("sYear", sYear);
	}

	public String geteDay() {
		return newData.containsKey("eDay") ? (String) newData.get("eDay") : "";
	}
	public void seteDay(String eDay) {
		newData.put("eDay", eDay);
	}
	public String geteMonth() {
		return newData.containsKey("eMonth") ? (String) newData.get("eDay") : "";
	}
	public void seteMonth(String eMonth) {
		newData.put("eMonth", eMonth);
	}
	public String geteYear() {
		return newData.containsKey("eYear") ? (String) newData.get("eYear") : "";
	}
	public void seteYear(String eYear) {
		newData.put("eYear", eYear);
	}

	public String getComment() {
		return newData.containsKey("comment") ? (String) newData.get("comment") : "";
	}
	public void setComment(String comment) {
		newData.put("comment", comment);
	}
	
	public String getWhere() {
		return newData.containsKey("institution") ? (String) newData.get("institution") : "";
	}
	public void setWhere(String where) {
		newData.put("institution", where);
	}
	
	public String getType() {
		return newData.containsKey("level") ? (String) newData.get("level") : "";
	}
	public void setType(String type) {
		newData.put("level", type);
	}

	public String getEmail() {
		return newData.containsKey("institutionEmail") ? (String) newData.get("institutionEmail") : "";
	}
	public void setEmail(String rEmail) {
		newData.put("institutionEmail", rEmail);
	}

	public String getPhoneNo() {
		return newData.containsKey("institutionPhoneNo") ? (String) newData.get("institutionPhoneNo") : "";
	}
	public void setPhoneNo(String rPhoneNo) {
		newData.put("institutionPhoneNo", rPhoneNo);
	}

	public String getReferee() {
		return newData.containsKey("refree") ? (String) newData.get("referee") : "";
	}
	public void setReferee(String referee) {
		newData.put("referee", referee);
	}

	public String getmainSubj() {
		return "";
	}
	public void setmainSubj(String mainSubj) {
		newData.put("mainSubj", mainSubj);
	}

	public String getSubj1() {
		return "";
	}
	public void setSubj1(String subj1) {
		newData.put("subj1", subj1);
	}
	public String getSubj2() {
		return "";
	}
	public void setSubj2(String subj2) {
		newData.put("subj2", subj2);
	}
	public String getSubj3() {
		return "";
	}
	public void setSubj3(String subj3) {
		newData.put("subj3", subj3);
	}
}