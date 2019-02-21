package storage;

/* ******************************************************************
 *                    InvalidSessionException                       *
 * Thrown when a request is sent, but no valid session is found     *
 * It signals the BL that login is required again                   *
 * ******************************************************************/
 
 import storage.StorageException;
 
 public class InvalidSessionException extends StorageException {
 	//may be used to save the context of the request so that operations may be retried
	//I still don't know how context is stored; probably a Hashmap<String, String>
	private Object context;
 	
	public InvalidSessionException(Exception nested) { 
		nestedException = nested; 
	}
 
	public void setContext(Object con) { context = con; }
	public Object getContext() { return context; }
	
 }