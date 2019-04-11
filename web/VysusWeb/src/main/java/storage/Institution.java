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
	//Object-specific queries:
	protected String retrieveStaff = "SELECT * FROM User WHERE accountID=?";		
	
	
//Initialisation: constructors and variables setup
	//Uses super constructors
	public Institution(String accountID) { super(accountID); }
	public Institution(String accountID, Map<String, Object> accountData, Connection connection)
		throws DBProblemException { super(accountID, accountData, connection); }
		
	protected void setDBVariables() {
		keys = new ArrayList<String>(Arrays.asList(
				"sysAdminID", "name", "type", "buildingIdentifier", "postcode", "email", "phoneNo"));
		delete = "DELETE FROM Qualification WHERE qualificationID=?";
		retrieve = "SELECT * FROM Institution WHERE accountID=?";
		create = "INSERT INTO Institution"
				+ "(accountID, sysAdminID, name, type, buildingIdentifier, postcode, email, phoneNo) "
				+ "VALUES(?, ?, ?, ?, ?, ?)";
	}
	protected String update(List<String> changed) {
		String upd = "UPDATE Institution SET " + changed.get(0);
		for(int i=1; i<changed.size(); i++) upd += "=?, " + changed.get(i);
		upd += "=? WHERE accountID=?";
		return upd;
	}

//Object-specific querying methods
	
//Public interfaces of protected methods
	//All the load methods
//Getters and show methods	
	public Map<String, Object> showMini() {
		return null;
	}
	public Map<String, Object> show() {
		return null;
	}
	public Map<String, Object> showFull() {
		Map<String, Object> show = new HashMap<String, Object>();
		/*show.put("accountData", data);
		//show.put("staffData", value)
		*/
		return show;
	}
}

class Staff extends User {
	public Staff(Connection connection, String username, String password, Map<String, Object> data, String accountID)
			throws DBProblemException, InvalidDataException {
		super(username, null);
		this.data = data;
		this.data.put("accountID", accountID);
		create(connection);
	}
	public Staff(String username)
			throws DBProblemException, InvalidDataException {
		super(username, null);
	}
	
//Show methods unusable:
	public Map<String, Object> showFull(){
		return null;
	}
	public Map<String, Object> show(){
		return null;
	}
	public Map<String, Object> showMini() {
		return null;
	}
	protected Map<String, Object> getData() {
		return data;
	}
//Static show methods to be used by the Institution object
	public static Map<String, Object> showStaff(String accountID, Connection connection) throws DBProblemException, InvalidDataException {
		Map<String, Object> staffData = new HashMap<String, Object>();
		for(String staffID : Staff.staffList(accountID, connection)) {
			Staff staff = new Staff(staffID);
			staff.load(connection);
			staffData.put(staffID, staff.getData());
		}
		return staffData;
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