package util;

import java.util.HashMap;
import java.util.Map;

import util.APICalls;

public class Ranking {
//to store candidates ranking, make a hashmap with keys 20 to 1, then when all candidates have been ranked, take from key 20 etc
	public static void main(String[] args) {
		int experience = 33;
		String candidateName = "Miles Everett";
		String jobRequirement = "Computer Science";
		String candidateFullQual = "Computer Science";
		String type = "phd";
		String jobPostcode = "cm34rl";
		String candidatePostcode = "ab245dj";
		int maxDist = 100;
		float jobPay = (float)10.50;
		float candidateMinimum = (float)10.70;
		
		//Fake candidate
		Map<String,Object> candidate = new HashMap<String,Object>();
		candidate.put("name", candidateName);
		candidate.put("full", candidateFullQual);
		candidate.put("experience", experience);
		candidate.put("type", type);
		
		//returns true if they are willing to travel that far
		if (!distanceCheck(candidatePostcode,jobPostcode,maxDist)) {
			System.out.println("Candidate will not travel that far");
		} else if (jobPay < candidateMinimum) {
			System.out.println("Candidate is too good for this job");
		} else {
			rankCandidates(candidate,jobRequirement);
			/*float qualificationValue;
			if (((String)candidate.get("full")).matches(jobRequirement)) {
				qualificationValue = (float) 5;
			} else {
				qualificationValue = (float) 2.5;
			}*/
			//float teacherValue = (float) ((qualificationRelevancy(jobRequirement,((String)candidate.get("full")))) * (qualificationRanking((String)candidate.get("type"))));
			//float teacherExperience = (float) experienceRanking(experience);
			//float actualRanking = teacherValue + teacherExperience;
			//System.out.println("Candidates experience: " + experienceRanking(experience));
			//System.out.println("Candidates qualification: " + teacherValue);
			//System.out.println("Candidates qualification: " + qualificationValue * (qualificationRanking((String)candidate.get("type"))));
			//System.out.println("Total ranking: " + actualRanking);

		}
	}
	
	public static boolean distanceCheck(String candidatePostcode, String jobPostcode, int maxDist) {
		return APICalls.getDistance(candidatePostcode,jobPostcode,maxDist);
	}
	
	public static void rankCandidates(Map<String,Object> teacher, String jobRequirement) {
		//Can add to showMini
		//Map<String,String> users = new User(userID, connection).showMini();
		//Return list 1-20 of candidates 
		float teacherValue = (float) ((qualificationRelevancy(jobRequirement,((String)teacher.get("full")))) * (qualificationRanking((String)teacher.get("type"))));
		float teacherExperience = (float) experienceRanking((int)teacher.get("experience"));
		float actualRanking = teacherValue + teacherExperience;
		System.out.println(actualRanking);
	}
	
	
	public static float qualificationRelevancy(String qualification, String jobRequirement) {
		if (qualification.matches(jobRequirement)) {
			return (float) 5;
		} else {
			return (float) 2.5;
		}
	}
	
	public static double qualificationRanking(String level) {
		if (level.matches("undergraduate")) {
			return 1;
		} else if (level.matches("masters")) {
			return 1.5;
		} else if (level.matches("phd")) {
			return 2;
		}
		return 0;
	}
	
	
	public static int experienceRanking(int experience) {
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
		} else if (experience > 40) {
			return 10;
		}
		return 0;
	}
}
