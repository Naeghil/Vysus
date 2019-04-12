package vysusWeb;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import storage.Account;
import storage.User;

@ManagedBean(name="profile")
@RequestScoped
public class Profile extends VysusBean {
	List<tab> tabs = new ArrayList<tab>();
	User user;
	Account account;
	
	public Profile() {
		super();
		//Testing:
		//if(actor==null) redirect("index.jsf");
		setTabs();
	}
	
	void setTabs() {
		
	}

//Getters and setters
	public List<tab> getTabs() {
		return this.tabs;
	}
	
}

class tab {
	public String title;
	public String page;
	
	public tab() {}
	public tab(String title, String page) {
		this.title = title;
		this.page = page;
	}
	
	public static tab user(){
		return new tab("You", "resources/includes/user-profile-tab.xhtml");
	}
}
