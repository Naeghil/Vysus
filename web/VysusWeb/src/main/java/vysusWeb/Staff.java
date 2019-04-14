package vysusWeb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import storage.*;

@Named("staff")
@ConversationScoped
public class Staff extends VysusBean implements Serializable {
	List<DispStaff> staff = new ArrayList<DispStaff>();
	
	public Staff() {}
	@PostConstruct
	void onInit() {
		try(Connection connection = getConnection()){
			for(storage.Staff staff : storage.Staff.allStaff(actor.account, connection)) {
				this.staff.add(new DispStaff(staff.showFull()));
			}
		} catch(DBProblemException | InvalidDataException | SQLException e) {
			actor.handleException(e, false);
		}
	}
	
	
	public boolean noStaff() {
		return staff.size()==0;
	}
}

class DispStaff {
	public String id;
	public String fullName;
	public String DOB;
	public String address;
	public String email;
	public String phoneNo;
	
	public DispStaff(Map<String, String> data) {
		id = data.get("username");
		fullName = data.get("fullName");
		DOB = data.get("DOB");
		address = data.get("address");
		email = data.get("email");
		phoneNo = data.get("phoneNo");
	}
}
