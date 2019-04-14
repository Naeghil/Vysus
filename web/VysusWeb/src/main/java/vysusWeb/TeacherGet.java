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
	float maxDistance;
	float minRatePerHour;
	public TeacherGet(){}
	@PostConstruct
	void onInit() {
		if(actor.isIn()) redirect("profile.jsf");
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
	public float getMaxDistance() {
		if (!(actor.accountField("maxDistance").equals(""))) {
			return Float.parseFloat(actor.accountField("maxDistance"));
		} else {
			return 0;
		}
	}
	public void setMaxDistance(float maxDistance) {
		if(getMaxDistance() != maxDistance) {
			newData.put("maxDistance", maxDistance);
		}
		//this.maxDistance = maxDistance;
	}
	public float getMinRatePerHour() {
		return Float.parseFloat(actor.accountField("minRatePerHour"));
	}
	public void setMinRatePerHour(float minimumRatePerHour) {
		if(getMinRatePerHour() != minimumRatePerHour) {
			newData.put("minRatePerHour", minimumRatePerHour);
		}
		//this.minRatePerHour = minimumRatePerHour;
	}
	public String getAboutMe() {
		return actor.accountField("aboutMe");
	}
	public void setAboutMe(String aboutMe) {
		if(hasChanged(aboutMe) && aboutMe.equals(getAboutMe()))  newData.put("aboutMe", aboutMe);
	}
}


