package vysusWeb.bases;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exceptions.DBProblemException;
import exceptions.InvalidDataException;

/************************************************
 * 					SecondaryBean	  			*
 * SecondaryBean, as the name suggests, 		*
 * is related to SecondaryStorage objects 		*
 * Usually exhibit a "show and form" behaviour	*
 * Data is loaded at display request time		*
 ***********************************************/

public abstract class SecondaryBean extends VysusBean {
	protected List<Map<String, String>> toShow = null;
	protected Map<String, Object> newData = new HashMap<String, Object>();
	
	//Perform class-specific operations for data load
	public abstract void onLoad();
	//Perform generic operations for data load
	public void onLoad(String adminStatus){ //'adminStatus' also detects teacher accounts
		if(!actor.accountField("admin").equals(adminStatus)) {
			redirect("profile.xhtml");
			message("You don't have the rights to go there.", "Bad navigation");
			return;
		}
		try (Connection connection = getConnection()) {
			toShow = new ArrayList<Map<String, String>>();
			loadData(connection);
		} catch (DBProblemException | InvalidDataException | SQLException e) {
			actor.handleException(e, true);
		}
	}
	//Class specific data loading
	protected abstract void loadData(Connection connection) throws DBProblemException, InvalidDataException;
	//Perform generic operations for form submission
	public void addNew() {
		try (Connection connection = getConnection()) {
			makeNew(connection);
		} catch (DBProblemException | InvalidDataException | SQLException e) {
			actor.handleException(e, false);
		}
	}
	//Perform class-specific operations for object creation
	protected abstract void makeNew (Connection connection) throws InvalidDataException, DBProblemException;
	//Object deletion
	public abstract void delete(String id);
	
	//Rendering methods:
	public boolean noData() {
		if(toShow==null) onLoad();
		return toShow!=null && toShow.size()==0;
	}
	public List<Map<String, String>> gettoShow() {
		if(toShow==null) onLoad();
		return toShow;
	}
}
