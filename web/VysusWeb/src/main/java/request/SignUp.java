package request;

import java.util.Map;
import java.sql.Connection;
import storage.*;

public class SignUp extends RequestAbstract {
	protected Map<String, Object> userCreationData;
	private Map<String, Object> accountCreationData;
	protected String username;
	protected String password;
	protected String accountID;
	
	public SignUp(Map<String, Object> userData, Map<String, Object> accountData, Connection con) {
		//This is passed from the appropriate bean as HashMap<String, String>
		userCreationData = userData;
		accountCreationData = accountData;
		username = (String)userData.get("username");
		password = (String)userData.get("password");
		accountID = (Integer)accountData.get("accType") + username;
		connection = con;
	}
	
	public void execute() throws DBProblemException, InvalidDataException {
		if(this.userCreationData == null) {
			throw new InvalidDataException("Data has not been submitted properly.");
		}
		if(!User.isUnique(username, connection)) throw new InvalidDataException("username", "This username already exists.");
		actor = new User(connection, username, password, userCreationData, accountCreationData, accountID);
	}
	
	public String getAccountID() {
		return accountID;
	}
	
	
}