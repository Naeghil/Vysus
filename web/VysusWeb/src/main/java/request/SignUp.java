package request;

import java.util.Map;
import java.sql.Connection;
import storage.*;
import util.DataConv;

public class SignUp extends RequestAbstract {
	//Keys: title, firstNames, lastNames, houseIdentifier, postcode, email, phoneNo, dateOfBirth
	protected Map<String, Object> userCreationData;
	private Map<String, Object> accountCreationData;
	protected String username;
	protected String password;
	protected int accType;
	
	public SignUp(Map<String, Object> userData, Map<String, Object> accountData, Connection con) {
		//This is passed from the appropriate bean as HashMap<String, String>
		userCreationData = userData;
		accountCreationData = accountData;
		username = (String)userData.get("username");
		password = (String)userData.get("password");
		accType = (int)accountData.get("accType");
		connection = con;
	}
	
	public void execute() throws DBProblemException, InvalidDataException {
		if(this.userCreationData == null) {
			throw new InvalidDataException(null, "Data has not been submitted properly.");
		}
		if(!User.isUnique(username, connection)) throw new InvalidDataException("username", "This username already exists.");
		String accId = accType+username;
		actor = new User(connection, username, password, userCreationData, accId);
	}
	
	
	
}