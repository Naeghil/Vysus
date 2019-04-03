package storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.List;
import java.sql.*;

public class Teacher extends Account{
	//Yes store as strings
	private String gender;
	private double maxDistance;		//TODO: as string?
	private double minRatePerHour;	//TODO: as string?
	private String aboutMe;
	
	// Two dates for each event
	protected List<Date> calendarStart;
	protected List<Date> calendarEnd;
	
	/** Supposedly covered by Qualifications 
	// what they want to teach? Won't be stored in Teacher DB again due to one-to-many relationship so figure that out as well
	private List<String> subjects;
	*/
	
	//Queries
		//Uniqueness is guaranteed by the user id
	protected static String uniqueness = "SELECT COUNT(*) FROM Teacher WHERE accountID=?";
	protected String deleteTeacher = "DELETE FROM Teacher WHERE accountID=?";
	protected String retrieveTeacher = "SELECT * FROM Teacher WHERE accountID=?";
	protected String createTeacher = "INSERT INTO Teacher(accountID, gender, maxDistance, minRatePerHour, aboutMe";
	//This is only valid for changes to details: changes to calendar and/or qualifications are different
	protected String updateTeacher(List<String> changed) {
		//This is only possible if everything is stored as string, otherwise needs preset string
		//And everything is always updated
		/*String upd = "UPDATE Teacher SET " + changed.get(0);
		for(int i=1; i<changed.size(); i++) upd += "=?, " + changed.get(i);
		upd += "=? WHERE userID=?";
		return upd;*/
		return null;
	}
	
	//These queries are for qualifications actually
	//protected String createTeacher = "INSERT INTO Teacher(qualificationID, accountID, title, startDate, endDate, comment, finalGrade, institution, institutionEmail, level) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	//protected ArrayList<String> keys = new ArrayList<String>(Arrays.asList(
			//"title", "startDate", "endDate", "comment", "finalGrade", "institution", "institutionEmail", "level"));

	protected void create(Connection con) throws StorageException, DBProblemException {
		try(PreparedStatement insert = con.prepareStatement(createTeacher);) {
			insert.setString(1, id.get("account"));
			//TODO: how are data stored?
			//for(int i=0; i<keys.size(); i++) insert.setString(i+2, userData.get(keys.get(i)));
			insert.executeUpdate();
		} catch (SQLException e) { throw new DBProblemException(e); }
	}

	protected void retrieve(Connection con) throws StorageException, InvalidDataException, DBProblemException {
		try (PreparedStatement retrieve = con.prepareStatement(retrieveTeacher);) {
			retrieve.setString(1, id.get("user"));
			try(ResultSet record = retrieve.executeQuery();){
				if(record.next()){
					//TODO: how are data stored?
				} else throw new InvalidDataException(null); //TODO: what is invalid?
			}
		} catch (SQLException e) { throw new DBProblemException(e); }
	}

	protected void delete(Connection con) throws StorageException, InvalidDataException, DBProblemException {
		try(PreparedStatement delete = con.prepareStatement(deleteTeacher);) {
			delete.setString(1, id.get("account"));
			if(delete.executeUpdate() != 1) throw new InvalidDataException(null); //TODO: what is invalid?
		} catch (SQLException e) { throw new DBProblemException(e); }
	}

	protected void update(Connection con) throws StorageException, InvalidDataException, DBProblemException {
		//TODO: how are data stored?
		/** List<String> changedFields = new ArrayList<String>(changes.keySet());
		try(PreparedStatement update = con.prepareStatement(updateUser(changedFields));) {
			//Setting up the statement:
			int i;
			for(i=0; i<changedFields.size(); i++) update.setString(i+1, changes.get(changedFields.get(i)));
			if(hashedPW!=null) update.setString(i++, hashedPW);
			update.setString(i, id.get("user"));
			//Execution:
			if(update.executeUpdate() != 1) throw InvalidDataException.invalidUser();
			else {
				hashedPW = null;
				changes = new HashMap<String, String>();
			}
		} catch (SQLException e) { throw new DBProblemException(e); } **/
	}
//TODO:
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