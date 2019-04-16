package storage;

/*************************************************
 * 					Qualification				 *
 * These are associated to Teacher objects but 	 *
 * have no nested data.							 *
 * The corresponding table has auto-increment id *
 ************************************************/

import java.util.Map;

import util.Conv;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Qualification extends StorageAbstract{
	// https://www.gov.uk/what-different-qualification-levels-mean/list-of-qualification-levels
	// according to ucas there's 19839 undergraduate courses in the UK
	// KISCourse.KISAIM valid entry --> basically level
//Object-specific variables
	protected Boolean verified = null; //Defaults to false in DB
	
//Initialisation: constructors and variables setup
	//Existing constructor, also enabling loading, if given a connection
	public Qualification(int qualificationID, Connection connection) throws InvalidDataException, DBProblemException {
		data.put("id", qualificationID);
		setDBVariables();
		retrieve(connection);
		verified = isVerified(connection);
		
	}
	//Creating constructor: no qualification id will be available this way
	public Qualification(Map<String, Object> data, Connection connection) throws DBProblemException {
		this.data = data;
		System.out.println(data);
		verified = false;
		setDBVariables();
		create(connection);
	}
	public void setDBVariables() {
		keys = new ArrayList<String>(Arrays.asList(
			"title", "startDate", "endDate", "comment", "institution", "level", "institutionEmail", "institutionPhoneNo", "referee", "mainSubj", "subj1", "subj2", "subj3"));
		delete = "DELETE FROM Qualification WHERE qualificationID=?";
		retrieve = "SELECT * FROM Qualification WHERE qualificationID=?";
		create = "INSERT INTO Qualification"
				+ "(accountID, title, startDate, endDate, comment, institution, level, institutionEmail, institutionPhoneNo, referee, mainSubj, subj1, subj2, subj3) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	}
	protected String update(List<String> changes) {
		String upd = "UPDATE Qualification SET " + changes.get(0);
		for(int i=1; i<changes.size(); i++) upd += "=?, " + changes.get(i);
		upd += "=? WHERE qualificationID=?";
		return upd;
	}
	
	public void deleteQualification(Connection connection) throws DBProblemException, InvalidDataException{
		this.delete(connection);
	}
	
//Object-specific querying methods
	//Used to set the verified byte to true
	public void verify(Connection connection) throws DBProblemException, InvalidDataException {
		try(PreparedStatement verify = connection.prepareStatement("UPDATE Qualification SET verified=? WHERE qualificationID=?");) {
			verified=true;
			verify.setBoolean(1, verified);
			verify.setObject(2, data.get("id"));
			if(verify.executeUpdate() != 1) throw new InvalidDataException(null, "Record not found");
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	
	public boolean isVerified(Connection connection) throws InvalidDataException, DBProblemException { 
		if(verified!=null) return verified;
		try(PreparedStatement veri = connection.prepareStatement("SELECT verified FROM Qualification WHERE qualificationID=?")) {
			veri.setObject(1, data.get("id"));
			try(ResultSet record = veri.executeQuery()) {
				if(record.next()) return record.getBoolean("verified");
				else throw new InvalidDataException(null, "Record not found");
			}
		} catch (SQLException e ) {throw new DBProblemException(e); }
	}
	
	public static List<Integer> qualificationList(Connection con, String account) throws DBProblemException {
		List<Integer> list = new ArrayList<Integer>();
		try(PreparedStatement qualifications = con.prepareStatement("SELECT qualificationID FROM Qualification WHERE accountID=?");) {
			qualifications.setObject(1, account);
			try(ResultSet result = qualifications.executeQuery();){
				while(result.next()) list.add(result.getInt("qualificationID"));
			}
		} catch (SQLException e) { throw new DBProblemException(e); }
		return list;
	}
	public static List<Qualification> allQualifications(String accountID, Connection connection) 
		throws DBProblemException, InvalidDataException {
		List<Qualification> allQualifications = new ArrayList<Qualification>();
		for(Integer qualificationID : qualificationList(connection, accountID)) {
			allQualifications.add(new Qualification(qualificationID, connection));
		}
		return allQualifications;
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