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

import javax.faces.bean.ManagedBean; 
import javax.faces.bean.SessionScoped;
//import javax.faces.context.FacesContext;

import request.*;
import storage.*;
@ManagedBean(name="bananas")
@SessionScoped
//username, title, password, firstNames, lastNames, houseIdentifier, postcode, email, phoneNo, dateOfBirth
public class UserLogin implements Serializable {
	String username;
	String password;

	HashMap<String, Object> userHash = new HashMap<String, Object>();
	
	public UserLogin(){ } //Default constructor

//username, title, password, firstNames, lastNames, houseIdentifier, postcode, email, phoneNo, dateOfBirth
	public void formHashmap() {
		this.userHash.put("username", this.username);
		this.userHash.put("password", this.password);
	}
	
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
			//User user = newUser.getActor();
			//TODO: save the user in the session context
			/*HashMap<String, Object> properties = new HashMap<String,Object>();
			properties.put("maxAge", 31536000);
			properties.put("path", "/");
			try {
				FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("username", URLEncoder.encode(this.username, "UTF-8"), properties);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}*/
			return "LoginSuccess";
			
		} catch(InvalidDataException e) {
			return "index"; // But set the flag saying the user is not unique
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