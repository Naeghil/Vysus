package storage;

import java.sql.*;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

//TODO: consider making this an interface

/*******************************************
 * 				Account					   *
 * This abstract class has the main aim to * 
 * enable instantiating account objects	   *
 * disregarding the type of account		   *
 ******************************************/

public abstract class Account extends StorageAbstract {
	
	public Account(String accountID, Map<String, String> accountData) { 
		id.put("account", accountID);
		data = accountData;
	}
	
//Common implementations:
	protected String updateAccount(List<String> changed) throws InvalidDataException {
		String upd = "UPDATE "+ accType()+" SET " + changed.get(0);
		for(int i=1; i<changed.size(); i++) upd += "=?, " + changed.get(i);
		upd += "=? WHERE accountID=?";
		return upd;
	}
	//Generic update based on the "changed fields"
	protected void update(Connection con) throws StorageException, InvalidDataException, DBProblemException {
		List<String> changedFields = new ArrayList<String>(changes.keySet());
		try(PreparedStatement update = con.prepareStatement(updateAccount(changedFields));) {
			//Setting up the statement:
			int i;
			for(i=0; i<changedFields.size(); i++) update.setString(i+1, changes.get(changedFields.get(i)));
			update.setString(i, id.get("account"));
			//Execution:
			if(update.executeUpdate() != 1) throw InvalidDataException.invalidUser();
			else changes = new HashMap<String, String>();
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	
	protected void delete(Connection con) throws StorageException, InvalidDataException, DBProblemException {
		String delAccount = "DELETE FROM "+accType()+" WHERE accountID=?";
		try(PreparedStatement delete = con.prepareStatement(delAccount);) {
			delete.setString(1, id.get("account"));
			if(delete.executeUpdate() != 1) throw new InvalidDataException(null);
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	
	
	protected String accType() throws InvalidDataException {
		char type = id.get("account").charAt(0);
		if(type=='0') return "Teacher";
		if(type=='1') return "Institution";
		
		InvalidDataException e = new InvalidDataException(null);
		e.addField("accountID", "Unrecognised account type");
		throw e;
	}
}