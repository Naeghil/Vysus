package vysusWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/************************
 * 		  Login			*
 * This bean is related *
 * to index.jsf			*
 ***********************/

@Named("login")
@RequestScoped
public class Login extends vysusWeb.bases.VysusBean {
	String username;
	String password;
	
	public void login() {
		actor.login(username, password);
	}
	
//Getters and setters
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}