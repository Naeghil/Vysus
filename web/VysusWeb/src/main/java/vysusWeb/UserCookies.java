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

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.*;

import request.*;
import storage.*;
@ManagedBean(name="UserCookies")
@SessionScoped

public class UserCookies {
	String username;
	
	UserCookies(){ }
	
	public void getCookie() {
		Cookie cookie = (Cookie) ExternalContext.getRequestCookieMap().get("username");
		String value = URLDecoder.decode(cookie.getValue(), "UTF-8");
	}
	
	
	public String getUsername() {
		
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}