package storage;

/*************************************************
 * 					Qualification				 *
 * These are associated to Teacher objects but 	 *
 * have no nested data.							 *
 * The corresponding table has auto-increment id *
 ************************************************/

import java.util.Map;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Qualification extends StorageAbstract{

	// https://www.gov.uk/what-different-qualification-levels-mean/list-of-qualification-levels
	
	// according to ucas there's 19839 undergraduate courses in the UK
	
	// KISCourse.KISAIM valid entry --> basically level
	
	protected static List<String> keys = new ArrayList<String>(Arrays.asList(
			"title", "startDate", "endDate", "comment", "finalGrade", "institution", "level", "institutionEmail", "institutionPhoneNo", "referee"));
	protected Boolean verified;
	//Existing constructor: give it null to avoid loading
	public Qualification(String qID, Connection connection) throws InvalidDataException, DBProblemException {
		id.put("qualification", qID);
		if(connection!=null) retrieve(connection);
	}
	//Creating constructor: no qualification id will be available this way
	public Qualification(Connection connection, String accountID, Map<String, String> data) throws DBProblemException {
		id.put("account", accountID);
		this.data = data;
		create(connection);
	}
	//For now this is to enact changes to the qualifications
	public void updateQualification(Map<String, String> changes, Connection con) throws InvalidDataException, DBProblemException {
		this.changes = changes;
		update(con);
	}
	public void verify(Connection connection) throws DBProblemException, InvalidDataException {
		try(PreparedStatement verify = connection.prepareStatement("UPDATE Qualification SET verified=? WHERE qualificationID=?");) {
			verified=true;
			verify.setBoolean(1, verified);
			verify.setString(2, id.get("qualification"));
			if(verify.executeUpdate() != 1) throw InvalidDataException.invalidQualification();
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	public boolean isVerified() { return verified; }
		
	//Queries:
	protected String deleteQualification = "DELETE FROM Qualification WHERE qualificationID=?";
	protected String retrieveQualification = "SELECT * FROM Qualification WHERE qualificationID=?";
	protected String createQualification = "INSERT INTO Qualification(accountID, title, startDate, endDate, comment, finalGrade, institution, level, institutionEmail, institutionPhoneNo, referee, verified) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	protected String updateQualification(List<String> changed) {
		String upd = "UPDATE Qualification SET " + changed.get(0);
		for(int i=1; i<changed.size(); i++) upd += "=?, " + changed.get(i);
		upd += "=? WHERE qualificationID=?";
		return upd;
	}
	//Abstract implementation
	protected void create(Connection con) throws DBProblemException {
		try(PreparedStatement insert = con.prepareStatement(createQualification);) {
			insert.setString(1, id.get("account"));
			for(int i=0; i<keys.size(); i++) insert.setString(i+2, data.get(keys.get(i)));	
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	protected void retrieve(Connection con) throws InvalidDataException, DBProblemException {
		try (PreparedStatement retrieve = con.prepareStatement(retrieveQualification);) {
			retrieve.setString(1, id.get("qualification"));
			try(ResultSet record = retrieve.executeQuery();){
				if(record.next()){
					for(String key : keys) data.put(key, record.getString(key));
					id.put("account", record.getString("accountID"));
					verified = record.getBoolean("verified");
				} else throw InvalidDataException.invalidQualification();
			}
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	protected void update(Connection con) throws InvalidDataException, DBProblemException {		
		List<String> changedFields = new ArrayList<String>(changes.keySet());
		try(PreparedStatement update = con.prepareStatement(updateQualification(changedFields));) {
			//Setting up the statement:
			int i;
			for(i=0; i<changedFields.size(); i++) update.setString(i+1, changes.get(changedFields.get(i)));
			update.setString(i, id.get("qualification"));
			//Execution:
			if(update.executeUpdate() != 1) throw InvalidDataException.invalidQualification();
			else changes = new HashMap<String, String>();
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	protected void delete(Connection con) throws InvalidDataException, DBProblemException {
		try(PreparedStatement delete = con.prepareStatement(deleteQualification);) {
				delete.setString(1, id.get("qualification"));
			if(delete.executeUpdate() != 1) throw InvalidDataException.invalidQualification();
		} catch (SQLException e) { throw new DBProblemException(e); }
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