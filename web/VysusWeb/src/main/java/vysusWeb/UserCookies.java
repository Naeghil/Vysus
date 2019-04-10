package vysusWeb;

import java.io.Serializable;
import java.net.URLDecoder;

import javax.sql.DataSource;
import javax.ws.rs.core.Cookie;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.*;

import request.*;
import storage.*;
@ManagedBean(name="UserCookies")
@RequestScoped

public class UserCookies {
	String username;
	public UserCookies(){ }
	
	public Object getCookie() {
		System.out.println("Is this ever run?");
		FacesContext context = FacesContext.getCurrentInstance();
		
		Map<String, Object> requestMap = context.getExternalContext().getSessionMap();
		System.out.println(requestMap.get("Username"));
		return requestMap.get("Username");
	}

}