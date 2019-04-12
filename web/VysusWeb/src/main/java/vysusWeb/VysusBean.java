package vysusWeb;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import storage.User;

import java.sql.Connection;
import java.sql.SQLException;

public class VysusBean implements Serializable {
	protected String actor = null;
	protected String account = null;
	User user = null;
	
	public VysusBean() {
		//Set the actor to the currently logged user, if any:
		Object actorO = getSessionMap().get("username");
		if(actorO!=null) actor = (String)actorO;
		Object accountO = getSessionMap().get("account");
		if(accountO!=null) account = (String)accountO;
	}
	
//Methods to retrieve session objects:
	protected Map<String, Object> getSessionMap() {
		return getExternalContext().getSessionMap();
	}
	protected ExternalContext getExternalContext() {
		return getContext().getExternalContext();
	}
	protected FacesContext getContext() {
		return FacesContext.getCurrentInstance();
	}
//Forward and Redirect
	protected void forward(String url) {
		try {
			getExternalContext().dispatch(url);
		} catch(IOException e) {
			message(null, "There was an error while redirecting you to the right place.");
		}
	}
	protected void redirect(String url) {
		try{
			getExternalContext().redirect(url);
		} catch(IOException e) {
			message(null, "There was an error while redirecting you to the right place.");
		}
	}
//Adds a message to a component:
	protected void message(String message, String detail) { message(null, message, detail); }
	protected void message(String clientID, String message, String detail) {
		FacesContext.getCurrentInstance().addMessage(clientID, new FacesMessage(message, detail));
	}
//Get current viewID:
	protected String viewID() {
		return getContext().getViewRoot().getViewId();
	}
//Produces a valid connection or displays error:
	protected Connection getConnection() {
		try {
			DataSource vysusdb = (DataSource)((Context)new InitialContext()).lookup("java:/vysusDB");
			return vysusdb.getConnection();
		} catch(NamingException | SQLException e) {
			message("No connection", "Sorry, a database connection could not be established");
		}
		return null;
	}
}
