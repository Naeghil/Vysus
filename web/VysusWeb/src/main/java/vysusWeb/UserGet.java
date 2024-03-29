package vysusWeb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import util.Conv;
import exceptions.*;

/****************************************
 * 				  UserGet				*
 * This bean is specific for user data	*
 ***************************************/

@Named("uGet")
@ConversationScoped
public class UserGet extends vysusWeb.bases.VysusBean implements Serializable {
	//The reason why this is not a map is because it's tricky to make it static:
	static List<String> monthWord = new ArrayList<String>(Arrays.asList(
		"January", "February", "March", "April", "May", "June", 
		"July", "August", "September", "October", "November", "December"));
	static List<String> monthNo = new ArrayList<String>(Arrays.asList(
		"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" ));
	static List<String> titles = new ArrayList<String>(Arrays.asList("Mr", "Mrs", "Ms", "Sir", "Mme"));
	
	Map<String, Object> newData = new HashMap<String, Object>();

	public Map<String, Object> userData() throws InvalidDataException {
		checkDate();
		checkName();
		return newData;
	}
	
	protected void checkDate() throws InvalidDataException {
		String date = newData.remove("year")+"-"+newData.remove("month")+"-"+newData.remove("day");
		if(!actor.userField("DOB").equals(date)) {
			newData.put("dateOfBirth", Conv.stringToDate(date));
		}
	}
	protected void checkName() {
		String fName = newData.remove("title")+" "+newData.remove("fullName");
		if(!actor.userField("fullName").equals(fName)) {
			newData.put("fullName", fName);
		}
	}
	
	protected boolean hasChanged(String smt) {
		return smt!=null && !smt.equals("");
	}
	
//Getter and setters
	//For drop-down
	public List<String> getMonthWord() {
		return monthWord;
	}
	public String monthToNo(String month) {
		return monthNo.get(monthWord.indexOf(month));
	}
	public List<String> getTitles(){
		return titles;
	}
	//Per specification: fields are "get" from actor and "set" to newData
	public String getUsername() {
		return actor.userField("username");
	}
	public void setUsername(String username) {
		if(hasChanged(username) && !username.equals(getUsername())) newData.put("username", username);
	}
	public String getPassword() {
		return "";
	}
	public void setPassword(String password) {
		if(actor.isIn() && hasChanged(password)) newData.put("newPassword", password);
		else newData.put("password", password);
	}
	//Used to change password
	public String getOldPassword() {
		return "";
	}
	public void setOldPassword(String oldPassword) {
		if(actor.isIn() && hasChanged(oldPassword)) newData.put("oldPassword", oldPassword);
	}
	//The fullName is checked separately
	public String getTitle() {
		return actor.userField("fullName").split(" ")[0];
	}
	public void setTitle(String title) {
		newData.put("title",  title);
	}
	//What a mess...
	public String getFullName() {
		String name = actor.userField("fullName");
		if(name.equals("")) return "";
		String fName[] = name.split(" ");
		name = fName[1];
		if(fName.length>2) for(String part : Arrays.copyOfRange(fName, 1, fName.length)) {
			name+=" "+part;
		}
		return name;
	}
	public void setFullName(String fullName) {
		newData.put("fullName", fullName);
	}
	public String getHouseIdentifier() {
		return "";
	}
	//The address will, unfortunately, be updated every time because it can't be null
	public void setHouseIdentifier(String houseIdentifier) {
		if(hasChanged(houseIdentifier)) newData.put("houseIdentifier", houseIdentifier);
	}
	public String getPostcode() {
		return "";
	}
	public void setPostcode(String postcode) {
		if(hasChanged(postcode)) newData.put("postcode", postcode.replaceAll("\\s+",""));
	}
	public String getEmail() {
		return actor.userField("email");
	}
	public void setEmail(String email) {
		if(hasChanged(email) && !email.equals(getEmail()))newData.put("email", email);
	}
	public String getPhoneNo() {
		return actor.userField("phoneNo");
	}
	public void setPhoneNo(String phoneNo) {
		if(hasChanged(phoneNo) && !phoneNo.equals(getPhoneNo())) newData.put("phoneNo", phoneNo);
	}
	//The date is checked separately
	public String getDay() {
		String[] date = actor.userField("DOB").split("-");
		return (actor.isIn() && date.length>1) ? date[2] : "";
	}
	//The date is checked separately
	public void setDay(String day) {
		if (day.length() == 1) {
			day = "0" + day;
		}
		newData.put("day", day);
	}
	public String getMonth() {
		String[] date = actor.userField("DOB").split("-");
		return (actor.isIn() && date.length>1) ? date[1] : "";
	}
	public void setMonth(String month) {
		newData.put("month", month);
	}
	public String getYear() {
		String[] date = actor.userField("DOB").split("-");
		return (actor.isIn() && date.length>1) ? date[0] : "";
	}
	public void setYear(String year) {
		newData.put("year", year);
	}
	
	public Map<String, Object> getData(){
		return newData;
	}
}