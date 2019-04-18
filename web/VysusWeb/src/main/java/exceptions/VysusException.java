package exceptions;

/* **********************************************************************
 *                           StorageException                           *
 * Generic storage Exception, possibly thrown when SQLExceptions happen.*
 * Data in these exceptions may be used to craft the error messages     *
 * **********************************************************************/

public class VysusException extends Exception {
	protected Exception nestedException;
	
	public VysusException() { 
		nestedException = null; 
	}
	public VysusException(Exception e) { 
		nestedException = e; 
	}
	
	public Exception getNested() { return nestedException; }
}