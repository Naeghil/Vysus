package storage;

/* *****************************************
 *                 User                    *
 * Represents a record in the table user   *
 *                                         *
 ******************************************/

import java.sql.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import org.mindrot.jbcrypt.BCrypt;

public class User extends StorageAbstract {
//User data:
	private HashMap<String, String> userData;
	
//Operation data:
	private String hashedPW; //for creation and modification purposes
	private HashMap<String, String> changes; //for modification purposes
	//This class also holds "userID" as id[0] and "accountID" as id[1]  
	
	//Abstract implementation:
	public void create() throws DBProblemException  {
		try(PreparedStatement insert = con.prepareStatement(createUser);) {
			//the username
			insert.setString(1, id.get(0));
			//the password is not normally stored
			insert.setBytes(2, hashedPW.getBytes());
			insert.setString(3, id.get(1));
			
			for(int i=0; i<keys.size(); i++) insert.setString(i+4, userData.get(keys.get(i)));	
			hashedPW = null;
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	public void retrieve() throws InvalidDataException, DBProblemException {
		try (PreparedStatement retrieve = con.prepareStatement(retrieveUser);) {
			retrieve.setString(1, id.get(0));
			try(ResultSet record = retrieve.executeQuery();){
				if(record.next()){
					for(String key : keys) userData.put(key, record.getString(key));
					id.add(record.getString("accountID"));
				} else throw InvalidDataException.invalidUser();
			}
		} catch (SQLException e) {
			throw new DBProblemException(e);
		}
	}
	public void update() throws InvalidDataException, DBProblemException {
		ArrayList<String> changedFields = new ArrayList<String>(changes.keySet());
		try(PreparedStatement update = con.prepareStatement(updateUser(changedFields));) {
			//Setting up the statement:
			int i;
			for(i=0; i<changedFields.size(); i++) update.setString(i+1, changes.get(changedFields.get(i)));
			if(hashedPW!=null) update.setString(i++, hashedPW);
			update.setString(i, id.get(0));
			//Execution:
			if(update.executeUpdate() != 1) throw InvalidDataException.invalidUser();
			else {
				hashedPW = null;
				changes = new HashMap<String, String>();
			}
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	public void delete() throws InvalidDataException, DBProblemException {
		try(PreparedStatement delete = con.prepareStatement(deleteUser);) {
			delete.setString(1, id.get(0));
			if(delete.executeUpdate() != 1) throw InvalidDataException.invalidUser();
		} catch (SQLException e) {
			throw new DBProblemException(e);
		}
	}
	
	//Constructor for existing user
	public User(String username, Connection connection){ 
		id.add(username);
		con = connection;
		changes = new HashMap<String, String>();
		hashedPW = null;
	}
	//Constructor for new user
	public User(Connection connection, String username, String password, HashMap<String, String> data, String accountID) throws DBProblemException, InvalidDataException {
		con = connection;
		id.add(username);
		id.add(accountID);
		userData = data;
		
		changes = new HashMap<String, String>();
		hashedPW = BCrypt.hashpw(password, BCrypt.gensalt());
		
		create();
	}
	
	public void logIn(String password, String device) throws DBProblemException, InvalidDataException {
		//Check credential
		checkPW(password);
		//Retrieve user data
		retrieve();      
	}

	//Checks
	private void checkPW(String password) throws DBProblemException, InvalidDataException {
		try (PreparedStatement getHash = con.prepareStatement(retrievePassword);){
			getHash.setString(1, id.get(0));
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
	public static boolean isUnique(String username, Connection con) throws DBProblemException {
		try(PreparedStatement unique = con.prepareStatement(uniqueness);) {
			unique.setString(1, username);
			try(ResultSet rs = unique.executeQuery();) {
				if(rs.getInt(1)!=0) return false;
			} catch (SQLException e2) {
				throw new DBProblemException(e2);
			}
		} catch (SQLException e) {
			throw new DBProblemException(e);
		}
		return true;
	}
	
	
	//Getters&Setters: assumes fields are vetted previously
	public String getAccount() { return id.get(1); }
	public void changeField(String field, Object newValue) { changes.put(field, (String) newValue); }																	   
	
	//Queries:
	protected static String uniqueness = "SELECT COUNT(*) FROM User WHERE userID=?";
	protected String retrievePassword = "SELECT password FROM User WHERE userID=?";
	protected String deleteUser = "DELETE FROM User WHERE userID=?";
	protected String retrieveUser = "SELECT * FROM User WHERE userID=?";
	protected String createUser = "INSERT INTO User(userID, password, accountID, title, firstNames, lastNames, houseIdentifier, postcode, email, phoneNo, dateOfBirth) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	protected String updateUser(ArrayList<String> changed) {
		String upd = "UPDATE User SET " + changed.get(0);
		for(int i=1; i<changed.size(); i++) upd += "=?, " + changed.get(i);
		if(hashedPW!=null) upd+= "=?, password";
		upd += "=? WHERE userID=?";
		return upd;
	}
	protected ArrayList<String> keys = new ArrayList<String>(Arrays.asList(
		"userID", "password", "accountID", "title", "firstNames", "lastNames", "houseIdentifier", "postcode", "email", "phoneNo", "dateOfBirth"));
	
	//Show methods unapplicable
	public HashMap<String, Object> showMini() {return null;}
	public HashMap<String, Object> show() { return null; }
	public HashMap<String, Object> showFull() { return null; }
}
