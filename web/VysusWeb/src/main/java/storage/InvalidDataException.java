package storage;

/* ************************************************************************
 *                         InvalidDataException                           *
 * Thrown when data is not valid at a db level (e.g. wrong login details) *
 * may be also used from the validation methods, to save some code        *
 * ************************************************************************/
//TODO: consider changing to "NotFoundException" to make it clearer what it means
//Distinguish it from format problems such as "InvalidInputException"
 
import storage.StorageException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

 public class InvalidDataException extends StorageException {
	//This map is <field, message>
	private Map<String, String> invalidFields = new HashMap<String, String>();
	
	public InvalidDataException(Exception nested) {
		nestedException = nested;
	}
	
	public void addField(String field, String message) { invalidFields.put(field, message); }
	public List<String> getFields() { return new ArrayList<String>(invalidFields.keySet()); }
	public String getMessage(String field) { return invalidFields.get(field); }
	 
	//Common exceptions:
	public static InvalidDataException invalidUser() {
		InvalidDataException e = new InvalidDataException(null);
		e.addField("username", "Wrong username");
		return e;
	}
	public static InvalidDataException invalidPassword() {
		InvalidDataException e = new InvalidDataException(null);
		e.addField("password", "Wrong password");
		return e;
	}
	public static InvalidDataException invalidQualification() {
		InvalidDataException e = new InvalidDataException(null);
		e.addField("qualification", "This qualification doesn't exist");
		return e;
	}
	public static InvalidDataException noRights() {
		InvalidDataException e = new InvalidDataException(null);
		e.addField("rights", "You don't have the rights to perform this operation");
		return e;
	}
	//This should not happen at all
	public static InvalidDataException invalidId() {
		InvalidDataException e = new InvalidDataException(null);
		e.addField("id", "Record not found");
		return e;
	}

}