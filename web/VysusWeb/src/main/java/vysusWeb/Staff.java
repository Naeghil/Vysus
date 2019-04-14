package vysusWeb;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.inject.Named;

@Named("staff")
public class Staff extends VysusBean {
	
	
	
	public boolean getNoStaff() {
		return true;
	}

}

class DispStaff {
	public String id;
	public String fullName;
	public String DOB;
	public String address;
	public String email;
	public String phoneNo;
	
	public DispStaff(Map<String, Object> data) {
		id = (String)data.get("id");
		fullName = (String)data.get("fullName");
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date dbDOB = (Date)data.get("dateOfBirth");
		DOB = format.format(new java.util.Date(dbDOB.getTime()));
		//Again, service:
		address = (String)data.get("houseIdentifier")+" "+(String)data.get("postcode");
		email = (String)data.get("email");
		phoneNo = (String)data.get("phoneNo");
	}
}
