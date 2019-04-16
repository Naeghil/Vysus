package vysusWeb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

//TODO: add a logger class that records database errors somewhere


@Named("login")
@RequestScoped
public class Login extends VysusBean {
	String username;
	String password;
	
	@Inject
	protected @Named("actor") Actor actor;
	
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