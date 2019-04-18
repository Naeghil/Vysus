package vysusWeb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import storage.*;
import util.Ranking;

@Named("ranking")
@ConversationScoped
public class RankingGet extends VysusBean implements Serializable {
	Integer jobID;
	Map<String, String> job = new HashMap<String, String>();
	List<Map<String, String>> candidates = new ArrayList<Map<String, String>>(); 
	
	@PostConstruct
	void onLoad() {
		if(getSessionMap().containsKey("jobID")) jobID = (Integer)getSessionMap().remove("jobID");
		else {
			redirect("profile.xhtml");
			message("No job", "No job selected");
			return;
		}
		try (Connection connection = getConnection()) {
			job = new Job(jobID, connection).show();
			//new ArrayList<Map<String, Object>>();
			List<Map<String, String>> gradedCandidates = new Ranking().rankingMain(job.get("subject"), Float.parseFloat(job.get("rate")), connection);
			System.out.println("Graded candidates: " + gradedCandidates);
			for(Map<String, String> cand : gradedCandidates) retrieveCandidateData(cand, connection);
			System.out.println("Candidates: " + candidates);

		} catch (InvalidDataException | DBProblemException | SQLException e) {
			actor.handleException(e, false);
		}
		
	}
	
	void retrieveCandidateData(Map<String, String> candidate, Connection connection) throws DBProblemException, InvalidDataException {
		Map<String, String> data = new User((String)candidate.get("userID"), connection).showMini();
		data.putAll(new Teacher(data.get("account"), connection).show());
		data.put("academic", (candidate.get("academic")));
		data.put("work", (candidate.get("work")));
		data.put("total", candidate.get("total"));
		candidates.add(data);
	}
	
	public void offer(String id) {
		try (Connection connection = getConnection()) {
			new Job(jobID, connection).proposeTo(id, connection);
			redirect("jobs.xhtml");
		} catch (InvalidDataException | DBProblemException | SQLException e) {
			actor.handleException(e, false);
		}
	}
	
	public Map<String, String> getJob(){
		return job;
	}
	public List<Map<String, String>> getCandidates(){
		return candidates;
	}
	
	
	
	

}