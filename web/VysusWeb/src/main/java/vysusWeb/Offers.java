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

@Named("offers")
@ConversationScoped
public class Offers extends VysusBean implements Serializable {
	List<Map<String, String>> all = new ArrayList<Map<String, String>>(); 
	
	@PostConstruct
	void onLoad() {
		try (Connection connection = getConnection()) {
			for(Job j : Job.offersFor(actor.account(), connection)) {
				Map<String, String> data = j.show();
				data.putAll(new Institution(data.get("schoolID")).showMini());
				all.add(data);
			}	
		} catch (InvalidDataException | DBProblemException | SQLException e) {
			actor.handleException(e, false);
		}
		
	}
	
	public boolean noOffers() {
		return all.size()==0;
	}
	
	public void accept(String id) {
		try (Connection connection = getConnection()) {
			new Job(Integer.parseInt(id), connection).accept(connection);
			redirect("jobs.xhtml");
		} catch (InvalidDataException | DBProblemException | SQLException e) {
			actor.handleException(e, false);
		}
	}
	
	public List<Map<String, String>> getAll(){
		return all;
	}
}