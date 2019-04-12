package vysusWeb;


import java.util.List;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean; 
import javax.faces.bean.SessionScoped;

import storage.InvalidDataException;

@ManagedBean(name="signup")
@SessionScoped

public class SignupBase extends VysusBean {
	//The reason why this is not a map is because there is no way to make it static:
	static List<String> monthWord = new ArrayList<String>(Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"));
	static List<String> monthNo = new ArrayList<String>(Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" ));
	
<<<<<<< HEAD
	Map<String, Object> userData = new HashMap<String, Object>();
=======
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
>>>>>>> 104df91452f782fdb032caea0d763a3627c35b50
	
	public SignupBase(){}

	public Map<String, Object> dataForSignup() throws InvalidDataException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateText = getYear()+"-"+getMonth()+"-"+getDay(); 
		Date userDOB = null;
		try {
<<<<<<< HEAD
			userDOB = new Date(format.parse(dateText).getTime()); 
		} catch(ParseException e) {
			InvalidDataException ex = new InvalidDataException(e);
			ex.addField("year", "This is an invalid date");
=======
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
>>>>>>> 104df91452f782fdb032caea0d763a3627c35b50
		}
		userData.put("dateOfBirth", userDOB);
		
		return userData;
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
	
	public List<String> getInstitutionText() {
		return institutionText;
	}
	public String instituonToData(String institution) {
		return institutionData.get(institutionText.indexOf(institution));
	}
}