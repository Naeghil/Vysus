package vysusWeb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import storage.*;

@Named("actor")
@SessionScoped
public class Actor extends VysusBase implements Serializable {
	String actor = null;
	String account = null;
	Map<String, String> userData = null;
	Map<String, String> accountData = null;

	public Actor() {
		System.out.println("Actor Instantiated");
	}
//User session:
	public void login(String username, String password)  {
		try(Connection connection = getConnection()){
			if(connection==null) return;
			account = (new User(username)).login(password, connection);
			actor = username;
			
			redirect("profile.jsf");
		} catch(InvalidDataException | DBProblemException | SQLException e) {
			 handleException(e, false);
		}
	}
	
	public void signup(String username, String password, String accountID, Map<String, Object> userData, Map<String, Object> accountData) {
		try (Connection connection = getConnection()){
			if(connection == null) return;
			if(User.exists(username, connection)) throw new InvalidDataException("username", "This username already exists.");
			Account.makeAccount(accountID, accountData, connection);
			
			userData.put("accountID", accountID);
			new User(connection, username, password, userData);
			
			actor = username;
			account = accountID;
			
			redirect("profile.jsf");
		} catch(InvalidDataException | DBProblemException | SQLException e) {
			handleException(e, false);
		}
	}
	
	public void logout() {
		getExternalContext().invalidateSession();
		redirect("index.jsf");
	}
	public boolean isIn() {
		return actor!=null;
	}
	
	public String onLoad(boolean internal) {
		if(internal && actor==null) return "index.jsf";
		if(!internal && actor!=null) return "profile.jsf";
		return "";
	}
//Refreshing data:
	public void requestUserData() {
		System.out.println("user data requested");
		if(!isIn()) return;
		try(Connection connection = getConnection()){
			 userData = new User(actor, connection).showFull();
		 } catch(InvalidDataException | DBProblemException | SQLException e) {
			 handleException(e, true);
		 }
		System.out.println(userData);
	}
	public void requestAccountData() {
		System.out.println("account data requested");
		if(!isIn()) return;
		try(Connection connection = getConnection()){
			 accountData = Account.getAccount(account, actor, connection).showFull();
		} catch(InvalidDataException | DBProblemException | SQLException e) {
			handleException(e, true);
		}
		System.out.println(accountData);
	}
	
	public void handleException(Exception ex, boolean fatal) {
		if(ex instanceof InvalidDataException) {
			InvalidDataException e = (InvalidDataException)ex;
			message(e);
			System.out.println("Invalid data exception " + e.message() + " " + e.field());
			if(fatal) logout();
		} 
		if(ex instanceof DBProblemException) {
			DBProblemException e = (DBProblemException)ex;
			message("Uh-oh", "We had a problem executing your request, refresh and try again");
			if (e.getNested() != null) System.out.println(e.getNested().getMessage());
		}
	}
//Getters and setters:
	public String userField(String key) {
		if(userData!=null && userData.containsKey(key)) return userData.get(key);
		return "";
	}
	public String accountField(String key) {
		if(accountData!=null && accountData.containsKey(key)) return accountData.get(key);
		return "";
	}
	
	public int accType() throws InvalidDataException {
		return Account.accType(account);
	}
	
	public String getUserPicture() { 
		return "resources/images/propic-default.jpg";
	}
}
