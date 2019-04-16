package vysusWeb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import storage.*;
import util.Conv;

@Named("staff")
@ConversationScoped
public class Staff extends VysusBean implements Serializable {
	List<Map<String, String>> staff = new ArrayList<Map<String, String>>();
	
	Map<String, Object> newStaff = new HashMap<String, Object>();
	
	@Inject
	protected @Named("actor") Actor actor;
	
	@PostConstruct
	void onInit() {
		try(Connection connection = getConnection()){
			for(storage.Staff staff : storage.Staff.allStaff(actor.account, connection)) {
				this.staff.add(staff.showFull());
			}
		} catch(DBProblemException | InvalidDataException | SQLException e) {
			actor.handleException(e, false);
		}
	}
	
	public void addNew() {
		try(Connection connection = getConnection()) {
			Date date = Conv.stringToDate((String)newStaff.remove("year")+"-"+(String)newStaff.remove("month")+"-"+(String)newStaff.remove("day"));
			String fullName = (String)newStaff.remove("title")+" "+(String)newStaff.remove("name");
			
			newStaff.put("dateOfBirth", date);
			newStaff.put("fullName", fullName);
			newStaff.put("accountID", actor.account);
			
			new User(connection, (String)newStaff.remove("username"), (String)newStaff.remove("password"), newStaff);
			
		} catch (DBProblemException | InvalidDataException | SQLException e) {
			actor.handleException(e, false);
		}
	}
	
	public void delete(String userID) {
		try(Connection connection = getConnection()) {
			new storage.Staff(userID).deleteStaff(connection);
		} catch (DBProblemException | InvalidDataException | SQLException e) {
			actor.handleException(e, false);
		}
	}

//Renderer
	public boolean noStaff() {
		return staff.size()==0;
	}
	
	public List<Map<String, String>> getStaff(){
		return staff;
	}
	
//Getters and setters:
	public String getUsername() {
		return newStaff.containsKey("username") ? (String)newStaff.get("username") : "";
	}
	public void setUsername(String username) {
		newStaff.put("username", username);
	}
	public String getPassword() {
		return newStaff.containsKey("password") ? (String)newStaff.get("password") : "";
	}
	public void setPassword(String password) {
		newStaff.put("password", password);
	}
	
	public String getTitle() {
		return "";
	}
	public void setTitle(String title) {
		newStaff.put("title", title);
	}
	public String getName() {
		return "";
	}
	public void setName(String name) {
		newStaff.put("name", name);
	}
	
	public String getHouseID() {
		return "";
	}
	public void setHouseID(String houseID) {
		newStaff.put("houseIdentifier", houseID);
	}
	public String getPostcode() {
		return "";
	}
	public void setPostcode(String postcode) {
		newStaff.put("postcode", postcode);
	}
	
	public String getDay() {
		return newStaff.containsKey("day") ? (String)newStaff.get("day") : "";
	}
	public void setDay(String day) {
		newStaff.put("day", day);
	}
	public String getMonth() {
		return newStaff.containsKey("month") ? (String)newStaff.get("day") : "";
	}
	public void setMonth(String month) {
		newStaff.put("month", month);
	}
	public String getYear() {
		return newStaff.containsKey("year") ? (String)newStaff.get("year") : "";
	}
	public void setYear(String year) {
		newStaff.put("year", year);
	}
	
	public String getEmail() {
		return newStaff.containsKey("email") ? (String)newStaff.get("email") : "";
	}
	public void setEmail(String email) {
		newStaff.put("email", email);
	}
	public String getPhoneNo() {
		return newStaff.containsKey("phoneNo") ? (String)newStaff.get("phoneNo") : "";
	}
	public void setPhoneNo(String phoneNo) {
		newStaff.put("phoneNo", phoneNo);
	}
}
