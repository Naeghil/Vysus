package vysusWeb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import exceptions.*;
import storage.*;

/*******************************************
 * 				   Ranking	  			   *
 * SecondaryBean using the ranking package *
 * to generate and display a ranking of	   *
 * candidates for a specific job, allowing *
 * a staff user to offer the job to them   *
 ******************************************/

@Named("ranking")
@ConversationScoped
public class Ranking extends vysusWeb.bases.SecondaryBean implements Serializable {
	Integer jobID;
	Map<String, String> job = new HashMap<String, String>();
	
	public void onLoad() {
		if(getSessionMap().containsKey("jobID")) jobID = (Integer)getSessionMap().get("jobID");
		else {
			redirect("profile.jsf");
			message("No job", "No job selected");
			return;
		}
		onLoad("no");
	} 
	protected void loadData(Connection connection) throws DBProblemException, InvalidDataException {
		Job j = new Job(jobID, connection);
		job = j.show();
		Map<String, Object> filterData = j.getData();
		filterData.put("postcode", actor.accountField("postcode"));
		for(Map<String, String> cand : new ranking.Ranking().makeRanking(filterData, connection)) {
			toShow.add(candidateData(cand, connection));
		}
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
			getSessionMap().remove("jobID");
			redirect("jobs.jsf");
		} catch (InvalidDataException | DBProblemException | SQLException e) {
			actor.handleException(e, false);
		}
	}
	
	public Map<String, String> getJob(){
		return job;
	}

	//Doesn't make nor deletes anything
	protected void makeNew(Connection connection) throws InvalidDataException, DBProblemException {	}
	public void delete(String id) { }
}