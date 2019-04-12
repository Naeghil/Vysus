package request;

import java.sql.Connection;
import storage.*;

public class LogIn extends RequestAbstract {
	protected String username;
	protected String password;
	protected String accountID;
	
	public LogIn(String username, String password, Connection con) {
		this.username = username;
		this.password = password;
		connection = con;
		actor = null;
	}
	
	public void execute() throws DBProblemException, InvalidDataException {
		if(this.username == null) throw new InvalidDataException("username", "Username not received");
		if(this.password == null) throw new InvalidDataException("password", "Password not received");
		actor = new User(username);
		accountID = actor.login(password, connection);
	}
	
	public String getAccountID() {
		return accountID;
	}
}