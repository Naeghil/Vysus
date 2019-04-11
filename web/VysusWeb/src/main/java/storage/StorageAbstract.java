package storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**************************************************************
 *						StorageAbstract						  *
 * Summarises features of objects representing DB entities.   *
 * If these objects have nested objects, they are loaded in   *
 * a lazy fashion; otherwise they are loaded at construction. *
 * The format of a storage object is:						  *
 * - Object-specific variables								  *
 * - Initialisation: constructors and variables setup		  *
 * 	- A constructor for existing objects, with retrieve option*
 * 	- A constructor for new objects							  *
 * 	- A setDBVariables method								  *
 * - Object-specific querying methods						  *
 * - Public interfaces of protected methods					  *
 * - Getters and show methods								  *
 *************************************************************/
//Object-specific variables
//Initialisation: constructors and variables setup
//Object-specific querying methods
//Public interfaces of protected methods
//Getters and show methods


import java.util.Map;


public abstract class StorageAbstract {
	//The data corresponding to the object, including their primary key as "id";
	protected Map<String, Object> data = new HashMap<String, Object>();
	//Used to enact changes
	protected Map<String, Object> changes = new HashMap<String, Object>();
	//List of known keys for the object, excluding the primary key;
	protected List<String> keys;
	//Statements:
	protected String create;
	protected String delete;
	protected String retrieve;
	protected abstract String update(List<String> changes);
	
	//Used to initialise object-specific statements and keys:
	protected abstract void setDBVariables();
	
	//Creates a new record	
	protected void create(Connection con) throws DBProblemException {
		try(PreparedStatement insert = con.prepareStatement(create);) {
			insert.setObject(1, data.get("id"));
			for(int i=0; i<keys.size(); i++) insert.setObject(i+2, data.get(keys.get(i)));
			insert.executeUpdate();
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	//Retrieve database record, populating the object fields
	protected void retrieve(Connection con) throws DBProblemException, InvalidDataException {
		try (PreparedStatement select = con.prepareStatement(retrieve);) {
			select.setObject(1, data.get("id"));
			try(ResultSet record = select.executeQuery();){
				if(record.next()){
					for(String key : keys) data.put(key, record.getObject(key));
				} else throw InvalidDataException.invalidId();
			}
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	//Delete the object record
	protected void delete(Connection con) throws InvalidDataException, DBProblemException {
		try(PreparedStatement remove = con.prepareStatement(delete);) {
			remove.setObject(1, data.get("id"));
			if(remove.executeUpdate() != 1) throw InvalidDataException.invalidId();
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	//Applies changes to the record
	protected void update(Connection con) throws InvalidDataException, DBProblemException {
		//Makes an ordered list to avoid potential problems with the Set class
		List<String> changed = new ArrayList<String>(changes.keySet());
		try(PreparedStatement update = con.prepareStatement(update(changed));) {
			//Setting up the statement:
			for(int i=0; i<changed.size(); i++) update.setObject(i+1, changes.get(changed.get(i)));
			update.setObject(changed.size()+1, data.get("id"));
			//Execution:
			if(update.executeUpdate() != 1) throw InvalidDataException.invalidId();
			else changes = new HashMap<String, Object>();
		} catch (SQLException e) { throw new DBProblemException(e); }
	}
	
//The following are methods used to construct views, return null if not applicable
	//Retrieves and shows minimal details doesn't need to be logged in
	public abstract Map<String, Object> showMini();
	//Retrieves and shows medium details, as seen by non-owner users
	public abstract Map<String, Object> show();
	//Retrieves and shows all data, as seen by owner users
	public abstract Map<String, Object> showFull();
}
