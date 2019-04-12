package request;

import java.util.Map;
import java.sql.Connection;
//import java.sql.SQLException;
import storage.*;

public class LogIn extends RequestAbstract {
	protected String username;
	protected String password;
	
	public LogIn(String username, String password, Connection con) {
		this.username = username;
		this.password = password;
		connection = con;
		actor = null;
	}
	
	public void execute() throws DBProblemException, InvalidDataException {
		if(this.username == null) throw InvalidDataException.invalidUser();
		if(this.password == null) throw InvalidDataException.invalidPassword();
		actor = new User(username, null);
		actor.login(password, connection);
	}	
}