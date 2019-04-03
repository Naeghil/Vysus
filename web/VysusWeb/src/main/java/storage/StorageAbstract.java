package storage;

/**************************************************************
 *						StorageAbstract						  *
 * Summarises the perks of objects representing db entities.  *
 * If these objects have nested objects, they are loaded in   *
 * a lazy fashion; otherwise they are loaded at construction. *
 **************************************************************/


import java.util.Map;
import java.util.HashMap;
import java.sql.*;


public abstract class StorageAbstract {
	//Some entities may have a composite private key, or foreign keys
	protected Map<String, String> id = new HashMap<String, String>();
	//The data corresponding to the object; implementation should provide a List<String> of known keys
	protected Map<String, String> data = new HashMap<String, String>();
	//Used to enact changes
	protected Map<String, String> changes = new HashMap<String, String>();
	
	//Creates a new record	
	protected abstract void create(Connection con) throws StorageException;
	//Retrieve database record, populating the object fields
	protected abstract void retrieve(Connection con) throws StorageException;
	//Delete the object record
	protected abstract void delete(Connection con) throws StorageException;
	//Applies changes to the record
	protected abstract void update(Connection con) throws StorageException;

//The following are methods used to construct views, return null if not applicable
	//Retrieves and shows minimal details doesn't need to be logged in
	public abstract Map<String, Object> showMini();
	//Retrieves and shows medium details, as seen by non-owner users
	public abstract Map<String, Object> show();
	//Retrieves and shows all data, as seen by owner users
	public abstract Map<String, Object> showFull();
}
