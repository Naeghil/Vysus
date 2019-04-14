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
	List<Staff> staff = new ArrayList<Staff>();
	//List<Job> jobs = new ArrayList<Job>();
	//TODO: what exactly does it mean?
	protected Object rankingPreferences;
	//Object-specific queries:
	protected String retrieveStaff = "SELECT * FROM User WHERE accountID=?";		
	
	
//Initialisation: constructors and variables setup
	//Uses super constructors
	public Institution(String accountID) {
		super(accountID); 
	}
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
		if(data.get("sysAdminID")!=null) admin = actor.equals(data.get("sysAdminID"));
	}
	//The following methods handle the account's staff
	public void addStaff(String staffID, String password, Map<String, Object> staffData, Connection connection)
		throws DBProblemException, InvalidDataException {
		if(!admin) throw new InvalidDataException(null, "You don't have the rights to perform this operation");
		@SuppressWarnings("unused")
		Staff newStaff = new Staff(connection, staffID, password, staffData, (String)data.get("id"));
	}
	public void deleteStaff(String staffID, Connection connection)
		throws InvalidDataException, DBProblemException {
		if(!admin) throw new InvalidDataException(null, "You don't have the rights to perform this operation");
		Staff toDelete = new Staff(staffID, null);
		toDelete.delete(connection);
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
	
	public void loadDeep(Connection connection)
		throws DBProblemException, InvalidDataException {
		if(admin) this.staff = Staff.allStaff(getID(), connection);
		//else this.jobs = Job.allJobs(getID(), connection);
	}
	public void deleteAccount(Connection connection) throws DBProblemException, InvalidDataException{
		for(Staff staff : Staff.allStaff(getID(), connection)) staff.delete(connection);
		//for(Job job : Job.allJobs(getID(), connection)) job.delete(connection);
		this.delete(connection);
	}
	
//Getters and show methods	
	public Map<String, Object> showMini() {
		return null;
	}
	public Map<String, Object> show() {
		return null;
	}
	public Map<String, Object> showFull() {
		Map<String, Object> show = new HashMap<String, Object>(data);
		show.put("admin", admin);
		if(admin) {
			List<Object> staffData = new ArrayList<Object>();
			for(Staff staff : this.staff) {
				staffData.add(staff.showFull());
			}
			show.put("staffData", staffData);
		}
		Map<String, String> fullAddress = APICalls.fullAddress((String)show.get("postcode"),(String)show.get("houseIdentifier"));
		String address = (fullAddress.get("Identifier")+"\n"+fullAddress.get("Town")+"\n"+fullAddress.get("City")+"\n"+fullAddress.get("County"));
		show.put("address", address);
		return show;
	}
}

class Staff extends User {
	public Staff(String username, Connection connection)
			throws DBProblemException, InvalidDataException {
		super(username, connection);
	}
	public Staff(Connection connection, String username, String password, Map<String, Object> data, String accountID)
			throws DBProblemException, InvalidDataException {
		super(username);
		this.data = data;
		this.data.put("accountID", accountID);
		create(connection);
	}
	
//Show methods unusable:
	public Map<String, Object> showFull(){
		return data;
	}
	public Map<String, Object> show(){
		return null;
	}
	public Map<String, Object> showMini() {
		return null;
	}
	
//Static show methods to be used by the Institution object
	public static List<Staff> allStaff(String accountID, Connection connection) throws DBProblemException, InvalidDataException {
		List<Staff> allStaff = new ArrayList<Staff>();
		for(String staffID : Staff.staffList(accountID, connection)) {
			allStaff.add(new Staff(staffID, connection));
		}
		return allStaff;
	}
	public static List<String> staffList(String accountID, Connection connection) 
		throws DBProblemException {
		List<String> staffList = new ArrayList<String>();
		try(PreparedStatement retList = connection.prepareStatement("SELECT userID FROM User WHERE accountID=?");) {
			retList.setString(1, accountID);
			try(ResultSet records = retList.executeQuery();) {
				while(records.next()) staffList.add(records.getString("userID"));
			}
		} catch (SQLException e) { throw new DBProblemException(e); }
		return staffList;
	}
}