package request;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import storage.*;

public interface RequestAbstract {
	protected Connection connection;
	protected User actor;
	protected void makeConnection() throws SQLException {
		String url = "jdbc:mysql://vysus1.cw2j7jythjii.eu-west-2.rds.amazonaws.com:3306/vysusdb";
    		String username = "foxtrot";
    		String password = "vysusfoxtrotprojectdatabasepassword";

    		connection = (Connection) DriverManager.getConnection(url, username, password);
    	
	}
	
	public abstract String getLog() throws NoLogException; //What is this tho
	public abstract HashMap<String, Object> execute() throws StorageException;
	
}
