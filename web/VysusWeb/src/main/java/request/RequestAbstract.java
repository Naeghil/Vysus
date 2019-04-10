package request;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import storage.*;

public abstract class RequestAbstract {
	protected Connection connection;
	protected User actor = null;
	/*protected void makeConnection() throws SQLException {
		String url = "jdbc:mysql://vysus1.cw2j7jythjii.eu-west-2.rds.amazonaws.com:3306/vysusdb";
    	String username = "foxtrot";
    	String password = "vysusfoxtrotprojectdatabasepassword";
    	connection = (Connection) DriverManager.getConnection(url, username, password);
	} */
	public User getActor() { return actor; }
	//public abstract String getLog() throws NoLogException; //What is this tho
	public abstract Map<String, Object> execute() throws StorageException;
	
}
