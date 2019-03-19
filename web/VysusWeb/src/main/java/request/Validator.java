package request;

//import java.util.regex.Matcher; 
import java.util.regex.Pattern; 

public class Validator {
		
	//checks that a string is within the max length allowed
	static boolean checkLength(int validatorLength, String userData) {
		if(userData.length() >= validatorLength) {
			return true;
		} else {
			return false;
		}
	}
	
	//checks that an email is in the correct format with some crazy regex
	static boolean checkEmailFormat(String userData) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                "[a-zA-Z0-9_+&*-]+)*@" + 
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                "A-Z]{2,7}$";
		Pattern emailPattern = Pattern.compile(emailRegex);
		
        if (userData == null) {
        	return false;
        } else if(emailPattern.matcher(userData).matches()) {
        	return true;
        } else {
        	return false;
        }
	}
	
	
}
