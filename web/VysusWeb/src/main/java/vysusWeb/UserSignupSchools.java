package vysusWeb;

import java.io.Serializable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean; 
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import request.*;
import storage.*;

@ManagedBean(name="signupSchools")
@SessionScoped
//username, title, password, firstNames, lastNames, houseIdentifier, postcode, email, phoneNo, dateOfBirth
public class UserSignupSchools extends SignupBase {
	static List<String> institutionText = new ArrayList<String>(Arrays.asList(
			"Primary eduction","Secondary education","College","Sixth Form","University"));
	static List<String> institutionData = new ArrayList<String>(Arrays.asList(
			"Primary eduction","Secondary education","College","Sixth Form","University"));
	
	
	public UserSignupSchools(){}
	
	public List<String> getInstitutionText() {
		return institutionText;
	}
	public String instituonToData(String institution) {
		return institutionData.get(institutionText.indexOf(institution));
	}
	
}