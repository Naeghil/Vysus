package vysusWeb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import storage.*;
import exceptions.*;
import util.Conv;

/****************************************
 * 				  Staff  				*
 * SecondaryBean allowing display and	*
 * creation of staff for administrator	*
 * users of a school account			*
 ***************************************/

@Named("staff")
@ConversationScoped
public class Staff extends vysusWeb.bases.SecondaryBean implements Serializable {
	List<Map<String, String>> staff = new ArrayList<Map<String, String>>();
	
	Map<String, Object> newData = new HashMap<String, Object>();
	
	public void onLoad() { onLoad("yes"); }
	
	protected void loadData(Connection connection) throws DBProblemException, InvalidDataException {
		for (SecondaryStorage s : User.all(actor.account(), connection)) {
			Map<String, String> sData = s.showFull();
			if(!sData.get("username").equals(actor.actor())) toShow.add(s.showFull());
		}
	}
	
	protected void makeNew (Connection connection) throws InvalidDataException, DBProblemException {
		Date date = Conv.stringToDate(
			(String)newData.remove("year")+"-"+(String)newData.remove("month")+"-"+(String)newData.remove("day"));
		
		newData.put("dateOfBirth", date);
		newData.put("fullName", (String)newData.remove("title")+" "+(String)newData.remove("name"));
		newData.put("accountID", actor.account());
		
		new User((String)newData.remove("username"), (String)newData.remove("password"), newData, connection);
		
		redirect("staff.xhtml");
	}
	
	public void delete(String id) {
		try(Connection connection = getConnection()) {
			new User(id).delete(connection);
			redirect("staff.xhtml");
		} catch (DBProblemException | InvalidDataException | SQLException e) {
			actor.handleException(e, false);
		}
	}
	
//Getters and setters:
	public String getUsername() {
		return newData.containsKey("username") ? (String)newData.get("username") : "";
	}
	public void setUsername(String username) {
		newData.put("username", username);
	}
	public String getPassword() {
		return newData.containsKey("password") ? (String)newData.get("password") : "";
	}
	public void setPassword(String password) {
		newData.put("password", password);
	}
	
	public String getTitle() {
		return "";
	}
	public void setTitle(String title) {
		newData.put("title", title);
	}
	public String getName() {
		return "";
	}
	public void setName(String name) {
		newData.put("name", name);
	}
	
	public String getHouseID() {
		return "";
	}
	public void setHouseID(String houseID) {
		newData.put("houseIdentifier", houseID);
	}
	public String getPostcode() {
		return "";
	}
	public void setPostcode(String postcode) {
		newData.put("postcode", postcode.replaceAll("\\s+",""));
	}
	
	public String getDay() {
		return newData.containsKey("day") ? (String)newData.get("day") : "";
	}
	public void setDay(String day) {
		newData.put("day", day);
	}
	public String getMonth() {
		return newData.containsKey("month") ? (String)newData.get("day") : "";
	}
	public void setMonth(String month) {
		newData.put("month", month);
	}
	public String getYear() {
		return newData.containsKey("year") ? (String)newData.get("year") : "";
	}
	public void setYear(String year) {
		newData.put("year", year);
	}
	
	public String getEmail() {
		return newData.containsKey("email") ? (String)newData.get("email") : "";
	}
	public void setEmail(String email) {
		newData.put("email", email);
	}
	public String getPhoneNo() {
		return newData.containsKey("phoneNo") ? (String)newData.get("phoneNo") : "";
	}
	public void setPhoneNo(String phoneNo) {
		newData.put("phoneNo", phoneNo);
	}
}
