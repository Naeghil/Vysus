package storage;

import java.util.Map;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import exceptions.*;

public class Job extends SecondaryStorage {
//Object-specific variables

//Initialisation: constructors and variables setup
	public Job(Object id) { super(id); }
	public Job(Object id, Connection connection) throws DBProblemException, InvalidDataException {
		super(id, connection);
		if(connection!=null) {
			String candidate = candidate(connection);
			if(candidate !=null) data.put("candidateID", candidate);
			data.put("accepted", accepted(connection));
		}
	}
	public Job(String account, Map<String, Object> data, Connection connection) throws DBProblemException {
		super(account, data, connection);
	}
	protected void setDBVariables() {
		keys = new ArrayList<String>(Arrays.asList("subject", "title", "description", "ratePerHour"));
		create = "INSERT INTO Job(accountID, subject, title, description, ratePerHour) VALUES(?, ?, ?, ?, ?)";
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
	//Special retrieve to also get the accountID
	protected void retrieve(Connection con) throws DBProblemException, InvalidDataException {
		keys.add("accountID");
		super.retrieve(con);
		keys.remove("accountID");
	}
	//Making an offer to 'candidate'
	public void proposeTo(String candidate, Connection connection) throws DBProblemException, InvalidDataException {
		try(PreparedStatement propose = connection.prepareStatement("UPDATE Job SET candidateID=? WHERE jobID=?");) {
			propose.setString(1, candidate);
			propose.setObject(2, data.get("id"));
			if(propose.executeUpdate() != 1) throw new InvalidDataException("Record not found");
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	//Retrieve the candidate
	public String candidate(Connection connection) throws DBProblemException, InvalidDataException{
		try(PreparedStatement candidate = connection.prepareStatement("SELECT candidateID FROM Job WHERE jobID=?")) {
			candidate.setObject(1, data.get("id"));
			try(ResultSet record = candidate.executeQuery()) {
				if(record.next()) {
					return record.getString("candidateID");
				} else throw new InvalidDataException("Record not found");
			}
		} catch (SQLException e ) {throw new DBProblemException(e); }
	}
	//Method allowing a candidate to accept a job
	public void accept(Connection connection) throws DBProblemException, InvalidDataException {
		try(PreparedStatement accept = connection.prepareStatement("UPDATE Job SET accepted=? WHERE jobID=?");) {
			accept.setBoolean(1, true);
			accept.setObject(2, data.get("id"));
			if(accept.executeUpdate() != 1) throw new InvalidDataException("Record not found");
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	//Retrieves the accepted attribute
	public boolean accepted(Connection connection) throws DBProblemException, InvalidDataException{
		try(PreparedStatement accepted = connection.prepareStatement("SELECT accepted FROM Job WHERE jobID=?")) {
			accepted.setObject(1, data.get("id"));
			try(ResultSet record = accepted.executeQuery()) {
				if(record.next()) {
					return record.getBoolean("accepted");
				} else throw new InvalidDataException("Record not found");
			}
		} catch (SQLException e ) {throw new DBProblemException(e); }
	}
	//From SecondaryStorage
	public static List<SecondaryStorage> all(Object id, Connection connection) throws DBProblemException, InvalidDataException {
		return all("storage.Job", id, connection, "jobID", "Job", "accountID");
	}
	//Special for Job to retrieve all offers to a certain teacher
	public static List<SecondaryStorage> offers(Object id, Connection connection) throws DBProblemException, InvalidDataException {
		return all("storage.Job", id, connection, "jobID", "Job", "candidateID");
	}
	
//Getters and show methods
	public Map<String, String> show(){
		Map<String, String> show = new HashMap<String, String>();
		show.put("id", ((Integer)data.get("id")).toString());
		show.put("schoolID", (String)data.get("accountID"));
		show.put("subject", (String)data.get("subject"));
		show.put("title", (String)data.get("title"));
		show.put("description", (String)data.get("description"));
		show.put("rate", Float.toString((float)data.get("ratePerHour")));
		if(data.containsKey("candidateID")) show.put("candidate", (String)data.get("candidateID"));
		else show.put("candidate", "");
		if((boolean)data.get("accepted")) show.put("accepted", "yes");
		else show.put("accepted", "no");
		return show;
	}
	//Special for ranking purposes
	public Map<String, Object> getData() {
		return new HashMap<String, Object>(data);
	}
}