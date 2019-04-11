package vysusWeb;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
import javax.faces.context.FacesContext;

import request.*;
import storage.*;

@ManagedBean(name="signup")
@SessionScoped
//username, title, password, firstNames, lastNames, houseIdentifier, postcode, email, phoneNo, dateOfBirth
public class UserSignup implements Serializable {
	String username;
	String password;
	String title;
	String firstNames;
	String lastNames;
	String houseIdentifier;
	String postcode;
	String email;
	String phoneNo;
	String dateOfBirth;
	String day;
	String month;
	String year;
	String type = "0";//0 = school 1 = teacher
	HashMap<String, Object> userHash = new HashMap<String, Object>();
	HashMap<String, String> userData = new HashMap<String, String>();
	
	public UserSignup(){ } //Default constructor

//username, title, password, firstNames, lastNames, houseIdentifier, postcode, email, phoneNo, dateOfBirth
	public void formHashmap() {
		this.userHash.put("username", this.username);
		this.userHash.put("password", this.password);
		this.userHash.put("accType", this.type);
		this.userData.put("title", this.title);
		this.userData.put("firstNames", this.firstNames);
		this.userData.put("lastNames", this.lastNames);
		this.userData.put("houseIdentifier", this.houseIdentifier);
		this.userData.put("postcode", this.postcode);
		this.userData.put("email", this.email);
		this.userData.put("phoneNo", this.phoneNo);
		this.userData.put("dateOfBirth", this.dateOfBirth);
		this.userHash.put("userData", this.userData);
	}
	
	public String signupUser() {
		formHashmap(); //Gather data from the form
		Connection connection = null;
		try {
			System.out.println(this.year + "-" + this.month + "-" + this.day);
			//Retrieve the database object
			DataSource vysusdb = (DataSource)((Context)new InitialContext()).lookup("java:/vysusDB");
			//Connect to the database
			connection = vysusdb.getConnection();
			//Initialise the request
			SignUp newUser = new SignUp(this.userHash, connection);
			
			newUser.execute();

			//add data to session
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getSessionMap().put("Username", this.username);
			Map<String, Object> requestMap = context.getExternalContext().getSessionMap();
			
			System.out.println(requestMap.get("Username"));
			
			return "myProfile.jsf";
		} catch(InvalidDataException e) {
			return "signup"; // But set the flag saying the user is not unique
		} catch(DBProblemException | NamingException | SQLException ex) {
			String msg = "There was a problem:"+System.getProperty("line.separator");
			if(ex instanceof DBProblemException) msg += ((DBProblemException)ex).getNested().getMessage();
			else msg += ex.getMessage();
			System.out.println(msg);
			//Print msg somewhere
			return "404temp";
		} finally {
			try{ //TODO: what does this even mean?
				if(connection!=null) connection.close();
			}catch(SQLException e) {}
		}
	}

//Getter & Setters as per jsf specification	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstNames() {
		return firstNames;
	}
	public void setFirstNames(String firstNames) {
		this.firstNames = firstNames;
	}
	public String getLastNames() {
		return lastNames;
	}
	public void setLastNames(String lastNames) {
		this.lastNames = lastNames;
	}
	public String getHouseIdentifier() {
		return houseIdentifier;
	}
	public void setHouseIdentifier(String houseIdentifier) {
		this.houseIdentifier = houseIdentifier;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

}