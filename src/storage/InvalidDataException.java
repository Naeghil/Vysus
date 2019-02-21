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
	String invalidField;
	
	public InvalidDataException(Exception nested, String inv) {
		nestedException = nested;
		invalidField = inv;
	}
	
	String getField() { return invalidField; };
 
 }