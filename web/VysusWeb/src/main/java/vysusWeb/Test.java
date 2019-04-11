package vysusWeb;

import java.io.Serializable;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.*;

import javax.faces.bean.ManagedBean; 
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import request.*;
import storage.*;

@SuppressWarnings("unused")
@ManagedBean(name="test")
@SessionScoped
public class Test implements Serializable {
	private static final long serialVersionUID = 1L;
	public List<String> list = new ArrayList<String>();
	
	
	public Test(){
		list.add("first element");
		list.add("second element");
		list.add("third element");
	}
//Getter & Setters as per jsf specification	
	public List<String> getList(){
		return list;
	}
}