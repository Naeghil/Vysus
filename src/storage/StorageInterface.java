package storage;

import java.util.ArrayList;

/* ********************************** 
 * 	Storage Interface           *
 * Summarises the perks of objects  *
 * representing db entities         *
 * *********************************/

public interface StorageInterface {
	//Some entities may have a composite private key
	public ArrayList<String> id;
	
	//Retrieve database record, populating the object fields
	public boolean retrieve(Connection con);
	//Delete the object record
	//usage: if(obj.delete(con)) obj = null;
	public boolean delete(Connection con);
	//Rewrites the whole record to the db
	//TODO: sensitivity to changes might be considered if it doesn't impact performance
	public boolean update(Connection con);
}
