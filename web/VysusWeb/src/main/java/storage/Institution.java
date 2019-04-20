package storage;

import java.util.List;
import java.util.Map;

import util.APICalls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.sql.Connection;

import exceptions.*;

/***************************************************
 *	 				Institution				   	   *
 * Represents an object from the Institution table *
 **************************************************/

public class Institution extends Account{
//Object-specific variables
	//TODO: currently unused
	protected Object rankingPreferences;
	
//Initialisation: constructors and variables setup
	public Institution(String id) { super(id);  }
	public Institution(String id, Connection connection) throws DBProblemException, InvalidDataException {
		super(id, connection);
	}
	public Institution(String id, String actor, Connection connection) 
		throws DBProblemException, InvalidDataException { 
		this(id, connection); 
		requestAdminRights(actor);
	}
	public Institution(String accountID, Map<String, Object> accountData, Connection connection)
		throws DBProblemException { 
		super(accountID, accountData, connection);
	}
	
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
	
	protected void requestAdminRights(String actor) {
		if(actor.equals(data.get("sysAdminID"))) data.put("admin", "yes");
		else data.put("admin", "no");
	}

//Public interfaces
	public void deleteAccount(Connection connection) throws DBProblemException, InvalidDataException{
		for(SecondaryStorage staff : User.all((String)data.get("id"), connection)) staff.delete(connection);
		for(SecondaryStorage job : Job.all((String)data.get("id"), connection)) job.delete(connection);
		this.delete(connection);
	}
	
//Getters and show methods	
	//Name, type, post code and address
	public Map<String, String> showMini(){
		Map<String, String> show = new HashMap<String, String>();
		show.put("name", (String)data.get("name"));
		show.put("type", (String)data.get("type"));
		show.put("postcode", (String)data.get("postcode"));
		//Retrieves the full address from post code and identifier using an external library
		Map<String, String> fullAddress = 
			APICalls.fullAddress((String)data.get("postcode"),(String)data.get("buildingIdentifier"));
		String address = 
			  fullAddress.get("Identifier")+"\n"
			+ fullAddress.get("Town")+"\n"
			+ fullAddress.get("City")+"\n"
			+ fullAddress.get("County");
		show.put("address", address);
		return show;
	}
	//Adds contact information
	public Map<String, String> show() {
		Map<String, String> show = showMini();
		show.put("email", (String)data.get("email"));
		show.put("phoneNo", (String)data.get("phoneNo"));
		return show;
	}
	//Adds administrator status
	public Map<String, String> showFull() {
		Map<String, String> show = show();
		show.put("admin", (String)data.get("admin"));
		return show;
	}
}