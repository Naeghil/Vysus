package vysusWeb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import storage.Account;
import storage.DBProblemException;
import storage.InvalidDataException;
import storage.User;

//Data has been refreshed by the *Get beans

@Named("profile")
@ConversationScoped
public class Profile extends VysusBean implements Serializable {
//The gets:
	@Inject
	@Named("uGet")
	private UserGet uGet;
	@Inject
	@Named("teacherGet")
	private TeacherGet teacherGet;
	@Inject
	@Named("institutionGet")
	private InstitutionGet institutionGet;
	
	int accType = -1; //0 = Teachers 1 = Institutions 
	
	public Profile() {}
	@PostConstruct
	void onInit() {
		/*if(!actor.isIn()) {
			redirect("index.jsf");
			return;
		}*/
		try {
			accType = actor.accType();
		} catch (InvalidDataException e) {
			actor.handleException(e, true);
		}
	}
	
	public void update() {
		try (Connection connection = getConnection()){
			if(connection == null) return;
			Map<String, Object> userChanges = uGet.userData();
			Map<String, Object> accountChanges = getChanges();
			
			new User(actor.actor).updateProfile(userChanges, connection);
			Account.getAccount(actor.account).updateAccount(accountChanges, connection);

			redirect("profile.jsf");
		} catch(DBProblemException | SQLException | InvalidDataException e) {
			actor.handleException(e, false);
		}
	}
	
	protected Map<String, Object> getChanges() {
		if(accType==0) return teacherGet.changes();
		if(accType==1) return institutionGet.changes();
		return null;
	}
	
//Tab renderers:	
	public boolean isAdmin() {
		return (actor.accountField("admin").equals("yes"));
	}
}