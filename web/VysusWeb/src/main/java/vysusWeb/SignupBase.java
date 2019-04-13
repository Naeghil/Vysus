package vysusWeb;


import java.util.List;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean; 

import storage.*;

@ManagedBean(name="signup")
@RequestScoped

public class SignupBase extends VysusBean {
	//The reason why this is not a map is because there is no way to make it static:
	static List<String> monthWord = new ArrayList<String>(Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"));
	static List<String> monthNo = new ArrayList<String>(Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" ));
	
	Map<String, Object> userData = new HashMap<String, Object>();

	public SignupBase(){}

	public Map<String, Object> userData() throws InvalidDataException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateText = getYear()+"-"+getMonth()+"-"+getDay(); 
		Date userDOB = null;
		try {
			userDOB = new Date(format.parse(dateText).getTime()); 
		} catch(ParseException e) {
			throw new InvalidDataException(e, "year", "This is an invalid date");
		}
		userData.put("dateOfBirth", userDOB);
		
		return userData;
	}
	
	public void signup(Map<String, Object> accountData) {
		try (Connection connection = getConnection()){
			String username = (String)userData.get("username");
			String password = (String)userData.get("password");
			String accountID = (Integer)accountData.get("accType")+username;
			
			if(!User.isUnique(username, connection)) throw new InvalidDataException("username", "This username already exists.");
			
			new User(connection, username, password, userData(), accountData, accountID);
			
			//Login:
			getSessionMap().put("username", username);
			getSessionMap().put("account", accountID);
			
			redirect("myProfile.jsf");
			
		} catch(InvalidDataException e) {
			String field = e.field();
			String msg = e.message();
			if(field!=null) if(field.equals("userID")) field= "username";
			message(field, "Invalid field", msg);
		} catch(DBProblemException e) {
			message("Uh-oh", "We had a problem executing your request");
		}catch(SQLException e) {}
	}
	
//Getter and setters
	
	//For dropdown
	public List<String> getMonthWord() {
		return monthWord;
	}
	public String monthToNo(String month) {
		return monthNo.get(monthWord.indexOf(month));
	}
	//As per specification
	public String getUsername() {
		return (String)userData.get("username");
	}
	public void setUsername(String username) {
		userData.put("username", username);
	}
	public String getTitle() {
		return (String)userData.get("title");
	}
	public void setTitle(String title) {
		userData.put("title",  title);
	}
	public String getPassword() {
		return (String)userData.get("password");
	}
	public void setPassword(String password) {
		userData.put("password", password);
	}
	public String getFullName() {
		return (String)userData.get("fullName");
	}
	public void setFullName(String fullName) {
		userData.put("fullName", fullName);
	}
	public String getHouseIdentifier() {
		return (String)userData.get("houseIdentifier");
	}
	public void setHouseIdentifier(String houseIdentifier) {
		userData.put("houseIdentifier", houseIdentifier);
	}
	public String getPostcode() {
		return (String)userData.get("postcode");
	}
	public void setPostcode(String postcode) {
		userData.put("postcode", postcode);
	}
	public String getEmail() {
		return (String)userData.get("email");
	}
	public void setEmail(String email) {
		userData.put("email", email);
	}
	public String getPhoneNo() {
		return (String)userData.get("phoneNo");
	}
	public void setPhoneNo(String phoneNo) {
		userData.put("phoneNo", phoneNo);
	}
	public String getDay() {
		return (String)userData.get("day");
	}
	public void setDay(String day) {
		userData.put("day", day);
	}
	public String getMonth() {
		return (String)userData.get("month");
	}
	public void setMonth(String month) {
		userData.put("month", month);
	}
	public String getYear() {
		return (String)userData.get("year");
	}
	public void setYear(String year) {
		userData.put("year", year);
	}
}