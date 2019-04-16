package vysusWeb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import storage.*;

/*
@Inject
protected @Named("actor") Actor actor;
*/

@Named("actor")
@SessionScoped
public class Actor extends VysusBean implements Serializable {
	String actor = null;
	String account = null;
	Map<String, String> userData = null;
	Map<String, String> accountData = null;

	
//User session:
	public void login(String username, String password)  {
		try(Connection connection = getConnection()){
			if(connection==null) return;
			this.account = (new User(username)).login(password, connection);
			this.actor = username;
			System.out.println("after login: " + this.account+" "+this.actor);
			redirect("profile.jsf");
		} catch(InvalidDataException | DBProblemException | SQLException e) {
			 handleException(e, false);
		}
	}
	
	public void signup(String username, String password, String accountID, Map<String, Object> userData, Map<String, Object> accountData) {
		System.out.println("before signup: "+this.account+" "+this.actor);
		try (Connection connection = getConnection()){
			if(connection == null) return;
			if(User.exists(username, connection)) throw new InvalidDataException("username", "This username already exists.");
			Account.makeAccount(accountID, accountData, connection);
			
			userData.put("accountID", accountID);
			new User(connection, username, password, userData);
			
			this.actor = username;
			this.account = accountID;
			
			redirect("profile.jsf");
		} catch(InvalidDataException | DBProblemException | SQLException e) {
			handleException(e, false);
		}
		System.out.println("after signup: "+this.account+" "+this.actor);
	}
	
	public void logout() {
		System.out.println("when logout: "+this.account+" "+this.actor);
		getExternalContext().invalidateSession();
		redirect("index.jsf");
	}
	
	public boolean isIn() {
		System.out.println("when isin: "+this.account+" "+this.actor);
		return this.actor!=null && this.account!=null;
	}
	
	public String onLoad(boolean internal) {
		System.out.println("when onload: "+this.account+" "+this.actor);
		if(internal && actor==null) return "index.jsf";
		if(!internal && actor!=null) return "profile.jsf";
		return "";
	}
	
//Refreshing data:
	public void requestUserData() {
		System.out.println("when requserdata: "+this.account+" "+this.actor);
		if(!isIn()) return;
		try(Connection connection = getConnection()){
			 userData = new User(actor, connection).showFull();
		 } catch(InvalidDataException | DBProblemException | SQLException e) {
			 handleException(e, true);
		 }
	}
	public void requestAccountData() {
		System.out.println("when reqaccountdata: "+this.account+" "+this.actor);
		if(!isIn()) return;
		try(Connection connection = getConnection()){
			 accountData = Account.getAccount(this.account, this.actor, connection).showFull();
			 //System.out.println("requestAccountData.accountData: " + accountData);
		} catch(InvalidDataException | DBProblemException | SQLException e) {
			handleException(e, true);
		}
		//System.out.println(accountData);
	}
	
	public void handleException(Exception ex, boolean fatal) {
		System.out.println("when handling exception: "+this.account+" "+this.actor);
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
		System.out.println("when userfield: "+this.account+" "+this.actor);
		if(userData!=null && userData.containsKey(key)) return userData.get(key);
		return "";
	}
	public String accountField(String key) {
		System.out.println("when accountfield: "+this.account+" "+this.actor);
		if(accountData!=null && accountData.containsKey(key)) return accountData.get(key);
		return "";
	}
	
	public int accType() throws InvalidDataException {
		System.out.println("when account type: "+this.account+" "+this.actor);
		return Account.accType(account);
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
