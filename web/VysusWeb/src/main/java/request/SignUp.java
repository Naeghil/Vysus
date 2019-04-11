package request;

import java.util.Map;
import java.sql.Connection;
import storage.*;
import util.DataConv;

public class SignUp extends RequestAbstract {
	//Keys: title, firstNames, lastNames, houseIdentifier, postcode, email, phoneNo, dateOfBirth
	protected Map<String, Object> userCreationData;
	//private Map<String, Object> accountCreationData;
	protected String username;
	protected String password;
	protected String accType;
	
	public SignUp(Map<String, Object> input, Connection con) {
		//This is passed from the appropriate bean as HashMap<String, String>
		this.userCreationData = DataConv.getObjectMap(input.get("userData"));
		//this.accountCreationData = DataConv.getObjectMap(input.get("accountData"));
		username = (String)input.get("username");
		password = (String)input.get("password");
		accType = (String)input.get("accType");
		connection = con;
	}
	
	public Map<String, Object> execute() throws DBProblemException, InvalidDataException {
		if(this.username == null) throw InvalidDataException.invalidUser();
		if(this.password == null) throw InvalidDataException.invalidPassword();
		if(this.userCreationData == null) {
			InvalidDataException e = new InvalidDataException(null);
			e.addField("all", "Cannot find data.");
			throw e;
		}
		if(!User.isUnique(username, connection)) throw InvalidDataException.invalidUser();
		String accId = accType+username;
		//TODO: the accountID for an institution should be independent from its own sysAdmin
		actor = new User(connection, username, password, userCreationData, accId);
		return null;
	}
	
	
	
}