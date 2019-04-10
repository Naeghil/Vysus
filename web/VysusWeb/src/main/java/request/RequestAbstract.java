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
	
	public static Map<String, String> getStringHash(Object hash) {
		Map<?, ?> temp;
		Map<String, String> converted = new HashMap<String, String>();
		String key;
		String value;
		if(hash instanceof HashMap<?,?>) temp = (HashMap<?, ?>)hash;
		else return null;
		for(Object keyO : temp.keySet()) {
			if(keyO instanceof String) key = (String)keyO;
			else return null;
			Object valueO = temp.get(keyO);
			if(valueO instanceof String) value = (String)valueO;
			else return null;
			converted.put(key, value);
		}
		if(converted.size()==0) return null;
		else return converted;
	}
	
}
