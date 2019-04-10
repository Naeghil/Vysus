package storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.List;
import java.sql.*;

public class Teacher extends Account{
//Object-specific variables
	//TODO: protected Calendar calendar;
//Initialisation: constructors and variables setup
	//Uses super constructors:
	public Teacher(String accountID) { super(accountID); }
	public Teacher(String accountID, Map<String, String> accountData, Map<String, Object> additionalData) {
		super(accountID, accountData, additionalData);
		//TODO: Create the calendar
	}
	protected void setDBVariables() {
		keys = new ArrayList<String>(Arrays.asList(
				"gender", "maxDistance", "minRatePerHour", "aboutMe"));
		delete = "DELETE FROM Qualification WHERE qualificationID=?";
		retrieve = "SELECT * FROM Teacher WHERE accountID=?";
		create = "INSERT INTO Teacher"
				+ "(accountID, gender, maxDistance, minRatePerHour, aboutMe) "
				+ "VALUES(?, ?, ?, ?, ?)";
	}
	protected String update(List<String> changed) {
		String upd = "UPDATE Teacher SET " + changed.get(0);
		for(int i=1; i<changed.size(); i++) upd += "=?, " + changed.get(i);
		upd += "=? WHERE accountID=?";
		return upd;
	}
	
//Object-specific querying methods
	
//Public interfaces of protected methods

//Getters and show methods
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