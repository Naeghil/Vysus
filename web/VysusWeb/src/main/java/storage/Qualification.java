package storage;

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
	
	protected Map<String, String> qualificationData = new HashMap<String, String>();
	//TODO: consider adding a "referee" name
	protected static List<String> keys = new ArrayList<String>(Arrays.asList(
			"title", "startDate", "endDate", "comment", "finalGrade", "institution", "institutionEmail", "institutionPhoneNo", "level"));
	protected Map<String, String> changes = new HashMap<String, String>();
	
	protected Boolean verified;
		
	//Queries:
	// incremental number 
	protected  String deleteQualification = "DELETE FROM Qualification WHERE qualificationID=?";
	protected String retrieveQualification = "SELECT * FROM Qualification WHERE qualificationID=?";
	protected String createQualification = "INSERT INTO Qualification(qualificationID, accountID, title, startDate, endDate, comment, finalGrade, institution, institutionEmail, institutionPhoneNo, level, verified) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	// isn't there a WHERE missing?
	protected String updateQualification(List<String> changed) {
		String upd = "UPDATE Qualification SET " + changed.get(0);
		for(int i=1; i<changed.size(); i++) upd += "=?, " + changed.get(i);
		upd += "=? WHERE qualificationID=?";
		return upd;
	}
	
	protected void create(Connection con) throws DBProblemException {
		try(PreparedStatement insert = con.prepareStatement(createQualification);) {
			//the qualification
			insert.setString(1, id.get("qualification"));
			insert.setString(2, id.get("account"));
			for(int i=0; i<keys.size(); i++) insert.setString(i+3, qualificationData.get(keys.get(i)));	
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	
	protected void retrieve(Connection con) throws InvalidDataException, DBProblemException {
		try (PreparedStatement retrieve = con.prepareStatement(retrieveQualification);) {
			retrieve.setString(1, id.get("qualification"));
			try(ResultSet record = retrieve.executeQuery();){
				if(record.next()){
					for(String key : keys) qualificationData.put(key, record.getString(key));
					id.put("account", record.getString("accountID"));
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
	
	/**
	public HashMap<String, String> getChanges() {
		return changes;
	}
	public void setChanges(HashMap<String, String> changes) {
		this.changes = changes;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getFinalGrade() {
		return finalGrade;
	}
	public void setFinalGrade(String finalGrade) {
		this.finalGrade = finalGrade;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	public String getInstitutionEmail() {
		return institutionEmail;
	}
	public void setInstitutionEmail(String institutionEmail) {
		this.institutionEmail = institutionEmail;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Boolean getVerified() {
		return verified;
	}
	public void setVerified(Boolean verified) {
		this.verified = verified;
	}
	public HashMap<String, String> getQualificationData() {
		return qualificationData; 
	} **/
	
	public Map<String, Object> showMini() {
		// TODO Auto-generated method stub
		return null;
	}
	public Map<String, Object> show() {
		// TODO Auto-generated method stub
		return null;
	}
	public Map<String, Object> showFull() {
		// TODO Auto-generated method stub
		return null;
	}
}