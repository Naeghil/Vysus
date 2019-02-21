package storage;

/* *********************************************************
 *                  NoLogException                         *
 * It wasn't possible to write a log of this operation     *
 * Write a log in a file and handle it in another moment   *
 * *********************************************************/
 
import storage.StorageException;

public class NoLogException extends StorageException {
	private String log;

	public NoLogException(Exception e, String l) {
		nestedException = e;
		log = l;
	}
	
	String getLog() { return log; }

}