package vysusWeb.bases;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import exceptions.InvalidDataException;

/**************************************
 * 			   VysusBase			  *
 * Provides basic functions available *
 * to all beans in the system		  *
 *************************************/

public class VysusBase {
//Methods to retrieve session objects:
	protected Map<String, Object> getSessionMap() {
		return getExternalContext().getSessionMap();
	}
	protected ExternalContext getExternalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}
//Forward and Redirect
	protected void forward(String url) { //TODO: Currently unused
		try { getExternalContext().dispatch(url); } 
		catch(IOException e) {
			message(null, "There was an error while redirecting you to the right place.");
		}
	}
	protected void redirect(String url) {
		try{ getExternalContext().redirect(url); } 
		catch(IOException e) {
			message(null, "There was an error while redirecting you to the right place.");
		}
	}
//Handles messages:
	protected void message(InvalidDataException e) {
		String field = e.field();
		String msg = e.message();
		if(field!=null) if(field.equals("userID")) field= "username";
		message(msg, "Invalid "+field);
	}
	protected void message(String message, String detail) { message(null, message, detail); }
	protected void message(String clientID, String message, String detail) {
		FacesContext.getCurrentInstance().addMessage(clientID, new FacesMessage(message, detail));
	}
//Produces a valid connection or displays error:
	protected Connection getConnection() {
		try {
			DataSource vysusdb = (DataSource)((Context)new InitialContext()).lookup("java:/vysusDB");
			return vysusdb.getConnection();
		} catch(NamingException | SQLException e) {
			message("Sorry, a database connection could not be established", "No connection");
		}
		return null;
	}
}