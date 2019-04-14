package vysusWeb;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import storage.InvalidDataException;

@Named("teacherGet")
@RequestScoped
public class TeacherGet extends VysusBean {
	Map<String, Object> newData = new HashMap<String, Object>();

	@Inject
	@Named("uGet")
	private UserGet uGet;
	
	public TeacherGet(){}
	@PostConstruct
	void onInit() {
		//if(actor.isIn()) redirect("profile.jsf");
	}
	
	public void signupTeacher() {
		try {
			Map<String, Object> userData = uGet.userData();
			String accountID = '0'+(String)userData.get("username");
			actor.signup((String)userData.remove("username"), (String)userData.remove("password"), accountID, userData, newData);
		} catch (InvalidDataException e) {
			actor.handleException(e, false);
		}
	}

	protected boolean hasChanged(String smt) {
		return smt!=null && !smt.equals("");
	}
	
//Getters and setters
	public String getMaxDistance() {
		return actor.accountField("maxDistance");
	}
	public void setMaxDistance(float maxDistance) {
		if(Float.parseFloat(getMaxDistance()) != maxDistance) {
			newData.put("maxDistance", maxDistance);
		}
	}
	public String getMinRatePerHour() {
		return actor.accountField("minRatePerHour");
	}
	public void setMinRatePerHour(float minimumRatePerHour) {
		if(Float.parseFloat(getMinRatePerHour()) != minimumRatePerHour) {
			newData.put("minRatePerHour", minimumRatePerHour);
		}
	}
	public String getAboutMe() {
		return actor.accountField("aboutMe");
	}
	public void setAboutMe(String aboutMe) {
		if(hasChanged(aboutMe) && aboutMe.equals(getAboutMe()))  newData.put("aboutMe", aboutMe);
	}
}


