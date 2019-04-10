package request;

import java.util.Map;
import java.sql.Connection;
//import java.sql.SQLException;
import storage.*;

public class LogIn extends RequestAbstract {
	protected String username;
	protected String password;
	
	public LogIn(Map<String, Object> input, Connection con) {
		username = (String)input.get("username");
		password = (String)input.get("password");
		connection = con;
		actor = null;
	}
	
	public Map<String, Object> execute() throws DBProblemException, InvalidDataException {
		if(this.username == null) throw InvalidDataException.invalidUser();
		if(this.password == null) throw InvalidDataException.invalidPassword();
		actor = new User(username);
		actor.login(password, connection);
		return null;
	}	
}