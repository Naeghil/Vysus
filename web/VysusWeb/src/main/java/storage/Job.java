package storage;

import java.util.Map;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Job extends StorageAbstract{
//Object-specific variables

//Initialisation: constructors and variables setup
	public Job(Integer jobID) {
		data.put("id", jobID);
		setDBVariables();
	}
	public Job(Integer jobID, Connection connection) 
		throws DBProblemException, InvalidDataException {
		data.put("id", jobID);
		setDBVariables();
		if(connection!=null) retrieve(connection);
	}
	public Job(Map<String, Object> data, Connection connection) throws DBProblemException {
		this.data = data;
		setDBVariables();
		create(connection);
	}
	protected void setDBVariables() {
		keys = new ArrayList<String>(Arrays.asList(
			"accountID", "subject", "title", "description", "ratePerHour", "candidateID", "accepted"));
		create = "INSERT INTO JOB"
				+ "(accountID, subject, title, description, ratePerHour) "
				+ "VALUES(?, ?, ?, ?, ?)";
		retrieve = "SELECT * FROM Job WHERE jobID=?";
		delete = "DELETE FROM Job WHERE jobID=?";
	}
	protected String update(List<String> changed) {
		String upd = "UPDATE Job SET " + changed.get(0);
		for(int i=1; i<changed.size(); i++) upd += "=?, " + changed.get(i);
		upd += "=? WHERE jobID=?";
		return upd;
	}
	
	public static List<Integer> jobList(Connection con, String account) throws DBProblemException {
		List<Integer> list = new ArrayList<Integer>();
		try(PreparedStatement qualifications = con.prepareStatement("SELECT jobID FROM Job WHERE accountID=?");) {
			qualifications.setObject(1, account);
			try(ResultSet result = qualifications.executeQuery();){
				while(result.next()) list.add(result.getInt("jobID"));
			}
		} catch (SQLException e) { throw new DBProblemException(e); }
		return list;
	}
	public static List<Job> allJobs(String accountID, Connection connection) 
		throws DBProblemException, InvalidDataException {
		List<Job> allJobs = new ArrayList<Job>();
		for(Integer jobID : jobList(connection, accountID)) {
			allJobs.add(new Job(jobID, connection));
		}
		return allJobs;
	}

	public void deleteJob(Connection connection) throws DBProblemException, InvalidDataException {
		this.delete(connection);
	}
//Getters and show methods
	public Map<String, String> show(){
		Map<String, String> show = new HashMap<String, String>();
		show.put("id", ((Integer)data.get("id")).toString());
		show.put("subject", (String)data.get("subject"));
		show.put("title", (String)data.get("title"));
		show.put("descritpion", (String)data.get("description"));
		show.put("rate", Float.toString((float)data.get("ratePerHour")));
		show.put("candidate", (String)data.get("candidateID"));
		if((boolean)data.get("accepted")) show.put("accepted", "yes");
		else show.put("accepted", "no");
		
		return show;
	}
}