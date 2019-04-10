package vysusWeb;

import java.io.Serializable;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean; 
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import request.*;
import storage.*;

@ManagedBean(name="bananas")
@SessionScoped
//username, title, password, firstNames, lastNames, houseIdentifier, postcode, email, phoneNo, dateOfBirth
public class UserLogin implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String username;
	String password;

	HashMap<String, Object> userHash = new HashMap<String, Object>();
	
	public UserLogin(){ } //Default constructor

//username, title, password, firstNames, lastNames, houseIdentifier, postcode, email, phoneNo, dateOfBirth
	public void formHashmap() {
		this.userHash.put("username", this.username);
		this.userHash.put("password", this.password);
	}
	
	@SuppressWarnings("finally")
	public String loginUser() {
		formHashmap(); //Gather data from the form
		Connection connection = null;
		try {
			//Retrieve the database object
			DataSource vysusdb = (DataSource)((Context)new InitialContext()).lookup("java:/vysusDB");
			//Connect to the database
			connection = vysusdb.getConnection();
			//Initialise the request
			LogIn newLogin = new LogIn(this.userHash, connection);
			
			newLogin.execute();
			
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getSessionMap().put("Username", this.username);
			Map<String, Object> requestMap = context.getExternalContext().getSessionMap();
			UIViewRoot testData3 = context.getViewRoot();
			UIComponent testData2 = context.getViewRoot().findComponent("password");
			//Map<String,Object> testData = context.getViewRoot().findComponent("username").getAttributes();
			//System.out.println(testData);
			
			System.out.println(testData2);
			//Printed /index.xhtml
			//System.out.println(testData3.getViewId()); 
			//Printed {} why is this empty?
			System.out.println(testData3.getViewMap());
			//System.out.println(requestMap.get("Username"));

			return "myProfile";
			
			//return "indexLoggedInTemp";	
		} catch(InvalidDataException e) {
			System.out.println("Invalid Data Exception");
			return "index"; //Username or password was incorrect
		} catch(DBProblemException | NamingException | SQLException ex) {
			String msg = "There was a problem:"+System.getProperty("line.separator");
			if(ex instanceof DBProblemException) msg += ((DBProblemException)ex).getNested().getMessage();
			else msg += ex.getMessage();
			System.out.println(msg);
			//Print msg somewhere
			return "404login";
		} finally {
			try { //TODO: what does this even mean?
				if(connection!=null) connection.close();
			} catch(SQLException e) {}
		}
		
	}
	
//Getter & Setters as per jsf specification	
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