package vysusWeb;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean; 

@ManagedBean(name="navigation")
@SessionScoped
public class Navigation extends VysusBean{
	String userPicture;

	public Navigation(){
		super();
		//Since it's not implemented:
		userPicture = "resources/images/propic-default.jpg";
	}
	
	public void logout() {
		getExternalContext().invalidateSession();
		redirect("index.jsf");
	}
//Sidebar buttons rendering:
	
	
	
//Getters and setters
	public String getUserPicture() {
		return this.userPicture;
	}
}