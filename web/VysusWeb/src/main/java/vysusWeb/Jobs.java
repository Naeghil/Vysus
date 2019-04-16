package vysusWeb;

import java.io.Serializable;
import java.sql.Connection;
//import java.sql.Date;
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

import storage.DBProblemException;
import storage.InvalidDataException;
import storage.Job;
//import util.Conv;

@Named("jobs")
@ConversationScoped
public class Jobs extends VysusBean implements Serializable {
	static List<String> subjects = new ArrayList<String>(Arrays.asList("English Literature","English Language","Maths","Chemistry","Biology","Physics","History","Geography","French","German","Spanish","Mandarin","Resistant Materials","Art","Music","PE","Pastoral Studies","Computing and IT","Religious Studies"));
	
	List<Map<String, String>> jobs = new ArrayList<Map<String, String>>();
	Map<String, Object> newJob = new HashMap<String, Object>();
	
	@Inject
	protected @Named("actor") Actor actor;
	
	@PostConstruct
	void onInit() {
		try (Connection connection = getConnection()){
			for(Job j : Job.allJobs(actor.account, connection)) {
				jobs.add(j.show());
			}
		} catch (DBProblemException | InvalidDataException | SQLException e) {
			actor.handleException(e, true);
		}
	}
	
	public void addNew() {
		try(Connection connection = getConnection()) {
			/*Date sDate = Conv.stringToDate((String)newJob.remove("sYear")+"-"+(String)newJob.remove("sMonth")+"-"+(String)newJob.remove("sDay"));
			Date eDate = Conv.stringToDate((String)newJob.remove("eYear")+"-"+(String)newJob.remove("eMonth")+"-"+(String)newJob.remove("eDay"));

			newJob.put("startDate", sDate);
			newJob.put("endDate", eDate); */
			newJob.put("accountID", actor.account);
			
			new Job(newJob, connection);
			
			//Need to add calendar stuff
		} catch (DBProblemException | SQLException e) {
			actor.handleException(e, false);
		}
	}
	
	public void delete(String stringID) {
		try(Connection connection = getConnection()) {
			new Job(Integer.parseInt(stringID), null).deleteJob(connection);
		} catch (DBProblemException | InvalidDataException | SQLException e) {
			actor.handleException(e, false);
		}
	}
	
//Renderer:	
	public boolean noJobs() {
		return jobs.size()==0;
	}
	
	public List<Map<String, String>> getJobs(){
		return jobs;
	}
	
//For dropdowns:
	public List<String> getSubjects (){
		return subjects;
	}
//Getters & Setters
	public String getTitle() {
		return newJob.containsKey("title") ? (String)newJob.get("title") : "";
	}
	public void setTitle(String title) {
		newJob.put("title", title);
	}
	public String getSubject() {
		return newJob.containsKey("subject") ? (String)newJob.get("subject") : "";
	}
	public void setSubject(String subject) {
		newJob.put("subject", subject);
	}
	/**
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
	Wait for the calendar **/
	
	public String getDescription() {
		return newJob.containsKey("description") ? (String)newJob.get("description") : "";
	}
	public void setDescription(String description) {
		newJob.put("description", description);
	}
	
	public float getRate() {
		return newJob.containsKey("ratePerHour") ? (float)newJob.get("ratePerHour") : 0;
	}
	public void setRate(String rate) {
		newJob.put("ratePerHour", rate);
	}
}

class showJobs {

	public String accountID;
	public String subject;
	public String description;
	public String ratePerHour;
	
	showJobs() {

	}

	public void setDetails(Map<String,String> job) {
		this.accountID = job.get("accountID");
		this.subject = job.get("subject");
		this.description = job.get("description");
		this.ratePerHour = job.get("ratePerHour");
	}
	
	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRatePerHour() {
		return ratePerHour;
	}

	public void setRatePerHour(String ratePerHour) {
		this.ratePerHour = ratePerHour;
	}
	}