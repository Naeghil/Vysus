package vysusWeb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import storage.Account;
import storage.DBProblemException;
import storage.InvalidDataException;
import storage.User;
import util.DataConv;

@ManagedBean(name="profile")
@RequestScoped
public class Profile extends VysusBean {
	Map<String, Object> userData = null;
	Map<String, Object> accountData = null;
	List<Object> additionalData = null;
	int accType = -1; //0 = Teachers 1 = Institutions 
	
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
		userData = DataConv.getObjectMap(fullData.get("userData"));
		accountData = DataConv.getObjectMap(fullData.get("accountData"));
		if(isAdmin()) additionalData = DataConv.getList(accountData.remove("staffData"));
		accType = Account.accType(account);
		System.out.println(accType);
	}
//Changes:
	public void changes() {
		//Do the thing where you submit a map to the user, and then it divides the changes for the account and then the account takes care of it
		//Or use getAccount() to submit the changes to the account
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
	
	
//Getters and setters
	public int getAccType() {
		return this.accType;
	}
	public String getFullName() {
		//return "Herr. Otto Renfield";
		return (String)userData.get("fullName");
	}
	public String getDateOfBirth() {
		//return "20-02-1962";
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date dbDOB = (Date)userData.get("dateOfBirth");
		return format.format(new java.util.Date(dbDOB.getTime()));
	}
	public String getUserAddress() {
		//return "sqrt(-1), Neverwhere Street </br> NVWHR, Notown";
		//This should use the address finding service tbh:
		return (String)userData.get("houseIdentifier")+", "+(String)userData.get("postcode");
	}
	public String getUserEmail() {
		//return "o.ren.24@realprovider.nope";
		return (String)userData.get("email");
	}
	public String getUserPhoneNo() {
		//return "+00 0000000000";
		return (String)userData.get("phoneNo");
	}
	
	//Getters for teachers:
	public String getMaxDistance() {
		if(accType==0) return ((Float)accountData.get("maxDistance")).toString();
		else return "";
	}
	public String getMinRate() {
		if(accType==0) return ((Float)accountData.get("minRatePerHour")).toString();
		else return "";
	}
	public String getAboutMe() {
		if(accType==0) return (String)accountData.get("aboutMe");
		else return "";
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
	public String getInstAddress() {
		//Again, this should use the address service:
		if(accType==1) return (String)accountData.get("buildingIdentifier")+" "+(String)accountData.get("postcode");
		else return "";
	}
	public String getInstEmail() {
		if(accType==1) return (String)accountData.get("email");
		else return "";
	}
	public String getInstPhoneNo() {
		if(accType==1) return (String)accountData.get("phoneNo");
		else return "";
	}	
	//Getters for InstitutionAdmin:

	public List<DispStaff> getStaff() {
		List<DispStaff> staff = new ArrayList<DispStaff>();
		if(additionalData!=null) {
			for(Object staffDataO : additionalData) {
				Map<String, Object> staffData = DataConv.getObjectMap(staffDataO);
				staff.add(new DispStaff(staffData));
			}
		}
		return staff;
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
