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
	protected String hashedPW = null; 	//for creation and modification purposes
	protected Account account = null;	//Associated account and related data
	
	//Object-specific queries:
	protected static String uniqueness = "SELECT COUNT(*) FROM User WHERE userID=?";
	protected static String retrievePassword = "SELECT password FROM User WHERE userID=?";
	protected static String updatePassword = "UPDATE User SET password=? WHERE userID=?";

//Object initialisation:
	//Constructor for existing user
	public User(String username) { 
		data.put("id", username); //this might need to be re-put; or alternatively use a separate variable
		setDBVariables();
	}
	//Constructor for new user
	public User(Connection connection, String username, String password, Map<String, String> data, String accountID) throws DBProblemException {
		this.data = data;
		this.data.put("id", username);
		this.data.put("accountID", accountID);
		setDBVariables();
		create(connection);
	}
	//Sets object-specific queries and keys:
	protected void setDBVariables() {
		keys = new ArrayList<String>(Arrays.asList(
				"accountID", "title", "firstNames", "lastNames", "houseIdentifier", "postcode", "email", "phoneNo", "dateOfBirth"));
		delete = "DELETE FROM User WHERE userID=?";
		retrieve = "SELECT * FROM User WHERE userID=?";
		//This excludes password:
		create = "INSERT INTO User"
				+ "(userID, accountID, title, firstNames, lastNames, houseIdentifier, postcode, email, phoneNo, dateOfBirth) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	}
	//Implementation of the update query (excludes password):
	protected String update(List<String> changed) {
		String upd = "UPDATE User SET " + changed.get(0);
		for(int i=1; i<changed.size(); i++) upd += "=?, " + changed.get(i);
		upd += "=? WHERE userID=?";
		return upd;
	}

//Object-specific querying methods:
	//Special method to set password, as it's the only field stored as a binary and not a string
	protected void setPassword(Connection con, String clear) throws DBProblemException  {
		try(PreparedStatement pwUpdate = con.prepareStatement(updatePassword);) {
			String hash = BCrypt.hashpw(clear, BCrypt.gensalt());
			pwUpdate.setBytes(1, hash.getBytes());
			pwUpdate.setString(2, data.get("id"));
			pwUpdate.executeUpdate();
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	//Checks user's password, effectively "logging in" the user if execution normally terminates
	public void login(String password, Connection con) throws DBProblemException, InvalidDataException {
		try (PreparedStatement getHash = con.prepareStatement(retrievePassword);){
			getHash.setString(1, data.get("id"));
			try(ResultSet rs = getHash.executeQuery();) {
				if(rs.next()) {
					String hash = new String(rs.getBytes("password"));
					if(!BCrypt.checkpw(password, hash)) throw InvalidDataException.invalidPassword();
				} else throw InvalidDataException.invalidUser();
			}
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	//Unneedingly complicated method to check uniqueness. 
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

//Public interfaces of protected methods:
	//Enables loading user data
	//TODO: better specify the role of this method
	public void load(Connection connection) throws DBProblemException, InvalidDataException {
		retrieve(connection);
	}
	//Enables user deletion
	public void deleteUser(String password, Connection connection) throws DBProblemException, InvalidDataException {
		login(password, connection); //Double checks password
		delete(connection);
	}
	//For now this is to enact changes to the user account
	public void updateProfile(Map<String, String> changes, Connection connection) throws InvalidDataException, DBProblemException {
		if(changes.containsKey("newPassword") && changes.containsKey("oldPassword")) {
			login(changes.remove("oldPassword"), connection);
			setPassword(connection, changes.remove("newPassword"));
		}
		this.changes = changes;
		update(connection);
	}
	
//Getters & Shows:
	//Obtain the Account object
	public Account getAccount() { return account; }
	
	//TODO: show methods
	public Map<String, Object> showMini() {return null;}
	public Map<String, Object> show() { return null; }
	//Should this also retrieve?
	public Map<String, Object> showFull() {
		Map<String, Object> show = new HashMap<String, Object>();
		show.put("userData", data);
		show.put("accountData", account.showFull());
		return show;
	}
}