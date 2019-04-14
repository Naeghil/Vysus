package storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.sql.*;

public class Teacher extends Account{
//Object-specific variables
	List<Qualification> qualifications = new ArrayList<Qualification>();
	//TODO: protected Calendar calendar;
//Initialisation: constructors and variables setup
	//Uses super constructors:
	public Teacher(String accountID) {
		super(accountID);
	}
	public Teacher(String accountID, Connection connection) throws DBProblemException, InvalidDataException { 
		super(accountID); 
		retrieve(connection);
	}
	public Teacher(String accountID, Map<String, Object> accountData, Connection connection)
		throws DBProblemException {
		super(accountID, accountData, connection);
		//TODO: Create the calendar
	}
	protected void setDBVariables() {
		keys = new ArrayList<String>(Arrays.asList(
				"maxDistance", "minRatePerHour", "aboutMe"));
		delete = "DELETE FROM Qualification WHERE qualificationID=?";
		retrieve = "SELECT * FROM Teacher WHERE accountID=?";
		create = "INSERT INTO Teacher"
				+ "(accountID, maxDistance, minRatePerHour, aboutMe) "
				+ "VALUES(?, ?, ?, ?)";
	}
	protected String update(List<String> changed) {
		String upd = "UPDATE Teacher SET " + changed.get(0);
		for(int i=1; i<changed.size(); i++) upd += "=?, " + changed.get(i);
		upd += "=? WHERE accountID=?";
		return upd;
	}
	
//Object-specific querying methods
	
//Public interfaces of protected methods
	public void loadDeep(Connection connection)
		throws DBProblemException, InvalidDataException {
		qualifications = Qualification.allQualifications((String)data.get("id"), connection);
	}
	public void deleteAccount(Connection connection) throws DBProblemException, InvalidDataException {
		for(Qualification qualification : Qualification.allQualifications(getID(), connection)) qualification.delete(connection);
	}
	
//Getters and show methods
	public Map<String, String> show() {
		Map<String, String> show = new HashMap<String, String>();
		show.put("aboutMe", (String)data.get("aboutMe"));
		return show;
	}
	public Map<String, String> showFull() {
		Map<String, String> show = show();
		show.put("maxDistance", Float.toString((float)data.get("maxDistance")));
		show.put("minRatePerHour", Float.toString((float)data.get("minRatePerHour")));
		return show;
	}
	
}