package vysusWeb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import storage.*;

@Named("institutionGet")
@ConversationScoped
public class InstitutionGet extends VysusBean implements Serializable {
	static List<String> types = new ArrayList<String>(Arrays.asList("Primary School","Secondary School","Further Education","University"));
	
	Map<String, Object> newData = new HashMap<String, Object>();

	@Inject
	@Named("uGet")
	private UserGet uGet;
	
	public InstitutionGet(){}
	@PostConstruct
	void onInit() {
		if(actor.isIn()) redirect("profile.jsf");
		actor.requestAccountData();
	}
	
	public void signupInstitution() {
		try {
			Map<String, Object> userData = uGet.userData();
			String accountID = '1'+(String)userData.get("username");
			newData.put("sysAdminID", uGet.getUsername());
			actor.signup((String)userData.remove("username"), (String)userData.remove("password"), accountID, userData, newData);
		} catch (InvalidDataException e) {
			actor.handleException(e, false);
		}
	}
	
	protected boolean hasChanged(String smt) {
		return smt!=null && !smt.equals("");
	}
	
//Getters and setters
	public String getInstName() {
		return actor.accountField("name");
	}
	public void setInstName(String instName) {
		if(hasChanged(instName) && !instName.equals(getInstName())) newData.put("name", instName);
	}

	public String getInstType() {
		return actor.accountField("type");
	}
	public void setInstType(String instType) {
		if(hasChanged(instType) && !instType.equals(getInstType())) newData.put("type", instType);
	}

	public String getBuildNo() {
		return actor.accountField("buildingNo");
	}
	public void setBuildNo(String buildNo) {
		if(hasChanged(buildNo) && !buildNo.equals(getBuildNo())) newData.put("buildingNo", buildNo);
	}

	public String getInstPostcode() {
		return actor.accountField("postcode");
	}
	public void setInstPostcode(String instPostcode) {
		if(hasChanged(instPostcode) && instPostcode.equals(getInstPostcode())) newData.put("postcode", instPostcode.replaceAll("\\s+",""));
	}

	public String getInstEmail() {
		return actor.accountField("email");
	}
	public void setInstEmail(String instEmail) {
		if(hasChanged(instEmail) && instEmail.equals(getInstEmail())) newData.put("email", instEmail);
	}

	public String getInstPhoneNo() {
		return actor.accountField("phoneNo");
	}
	public void setInstPhoneNo(String instPhoneNo) {
		if(hasChanged(instPhoneNo) && instPhoneNo.equals(getInstPhoneNo())) newData.put("phoneNo", instPhoneNo);
	}
	
	public List<String> getTypes() {
		return types;
	}
	public Map<String, Object> changes(){
		return newData;
	}
	
}


