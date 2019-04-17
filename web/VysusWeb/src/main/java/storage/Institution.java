package storage;

import java.util.List;
import java.util.Map;

import util.APICalls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.sql.*;

public class Institution extends Account{
//Object-specific variables
	boolean admin = false;
	protected Object rankingPreferences;
	
//Initialisation: constructors and variables setup
	//Uses super constructors
	public Institution(String accountID) { super(accountID);  }
	
	public Institution(String accountID, String actor, Connection connection) throws DBProblemException, InvalidDataException { 
		super(accountID); 
		retrieve(connection);
		requestAdminRights(actor);
	}
	public Institution(String accountID, Map<String, Object> accountData, Connection connection)
		throws DBProblemException { super(accountID, accountData, connection);}
	
	protected void setDBVariables() {
		keys = new ArrayList<String>(Arrays.asList(
				"sysAdminID", "name", "type", "buildingIdentifier", "postcode", "email", "phoneNo"));
		delete = "DELETE FROM Qualification WHERE qualificationID=?";
		retrieve = "SELECT * FROM Institution WHERE accountID=?";
		create = "INSERT INTO Institution"
				+ "(accountID, sysAdminID, name, type, buildingIdentifier, postcode, email, phoneNo) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	}
	protected String update(List<String> changed) {
		String upd = "UPDATE Institution SET " + changed.get(0);
		for(int i=1; i<changed.size(); i++) upd += "=?, " + changed.get(i);
		upd += "=? WHERE accountID=?";
		return upd;
	}

//Object-specific querying methods
	//There are no specific querying methods for this class
//Public interfaces of protected methods
	//Sets admin to true if the person making the request is the sysAdmin
	public void requestAdminRights(String actor) {
		//System.out.println("Insitution.requestAdmin.data: " + data);
		admin = actor.equals(data.get("sysAdminID"));
	}
	//The following methods handle the account's jobs
	public void postJob(Map<String, Object> jobData, Connection connection)
		throws DBProblemException, InvalidDataException {
		if(admin) throw new InvalidDataException(null, "You don't have the rights to perform this operation");
		//@SuppressWarnings("unused")
		//Job newJob = new Job(jobData, connection)
	}
	public void deleteJob(Integer jobID, Connection connection)
		throws DBProblemException, InvalidDataException {
		if(admin) throw new InvalidDataException(null, "You don't have the rights to perform this operation");
		//Job toDelete = new Job(jobID, null);
		//toDelete.delete(connection);
	}
	
	public void deleteAccount(Connection connection) throws DBProblemException, InvalidDataException{
		for(Staff staff : Staff.allStaff(getID(), connection)) staff.delete(connection);
		//for(Job job : Job.allJobs(getID(), connection)) job.delete(connection);
		this.delete(connection);
	}
	
//Getters and show methods	
	public Map<String, String> showMini(){
		Map<String, String> show = new HashMap<String, String>();
		show.put("name", (String)data.get("name"));
		show.put("type", (String)data.get("type"));
		//System.out.println(show);
		Map<String, String> fullAddress = APICalls.fullAddress((String)data.get("postcode"),(String)data.get("buildingIdentifier"));
		String address = (fullAddress.get("Identifier")+"\n"+fullAddress.get("Town")+"\n"+fullAddress.get("City")+"\n"+fullAddress.get("County"));
		show.put("address", address);
		return show;
	}
	//Only show contacts after started talking/accepted the job:
	public Map<String, String> show() {
		Map<String, String> show = showMini();
		show.put("email", (String)data.get("email"));
		show.put("phoneNo", (String)data.get("phoneNo"));
		return show;
	}
	//You get staff data separately
	public Map<String, String> showFull() {
		Map<String, String> show = show();
		if(admin) show.put("admin", "yes");
		else show.put("admin", "no");
		return show;
	}
}