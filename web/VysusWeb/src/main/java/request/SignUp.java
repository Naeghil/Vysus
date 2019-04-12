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
		if(this.username == null) throw InvalidDataException.invalidUser();
		if(this.password == null) throw InvalidDataException.invalidPassword();
		if(this.userCreationData == null) {
			InvalidDataException e = new InvalidDataException(null);
			e.addField(null, "Cannot find data.");
			throw e;
		}
		if(!User.isUnique(username, connection)) throw InvalidDataException.invalidUser();
		String accId = accType+username;
		//TODO: the accountID for an institution should be independent from its own sysAdmin
		actor = new User(connection, username, password, userCreationData, accId);
	}
	
	
	
}