package vysusWeb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
	int accType; //0 = Teachers 1 = Institutions 
	
	public Profile() {
		super();
		//Testing:
		//if(actor==null) redirect("index.jsf");
		gatherData();
	}
	
	//TODO: should this invalidate the session and redirect to the index.xhtml?
	void gatherData() {
		try(Connection connection = getConnection()){
			if(connection==null) return;
			Map<String, Object> fullData = new User(actor, account, connection).showFull();
			userData = DataConv.getObjectMap(fullData.get("userData"));
			accountData = DataConv.getObjectMap(fullData.get("accountData"));
			accType = Account.accType(account);
		} catch(InvalidDataException e) {
			String field = e.field();
			if(field!=null) if(field.equals("userID") || field.equals("username")) field = null;
			message(field, "Invalid field", e.message());
		} catch(DBProblemException e) {
			message("Uh-oh", "We had a problem executing your request");
		} catch (SQLException e) { }
	}

//Tab renderers:
	
	
	public boolean isAdmin() {
		if(accountData.get("admin")!=null) return (boolean)accountData.get("admin");
		else return false;
	}
	
//Getters and setters
	public int getAccType() {
		return this.accType;
	}
	public String getFullName() {
		return "Herr. Otto Renfield";
		//return (String)userData.get("fullName");
	}
	public String getDateOfBirth() {
		return "20-02-1962";
		//SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		//Date dbDOB = (Date)userData.get("dateOfBirth");
		//return format.format(new java.util.Date(dbDOB.getTime()));
	}
	public String getUserAddress() {
		return "sqrt(-1), Neverwhere Street </br> NVWHR, Notown";
		//This should use the address finding service tbh:
		//return (String)userData.get("houseIdentifier")+", "+(String)userData.get("postcode");
	}
	public String getUserEmail() {
		return "o.ren.24@realprovider.nope";
		//return (String)userData.get("email");
	}
	public String getUserPhoneNo() {
		return "+00 0000000000";
		//return (String)userData.get("phoneNo");
	}
	
	
}
