package vysusWeb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import java.sql.Connection;
import java.sql.SQLException;

import storage.*;

@ManagedBean(name="schoolSignup")
@SessionScoped
public class SchoolSignup extends VysusBean {
	
	List<String> types = new ArrayList<String>(Arrays.asList("Primary School","Secondary School","Further Education","University"));
	
	String institutionName;
	String institutionType;
	String institutionNo;
	String institutionPostcode;
	String institutionEmail;
	String institutionPhone;
	
	@ManagedProperty(value = "#{signup}")
	private SignupBase signup;
	
	public SchoolSignup(){
		super();

		if(actor!=null) redirect("myProfile.jsf");
	}
	
	public Map<String, Object> accountData() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("name", this.institutionName);
		data.put("type", this.institutionType);
		data.put("buildingIdentifier", this.institutionNo);
		data.put("postcode", this.institutionPostcode);
		data.put("email", this.institutionEmail);
		data.put("phoneNo", this.institutionPhone);
		data.put("accType", 1);
		data.put("sysAdminID", signup.getUsername());
		return data;
	}
	
	public void signupSchool() {
		signup.signup(accountData());
	}
	
	//Getters and setters

	public SignupBase getSignup() {
	    return signup;
	}
	public void setSignup (SignupBase signup) {
	    this.signup = signup;
	}

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	public String getInstitutionType() {
		return institutionType;
	}

	public void setInstitutionType(String institutionType) {
		this.institutionType = institutionType;
	}

	public String getInstitutionNo() {
		return institutionNo;
	}

	public void setInstitutionNo(String institutionNo) {
		this.institutionNo = institutionNo;
	}

	public String getInstitutionPostcode() {
		return institutionPostcode;
	}

	public void setInstitutionPostcode(String institutionPostcode) {
		this.institutionPostcode = institutionPostcode;
	}

	public String getInstitutionEmail() {
		return institutionEmail;
	}

	public void setInstitutionEmail(String institutionEmail) {
		this.institutionEmail = institutionEmail;
	}

	public String getInstitutionPhone() {
		return institutionPhone;
	}

	public void setInstitutionPhone(String institutionPhone) {
		this.institutionPhone = institutionPhone;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}
	



}


