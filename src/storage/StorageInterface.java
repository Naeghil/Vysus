package storage;

/* ***********************************************************
 *                     Storage Interface                     *
 * Summarises the perks of objects representing db entities  *
 * ***********************************************************/

import java.util.ArrayList;
import java.sql.*;


public interface StorageInterface {
	//Some entities may have a composite private key
	private ArrayList<String> id;
	
	//Usually given at creation
	private Connection con;
	
	//Creates a new record	
	public boolean create();
	//Retrieve database record, populating the object fields
	public boolean retrieve();
	//Delete the object record
	//usage: if(obj.delete()) obj = null;
	public boolean delete();
	//Rewrites the whole record to the db
	//TODO: sensitivity to changes might be considered if it doesn't impact performance
	public boolean update();
}
