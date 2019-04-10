package storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.List;
import java.sql.*;

public class Teacher extends Account{
//Object-specific variables
//Initialisation: constructors and variables setup
	public Teacher(String accountID) {
		//TODO: this
	}
	public Teacher(String accountID, Map<String, String> accountData, Map<String, Object> additionalData) {
		//TODO: this
	}
	
//Object-specific querying methods
//Public interfaces of protected methods
//Getters and show methods
	protected static List<String> keys = new ArrayList<String>(Arrays.asList(
			"gender", "maxDistance", "minRatePerHour", "aboutMe"));
	//Queries
	protected String retrieveTeacher = "SELECT * FROM Teacher WHERE accountID=?";
	protected String createTeacher = "INSERT INTO Teacher(accountID, gender, maxDistance, minRatePerHour, aboutMe) VALUES(?, ?, ?, ?, ?)";

	//TODO: protected Calendar calendar;	
	
	//Constructor: Map<String, Object> additionalData
	
	//These queries are for qualifications actually
	//protected String createTeacher = "INSERT INTO Teacher(qualificationID, accountID, title, startDate, endDate, comment, finalGrade, institution, institutionEmail, level) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	//protected ArrayList<String> keys = new ArrayList<String>(Arrays.asList(
			//"title", "startDate", "endDate", "comment", "finalGrade", "institution", "institutionEmail", "level"));
	//Abstract methods
	protected void create(Connection con) throws StorageException, DBProblemException {
		try(PreparedStatement insert = con.prepareStatement(createTeacher);) {
			insert.setString(1, id.get("account"));
			for(int i=0; i<keys.size(); i++) insert.setString(i+2, data.get(keys.get(i)));
			insert.executeUpdate();
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	protected void retrieve(Connection con) throws StorageException, InvalidDataException, DBProblemException {
		try (PreparedStatement retrieve = con.prepareStatement(retrieveTeacher);) {
			retrieve.setString(1, id.get("user"));
			try(ResultSet record = retrieve.executeQuery();){
				if(record.next()){
					for(String key : keys) data.put(key, record.getString(key));
				} else throw new InvalidDataException(null);
			}
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	
	protected void delete(Connection con) throws StorageException, InvalidDataException, DBProblemException{
		super.delete(con);
		for(String qID : Qualification.qualificationList(con, id.get("account"))) new Qualification(qID, null).delete(con);
	}
	//Update methods should add stuff to changes and then take it out after using super.update
	protected void update(Connection con) throws StorageException, InvalidDataException, DBProblemException {
		
	}

	public Map<String, Object> showMini() {
		return null;
	}
	public Map<String, Object> show() {
		return null;
	}
	public Map<String, Object> showFull() {
		return null;
	}
	
}