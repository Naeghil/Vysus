package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exceptions.DBProblemException;
import util.APICalls;

public class Ranking {
//to store candidates ranking, make a hashmap with keys 20 to 1, then when all candidates have been ranked, take from key 20 etc
	public static void main(String[] args) {
		}
	
	
	
	public List<Map<String,String>> rankingMain(String subject, float rate, Connection connection) {
		Map<String,String> alreadyTested = new HashMap<String,String>();
		Map<Float,List<String>> allRankings = new HashMap<Float,List<String>>();
		Map<String, Map<String,String>> rankingBreakdowns = new HashMap<String, Map<String,String>>();
		try {
			List<Candidate> candidates = jobFilter(subject, rate, connection);
			
			for (int i = 0; i < candidates.size(); i++) {
				Candidate current = (candidates.get(i));
				if (alreadyTested.containsKey(current.accountID)) {
					continue;
				} 
				float teacherExperience = 0;
				float teacherValue = 0;
				List<Qualification> qualifications = findQualification(subject, current.accountID, connection);
				Map<String,String> rankingBreakdown = new HashMap<String,String>();
				for (int j = 0; j < qualifications.size(); j++) {
					Qualification qualification = (qualifications.get(j));
					
					if (qualification.type.matches("Work experience")) {
						teacherExperience = (float) experienceRanking(qualification.startDate,qualification.endDate);
						rankingBreakdown.put("work", Float.toString(teacherExperience));
					} else {
						teacherValue = (float) ((qualificationRelevancy(current.fromMain) * (qualificationRanking(qualification.type))));
						rankingBreakdown.put("academic", Float.toString(teacherValue));
					}
				}

				float actualRanking = teacherValue + teacherExperience;
				rankingBreakdown.put("total", Float.toString(actualRanking));
				rankingBreakdowns.put(current.accountID, rankingBreakdown);
				alreadyTested.put(current.accountID,"yes");
				
				if (allRankings.containsKey(actualRanking)) {
					List<String> groupedCandidates = allRankings.remove(actualRanking);
					groupedCandidates.add(current.accountID);
					allRankings.put(actualRanking, groupedCandidates);
				} else {
					List<String> newGroupedCandidates = new ArrayList<String>();
					newGroupedCandidates.add(current.accountID);
					allRankings.put(actualRanking, newGroupedCandidates);
				}
				//allRankings.put(current.accountID,actualRanking);
				}
		List<Float> keys = asSortedList(allRankings.keySet());
		List<Map<String,String>> finalRanking = new ArrayList<Map<String,String>>();
		for (int i = 0; i < keys.size(); i++) {
			List<String> people = allRankings.get(keys.get(i));
			for (int j = 0; j < people.size(); j++) {
				Map<String,String> userData = new HashMap<String,String>();
				userData.put("userID", findActor(people.get(j),connection));
				userData.put("academic", rankingBreakdowns.get(people.get(j)).get("academic"));
				userData.put("work", rankingBreakdowns.get(people.get(j)).get("work"));
				userData.put("total", rankingBreakdowns.get(people.get(j)).get("total"));
				finalRanking.add(userData);
			}
		}
		return finalRanking;

			
		} catch (DBProblemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
	  List<T> list = new ArrayList<T>(c);
	  java.util.Collections.sort(list);
	  Collections.reverse(list);
	  return list;
	}
	
	public static boolean distanceCheck(String candidatePostcode, String jobPostcode, int maxDist) {
		return APICalls.checkDistance(candidatePostcode,jobPostcode,maxDist);
	}
	
	public static float qualificationRelevancy(boolean result) {
		if (result = true) {
			return (float) 5;
		} else {
			return (float) 2.5;
		}
	}
	
	public static double qualificationRanking(String level) {
		if (level.matches("Undergraduate")) {
			return 1;
		} else if (level.matches("Masters")) {
			return 1.5;
		} else if (level.matches("PHD")) {
			return 2;
		}
		return 0;
	}
	
	
	public static int experienceRanking(String start, String end) {
		//System.out.println("Start pre: " + start);
		//System.out.println("End pre: " + end);
		start = start.substring(0, 4);
		end = end.substring(0, 4);
		//System.out.println("Start post: " + start);
		//System.out.println("End post: " + end);
		int experience = (Integer.parseInt(end) - Integer.parseInt(start));
		//System.out.println("Experience: " + experience);
		if (experience < 1) {
			return 1;			
		} else if (experience < 3) {
			return 2;
		} else if (experience < 5) {
			return 3;
		} else if (experience < 7) {
			return 4;
		} else if (experience < 9) {
			return 5;
		} else if (experience < 15) {
			return 6;
		} else if (experience < 20) {
			return 7;
		} else if (experience < 30) {
			return 8;
		} else if (experience < 40) {
			return 9;
		} else if (experience >= 40) {
			return 10;
		}
		return 0;
	}
	
	
	
	
	public List<Candidate> distanceFilter(String schoolPostcode, String subject, float rate, Connection connection) throws DBProblemException{
		List<Candidate> filtered = new ArrayList<Candidate>();
		for(Candidate c : jobFilter(subject, rate, connection)) {
			if(APICalls.checkDistance(schoolPostcode, c.postcode, c.maxDistance)) filtered.add(c); 
		}
		return filtered;
	}
	

	
	
	
	
	public List<Qualification> findQualification(String subject, String accountID, Connection connection) throws DBProblemException {
		List<Qualification> qualifications = new ArrayList<Qualification>();
		//System.out.println("FilterSecondary");
		try(PreparedStatement findQualificationType = connection.prepareStatement(
		  "SELECT * FROM Qualification WHERE accountID=? AND mainSubj=?")){
			findQualificationType.setString(1, accountID);
			findQualificationType.setString(2, subject);
			
			
			try(ResultSet rs = findQualificationType.executeQuery()){
				while(rs.next()) {
					qualifications.add(new Qualification(rs.getString("level"),rs.getString("startDate"),rs.getString("endDate")));
				}
			}
		} catch (SQLException e) { 
			System.out.println(e.getMessage().toString());
			throw new DBProblemException(e); 
		}
			
		return qualifications;		
	}

	public String findActor(String accountID, Connection connection) throws DBProblemException {
		String actorID = null;
		try(PreparedStatement findQualificationType = connection.prepareStatement(
		  "SELECT userID FROM User WHERE accountID=?")){
			findQualificationType.setString(1, accountID);
			
			
			try(ResultSet rs = findQualificationType.executeQuery()){
				while(rs.next()) {
					actorID = (rs.getString("userID"));
				}
			return actorID;
			}
			
		} catch (SQLException e) { 
			System.out.println(e.getMessage().toString());
			throw new DBProblemException(e); 
		}
					
	}
//Filters: reduce the number of candidates based on relevant information
	
	
	
	//The firts filter actually generates the data
	public List<Candidate> jobFilter(String subject, float rate, Connection connection) throws DBProblemException {
		List<Candidate> candidates = new ArrayList<Candidate>(checkMain(subject, rate, connection));
		candidates.addAll(checkSecondary(subject, rate, connection));
		System.out.print("Ranking.jobFilter: ");
		for(Candidate c : candidates) System.out.println(c.userID+" ");
		System.out.println();
		return candidates;
	}
//Checks: used in the filters
	
	
	/** These two checks initially retrieve candidate data, depending on the job info (pay and subject)
	 *  The two resulting lists differ for the "grading" they are going to receive **/
	//Uses main subject for qualifications
	public List<Candidate> checkMain(String subject, float rate, Connection connection) 
		throws DBProblemException {
		List<Candidate> firstFilter = new ArrayList<Candidate>();
		try(PreparedStatement jobFilter = connection.prepareStatement(
			"SELECT User.userID as user, User.accountID as account, User.postcode as postcode, "
		  + "Teacher.maxDistance as maxDist "
		  + "FROM User INNER JOIN Teacher ON User.accountID=Teacher.accountID "
		  + "INNER JOIN Qualification ON Teacher.accountID=Qualification.accountID "
		  + "WHERE Qualification.mainSubj=? AND Teacher.minRatePerHour<=?")){
			jobFilter.setString(1, subject);
			jobFilter.setFloat(2, rate);
			try(ResultSet rs = jobFilter.executeQuery()){
				while(rs.next()) {
					Candidate newC = new Candidate( 
						rs.getString("user"), rs.getString("account"), 
						rs.getString("postcode"), rs.getFloat("maxDist"), true);
					if(!firstFilter.contains(newC)) firstFilter.add(newC);
				}
			}
		} catch (SQLException e) { throw new DBProblemException(e); }
		return firstFilter;		
	}
	//Uses secondary subjects for qualifications
	public List<Candidate> checkSecondary(String subject, float rate, Connection connection) 
		throws DBProblemException {
		List<Candidate> firstFilter = new ArrayList<Candidate>();
		try(PreparedStatement jobFilter = connection.prepareStatement(
			"SELECT User.userID as user, User.accountID as account, User.postcode as postcode, "
		  + "Teacher.maxDistance as maxDist "
		  + "FROM User INNER JOIN Teacher ON User.accountID=Teacher.accountID "
		  + "INNER JOIN Qualification ON Teacher.accountID=Qualification.accountID "
		  + "WHERE Qualification.subj1=? OR Qualification.subj2=? OR Qualification.subj3=? "
		  + "AND Qualification.mainSubj!=? AND Teacher.minRatePerHour<=?")){
			for(int i=1; i<5; i++) jobFilter.setString(i, subject);
			jobFilter.setFloat(5, rate);
			try(ResultSet rs = jobFilter.executeQuery()){
				while(rs.next()) {
					Candidate newC = new Candidate(
						rs.getString("user"), rs.getString("account"), 
						rs.getString("postcode"), rs.getFloat("maxDist"), false);
					if(!firstFilter.contains(newC)) firstFilter.add(newC);
				}	
			}
		} catch (SQLException e) { throw new DBProblemException(e); }
		return firstFilter;		
	}
}


//These classes help us temporarily store candidate and qualification data:
class Candidate {
	public String userID;
	public String accountID;
	public String postcode;
	public float maxDistance;
	public boolean fromMain;
	
	public Candidate(String uID, String aID, String pCode, float mDis, boolean main) {
		this.userID = uID;
		this.accountID = aID;
		this.postcode = pCode;
		this.maxDistance = mDis;
		this.fromMain = main;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other!=null && other instanceof Candidate) {
			Candidate o = (Candidate)other;
			if(o.userID!=null) return this.userID.equals(o.userID);
		}
		return false;
	}
	
	
}

class Qualification {
	public String type;
	public String startDate;
	public String endDate;
	
	public Qualification(String initType, String startDate, String endDate) {
		this.type = initType;
		this.startDate = startDate;
		this.endDate = endDate;
	}
}