package vysusWeb;

import java.io.Serializable;
import java.util.HashMap;

import javax.faces.bean.ManagedBean; 
import javax.faces.bean.SessionScoped; 
import request.*;
import storage.*;
@ManagedBean(name="signup")
@SessionScoped
//username, title, password, firstNames, lastNames, houseIdentifier, postcode, email, phoneNo, dateOfBirth
public class UserSignup implements Serializable {
	String username;
	String password;
	String title;
	String firstNames;
	String lastNames;
	String houseIdentifier;
	String postcode;
	String email;
	String phoneNo;
	String dateOfBirth;
	String type = "0";//0 = school 1 = teacher
	HashMap<String, Object> userHash = new HashMap<String, Object>();
	HashMap<String, String> userData = new HashMap<String, String>();
	
	public UserSignup(){
		
	}

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

	public String getFirstNames() {
		return firstNames;
	}

	public void setFirstNames(String firstNames) {
		this.firstNames = firstNames;
	}

	public String getLastNames() {
		return lastNames;
	}

	public void setLastNames(String lastNames) {
		this.lastNames = lastNames;
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
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	//username, title, password, firstNames, lastNames, houseIdentifier, postcode, email, phoneNo, dateOfBirth
	public void formHashmap() {
		this.userHash.put("username", this.username);
		this.userHash.put("password", this.password);
		this.userHash.put("accType", this.type);
		this.userData.put("title", this.title);
		this.userData.put("firstNames", this.firstNames);
		this.userData.put("lastNames", this.lastNames);
		this.userData.put("houseIdentifier", this.houseIdentifier);
		this.userData.put("postcode", this.postcode);
		this.userData.put("email", this.email);
		this.userData.put("phoneNo", this.phoneNo);
		this.userData.put("dateOfBirth", this.dateOfBirth);
		this.userHash.put("userData", this.userData);
	}
	
	public String signupUser() {
		formHashmap();
		request.SignUp newUser = new request.SignUp(this.userHash);
		try {
			newUser.execute();
			storage.User user = newUser.getActor();
			return "index";
		} catch(DBProblemException e1) {
			Exception n = e1.getNested();
			return "404temp"; 
		} catch(InvalidDataException e2) {
			return "404temp";
		} catch(StorageException ex) /*new catch for every different error*/ {
			Exception n = ex.getNested();
			if(ex!= null) {
				//print getMessage
			}
		}
		return "SignupTest";
	}
	
	public String getHashData(String key) {
		
		return key;
	}

}