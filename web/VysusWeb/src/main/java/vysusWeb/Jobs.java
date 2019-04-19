package vysusWeb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import exceptions.*;
import storage.Job;
import storage.SecondaryStorage;

@Named("jobs")
@ConversationScoped
public class Jobs extends vysusWeb.bases.SecondaryBean implements Serializable {
	static List<String> subjects = new ArrayList<String>(Arrays.asList("English Literature","English Language","Maths","Chemistry","Biology","Physics","History","Geography","French","German","Spanish","Mandarin","Resistant Materials","Art","Music","PE","Pastoral Studies","Computing and IT","Religious Studies"));
	
	public void onLoad() { 
		onLoad("no"); 
	}
	
	protected void loadData(Connection connection) throws DBProblemException, InvalidDataException {
		for (SecondaryStorage j : Job.all(actor.account(), connection)) toShow.add(j.show());
	}
	
	protected void makeNew (Connection connection) throws InvalidDataException, DBProblemException {
		new Job(actor.account(), newData, connection);
		redirect("jobs.jsf");
	}
	
	public void findCandidates(String id) {
		getSessionMap().put("jobID", new Integer(id));
		redirect("ranking.jsf");
	}
	
	public void revoke(String id) {
		try (Connection connection = getConnection()) {
			new Job(Integer.parseInt(id), connection).proposeTo(null, connection);
			redirect("jobs.jsf");
		} catch (InvalidDataException | DBProblemException | SQLException e) {
			actor.handleException(e, false);
		}
	}
	
	public void delete(String id) {
		try(Connection connection = getConnection()) {
			new Job(Integer.parseInt(id)).deleteJob(connection);
			redirect("jobs.jsf");
		} catch (DBProblemException | InvalidDataException | SQLException e) {
			actor.handleException(e, false);
		}
	}
	
//For dropdowns:
	public List<String> getSubjects (){
		return subjects;
	}

	public String getTitle() {
		return newData.containsKey("title") ? (String)newData.get("title") : "";
	}
	public void setTitle(String title) {
		newData.put("title", title);
	}
	public String getSubject() {
		return newData.containsKey("subject") ? (String)newData.get("subject") : "";
	}
	public void setSubject(String subject) {
		newData.put("subject", subject);
	}
	
	public String getDescription() {
		return newData.containsKey("description") ? (String)newData.get("description") : "";
	}
	public void setDescription(String description) {
		newData.put("description", description);
	}
	
	public float getRate() {
		return newData.containsKey("ratePerHour") ? (float)newData.get("ratePerHour") : 0;
	}
	public void setRate(float rate) {
		newData.put("ratePerHour", rate);
	}
}