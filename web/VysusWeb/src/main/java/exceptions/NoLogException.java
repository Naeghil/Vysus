package exceptions;

/***********************************************************
 *                  NoLogException                         *
 * It wasn't possible to write a log of this operation     *
 * Write a log in a file and handle it in another moment   *
 * *********************************************************/
 

public class NoLogException extends VysusException {
	private String log;

	public NoLogException(Exception e, String l) {
		nestedException = e;
		log = l;
	}
	
	String getLog() { return log; }

}