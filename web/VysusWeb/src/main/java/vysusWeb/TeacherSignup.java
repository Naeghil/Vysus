package vysusWeb;

import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import java.sql.Connection;
import java.sql.SQLException;

import request.SignUp;
import storage.*;

@ManagedBean(name="teacherSignup")
@SessionScoped
public class TeacherSignup extends VysusBean {
	int maxDistance;
	float minRatePerHour;
	String aboutMe;
	
	@ManagedProperty(value = "#{signup}")
	private SignupBase signup;
	
	public TeacherSignup(){
		super();
		if(actor!=null) redirect("myProfile.jsf");
	}
	
	public Map<String, Object> accountData() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("maxDistance", maxDistance);
		data.put("minRatePerHour", minRatePerHour);
		data.put("aboutMe", aboutMe);
		data.put("accType", 0);
		return data;
	}
	
	public void signupTeacher() {
		try (Connection connection = getConnection()){
			Map<String, Object> userData = signup.dataForSignup();
			Map<String, Object> accountData = this.accountData();
			//TODO: this doesn't take into account... the account!
			SignUp newUser = new SignUp(userData, accountData, connection);
			newUser.execute();
			
			getSessionMap().put("username", userData.get("username"));
			getSessionMap().put("accountType", 0);
			
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
	

//Getters and setters
	public int getMaxDistance() {
		return this.maxDistance;
	}
	public void setMaxDistance(int maxDistance) {
		this.maxDistance = maxDistance;
	}
	public float getMinRatePerHour() {
		return this.minRatePerHour;
	}
	public void setMinRatePerHour(float minimumRatePerHour) {
		this.minRatePerHour = minimumRatePerHour;
	}
	public String getAboutMe() {
		return this.aboutMe;
	}
	public void setAboutMe(String aboutMe) {
		if(aboutMe!=null) this.aboutMe = aboutMe;
		else aboutMe="";
	}
	
	public SignupBase getSignup() {
	    return signup;
	}
	public void setSignup (SignupBase signup) {
	    this.signup = signup;
	}

}


