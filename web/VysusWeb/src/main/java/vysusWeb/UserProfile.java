package vysusWeb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
// or import javax.inject.Named;
import javax.faces.bean.SessionScoped; 
   // or import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import request.RequestAbstract;
import request.ShowProfile;
import request.SignUp;
import storage.DBProblemException;
import storage.InvalidDataException;
import storage.StorageException;
import storage.User;
import util.DataConv;

@ManagedBean(name="profile") // or @Named("user")
@RequestScoped
public class UserProfile implements Serializable {
	Map<String,String> userData;
	
	public UserProfile() {
		
	}
	
	public void onPageLoad(){
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> requestMap = context.getExternalContext().getSessionMap();
		
		Connection connection = null;
		try {
			//Retrieve the database object
			DataSource vysusdb = (DataSource)((Context)new InitialContext()).lookup("java:/vysusDB");
			
			//Connect to the database
			connection = vysusdb.getConnection();
			
			//Cast username to string
			String username = null;
			Object uNameO = requestMap.get("Username");
			if(uNameO instanceof String) {
				username = (String)uNameO;
			} else {
				InvalidDataException e = new InvalidDataException(null);
				e.addField("username", "invalid user data");
				throw e;
			}
			
			//Initialise the request
			ShowProfile profile = new ShowProfile(username, username, connection);
			
			//inside profileData is a map<string,string>, call method "getStringHash"
			//getStringHash will take a map/hashmap object and return it as Map<String,String> if it is possible
			Map<String, Object> profileData = profile.execute(); 
			System.out.println(profileData);
			userData = DataConv.getStringMap(profileData.get("userData"));
			System.out.println(userData);
			//User user = newUser.getActor();
			return;
		} catch(InvalidDataException e) {
			return; // But set the flag saying the user is not unique
		} catch(DBProblemException | NamingException | SQLException ex) {
			String msg = "There was a problem:"+System.getProperty("line.separator");
			if(ex instanceof DBProblemException) msg += ((DBProblemException)ex).getNested().getMessage();
			else msg += ex.getMessage();
			System.out.println(msg);
			//Print msg somewhere
			return;
		} catch(StorageException e){
			return;
		} finally {
			try{ //TODO: what does this even mean?
				if(connection!=null) connection.close();
			} catch(SQLException e) {}
		}
	}
	public String getTitle() {
		return userData.get("title");
	}
	
	public String getFirstNames() {
		return userData.get("firstNames");
	}
	
	public String getLastNames() {
		return userData.get("lastNames");
	}
	
	public String getHouseIdentifier() {
		return userData.get("houseIdentifier");
	}
	
	public String getPostcode() {
		return userData.get("postcode");
	}
	
	public String getEmail() {
		return userData.get("email");
	}
	
	public String getPhoneNo() {
		return userData.get("phoneNo");
	}
	
	public String getDateOfBirth() {
		return userData.get("dateOfBirth");
	}
}