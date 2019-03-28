package storage;

/* *****************************************
 *                 User                    *
 * Represents a record in the table user   *
 *                                         *
 ******************************************/

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class User extends StorageAbstract {
//User data: this excludes the user and the account ids
	protected Map<String, String> userData = new HashMap<String, String>();
	
//Operation data:
	private String hashedPW = null; 									//for creation and modification purposes
	private Map<String, String> changes = new HashMap<String, String>(); 	//for modification purposes
	
	//Constructor for existing user
	public User(Connection connection, String username, String password) 
		throws DBProblemException, InvalidDataException { 
		id.put("user", username);
		//Check credential
		checkPW(password, connection);
		//Retrieve user data
		retrieve(connection);
	}
	//Constructor for new user
	public User(Connection connection, String username, String password, Map<String, String> data, String accountID) 
		throws DBProblemException {
		id.put("user", username);
		id.put("account", accountID);
		userData = data;
		hashedPW = BCrypt.hashpw(password, BCrypt.gensalt());
		
		create(connection);
	}
	
	public void deleteUser(String password, Connection connection) 
		throws DBProblemException, InvalidDataException {
		checkPW(password, connection);
		delete(connection);
	}
	
	//TODO: a method to enact changes

	//Checks
	private void checkPW(String password, Connection con) throws DBProblemException, InvalidDataException {
		try (PreparedStatement getHash = con.prepareStatement(retrievePassword);){
			getHash.setString(1, id.get("user"));
			try(ResultSet rs = getHash.executeQuery();) {
				if(rs.next()) {
					String hash = new String(rs.getBytes("password"));
					if(!BCrypt.checkpw(password, hash)) throw InvalidDataException.invalidPassword();
				} else throw InvalidDataException.invalidUser();
			}
		} catch (SQLException e) {
			throw new DBProblemException(e);
		}
	}
	//TODO: to check
	public static boolean isUnique(String username, Connection con) throws DBProblemException {
		try(PreparedStatement unique = con.prepareStatement(uniqueness);) {
			unique.setString(1, username);
			try(ResultSet rs = unique.executeQuery();) {
				if(rs.next()) {
					if(rs.getInt(1)!=0) return false;
				} else throw new DBProblemException(null);
			}
		} catch (SQLException e) {
			throw new DBProblemException(e);
		}
		return true;
	}
	
	
	//Getters&Setters: assumes fields are vetted previously
	public String getAccount() { return id.get("account"); }
		//TODO: review this
	public void changeField(String field, Object newValue) { changes.put(field, (String) newValue); }																	   
	
	//Queries:
	protected static String uniqueness = "SELECT COUNT(*) FROM User WHERE userID=?";
	protected String retrievePassword = "SELECT password FROM User WHERE userID=?";
	protected String deleteUser = "DELETE FROM User WHERE userID=?";
	protected String retrieveUser = "SELECT * FROM User WHERE userID=?";
	protected String createUser = "INSERT INTO User(userID, password, accountID, title, firstNames, lastNames, houseIdentifier, postcode, email, phoneNo, dateOfBirth) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	protected String updateUser(List<String> changed) {
		String upd = "UPDATE User SET " + changed.get(0);
		for(int i=1; i<changed.size(); i++) upd += "=?, " + changed.get(i);
		if(hashedPW!=null) upd+= "=?, password";
		upd += "=? WHERE userID=?";
		return upd;
	}
	//Abstract implementation:
	protected void create(Connection con) throws DBProblemException  {
		try(PreparedStatement insert = con.prepareStatement(createUser);) {
			insert.setString(1, id.get("user"));
			//The hash is not normally stored in the object
			insert.setBytes(2, hashedPW.getBytes());
			hashedPW = null;
			insert.setString(3, id.get("account"));
			for(int i=0; i<keys.size(); i++) insert.setString(i+4, userData.get(keys.get(i)));
			insert.executeUpdate();
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	protected void retrieve(Connection con) throws InvalidDataException, DBProblemException {
		try (PreparedStatement retrieve = con.prepareStatement(retrieveUser);) {
			retrieve.setString(1, id.get("user"));
			try(ResultSet record = retrieve.executeQuery();){
				if(record.next()){
					for(String key : keys) userData.put(key, record.getString(key));
					id.put("account", record.getString("accountID"));
				} else throw InvalidDataException.invalidUser();
			}
		} catch (SQLException e) {
			throw new DBProblemException(e);
		}
	}
	protected void update(Connection con) throws InvalidDataException, DBProblemException {
		//Makes an ordered list to avoid potential problems with the Set class
		List<String> changedFields = new ArrayList<String>(changes.keySet());
		try(PreparedStatement update = con.prepareStatement(updateUser(changedFields));) {
			//Setting up the statement:
			int i;
			for(i=0; i<changedFields.size(); i++) update.setString(i+1, changes.get(changedFields.get(i)));
			if(hashedPW!=null) update.setString(i++, hashedPW);
			update.setString(i, id.get("user"));
			//Execution:
			if(update.executeUpdate() != 1) throw InvalidDataException.invalidUser();
			else {
				hashedPW = null;
				changes = new HashMap<String, String>();
			}
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	protected void delete(Connection con) throws InvalidDataException, DBProblemException {
		try(PreparedStatement delete = con.prepareStatement(deleteUser);) {
			delete.setString(1, id.get("user"));
			if(delete.executeUpdate() != 1) throw InvalidDataException.invalidUser();
		} catch (SQLException e) {
			throw new DBProblemException(e);
		}
	}
	//Ordered list of strings to loop into the user data map
	protected List<String> keys = new ArrayList<String>(Arrays.asList(
			"title", "firstNames", "lastNames", "houseIdentifier", "postcode", "email", "phoneNo", "dateOfBirth"));
	//TODO: show methods
	public HashMap<String, Object> showMini() {return null;}
	public HashMap<String, Object> show() { return null; }
	public HashMap<String, Object> showFull() { return null; }
}
