package vysusWeb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;

import storage.Account;
import storage.DBProblemException;
import storage.InvalidDataException;
import storage.User;
import util.Conv;

//Data has been refreshed by the *Get beans


@Named("profile")
@RequestScoped
public class Profile extends VysusBean {
	Map<String, Object> userData = null;
	Map<String, Object> accountData = null;
	Map<String, Object> userChanges = new HashMap<String, Object>();
	Map<String, Object> accountChanges = new HashMap<String, Object>();
	
	List<Object> additionalData = null;
	int accType = -1; //0 = Teachers 1 = Institutions 
	
	static List<String> monthWord = new ArrayList<String>(Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"));
	static List<String> monthNo = new ArrayList<String>(Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" ));
	static List<String> types = new ArrayList<String>(Arrays.asList("Primary School","Secondary School","Further Education","University"));
	
	
	
	public Profile() {
		super();
		if(actor==null) redirect("index.jsf");
		gatherData();
	}
	
	//TODO: should this invalidate the session and redirect to the index.xhtml?
	void gatherData() {
		try(Connection connection = getConnection()){
			if(connection==null) return;
			extract(new User(actor, account, connection).showFull());
		} catch(InvalidDataException e) {
			String field = e.field();
			if(field!=null) if(field.equals("userID") || field.equals("username")) field = null;
			message(field, "Invalid field", e.message());
		} catch(DBProblemException e) {
			message("Uh-oh", "We had a problem executing your request");
		} catch (SQLException e) { }
	}

	void extract(Map<String, Object> fullData) throws InvalidDataException {
		userData = Conv.getObjectMap(fullData.get("userData"));
		accountData = Conv.getObjectMap(fullData.get("accountData"));
		if(isAdmin()) additionalData = Conv.getList(accountData.remove("staffData"));
		accType = Account.accType(account);
		System.out.println("From extract: " + accType);
	}
//Changes:
	public void changes() {
		try (Connection connection = getConnection()){
			if(connection == null) return;
			this.user = new User(actor, account, null);
			user.updateProfile(userChanges, connection);
			user.getAccount().updateAccount(accountChanges, connection);
			
			redirect("profile.jsf");
		} catch(InvalidDataException e) {
			String field = e.field();
			String msg = e.message();
			if(field!=null) if(field.equals("userID")) field= "username";
			message(field, "Invalid field", msg);
			System.out.println("Invalid data exception " + msg + " " + field);
		} catch(DBProblemException e) {
			message("Uh-oh", "We had a problem executing your request");
			if (e.getNested() != null) {
				System.out.println(e.getNested().getMessage());
			}
		}catch(SQLException e) {}
	}
	
	
//Tab renderers:	
	public boolean isAdmin() {
		if(accountData.get("admin")!=null) return (boolean)accountData.get("admin");
		else return false;
	}
	public boolean getNoStaff() {
		if(additionalData!=null) return additionalData.size()==0;
		return true;
	}
	public int getAccType() {
		System.out.println("From getAccType: " + this.accType);
		return this.accType;
	}
	
//Getters and setters
	public String getFullName() {
		return (String)userData.get("fullName");
	}
	
	public String getDateOfBirth() {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date dbDOB = (Date)userData.get("dateOfBirth");
		return format.format(new java.util.Date(dbDOB.getTime()));
	}
	public String getDay() {
		return "";
	}
	public void setDay(String day) {
		if(day!=null) if(day!="") userChanges.put("day", day);
	}
	public String getMonth() {
		return "";
	}
	public void setMonth(String month) {
		if(month!=null) if(month!="") userChanges.put("month", month);
	}
	public String getYear() {
		return "";
	}
	public void setYear(String year) {
		if(year!=null) if(year!="") userChanges.put("year", year);
	}
	
	public String getUserAddress() {
		//This should use the address finding service tbh:
		return (String)userData.get("houseIdentifier")+", "+(String)userData.get("postcode");
	}
	public void setHouseIdentifier(String houseIdentifier) {
		if(houseIdentifier!=null) if(houseIdentifier!="") userChanges.put("houseIdentifier", houseIdentifier);
	}
	public String getHouseIdentifier() {
		return "";
	}
	public void setUserPostcode(String userPostcode) {
		if(userPostcode!=null) if(userPostcode!="") userChanges.put("postcode", userPostcode);
	}
	public String getUserPostcode() {
		return "";
	}
	
	public String getUserEmail() {
		return (String)userData.get("email");
	}
	public void setUserEmail(String userEmail) {
		userChanges.put("email", userEmail);
	}
	public String getUserPhoneNo() {
		return (String)userData.get("phoneNo");
	}
	public void setUserPhoneNo(String userPhoneNo) {
		userChanges.put("phoneNo", userPhoneNo);
	}
	
	//Getters for teachers:
	public String getMaxDistance() {
		if(accType==0) return ((Float)accountData.get("maxDistance")).toString();
		else return "";
	}
	public void setMaxDistance(float maxDistance) {
		if(accType==0) accountChanges.put("maxDistance", maxDistance);
	}
	public String getMinRate() {
		if(accType==0) return ((Float)accountData.get("minRatePerHour")).toString();
		else return "";
	}
	public void setMinRate(float minRate) {
		if(accType==0) accountChanges.put("minRatePerHour", minRate);
	}
	public String getAboutMe() {
		if(accType==0) return (String)accountData.get("aboutMe");
		else return "";
	}
	public void setAboutMe(String aboutMe) {
		if(accType==0 && aboutMe!=null) accountChanges.put("aboutMe", aboutMe);
	}
	//Getters for Institutions:
	public String getInstName() {
		if(accType==1) return (String)accountData.get("name");
		else return "";
	}
	public String getInstType() {
		if(accType==1) return (String)accountData.get("type");
		else return "";
	}
	public void setInstType(String instType) {
		if(accType==1) accountChanges.put("type", instType);
	}
	
	public String getInstAddress() {
		//Again, this should use the address service:
		if(accType==1) return (String)accountData.get("buildingIdentifier")+" "+(String)accountData.get("postcode");
		else return "";
	}
	public String getInstitutionNo() {
		return "";		
	}
	public void setInstitutionNo(String institutionNo) {
		if(accType==1) accountChanges.put("buldingIdentifier", institutionNo);
	}
	public String getInstitutionPostcode() {
		return "";
	}
	public void setInstitutionPostcode(String institutionPostcode) {
		if(accType==1) accountChanges.put("postcode", institutionPostcode);
	}
	
	public String getInstEmail() {
		if(accType==1) return (String)accountData.get("email");
		else return "";
	}
	public void setInstEmail(String email) {
		if(accType==1) accountChanges.put("email", email);
	}
	public String getInstPhoneNo() {
		if(accType==1) return (String)accountData.get("phoneNo");
		else return "";
	}
	public void setInstPhoneNo(String phoneNo) {
		if(accType==1) accountChanges.put("phoneNo", phoneNo);
	}
	//Getters for InstitutionAdmin:

	public List<DispStaff> getStaff() {
		List<DispStaff> staff = new ArrayList<DispStaff>();
		if(additionalData!=null) {
			for(Object staffDataO : additionalData) {
				Map<String, Object> staffData = Conv.getObjectMap(staffDataO);
				staff.add(new DispStaff(staffData));
			}
		}
		return staff;
	}
	
	//For dropdown
	public List<String> getMonthWord() {
		return monthWord;
	}
	public String monthToNo(String month) {
		return monthNo.get(monthWord.indexOf(month));
	}
	public List<String> getTypes() {
		return types;
	}
}

class DispStaff {
	public String id;
	public String fullName;
	public String DOB;
	public String address;
	public String email;
	public String phoneNo;
	
	public DispStaff(Map<String, Object> data) {
		id = (String)data.get("id");
		fullName = (String)data.get("fullName");
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date dbDOB = (Date)data.get("dateOfBirth");
		DOB = format.format(new java.util.Date(dbDOB.getTime()));
		//Again, service:
		address = (String)data.get("houseIdentifier")+" "+(String)data.get("postcode");
		email = (String)data.get("email");
		phoneNo = (String)data.get("phoneNo");
	}
}
