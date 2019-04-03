package storage;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.sql.*;

//Also has sysAdmin as id

public class Institution extends Account{
	protected Map<String, String> accountData = new HashMap<String, String>();
	protected Map<String, String> changes;
	protected static List<String> keys = new ArrayList<String>(Arrays.asList(
			"name", "address", "email", "phoneNo"));
	//TODO: consider adding an "institution type" to mean the level or collection of levels/ yes
	
	//TODO: what exactly does it mean?
	protected Object rankingPreferences;
	//TODO: Consider using a map for the objects (Job, User): map, lazy; also make user lazy
	protected List<String> postedJobs;
	protected List<String> staff;		
	
	//Queries:
	protected static String deleteInstitution = "DELETE FROM Institution WHERE accountID=?";
	protected static String retrieveInstitution = "SELECT * FROM Institution WHERE accountID=?";
	protected static String createInstitution = "INSERT INTO Institution(accountID, sysAdminID, name, address, email, phoneNo) VALUES(?, ?, ?, ?, ?, ?)";
	protected String updateInstitution(List<String> changed) {
		String upd = "UPDATE Institution SET " + changed.get(0);
		for(int i=1; i<changed.size(); i++) upd += "=?, " + changed.get(i);
		upd += "=? WHERE accountID=?";
		return upd;
	}
	
	// for Staff
	//protected String createStaff = "INSERT INTO Staff(institutionID, name, address, email, rankingPreferences, systemAdministrator, finalGrade,) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	protected String retrieveStaff = "SELECT * FROM User WHERE accountID=?";
	
	//Abstract implementation
	protected void create(Connection con) throws DBProblemException {
		try(PreparedStatement insert = con.prepareStatement(createInstitution);) {
			insert.setString(1, id.get("account"));
			insert.setString(2, id.get("sysAdmin"));
			for(int i=0; i<keys.size(); i++) insert.setString(i+3, accountData.get(keys.get(i)));
			//TODO: add staff creation; where do we get the data?
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	
	protected void retrieve(Connection con) throws InvalidDataException, DBProblemException {
		try (PreparedStatement retrieve = con.prepareStatement(retrieveInstitution);) {
			retrieve.setString(1, id.get("account"));
			try(ResultSet record = retrieve.executeQuery();){
				if(record.next()){
					for(String key : keys) accountData.put(key, record.getString(key));
				} else throw new InvalidDataException(null); //TODO: what is invalid?
			}
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	
	protected void delete(Connection con) throws StorageException, InvalidDataException, DBProblemException {
		try(PreparedStatement delete = con.prepareStatement(deleteInstitution);) {
			delete.setString(1, id.get("account"));
			if(delete.executeUpdate() != 1) throw new InvalidDataException(null); //TODO: what is invalid?
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	
	protected void update(Connection con) throws StorageException, InvalidDataException, DBProblemException {
		ArrayList<String> changedFields = new ArrayList<String>(changes.keySet());
		try(PreparedStatement update = con.prepareStatement(updateInstitution(changedFields));) {
			int i;
			for(i=0; i<changedFields.size(); i++) update.setString(i+1, changes.get(changedFields.get(i)));
			update.setString(i, id.get("account"));
			//Execution:
			if(update.executeUpdate() != 1) throw new InvalidDataException(null); //TODO: what is invalid?
			else changes = new HashMap<String, String>();
			//TODO: other changes are atomic, e.g. sysAdmin etc
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	
	//make retrieve method call this automatically
	//TODO: consider using a special user constructor instead, taking in their id and the sysAdmin id
	//sysAdmin is the only one who can actually visualize/retrieve the data for all the staff
	protected void retrieveStaff(Connection con) throws InvalidDataException, DBProblemException {
		try (PreparedStatement retrieve = con.prepareStatement(retrieveStaff);) {
			/* retrieve.setString(1, id.get(staffID));
			try(ResultSet record = retrieve.executeQuery();){
				if(record.next()){
					//??
				}
			} */
		} catch (SQLException e) {
			throw new DBProblemException(e);
		}
	}
	
	// METHODS FOR INSTITUTIONS LIKE MAKE HR - IF YOU HAVE ADMIN RIGHTS THAT IS - AND OTHER FUNKY METHODS TO BE ADDED SHORTLY
	
	public void makeHR() {
		
	}
	
	// FUNKY METHODS END
	/**
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Object getRankingPreferences() {
		return rankingPreferences;
	}
	public void setRankingPreferences(Object rankingPreferences) {
		this.rankingPreferences = rankingPreferences;
	}
	public String getSystemAdministrator() {
		return systemAdministrator;
	}
	public void setSystemAdministrator(String systemAdministrator) {
		this.systemAdministrator = systemAdministrator;
	}
	
	// Related To Staff
	public ArrayList<String> getStaff() {
		return staff;
	}
	public void setStaff(ArrayList<String> staff) {
		this.staff = staff;
	}
	TODO: getters and setters might not be necessary:
	display is achieved through the abstract methods "show"
	changes should be made more complex, as they depend on whether one is sysAdmin or not **/
	
	public HashMap<String, Object> showMini() {
		// TODO Auto-generated method stub
		return null;
	}

	public HashMap<String, Object> show() {
		// TODO Auto-generated method stub
		return null;
	}

	public HashMap<String, Object> showFull() {
		// TODO Auto-generated method stub
		return null;
	}
}