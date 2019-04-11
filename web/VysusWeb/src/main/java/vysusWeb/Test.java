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
public class Test extends VysusBean {
	private UIComponent test;

    public void press() {
       String id = test.getClientId(getContext());
       message(id, "Message:", "this is a message");
       message("Message:", "this is a global message");
    }

    public void setTest(UIComponent test) {
        this.test= test;
    }

    public UIComponent getTest() {
        return test;
    }
}