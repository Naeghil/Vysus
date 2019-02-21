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
	private String accountId;
	//Fields: firstNames, lastNames, address, email, phoneNo, mobile1, mobile2, dateOfBirth (dd-MM-yyyy)
	private HashMap<String, String> userData;
	
	//User has account?

//Operation data:
	private Session session;
	private String hashedPW; //for creation and modification purposes
	private HashMap<String, String> changes; //for modification purposes
	
	//Abstract implementation:
	public void create() throws DBProblemException {
		try(PreparedStatement insert = con.prepareStatement("INSERT INTO User(userID, password, accountID, firstNames, lastNames, address, email, phoneNo, mobile1, mobile2, dateOfBirth) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");) {
			ArrayList<String> keys = new ArrayList<String>(Arrays.asList("firstNames", "lastNames", "address", "email", "phoneNo", "mobile1", "mobile2, dateOfBirth"));
			
			//the username
			insert.setString(1, id.get(0));
			//the password is not normally stored
			insert.setString(2, hashedPW); //TODO: how to store BINARY
			//the account id is not stored among other userData because of conceptual differences
			insert.setString(3, accountId);
			
			for(int i=0; i<keys.size(); i++) insert.setString(i+4, userData.get(keys.get(i)));	
			if(insert.executeUpdate() != 1) throw new DBProblemException(null);
			else hashedPW = null;
		} catch (SQLException e) {
			throw new DBProblemException(e);
		}
	}
	
	public void retrieve() throws InvalidDataException, DBProblemException {
		try (PreparedStatement retrieve = con.prepareStatement("SELECT * FROM User WHERE userID=?");) {
			retrieve.setString(1, id.get(0));
			try(ResultSet record = retrieve.executeQuery();){
				if(record.next()){
					ArrayList<String> keys = new ArrayList<String>(Arrays.asList("firstNames", "lastNames", "address", "email", "phoneNo", "mobile1", "mobile2, dateOfBirth"));
					for(String key : keys) userData.put(key, record.getString(key));
					//the account id is not stored with other user data
					accountId = record.getString("accountID");
				} else throw new InvalidDataException(null, "userID");
			} catch (SQLException e2) {
				throw new DBProblemException(e2);
			}
		} catch (SQLException e) {
			throw new DBProblemException(e);
		}
	}

	public void update() throws InvalidDataException, DBProblemException {
		ArrayList<String> changedFields = new ArrayList<String>(changes.keySet());
		//Preparing the statement:
		String upd = "UPDATE User SET " + changedFields.get(0);
		for(int i=1; i<changedFields.size(); i++) upd += "=?, " + changedFields.get(i);
		if(hashedPW!=null) upd+= "=?, password";
		upd += "=? WHERE userID=?";
		//Setting up and executing the statement:
		try(PreparedStatement update = con.prepareStatement(upd);) {
			int i;
			for(i=0; i<changedFields.size(); i++) update.setString(i+1, changes.get(changedFields.get(i)));
			if(hashedPW!=null) update.setString(i++, hashedPW);
			update.setString(i, id.get(0));
			
			if(update.executeUpdate() != 1) throw new InvalidDataException(null, "userID");
			else {
				hashedPW = null;
				changes = new HashMap<String, String>();
			}
		} catch (SQLException e) {
			throw new DBProblemException(null);
		}
	}
	
	public void delete() throws InvalidDataException, DBProblemException {
		try(PreparedStatement delete = con.prepareStatement("DELETE FROM User WHERE userID=?");) {
			delete.setString(1, id.get(0));
			if(delete.executeUpdate() != 1) throw new InvalidDataException(null, "userID");
		} catch (SQLException e) {
			throw new DBProblemException(e);
		}
	}
	
	//Existing user object, needs login
	public User(String usr, Connection connection){ 
		id.add(usr); 
		this.con = connection;
		changes = new HashMap<String, String>();
		hashedPW = null;
		session = null;
	}	
	//Creates a new user by populating its object; still needs logIn
	public User(Connection connection, String usr, String pw, HashMap<String, String> data, String accId) throws DBProblemException, InvalidDataException {
		this.con = connection;
		if(!isUnique(usr)) throw new InvalidDataException(null, "userID");
		
		id.add(usr);
		userData = data;
		accountId = accId;
		
		changes = new HashMap<String, String>();
		hashedPW = BCrypt.hashpw(pw, BCrypt.gensalt());
		
		create();
	}
	
	//Session manipulation
	public void logIn(String pw, String dev) throws DBProblemException, InvalidDataException {
		//Throws exception if data is not valid:
		checkPW(pw);
		//Establish a session
		session = new Session(id.get(0), dev, con);
		//Retrieve user data
		retrieve();      
	}
	public void checkSession(String key, String dev) throws DBProblemException, InvalidDataException, InvalidSessionException {
		session = new Session(key, con);
		session.checkKey(id.get(0), dev);
		retrieve();
	}
	public void logOut() throws InvalidDataException, DBProblemException, NoLogException {
		if(changes.size()!=0 | hashedPW !=null) update();
		session.delete();
	}

	//Checks
	private void checkPW(String pw) throws DBProblemException, InvalidDataException {
		try (PreparedStatement getHash = con.prepareStatement("SELECT password FROM User WHERE userID=?");){
			getHash.setString(1, id.get(0));
			try(ResultSet rs = getHash.executeQuery();) {
				if(rs.next()) {
					String hash = rs.getString("password");
					if(!BCrypt.checkpw(pw, hash)) throw new InvalidDataException(null, "password");
				} else throw new InvalidDataException(null, "userID");
			} catch (SQLException e2) { 
				throw new DBProblemException(e2);
			}
		} catch (SQLException e) {
			throw new DBProblemException(e);
		}
	}
	private boolean isUnique(String username) throws DBProblemException {
		try(PreparedStatement unique = con.prepareStatement("SELECT COUNT(*) FROM User WHERE userID=?");) {
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
	public String getValue(String field) { return userData.get(field); }
	public String getAccount() { return accountId; }
	public void changeField(String field, Object newValue) { changes.put(field, (String) newValue); }																	   
																	
}
