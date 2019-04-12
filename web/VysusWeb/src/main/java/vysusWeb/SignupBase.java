package vysusWeb;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
public class SignupBase implements Serializable {
	//The reason why this is not a map is because there is no way to make it static:
	static List<String> monthWord = new ArrayList<String>(Arrays.asList(
			"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"));
	static List<String> monthNo = new ArrayList<String>(Arrays.asList(
			"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" ));
	
	static List<String> institutionText = new ArrayList<String>(Arrays.asList(
			"Primary eduction","Secondary education","College","Sixth Form","University"));
	static List<String> institutionData = new ArrayList<String>(Arrays.asList(
			"Primary eduction","Secondary education","College","Sixth Form","University"));
	
	String username;
	String password;
	String title;
	String fullName;
	String houseIdentifier;
	String postcode;
	String email;
	String phoneNo;
	java.sql.Date dateOfBirth;
	String day;
	String month;
	String year;
	String type = "0";//0 = school 1 = teacher
	HashMap<String, Object> userHash = new HashMap<String, Object>();
	HashMap<String, Object> userData = new HashMap<String, Object>();
	
	public SignupBase(){ } //Default constructor

	public void formHashmap() {
		this.userHash.put("username", this.username);
		this.userHash.put("password", this.password);
		this.userHash.put("accType", this.type);
		this.userData.put("fullName", this.title+" "+this.fullName);
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
			//System.out.println(this.year + "-" + this.month + "-" + this.day);
			
			
			//Change date to correct format
			
			try {
				Date newDate = new SimpleDateFormat().parse(this.year + "-" + this.month + "-" + this.day);
				//Date newDate = new SimpleDateFormat("yyyy-MM-dd").parse("1996-02-22");
				this.dateOfBirth = new java.sql.Date(newDate.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
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
	public List<String> getMonthWord() {
		return monthWord;
	}
	public String monthToNo(String month) {
		return monthNo.get(monthWord.indexOf(month));
	}
	
	public List<String> getInstitutionText() {
		return institutionText;
	}
	public String instituonToData(String institution) {
		return institutionData.get(institutionText.indexOf(institution));
	}
}