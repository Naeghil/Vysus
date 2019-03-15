package vysusWeb;

import java.io.Serializable;
import javax.faces.bean.ManagedBean; 
   // or import javax.inject.Named;
import javax.faces.bean.SessionScoped; 
   // or import javax.enterprise.context.SessionScoped;

@ManagedBean(name="profile") // or @Named("user")
@SessionScoped
public class UserProfile implements Serializable {
	
	
}