package storage;

import java.sql.Connection;
import java.util.Map;
import exceptions.*;

/*******************************************
 * 				Account					   *
 * This abstract class has the main aim to * 
 * enable instantiating account objects	   *
 * disregarding the type of account		   *
 ******************************************/

public abstract class Account extends StorageAbstract {
//Initialisation: constructors and variables setup
	public Account(String id) { super(id); }
	public Account(String id, Connection connection) throws DBProblemException, InvalidDataException {
		super(id, connection);
	}
	public Account(String id, Map<String, Object> data, Connection connection) throws DBProblemException {
		super(id, data, connection);
	}
	//Masking constructor for existing accounts
	public static Account getAccount(String accountID) throws InvalidDataException {
		int accType = Account.accType(accountID);
		if(accType==0) return new Teacher(accountID);
		if(accType==1) return new Institution(accountID);
		return null;
	}
	public static Account getAccount(String accountID, String actor, Connection connection) 
		throws DBProblemException, InvalidDataException {
		int accType = Account.accType(accountID);
		if(accType==0) return new Teacher(accountID, connection);
		if(accType==1) return new Institution(accountID, actor, connection);
		return null;
	}
	//Masking constructor for new accounts
	public static Account makeAccount(String accountID, Map<String, Object> accountData, Connection connection)
		throws InvalidDataException, DBProblemException {
		char accType = accountID.charAt(0);
		if(accType=='0') return new Teacher(accountID, accountData, connection);
		if(accType=='1') return new Institution(accountID, accountData, connection);
		
		throw new InvalidDataException(null, "Unrecognised account type");
	}
//Public interfaces of protected methods
	//TODO: currently unused
	public abstract void deleteAccount(Connection connection) throws DBProblemException, InvalidDataException;
	//TODO: currently unused
	public void updateAccount(Map<String, Object> changes, Connection connection)
		throws DBProblemException, InvalidDataException {
		this.changes = changes;
		update(connection);
	}
	
//Getters and show methods	
	public static int accType(String accountID) throws InvalidDataException {
		int type = Character.getNumericValue(accountID.charAt(0));
		if(type>=0 && type<=1) return type;
		
		throw new InvalidDataException("Unrecognised account type");
	}
	
	
}