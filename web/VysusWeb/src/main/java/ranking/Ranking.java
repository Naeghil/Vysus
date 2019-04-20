package ranking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import exceptions.DBProblemException;
import util.APICalls;

public class Ranking {	
	public List<Map<String,String>> makeRanking(Map<String, Object> jobData, Connection connection)
	throws DBProblemException {
		List<Candidate> candidates;
		
		//Filter by job
		candidates = jobFilter((String)jobData.get("subject"), (float)jobData.get("rate"), connection);
		//Filter by distance
		candidates = distanceFilter((String)jobData.get("postcode"), candidates);
		//Give the scores to candidates
		candidates = giveScores((String)jobData.get("subject"), candidates, connection);
		//Normalise the scores into grades out of 10
		candidates = normaliseScores(candidates);
		//Rank by total grade
		Collections.sort(candidates, Collections.reverseOrder());
		
		//Make a map for display purposes
		return Candidate.toDisplay(candidates);
	}

//Filters: manipulate candidate data
	//Normalise scores of the candidates depending on the best scores
	protected List<Candidate> normaliseScores(List<Candidate> list) {
		float maxWork = Candidate.maxWork(list);
		float maxAcademic = Candidate.maxAcademic(list);
		for(Candidate c : list) {
			c.work = (c.work/maxWork)*10;
			c.academic = (c.academic/maxAcademic)*10;
		}
		return list;
	}
	//Gives scores to candidates depending on their qualifications
	protected List<Candidate> giveScores(String subject, List<Candidate> list, Connection connection)
		throws DBProblemException {
		for (Candidate current : list) {
			for(Qualification qualification : findQualifications(subject, current.accountID, connection)) {
				//The methods used are safe for Qualification.type;
				//in the future some qualifications may count as both
				current.work += qualification.addWorkYears(subject);
				current.academic += qualification.addAcademic(subject);
			}
		}
		return list;
	}

//Filters: reduce the number of candidates based on relevant information	
	//Only retains candidates if they are within distance
	protected List<Candidate> distanceFilter(String schoolPostcode, List<Candidate> list) throws DBProblemException {
		List<Candidate> filtered = new ArrayList<Candidate>();
		for(Candidate c : list) if(distanceCheck(c.postcode, schoolPostcode, c.maxDistance)) filtered.add(c); 
		return filtered;
	}
	
	//The first filter actually generates the data
	protected List<Candidate> jobFilter(String subject, float rate, Connection connection) throws DBProblemException {
		List<Candidate> candidates = new ArrayList<Candidate>();
		try(PreparedStatement jobFilter = connection.prepareStatement(candidatesQuery)){
			for(int i=1; i<5; i++) jobFilter.setString(i, subject);
			jobFilter.setFloat(5, rate);
			try(ResultSet rs = jobFilter.executeQuery()){
				while(rs.next()) {
					Candidate newC = new Candidate( 
						rs.getString("user"), rs.getString("account"), 
						rs.getString("postcode"), rs.getFloat("maxDist"));
					if(!candidates.contains(newC)) candidates.add(newC);
				}
			}
		} catch (SQLException e) { throw new DBProblemException(e); }	
		return candidates;
	}
//Support methods: used in the filters
	//Retrieves qualifications for ranking purposes
	public List<Qualification> findQualifications(String subject, String accountID, Connection connection) 
		throws DBProblemException {
		List<Qualification> qualifications = new ArrayList<Qualification>();
		try(PreparedStatement findQualificationType = connection.prepareStatement(retrieveQualifications)){
			findQualificationType.setString(1, accountID);
			for(int i = 2; i<6; i++) findQualificationType.setString(i, subject);
			try(ResultSet rs = findQualificationType.executeQuery()){
				while(rs.next()) {
					qualifications.add(new Qualification(
						rs.getString("level"), rs.getString("mainSubj"),
						//Using getString for a date relies on the JDBC implementation:
						rs.getString("startDate"),rs.getString("endDate")));
				}
			}
		} catch (SQLException e) { throw new DBProblemException(e); }
		return qualifications;		
	}
	
	//Wraps api distance check
	public static boolean distanceCheck(String candidatePostcode, String jobPostcode, float maxDist) {
		return APICalls.checkDistance(candidatePostcode,jobPostcode,maxDist);
	}
	
//Queries:
	//Used to initially filter and retrieve candidates
	protected static String candidatesQuery = "SELECT "
	+ "User.userID as user, User.accountID as account, User.postcode as postcode, Teacher.maxDistance as maxDist "
	+ "FROM User INNER JOIN Teacher ON User.accountID=Teacher.accountID "
			  + "INNER JOIN Qualification ON Teacher.accountID=Qualification.accountID "
	+ "WHERE Qualification.subj1=? OR Qualification.subj2=? OR Qualification.subj3=? "
	+ "OR Qualification.mainSubj=? AND Teacher.minRatePerHour<=?";
	
	protected static String retrieveQualifications = "SELECT * FROM Qualification "
		  + "WHERE accountID=? AND (mainSubj=? OR subj1=? OR subj2=? OR subj3=?)";
}
