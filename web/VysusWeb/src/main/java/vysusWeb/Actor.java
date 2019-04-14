package vysusWeb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import storage.*;

@Named("actor")
@SessionScoped
public class Actor extends VysusBase {
	String actor = null;
	String account = null;
	Map<String, String> userData = null;
	Map<String, String> accountData = null;

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
			System.out.println("actor.Signup: " + account);
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
		return (actor!=null && account!=null);
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
			 userData = Account.getAccount(account, actor, connection).showFull();
		} catch(InvalidDataException | DBProblemException | SQLException e) {
			handleException(e, true);
		}
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
		if(accountData!=null && accountData.containsKey(key)) return userData.get(key);
		return "";
	}
	
	public int accType() throws InvalidDataException {
		return Account.accType(account);
	}
}
