package vysusWeb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import storage.*;
import exceptions.*;

@Named("offers")
@ConversationScoped
public class Offers extends vysusWeb.bases.SecondaryBean implements Serializable {
	
	public void onLoad() { onLoad(""); }
	
	protected void loadData(Connection connection) throws DBProblemException, InvalidDataException {
		for (SecondaryStorage j : Job.offers(actor.account(), connection)) {
			Map<String, String> data = j.show();
			data.putAll(new Institution(data.get("schoolID")).showMini());
			toShow.add(data);
		}
	}
	
	public void accept(String id) {
		try (Connection connection = getConnection()) {
			new Job(Integer.parseInt(id), connection).accept(connection);
			redirect("jobs.xhtml");
		} catch (InvalidDataException | DBProblemException | SQLException e) {
			actor.handleException(e, false);
		}
	}
	
	//Doesn't make new, nor deletes anything:
	protected void makeNew (Connection connection) throws InvalidDataException, DBProblemException { }
	public void delete(String id) { }
	
}