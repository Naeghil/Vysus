package request;

import storage.User;
import java.sql.Connection;
import java.sql.SQLException;


public class SignUp implements RequestInterface {
	//Keys: title, firstNames, lastNames, houseIdentifier, postcode, email, phoneNo, dateOfBirth
	private HashMap<String, Object> userCreationData;
	//private HashMap<String, Object> accountCreationData;
	
	public SignUp(HashMap<String, Object> input) throws InvalidDataException {
		userCreationData = (HashMap<String, String>)input.get("userData");
		//accountCreationData = input.get("accountData");
		connection = null;
		actor = null;
	}
	
	public HashMap<String, Object> execute() throws StorageException {
		HashMap<String, Object> output = new HashMap<String, Object>();
		validateUserData();
		//validateAccountData();
		try{ makeConnection() } catch (SQLException e) {
			throw  new DBProblemException(e);
		} finally { 
			if(connection!=null) connection.close();
		}
		actor = new User(connection, input.get("username"), input.get("password"), userCreationData, //String accId
		HashMap<String, Object>
		
	}


}