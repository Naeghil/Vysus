package storage;

/******************************************
 *                 User                   *
 * Represents a record in the table User  *
 ******************************************/

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import org.mindrot.jbcrypt.BCrypt;

import util.Conv;
import util.APICalls;

import java.sql.*;

import exceptions.*;

//This extends SecondaryStorage for its use as "staff";
public class User extends SecondaryStorage {
//Initialisation:
	public User(String username) { super(username);	}
	public User(String username, Connection connection) throws DBProblemException, InvalidDataException {
		super(username, connection);
	}
	public User(String username, String password, Map<String, Object> data, Connection connection) 
		throws DBProblemException, InvalidDataException {
		super(username, data, connection);
		setPassword(connection, password);
	}
	//Excludes password
	protected void setDBVariables() {
		keys = new ArrayList<String>(Arrays.asList(
			"accountID", "fullName", "houseIdentifier", "postcode", "email", "phoneNo", "dateOfBirth"));
		delete = "DELETE FROM User WHERE userID=?";
		retrieve = "SELECT * FROM User WHERE userID=?";
		create = "INSERT INTO User"
				+ "(userID, accountID, fullName, houseIdentifier, postcode, email, phoneNo, dateOfBirth) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	}
	protected String update(List<String> changed) {
		String upd = "UPDATE User SET " + changed.get(0);
		for(int i=1; i<changed.size(); i++) upd += "=?, " + changed.get(i);
		upd += "=? WHERE userID=?";
		return upd;
	}

//Object-specific querying methods:
	//Special method to set password, as it's the only field stored as a binary and not a string
	protected void setPassword(Connection con, String clear) throws DBProblemException  {
		try(PreparedStatement pwUpdate = con.prepareStatement("UPDATE User SET password=? WHERE userID=?");) {
			String hash = BCrypt.hashpw(clear, BCrypt.gensalt());
			pwUpdate.setBytes(1, hash.getBytes());
			pwUpdate.setObject(2, data.get("id"));
			pwUpdate.executeUpdate();
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	//Checks user's password, returning the accountID if execution normally terminates
	public String login(String password, Connection con) throws DBProblemException, InvalidDataException {
		try (PreparedStatement getHash = con.prepareStatement(
				"SELECT password, accountID FROM User WHERE userID=?");){
			getHash.setObject(1, data.get("id"));
			try(ResultSet rs = getHash.executeQuery();) {
				if(rs.next()) {
					String hash = new String(rs.getBytes("password"));
					if(!BCrypt.checkpw(password, hash)) throw new InvalidDataException("password", "Wrong password");
					return rs.getString("accountID");
				} else throw new InvalidDataException("username", "No such user exists");
			}
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	//To check uniqueness 
	public static boolean exists(String username, Connection con) throws DBProblemException {
		try(PreparedStatement unique = con.prepareStatement("SELECT userID FROM User WHERE userID=?");) {
			unique.setString(1, username);
			try(ResultSet rs = unique.executeQuery();) { return rs.next(); }
		} catch (SQLException e) { throw new DBProblemException(e); }
	}

//Public interfaces of protected methods:
	//TODO: currently unused
	public void deleteUser(String password, Connection connection) throws DBProblemException, InvalidDataException {
		Account account = Account.getAccount(login(password, connection)); //Double checks password
		account.deleteAccount(connection);
		delete(connection);
	}
	//TODO: currently unused
	public void updateProfile(Map<String, Object> changes, Connection connection) 
		throws InvalidDataException, DBProblemException {
		if(changes.containsKey("newPassword") && changes.containsKey("oldPassword")) {
			login((String)changes.remove("oldPassword"), connection);
			setPassword(connection, (String)changes.remove("newPassword"));
		}
		this.changes = changes;
		update(connection);
	}
	//From SecondaryStorage
	public static List<SecondaryStorage> all(Object id, Connection connection) 
		throws DBProblemException, InvalidDataException {
		return all("storage.User", id, connection, "userID", "User", "accountID");
	}
//Getters & Shows:
	//Name and IDs
	public Map<String, String> showMini() {
		Map<String, String> show = new HashMap<String, String>();
		show.put("username", (String)data.get("id"));
		show.put("account", (String)data.get("accountID"));
		show.put("fullName", (String)data.get("fullName"));
		return show;
	}
	//Communication information
	public Map<String, String> show() {
		Map<String, String> show = showMini();
		show.put("email", (String)data.get("email"));
		show.put("phoneNo", (String)data.get("phoneNo"));
		return show;
	}
	//Personal information
	public Map<String, String> showFull() {
		Map<String, String> show = show();
		show.put("DOB", Conv.dateToString((Date)data.get("dateOfBirth")));
		//Retrieves the full address from post code and identifier using an external library
		Map<String, String> fullAddress = 
			APICalls.fullAddress((String)data.get("postcode"),(String)data.get("houseIdentifier"));
		String address = 
			  fullAddress.get("Identifier")+"\n"
			+ fullAddress.get("Town")+"\n"
			+ fullAddress.get("City")+"\n"
			+ fullAddress.get("County");
		show.put("address", address);
		return show;
	}
}