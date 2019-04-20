package vysusWeb.bases;

import javax.inject.Inject;
import javax.inject.Named;

/************************************
 * 			  VysusBean				*
 * Provides injection of the actor	*
 * bean to all beans using it		*
 ***********************************/

public class VysusBean extends VysusBase {
	@Inject
	protected @Named("actor") vysusWeb.Actor actor;
}