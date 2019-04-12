package vysusWeb;

import java.sql.Connection;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean; 
import javax.faces.bean.SessionScoped;
import request.*;
import storage.*;

//TODO: add a logger class that records database errors somewhere

@ManagedBean(name="login")
@SessionScoped
public class Login extends VysusBean{
	String username;
	String password;

	public Login(){
		super();
		//Avoid session conflict not allowing a logged user to login again
		if(actor!=null) redirect("myProfile.jsf");
	}
	
	public void login() {
		try(Connection connection = getConnection()) {
			if(connection==null) return;
			//Initialise the request
			LogIn login = new LogIn(username, password, getConnection());
			login.execute();
			getSessionMap().put("username", this.username);
			
			redirect("myProfile.jsf");
		} catch(InvalidDataException e) {
			String field = e.field();
			String msg = e.message();
			if(field!=null) if(field.equals("userID")) field = "username";
			message(field, "Invalid field", msg);
		} catch(DBProblemException e) {
			message("Uh-oh", "We had a problem executing your request");
		} catch (SQLException e) { }
	}
	
//Getters and setters
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}