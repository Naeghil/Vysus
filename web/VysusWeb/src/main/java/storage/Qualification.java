package storage;

/*************************************************
 * 					Qualification				 *
 * These are associated to Teacher objects but 	 *
 * have no nested data.							 *
 * The corresponding table has auto-increment id *
 ************************************************/

import java.util.Map;

import util.DataConv;

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
	protected Boolean verified = null; //TODO: in db must default to false;
	
	//Object-specific queries:
	
//Initialisation: constructors and variables setup
	//Existing constructor, also enabling loading, if given a connection
	public Qualification(Integer qualificationID, Connection connection) throws InvalidDataException, DBProblemException {
		data.put("id", qualificationID);
		setDBVariables();
		if(connection!=null) retrieve(connection);
		
	}
	//Creating constructor: no qualification id will be available this way
	public Qualification(Connection connection, Map<String, Object> data) throws DBProblemException {
		this.data = data;
		setDBVariables();
		create(connection);
	}
	public void setDBVariables() {
		keys = new ArrayList<String>(Arrays.asList( //TODO: should this include the qualificationID?
			"accountID", "title", "startDate", "endDate", "comment", "finalGrade", "institution", "level", "institutionEmail", "institutionPhoneNo", "referee"));
		delete = "DELETE FROM Qualification WHERE qualificationID=?";
		retrieve = "SELECT * FROM Qualification WHERE qualificationID=?";
		create = "INSERT INTO Qualification"
				+ "(accountID, title, startDate, endDate, comment, finalGrade, institution, level, institutionEmail, institutionPhoneNo, referee) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	}
	protected String update(List<String> changes) {
		String upd = "UPDATE Qualification SET " + changes.get(0);
		for(int i=1; i<changes.size(); i++) upd += "=?, " + changes.get(i);
		upd += "=? WHERE qualificationID=?";
		return upd;
	}
	
//Object-specific querying methods
	//Used to set the verified byte to true
	public void verify(Connection connection) throws DBProblemException, InvalidDataException {
		try(PreparedStatement verify = connection.prepareStatement("UPDATE Qualification SET verified=? WHERE qualificationID=?");) {
			verified=true;
			verify.setBoolean(1, verified);
			verify.setObject(2, data.get("id")); //TODO:This may return null;
			if(verify.executeUpdate() != 1) throw InvalidDataException.invalidQualification();
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	public boolean isVerified(Connection connection) throws InvalidDataException, DBProblemException { 
		if(verified!=null) return verified;
		try(PreparedStatement veri = connection.prepareStatement("SELECT verified FROM Qualification WHERE qualificationID=?")) {
			veri.setObject(1, data.get("id")); //TODO:This can return null;
			try(ResultSet record = veri.executeQuery()) {
				if(record.next()) return record.getBoolean("verified");
				else throw InvalidDataException.invalidQualification();
			}
		} catch (SQLException e ) {throw new DBProblemException(e); }
	}
	//TODO: list string or list int?
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
//Public interfaces of protected methods
	//For now this is to enact changes to the qualifications
	/**public void updateQualification(Map<String, String> changes, Connection con) throws InvalidDataException, DBProblemException {
		this.changes = changes;
		update(con);
	} **/
//Getters and show methods		
	public Map<String, Object> showMini() {
		return null;
	}
	public Map<String, Object> show() {
		return null;
	}
	public Map<String, Object> showFull() {
		Map<String, Object> show = new HashMap<String, Object>();
		show.put("data", DataConv.makeStringMap(data));
		show.put("verified", verified);
		return show;
	}
}