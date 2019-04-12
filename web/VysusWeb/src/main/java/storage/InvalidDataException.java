package storage;

/* ************************************************************************
 *                         InvalidDataException                           *
 * Thrown when data is not valid at a db level (e.g. wrong login details) *
 * may be also used from the validation methods, to save some code        *
 * ************************************************************************/
//TODO: consider changing to "NotFoundException" to make it clearer what it means
//Distinguish it from format problems such as "InvalidInputException"
 
import storage.StorageException;

 public class InvalidDataException extends StorageException {
	 String field;
	 String message;
	 
	public InvalidDataException(Exception nested, String field, String message) {
		nestedException = nested;
		this.field = field;
		this.message = message;
	}
	public InvalidDataException(String field, String message) {
		nestedException = null;
		this.field = field;
		this.message = message;
	}
	
	public String field() { return field; }
	public String message() { return message; }
	 
	//Common exceptions:
	//"username", "No such user");
	
	//("password", "Wrong password");

	//("qualification", "This qualification doesn't exist");

	//("rights", "You don't have the rights to perform this operation");

	//("id", "Record not found");

}