package storage;

/*************************************************
 * 					Qualification				 *
 * These are associated to Teachers 			 *
 * The corresponding table has auto-increment id *
 ************************************************/

import java.util.Map;

import util.Conv;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import exceptions.*;

public class Qualification extends SecondaryStorage {
//Object-specific variables
	protected Boolean verified = false; //Defaults to false in DB
	
//Initialisation: constructors and variables setup
	public Qualification(Object id) { super(id); }
	public Qualification(Object id, Connection connection) throws InvalidDataException, DBProblemException {
		super(id, connection);
		if (connection!=null) verified = isVerified(connection);
	}
	public Qualification(String account, Map<String, Object> data, Connection connection) 
		throws DBProblemException {
		super(account, data, connection);
	}
	public void setDBVariables() {
		keys = new ArrayList<String>(Arrays.asList(
			"title", "startDate", "endDate", "comment", "institution", 
			"level", "institutionEmail", "institutionPhoneNo", "referee", 
			"mainSubj", "subj1", "subj2", "subj3"));
		delete = "DELETE FROM Qualification WHERE qualificationID=?";
		retrieve = "SELECT * FROM Qualification WHERE qualificationID=?";
		create = "INSERT INTO Qualification"
				+ "(accountID, title, startDate, endDate, comment, institution, "
				+ "level, institutionEmail, institutionPhoneNo, referee, "
				+ "mainSubj, subj1, subj2, subj3) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	}
	protected String update(List<String> changes) {
		String upd = "UPDATE Qualification SET " + changes.get(0);
		for(int i=1; i<changes.size(); i++) upd += "=?, " + changes.get(i);
		upd += "=? WHERE qualificationID=?";
		return upd;
	}
	
//Object-specific querying methods
	//Used to set verified to true
	public void verify(Connection connection) throws DBProblemException, InvalidDataException {
		try(PreparedStatement verify = connection.prepareStatement(
			"UPDATE Qualification SET verified=? WHERE qualificationID=?");) {
			verified=true;
			verify.setBoolean(1, verified);
			verify.setObject(2, data.get("id"));
			if(verify.executeUpdate() != 1) throw new InvalidDataException("Record not found");
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	//Used to check if the qualification is verified
	protected boolean isVerified(Connection connection) 
		throws InvalidDataException, DBProblemException { 
		try(PreparedStatement veri = connection.prepareStatement(
			"SELECT verified FROM Qualification WHERE qualificationID=?")) {
			veri.setObject(1, data.get("id"));
			try(ResultSet record = veri.executeQuery()) {
				if(record.next()) return record.getBoolean("verified");
				else throw new InvalidDataException("Record not found");
			}
		} catch (SQLException e ) {throw new DBProblemException(e); }
	}
	//From SecondaryStorage
	public static List<SecondaryStorage> all(Object id, Connection connection) 
		throws DBProblemException, InvalidDataException {
		return all("storage.Qualification", id, connection, "qualificationID", "Qualification", "accountID");
	}

//Getters and show methods
	public Map<String, String> show() {
		Map<String, String> show = new HashMap<String, String>();
		show.put("id", ((Integer)data.get("id")).toString());
		show.put("title", (String)data.get("title"));
		show.put("from", Conv.dateToString((Date)data.get("startDate")));
		show.put("to", Conv.dateToString((Date)data.get("endDate")));
		show.put("comment", (String)data.get("comment"));
		show.put("where", (String)data.get("institution"));
		show.put("type", (String)data.get("level"));
		show.put("email", (String)data.get("institutionEmail"));
		show.put("phoneNo", (String)data.get("institutionPhoneNo"));
		show.put("referee", (String)data.get("referee"));
		show.put("mainSubj", (String)data.get("mainSubj"));
		show.put("subj1", (String)data.get("subj1"));
		show.put("subj2", (String)data.get("subj2"));
		show.put("subj3", (String)data.get("subj3"));
		if(verified) show.put("verified", "yes");
		else show.put("verified", "no");
			
		return show;
	}
}