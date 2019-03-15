package vysusWeb;

import java.io.Serializable;
import javax.faces.bean.ManagedBean; 
   // or import javax.inject.Named;
import javax.faces.bean.SessionScoped; 
   // or import javax.enterprise.context.SessionScoped;

@ManagedBean(name="user") // or @Named("user")
@SessionScoped
public class UserBean implements Serializable {
   private String name;
   private String password;
   private String correctUsername = "miles";
   private String correctPassword;
   private String customerID;
   UserHash idk = new UserHash();

   public UserBean(){
	   correctPassword = "12345";
   }
   
   public String getName() { 
	   return name; 
   }   
   
   public void setName(String newValue) { 
	   name = newValue; 
   }

   public String getPassword() { 
	   return password; 
   }
   
   public void setPassword(String newValue) { 
	   password = newValue; 
   } 
   
   public String getCustomerID() {
	   return customerID;
   }

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String passwordCheck(){
		   if ((this.password.matches(this.correctPassword)) && (this.name.matches(this.correctUsername))) {
			   return "correct";
		   } else {
			   return "incorrect password";
		   }
	}
	
	public String getCustomerData(){
		return idk.getHashResult(this.customerID);
	}
	
	public String getCustomerDataNoForm(String customerDataID){
		return idk.getHashResult(customerDataID);
	}
	
	public void onPageLoad() {
		this.correctUsername = "miles";
	}
}


