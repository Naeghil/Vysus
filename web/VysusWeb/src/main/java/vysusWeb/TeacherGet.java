package vysusWeb;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import exceptions.InvalidDataException;

@Named("teacherGet")
@ConversationScoped
public class TeacherGet extends vysusWeb.bases.VysusBean implements Serializable {
	Map<String, Object> newData = new HashMap<String, Object>();

	@Inject
	private @Named("uGet") UserGet uGet;
	
	float maxDistance;
	float minRatePerHour;
	
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
		if (!(actor.accountField("minRatePerHour").equals(""))) {
			return Float.parseFloat(actor.accountField("minRatePerHour"));
		} else {
			return 0;
		}
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
		if(!actor.isIn() || (hasChanged(aboutMe) && aboutMe.equals(getAboutMe())))  newData.put("aboutMe", aboutMe);
	}
	
	public Map<String, Object> changes(){
		return newData;
	}
}


