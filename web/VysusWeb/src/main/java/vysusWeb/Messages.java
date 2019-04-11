package vysusWeb;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;


public class Messages implements Serializable {
    private static final long serialVersionUID = -6479897299239746841L;
    private static final String BEAN_NAME = Messages.class.getName();
    private String messageHeader;
    private String messageImage;
    private Severity severityLevel;

    public PageMessages() {
        messageHeader = null;

        // See if there are messages queued for the page
        severityLevel = getFacesContext().getMaximumSeverity();

        if (null != severityLevel) {
            getLogger().debug("Severity Level Trapped: level = '" +
            severityLevel.toString() + "'");

            if (severityLevel.equals(FacesMessage.SEVERITY_ERROR)) {
            messageHeader = Messages.getString("PAGE_MESSAGE_ERROR");
            messageImage = "resources/warning.gif";
            } else if (severityLevel.equals(FacesMessage.SEVERITY_INFO)) {
            messageHeader = null;
            messageImage = "resources/information.gif";
            } else if (severityLevel.equals(FacesMessage.SEVERITY_WARN)) {
            messageHeader = null;
            messageImage = "resources/warning.gif";
            } else if (severityLevel.equals(FacesMessage.SEVERITY_FATAL)) {
            messageHeader = Messages.getString("PAGE_FATAL_ERROR");
            messageImage = "resources/stop.gif";
            }
        } else {
            getLogger().debug("Severity Level Trapped: level = 'null'");
        }
    }

    public Boolean getRenderMessage() {
        return new Boolean(StringUtils.isNotBlank(getMessageHeader()));
    }

    public String getBeanName() {
        return BEAN_NAME;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public String getMessageImage() {
        return messageImage;
    }
}
