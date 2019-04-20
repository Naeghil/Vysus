package exceptions;

/************************************************************
 *                  InvalidDataException             		*
 * Thrown when data is not valid (e.g. wrong login details) *
 * *********************************************************/

 public class InvalidDataException extends VysusException {
	 String field;
	 String message;
	 
	public InvalidDataException(Exception nested, String field, String message) {
		super(nested);
		this.field = field;
		this.message = message;
	}
	public InvalidDataException(String field, String message) {
		this(null, field, message);
	}
	public InvalidDataException(String message) {
		this(null, null, message);
	}
	
	public String field() { return field; }
	public String message() { return message; }

}