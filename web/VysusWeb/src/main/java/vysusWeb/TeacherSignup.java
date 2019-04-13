package vysusWeb;

import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

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
		if(actor!=null) redirect("Profile.jsf");
	}
	
	public Map<String, Object> accountData() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("maxDistance", maxDistance);
		data.put("minRatePerHour", minRatePerHour);
		data.put("aboutMe", aboutMe);
		data.put("accType", new Integer(0));
		return data;
	}
	
	public void signupTeacher() {
		signup.signup(accountData());
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


