package storage;

/* ************************************************************************
 *                         InvalidDataException                           *
 * Thrown when data is not valid at a db level (e.g. wrong login details) *
 * may be also used from the validation methods, to save some code        *
 * ************************************************************************/
//TODO: consider changing to "NotFoundException" to make it clearer what it means
//Distinguish it from format problems such as "InvalidInputException"
 
import storage.StorageException;
import java.util.ArrayList; 

 public class InvalidDataException extends StorageException {
	private ArrayList<String> invalidFields;
	
	public InvalidDataException(Exception nested) {
		nestedException = nested;
	}
	
	public void addField(String field) { this.invalidFields.add(field); System.out.println(invalidFields);}
	public ArrayList<String> getFields() { return invalidFields; }
	 
	 //Common exceptions:
	 public static InvalidDataException invalidUser() {
		InvalidDataException e = new InvalidDataException(null);
		e.addField("username");
		return e;
	 }
	 public static InvalidDataException invalidPassword() {
		InvalidDataException e = new InvalidDataException(null);
		e.addField("password");
		return e;
	 }
	 public static InvalidDataException invalidQualification() {
		InvalidDataException e = new InvalidDataException(null);
		e.addField("qualification");
		return e;
	 }
 
 }