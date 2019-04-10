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
	public Account(String accountID, Map<String, String> accountData, Connection connection)
		throws DBProblemException {
		data.put("id", accountID);
		data = accountData;
		setDBVariables();
		create(connection);
	}
	//Masking constructor for existing accounts
	public static Account getAccount(String accountID) throws InvalidDataException {
		char accType = accountID.charAt(0);
		if(accType=='0') return new Teacher(accountID);
		if(accType=='1') return new Institution(accountID);
		
		InvalidDataException e = new InvalidDataException(null);
		e.addField("accountID", "Unrecognised account type");
		throw e;
	}
	//Masking constructor for new accounts
	public static Account makeAccount(String accountID, Map<String, String> accountData, Connection connection)
		throws InvalidDataException, DBProblemException {
		char accType = accountID.charAt(0);
		if(accType=='0') return new Teacher(accountID, accountData, connection);
		if(accType=='1') return new Institution(accountID, accountData, connection);
		
		InvalidDataException e = new InvalidDataException(null);
		e.addField("accountID", "Unrecognised account type");
		throw e;
	}
	//DB variables setup depends on the final account class
	
//Object-specific querying methods

//Public interfaces of protected methods

//Getters and show methods	


	protected String accType() throws InvalidDataException {
		char type = data.get("id").charAt(0);
		if(type=='0') return "Teacher";
		if(type=='1') return "Institution";
		
		InvalidDataException e = new InvalidDataException(null);
		e.addField("accountID", "Unrecognised account type");
		throw e;
	}
}