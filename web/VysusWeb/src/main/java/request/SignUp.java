package request;

import java.util.HashMap;
import java.sql.Connection;
//import java.sql.SQLException;
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
		this.userCreationData = getStringHash(input.get("userData"));
		//this.accountCreationData = getStringHash(input.get("accountData"));
		username = (String)input.get("username");
		password = (String)input.get("password");
		accType = (String)input.get("accType");
		connection = con;
		actor = null;
		
	}
	
	public HashMap<String, Object> execute() throws DBProblemException, InvalidDataException {
		if(this.userCreationData == null) {
			InvalidDataException e = new InvalidDataException(null);
			e.addField("all");
			throw e;
		}
		try{
			System.out.println("isUnique method called");
			if(!User.isUnique(username, connection)) throw InvalidDataException.invalidUser();
			System.out.println("isUnique method ended");
			String accId = accType+username;
			actor = new User(connection, username, password, userCreationData, accId);
			return null;
		} finally { } /*
			try {
				if(connection!=null) connection.close();
			} catch (SQLException e) {
				//Well it won't close, what do you want me to do?
			}
		} */ //TODO: find another way to close the connection
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