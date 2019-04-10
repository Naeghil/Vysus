package storage;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.sql.*;

public class Institution extends Account{
//Object-specific variables
	//TODO: what exactly does it mean?
	protected Object rankingPreferences;
	//protected List<Job> postedJobs = null;
	//Consider extending the class user, privately, in this file
	protected List<User> staff = null; //TODO: make a staff private class that overrides login checking with the sysAdmin
	//Object-specific queries:
	protected String retrieveStaff = "SELECT * FROM User WHERE accountID=?";		
	
	
//Initialisation: constructors and variables setup
	//Uses super constructors
	public Institution(String accountID) { 
		super(accountID); 
	}
	public Institution(String accountID, Map<String, String> accountData, Map<String, Object> additionalData) {
		super(accountID, accountData, additionalData);
	}
	protected void setDBVariables() {
		keys = new ArrayList<String>(Arrays.asList(
				"sysAdminID", "name", "type", "address", "email", "phoneNo"));
		delete = "DELETE FROM Qualification WHERE qualificationID=?";
		retrieve = "SELECT * FROM Institution WHERE accountID=?";
		create = "INSERT INTO Institution"
				+ "(accountID, sysAdminID, name, type, address, email, phoneNo) "
				+ "VALUES(?, ?, ?, ?, ?, ?)";
	}
	protected String update(List<String> changed) {
		String upd = "UPDATE Institution SET " + changed.get(0);
		for(int i=1; i<changed.size(); i++) upd += "=?, " + changed.get(i);
		upd += "=? WHERE accountID=?";
		return upd;
	}
	protected void processAdditionalData(Map<String, Object> data) {
		
	}
	
//Object-specific querying methods
	
//Public interfaces of protected methods
	public void makeHR() {
		
	}

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