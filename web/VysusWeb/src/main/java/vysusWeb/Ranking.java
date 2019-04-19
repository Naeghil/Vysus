package vysusWeb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import exceptions.*;
import storage.*;

@Named("ranking")
@ConversationScoped
public class Ranking extends vysusWeb.bases.SecondaryBean implements Serializable {
	Integer jobID;
	Map<String, String> job = new HashMap<String, String>();
	
	public void onLoad() {
		if(jobID==null) {
			redirect("profile.jsf");
			message("No job", "No job selected");
			return;
		}
		onLoad("");
	} 
	protected void loadData(Connection connection) throws DBProblemException, InvalidDataException {
		System.out.println("Ranking.loadData");
		job = new Job(jobID, connection).show();
		if(!job.get("schoolID").equals(actor.account())) {
			redirect("profile.jsf");
			message("This job is not yours", "Not your job");
			return;
		}
		List<Map<String, String>> gradedCandidates = 
			new util.Ranking().rankingMain(job.get("subject"), Float.parseFloat(job.get("rate")), connection);
		for(Map<String, String> cand : gradedCandidates) toShow.add(candidateData(cand, connection));
	}
	
	protected Map<String, String> candidateData(Map<String, String> candidate, Connection connection) throws DBProblemException, InvalidDataException {
		Map<String, String> data = new User((String)candidate.get("userID"), connection).showMini();
		data.putAll(new Teacher(data.get("account"), connection).show());
		data.put("academic", (candidate.get("academic")));
		data.put("work", (candidate.get("work")));
		data.put("total", candidate.get("total"));
		return data;
	}
	
	public void offer(String id) {
		System.out.println("Got here");
		try (Connection connection = getConnection()) {
			System.out.println("RankingGet.offer.id: "+id);
			new Job(jobID, connection).proposeTo(id, connection);
			redirect("jobs.jsf");
		} catch (InvalidDataException | DBProblemException | SQLException e) {
			actor.handleException(e, false);
		}
	}
	
	public Map<String, String> getJob(){
		return job;
	}
	public Integer getjobID() {
		return jobID;
	}
	public void setjobID(Integer id) {
		jobID = id;
	}

	//Doesn't make nor deletes anything
	protected void makeNew(Connection connection) throws InvalidDataException, DBProblemException {	}
	public void delete(String id) { }
	
	
	
	

}