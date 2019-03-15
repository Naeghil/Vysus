package storage;

/* **********************************************************************
 *                           StorageException                           *
 * Generic storage Exception, possibly thrown when SQLExceptions happen.*
 * Data in these exceptions may be used to craft the error messages     *
 * **********************************************************************/

import java.io.*;


public class StorageException extends Exception {
	protected Exception nestedException;
	
	public StorageException() { 
		nestedException = null; 
	}
	public StorageException(Exception e) { 
		nestedException = e; 
	}
	
	public Exception getNested() { return nestedException; }
}