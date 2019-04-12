package vysusWeb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
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

	HashMap<String, Object> userHash = new HashMap<String, Object>();
	
	public Login(){
		super();
		//Avoid session conflict not allowing a logged user to login again
		if(actor!=null) redirect("myProfile.jsf");
	}
	
	public void formHashmap() {
		this.userHash.put("username", this.username);
		this.userHash.put("password", this.password);
	}
	
	public void login() {
		formHashmap(); //Gather data from the form
		try(Connection connection = getConnection()) {
			if(connection==null) return;
			//Initialise the request
			LogIn login = new LogIn(this.userHash, getConnection());
			login.execute();
			getSessionMap().put("username", this.username);
			/** What is this for?
			Map<String, Object> requestMap = context.getExternalContext().getSessionMap();
			UIViewRoot testData3 = context.getViewRoot();
			UIComponent testData2 = context.getViewRoot().findComponent("loginForm");
			//Map<String,Object> testData = context.getViewRoot().findComponent("loginForm").getAttributes();
			//System.out.println(testData);
			
			System.out.println(testData2);
			//Printed /index.xhtml
			//System.out.println(testData3.getViewId()); 
			//Printed {} why is this empty?
			System.out.println(testData3.getViewMap());
			//System.out.println(requestMap.get("Username"));
			*/
			redirect("myProfile.jsf");
		} catch(InvalidDataException e) {
			String invalidField = e.getFields().get(0);
			String msg = e.getMessage(invalidField);
			if(invalidField.equals("userID")) invalidField = "username";
			message(invalidField, "Invalid field", msg);
		} catch(DBProblemException e) {
			message("Uh-oh", "We had a problem contacting the database");
		} catch (SQLException e) { }
	}
	
//Getter & Setters as per JSF specification	
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