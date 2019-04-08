package request;

import java.util.Map;
import java.util.HashMap;
import java.sql.Connection;
//import java.sql.SQLException;
import storage.*;

public class SignUp extends RequestAbstract {
	//Keys: title, firstNames, lastNames, houseIdentifier, postcode, email, phoneNo, dateOfBirth
	protected Map<String, String> userCreationData;
	//private Map<String, Object> accountCreationData;
	protected String username;
	protected String password;
	protected String accType;
	
	public SignUp(Map<String, Object> input, Connection con) {
		//This is passed from the appropriate bean as HashMap<String, String>
		this.userCreationData = getStringHash(input.get("userData"));
		//this.accountCreationData = getStringHash(input.get("accountData"));
		username = (String)input.get("username");
		password = (String)input.get("password");
		accType = (String)input.get("accType");
		connection = con;
		actor = null;
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
	
	public HashMap<String, String> getStringHash(Object hash) {
		HashMap<?, ?> temp;
		HashMap<String, String> converted = new HashMap<String, String>();
		String key;
		String value;
		if(hash instanceof HashMap<?,?>) temp = (HashMap<?, ?>)hash;
		else return null;
		for(Object keyO : temp.keySet()) {
			if(keyO instanceof String) key = (String)keyO;
			else continue;
			Object valueO = temp.get(keyO);
			if(valueO instanceof String) value = (String)valueO;
			else continue;
			converted.put(key, value);
		}
		if(converted.size()==0) return null;
		else return converted;
	}
	
}