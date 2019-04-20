package ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Used to store and manipulate candidate data
public class Candidate implements Comparable<Candidate> {
	public String userID;
	public String accountID;
	public String postcode;
	public float maxDistance;
	
	public float work = 0;
	public float academic = 0;
	
	public Candidate(String uID, String aID, String pCode, float mDis) {
		this.userID = uID;
		this.accountID = aID;
		this.postcode = pCode;
		this.maxDistance = mDis;
	}
	
	//Used to use List.contains, to avoid duplicates generated by the queries
	@Override
	public boolean equals(Object other) {
		if(other!=null && other instanceof Candidate) {
			Candidate o = (Candidate)other;
			if(o.userID!=null) return this.userID.equals(o.userID);
		}
		return false;
	}
	
	public Float getTotal() {
		return this.work+this.academic;
	}
	//Used to find the maximum value of a grade in a list of candidates
	public static float maxWork(List<Candidate> list) {
		List<Float> allWork = new ArrayList<Float>();
		for(Candidate c : list) allWork.add(c.work);
		return (Float)Collections.max(allWork);
	}
	public static float maxAcademic(List<Candidate> list) {
		List<Float> allAcademic = new ArrayList<Float>();
		for(Candidate c : list) allAcademic.add(c.academic);
		return (Float)Collections.max(allAcademic);
	}
	//Used to convert in a format that can be displayed
	public Map<String, String> toDisplay(){
		Map<String, String> disp = new HashMap<String, String>();
		disp.put("work", Float.toString(work));
		disp.put("academic", Float.toString(academic));
		disp.put("total", getTotal().toString());
		disp.put("userID", userID);
		return disp;
	}
	public static List<Map<String, String>> toDisplay(List<Candidate> list) {
		List<Map<String, String>> disp = new ArrayList<Map<String, String>>();
		for(Candidate c : list) disp.add(c.toDisplay());
		return disp;
	}
	//Used to use Collections.sort
	@Override
	public int compareTo(Candidate o) {
		if(o!=null) return getTotal().compareTo(o.getTotal());
		return 0;
	}
	//For testing purposes
	@Override
	public String toString() {
		String thisString = 
			"userID: "+userID+" accountID: "+accountID+" posctode: "+postcode+" maxDistance: "+maxDistance+
			" work: "+work+" academic: "+academic;
		return thisString;
	}
}