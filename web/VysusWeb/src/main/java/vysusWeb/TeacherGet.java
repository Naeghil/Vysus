package vysusWeb;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import storage.InvalidDataException;

@Named("teacherGet")
@RequestScoped
public class TeacherGet extends VysusBean {
	Map<String, Object> accountData = new HashMap<String, Object>();

	@Inject
	@Named("uGet")
	private UserGet uGet;
	
	public TeacherGet(){}
	@PostConstruct
	void onInit() {
		if(actor.isIn()) redirect("profile.jsf");
	}
	
	public void signupTeacher() {
		try {
			Map<String, Object> userData = uGet.userData();
			String accountID = '0'+(String)userData.get("username");
			actor.signup((String)userData.remove("username"), (String)userData.remove("password"), accountID, userData, accountData);
		} catch (InvalidDataException e) {
			actor.handleException(e, false);
		}
	}

//Getters and setters
	public float getMaxDistance() {
		return this.maxDistance;
	}
	public void setMaxDistance(float maxDistance) {
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


