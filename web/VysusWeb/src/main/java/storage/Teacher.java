package storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.sql.Connection;

import exceptions.*;

/***************************************************
 *	 				Institution				   	   *
 * Represents an object from the Institution table *
 **************************************************/

public class Teacher extends Account{
//Object-specific variables
	//TODO: protected Calendar calendar; currently not implemented
//Initialisation: constructors and variables setup
	public Teacher(String id) { super(id); }
	public Teacher(String id, Connection connection) throws DBProblemException, InvalidDataException { 
		super(id, connection);
	}
	public Teacher(String id, Map<String, Object> data, Connection connection) throws DBProblemException {
		super(id, data, connection);
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
	public void deleteAccount(Connection connection) throws DBProblemException, InvalidDataException {
		for(SecondaryStorage qualification : Qualification.all((String)data.get("id"), connection)) {
			qualification.delete(connection);
		}
	}
	
//Getters and show methods
	//Data available to the employers
	public Map<String, String> show() {
		Map<String, String> show = new HashMap<String, String>();
		show.put("aboutMe", (String)data.get("aboutMe"));
		return show;
	}
	//Personal work preferences
	public Map<String, String> showFull() {
		Map<String, String> show = show();
		show.put("maxDistance", Float.toString((float)data.get("maxDistance")));
		show.put("minRatePerHour", Float.toString((float)data.get("minRatePerHour")));
		return show;
	}
	
}