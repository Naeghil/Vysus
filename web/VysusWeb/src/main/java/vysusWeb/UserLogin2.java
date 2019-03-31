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
@ManagedBean(name="login")
@SessionScoped
//username, title, password, firstNames, lastNames, houseIdentifier, postcode, email, phoneNo, dateOfBirth
public class UserLogin2 implements Serializable {
	String username;
	String password;

	HashMap<String, Object> userHash = new HashMap<String, Object>();
	
	public UserLogin2(){ } //Default constructor

//username, title, password, firstNames, lastNames, houseIdentifier, postcode, email, phoneNo, dateOfBirth
	public void formHashmap() {
		this.userHash.put("username", this.username);
		this.userHash.put("password", this.password);
	}
	
	public String loginUser() {
			return "LoginSuccess";
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