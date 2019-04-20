package exceptions;

/************************************************************************
 *                           StorageException                           *
 * Generic custom Exception, whose subclasses are used internally to	*
 * signal certain common error for error display and log purposes		*
 * Data in these exceptions may be used to craft the error messages     *
 * **********************************************************************/

public abstract class VysusException extends Exception {
	protected Exception nestedException;
	
	public VysusException() { 
		nestedException = null; 
	}
	public VysusException(Exception e) { 
		nestedException = e; 
	}
	
	public Exception getNested() { return nestedException; }
}