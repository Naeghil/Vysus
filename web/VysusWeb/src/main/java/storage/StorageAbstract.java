package storage;

/* ***********************************************************
 *                     StorageAbstract                       *
 * Summarises the perks of objects representing db entities  *
 * ***********************************************************/


import java.util.ArrayList;
import java.util.HashMap;
import java.sql.*;


public abstract class StorageAbstract {
	//Some entities may have a composite private key
	protected ArrayList<String> id;
	
	//Usually given at creation
	protected Connection con;
	
	//Creates a new record	
	public abstract void create() throws StorageException;
	//Retrieve database record, populating the object fields
	public abstract void retrieve() throws StorageException;
	//Delete the object record
	public abstract void delete() throws StorageException;
	//Applies changes to the record
	public abstract void update() throws StorageException;
//The following return null if not applicable
	//Retrieves and shows minimal details doesn't need to be logged in
	public abstract HashMap<String, Object> showMini();
	//Retrieves and shows medium details, as seen by non-owner users
	public abstract HashMap<String, Object> show();
	//Retrieves and shows all data, as seen by owner users
	public abstract HashMap<String, Object> showFull();
}
