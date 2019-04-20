package vysusWeb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import storage.*;
import exceptions.*;

/******************************************************
 * 						Actor						  *
 * The actor bean is a session-scoped bean that holds *
 * data about the user currently using the system.	  *
 * It lazily loads data from the DB to display them,  *
 * and its methods are null-safe.					  *
 *****************************************************/

@Named("actor")
@SessionScoped
public class Actor extends vysusWeb.bases.VysusBase implements Serializable {
	String actor = null;
	String account = null;
	Map<String, String> userData = null;
	Map<String, String> accountData = null;

//User session methods:
	//Tries and login a user, storing its data if successful
	public void login(String username, String password)  {
		try(Connection connection = getConnection()){
			if(connection==null) return;
			this.account = (new User(username)).login(password, connection);
			this.actor = username;
			redirect("profile.jsf");
		} catch(InvalidDataException | DBProblemException | SQLException e) {
			 handleException(e, false);
		}
	}
	//Tries and create a new user/account and logs them in
	public void signup(String username, String password, String accountID, 
		Map<String, Object> userData, Map<String, Object> accountData) {
		try (Connection connection = getConnection()){
			if(connection == null) return;
			if(User.exists(username, connection)) {
				throw new InvalidDataException("username", "This username already exists.");
			}
			Account.makeAccount(accountID, accountData, connection);
			userData.put("accountID", accountID);
			new User(username, password, userData, connection);
			//Login
			this.actor = username;
			this.account = accountID;
			redirect("profile.jsf");
		} catch(InvalidDataException | DBProblemException | SQLException e) {
			handleException(e, false);
		}
	}
	//Logging out means invalidating the session, thus creating a new, empty Actor object
	public void logout() {
		getExternalContext().invalidateSession();
		redirect("index.jsf");
	}
	//Checks login status
	public boolean isIn() {
		return this.actor!=null && this.account!=null;
	}
	//Generic method to ensure internal pages are not available if not logged in
	//and external pages are not available if logged in
	public String onLoad(boolean internal) {
		if(internal && actor==null) return "index.jsf";
		if(!internal && actor!=null) return "profile.jsf";
		return "";
	}
	
//Refreshing data:
	public void requestUserData() {
		if(!isIn()) return;
		try(Connection connection = getConnection()){
			 userData = new User(actor, connection).showFull();
		 } catch(InvalidDataException | DBProblemException | SQLException e) {
			 handleException(e, true);
		 }
	}
	public void requestAccountData() {
		if(!isIn()) return;
		try(Connection connection = getConnection()){
			 accountData = Account.getAccount(this.account, this.actor, connection).showFull();
		} catch(InvalidDataException | DBProblemException | SQLException e) {
			handleException(e, true);
		}
	}
	//Handles an exception and logs out if it's fatal
	public void handleException(Exception ex, boolean fatal) {
		if(ex instanceof InvalidDataException) {
			message((InvalidDataException)ex);
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
		if(isIn()) return Account.accType(account);
		else return -1;
	}
	
	public String getUserPicture() { 
		return "resources/images/propic-default.jpg";
	}
	
	public String account() {
		return account;
	}
	public String actor() {
		return actor;
	}
}
