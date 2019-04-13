package storage;

import java.sql.*;
import java.util.Map;

/*******************************************
 * 				Account					   *
 * This abstract class has the main aim to * 
 * enable instantiating account objects	   *
 * disregarding the type of account		   *
 ******************************************/

public abstract class Account extends StorageAbstract {
//Object-specific variables
	//There are no object-specific variables

//Initialisation: constructors and variables setup
	public Account(String accountID) {
		data.put("id", accountID);
		setDBVariables();
	}
	public Account(String accountID, Map<String, Object> accountData, Connection connection)
		throws DBProblemException {
		data = accountData;
		data.put("id", accountID);
		setDBVariables();
		create(connection);
	}
	//Masking constructor for existing accounts
	public static Account getAccount(String accountID, String actor, Connection connection) throws DBProblemException, InvalidDataException {
		char accType = accountID.charAt(0);
		if(accType=='0') return new Teacher(accountID, connection);
		if(accType=='1') return new Institution(accountID, actor, connection);
		
		throw new InvalidDataException(null, "Unrecognised account type");
	}
	//Masking constructor for new accounts
	public static Account makeAccount(String accountID, Map<String, Object> accountData, Connection connection)
		throws InvalidDataException, DBProblemException {
		char accType = accountID.charAt(0);
		if(accType=='0') return new Teacher(accountID, accountData, connection);
		if(accType=='1') return new Institution(accountID, accountData, connection);
		
		throw new InvalidDataException(null, "Unrecognised account type");
	}
	//DB variables setup depends on the final account class
	
//Object-specific querying methods

//Public interfaces of protected methods
	public abstract void loadDeep(Connection connection) throws DBProblemException, InvalidDataException;
	
	public abstract void deleteAccount(Connection connection) throws DBProblemException, InvalidDataException;
	
//Getters and show methods	
	public static int accType(String accountID) throws InvalidDataException {
		int type = Character.getNumericValue(accountID.charAt(0));
		if(type>=0 && type<=1) return type;
		
		throw new InvalidDataException(null, "Unrecognised account type");
	}
	public String getID() {
		return (String)data.get("id");
	}
	
}