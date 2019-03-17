package request;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.SQLException;
import storage.*;

public class SignUp extends RequestAbstract {
	//Keys: title, firstNames, lastNames, houseIdentifier, postcode, email, phoneNo, dateOfBirth
	protected HashMap<String, String> userCreationData;
	protected String username;
	protected String password;
	protected String accType;
	//private HashMap<String, Object> accountCreationData;
	
	public SignUp(HashMap<String, Object> input, Connection con) {
		//This is passed from the appropriate bean as HashMap<String, String> so whatever
		this.userCreationData = (HashMap<String, String>)input.get("userData");
		username = (String)input.get("username");
		password = (String)input.get("password");
		accType = (String)input.get("accType");
		//accountCreationData = input.get("accountData");
		connection = con;
		actor = null;
	}
	
	public HashMap<String, Object> execute() throws StorageException {
		//validateAccountData();
		try{
			//makeConnection();
			if(!User.isUnique(username, connection)) throw InvalidDataException.invalidUser();
			String accId = accType+username;
			actor = new User(connection, username, password, userCreationData, accId);
			return null;
		} catch (SQLException e) { 
			throw new DBProblemException(e); 
		} finally {
			try {
				if(connection!=null) connection.close();
			} catch (SQLException e2) {
				//Well it won't close, what do you want me to do?
			}
		}
	}

	
	

	
}