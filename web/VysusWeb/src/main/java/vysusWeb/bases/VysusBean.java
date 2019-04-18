package vysusWeb.bases;

import javax.inject.Inject;
import javax.inject.Named;

public class VysusBean extends VysusBase {
	@Inject
	protected @Named("actor") vysusWeb.Actor actor;
}