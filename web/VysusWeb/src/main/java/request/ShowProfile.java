package request;

import java.sql.Connection;
import java.util.Map;

import storage.StorageException;
import storage.User;

public class ShowProfile extends RequestAbstract {
	
	protected String actorID;
	protected String targetID;
		
	public ShowProfile(String actor, String target, Connection con) {
		this.actorID = actor;
		this.targetID = target;
		this.connection = con;
	}
	
	public void execute() throws StorageException {
		User newUser = new User(this.actorID, this.connection);
		//return newUser.showFull();
	}
	
}
