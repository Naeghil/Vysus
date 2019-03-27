package storage;

/* ***********************************************************
 *                     StorageAbstract                       *
 * Summarises the perks of objects representing db entities  *
 * ***********************************************************/


import java.util.Map;
import java.util.HashMap;
import java.sql.*;


public abstract class StorageAbstract {
	//Some entities may have a composite private key, or foreign keys
	protected Map<String, String> id = new HashMap<String, String>();
	
	//Creates a new record	
	protected abstract void create(Connection con) throws StorageException;
	//Retrieve database record, populating the object fields
	protected abstract void retrieve(Connection con) throws StorageException;
	//Delete the object record
	protected abstract void delete(Connection con) throws StorageException;
	//Applies changes to the record
	protected abstract void update(Connection con) throws StorageException;
//The following return null if not applicable
	//Retrieves and shows minimal details doesn't need to be logged in
	public abstract Map<String, Object> showMini();
	//Retrieves and shows medium details, as seen by non-owner users
	public abstract Map<String, Object> show();
	//Retrieves and shows all data, as seen by owner users
	public abstract Map<String, Object> showFull();
}
