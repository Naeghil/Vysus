package storage;

/* *****************************************
 *                 User                    *
 * Represents a record in the table user   *
 *                                         *
 ******************************************/

/* general TODO:
	statements need to be closed 
	try catch finally 
	execute or execute query
	check the exceptions for connection and preparedstatement and resultset
	resultset is used as .getTYPE("field")
	needs a special retrieve for accepted sessions
	however, user has sessions, so why not make a creator that accepts a session id
	to log in is an action that the user does, so it should be ok for the class
	to check by itself
	also needs a logout method that saves changes (?) and deletes sessions
	also do getters and settes once you know what to get and set -.-
*/

import java.util.HashMap;

public class User implements StorageInterface {

	private String username;
	private String accountId;
	//Fields: 
	private HashMap<String, String> contacts;
	//Fieds:
	private HashMap<String, String> personalInfo;		
	private boolean logged;
	
	public User(String usr){ username=usr; }
	//Creates a new user by populating its object and then creating the record
	public User(Connection con, String usr, String pw, HashMap<String, String> cont, HashMap<String, String> pInfo, String accountId) {
		//TODO populate object
		create(con);
	}
	
	//Logs in the user, and loads their information
	//TODO: write the relevant exceptions; maybe have a cover-all "dbdown exception"
	// 	for all the times a storage method returns false
	public void logIn(Connection con, String pw) /*throws InvalidLoginDetailsException, dbexceptionthing */ {
		/*TODO 
			1. PreparedStatement getPW = (PreparedStatement) con.blabla
			2. if(hash(pw)==getPW.execute().result) {
				logged = true;
				return retrieve(con);
			3. else throw InvalidLoginDetailsException; 
		*/
	}

	//Interface implementation:
	private boolean create(Connection con) {
		//TODO just do the statement
		return false;
	}
	private boolean retrieve(Connection con){
		/*TODO
			1. PreparedStatement getRecord = (PreparedStatement) con.blabla
			2. Smt record = getRecord.execute().smt 
			2. fill contacts
			3. fill personal info	
		*/
		return true;		
	}
	private boolean update(Connection con){
		/*TODO: consider change sensitivity through a HashMap<String, String> changes
			
		*/	
	}
	private boolean delete(Connection con){
		
	}


}
