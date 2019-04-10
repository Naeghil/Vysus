package storage;

import java.sql.*;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

//TODO: consider making this an interface

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
	public Account() {};
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
	public static Account makeAccount(String accountID, Map<String, String> accountData, Map<String, Object> additionalData)
		throws InvalidDataException, DBProblemException {
		char accType = accountID.charAt(0);
		if(accType=='0') return new Teacher(accountID, accountData, additionalData);
		if(accType=='1') return new Institution(accountID, accountData, additionalData);
		
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