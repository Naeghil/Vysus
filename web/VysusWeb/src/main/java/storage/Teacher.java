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
	public Teacher(String accountID, Connection connection) throws DBProblemException, InvalidDataException { 
		super(accountID); 
		if(connection!=null) retrieve(connection);
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
	public Map<String, Object> showMini() {
		return null;
	}
	public Map<String, Object> show() {
		Map<String, Object> show = new HashMap<String, Object>();
		show.put("aboutMe", data.get("aboutMe"));
		return show;
	}
	public Map<String, Object> showFull() {
		return data;
	}
	
}