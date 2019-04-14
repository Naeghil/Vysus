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
		keys = new ArrayList<String>(Arrays.asList( //TODO: understand the calendar better
			"accountID", "subject", "title", "comment",
			"startDate", "endDate", "calendarID", "oneOff", 
			"ratePerHour", "candidateID", "accepted"));
		create = "INSERT INTO JOB"
				+ "(accountID, subject, title, comment, startDate, endDate, calendarID, oneOff, ratePerHour, candidateID, accepted) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		retrieve = "SELECT * FROM Job WHERE jobID=?";
		delete = "DELETE FROM Job WHERE jobID=?";
	}
	protected String update(List<String> changed) {
		String upd = "UPDATE Job SET " + changed.get(0);
		for(int i=1; i<changed.size(); i++) upd += "=?, " + changed.get(i);
		upd += "=? WHERE jobID=?";
		return upd;
	}
//Object-specific querying methods
//Public interfaces of protected methods
	//Add the all jobs and job list
//Getters and show methods

}