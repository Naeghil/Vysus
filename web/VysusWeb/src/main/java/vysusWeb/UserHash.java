package vysusWeb;
import java.util.HashMap;

public class UserHash {
    /* This is how to declare HashMap */
    HashMap<String, String> hmap = new HashMap<String, String>();

    /*Adding elements to HashMap*/
    UserHash(){
	    this.hmap.put("Forename", "Miles");
	    this.hmap.put("Surname", "Everett");
	    this.hmap.put("DOB", "DDMMYYYY"); //hmm how am I gonna do this properly
	    this.hmap.put("LeastFavouriteBuilding", "MacRobert");   
    }
    
    public String getHashResult(String customerID){
    	return this.hmap.get(customerID);
    }
}
