package vysusWeb.bases;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exceptions.DBProblemException;
import exceptions.InvalidDataException;

public abstract class SecondaryBean extends VysusBean {
	protected List<Map<String, String>> toShow = null;
	protected Map<String, Object> newData = new HashMap<String, Object>();
	
	public void onLoad(String adminStatus){
		if(!actor.accountField("admin").equals(adminStatus)) {
			redirect("profile.xhtml");
			message("You don't have the rights to go there.", "Bad navigation");
			return;
		}
		toShow = new ArrayList<Map<String, String>>();
		try (Connection connection = getConnection()) {
			loadData(connection);
		} catch (DBProblemException | InvalidDataException | SQLException e) {
			actor.handleException(e, true);
		}
	}

	protected abstract void loadData(Connection connection) throws DBProblemException, InvalidDataException;
	
	public void addNew() {
		try (Connection connection = getConnection()) {
			makeNew(connection);
		} catch (DBProblemException | InvalidDataException | SQLException e) {
			actor.handleException(e, false);
		}
	}
	
	protected abstract void makeNew (Connection connection) throws InvalidDataException, DBProblemException;
	
	public abstract void delete(String id);
	
	public boolean noData() {
		return toShow!=null && toShow.size()==0;
	}
	
	public List<Map<String, String>> gettoShow() {
		return toShow;
	}
}
