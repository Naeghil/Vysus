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
	int accType = -1;
	boolean admin = false;
//The gets:
	@Inject
	private @Named("uGet") UserGet uGet;
	@Inject
	private @Named("teacherGet") TeacherGet teacherGet;
	@Inject
	private @Named("institutionGet") InstitutionGet institutionGet;
	@Inject
	protected @Named("actor") Actor actor;

	@PostConstruct
	void onInit() {
		try {
			accType = actor.accType();
			admin = actor.accountField("admin").equals("yes");
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
	
	public int getAccType() {
		return accType;
	}
	public boolean getAdmin() {
		return admin;
	}
}