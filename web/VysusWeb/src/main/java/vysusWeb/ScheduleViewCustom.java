package vysusWeb;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@Named("scheduleViewCustom")
@ConversationScoped
public class ScheduleViewCustom implements Serializable {
 
    private ScheduleModel eventModel = new DefaultScheduleModel();
 
    private ScheduleEvent event = new DefaultScheduleEvent();
    
    private Date selectedDate;
    
    private boolean daily;
	private boolean weekly;
	
	protected Date patternEndDate;
 
    public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	@PostConstruct
    public void init() {
        eventModel.addEvent(new DefaultScheduleEvent("Teach this class", previousDay8Am(), previousDay10Am()));
        eventModel.addEvent(new DefaultScheduleEvent("Teach other class", today1Pm(), today2Pm()));
        eventModel.addEvent(new DefaultScheduleEvent("Teach great class", nextDay9Am(), nextDay11Am()));        
    }
     
    public Date getInitialDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);
         
        return calendar.getTime();
    }
 
    private Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
 
        return calendar;
    }
     
    private Date previousDay8Am() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.AM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
        t.set(Calendar.HOUR, 8);
         
        return t.getTime();
    }
     
    private Date previousDay10Am() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.AM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
        t.set(Calendar.HOUR, 10);
         
        return t.getTime();
    }
     
    private Date today1Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 1);
         
        return t.getTime();
    }
     
    private Date theDayAfter3Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 2);     
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 3);
         
        return t.getTime();
    }
 
    private Date today2Pm() {
        Calendar t = (Calendar) today().clone(); 
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 2);
         
        return t.getTime();
    }
     
    private Date nextDay9Am() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.AM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
        t.set(Calendar.HOUR, 9);
         
        return t.getTime();
    }
     
    private Date nextDay11Am() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.AM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
        t.set(Calendar.HOUR, 11);
         
        return t.getTime();
    }
    
    
    
    public Date getPatternEndDate() {
		return patternEndDate;
	}

	public void setPatternEndDate(Date patternEndDate) {
		this.patternEndDate = patternEndDate;
	}

	public void onActiveStatusChange() {
    	if (daily) setWeekly(false); 
    }
    
    public void onInactiveStatusChange() {
        if (weekly) setDaily(false);
     }
     
    public ScheduleEvent getEvent() {
        return event;
    }
 
    public void setEvent(DefaultScheduleEvent event) {
        this.event = event;
    }
     
    @SuppressWarnings("deprecation")
	public void addEvent() {
        if(event.getId() == null) {
        	if (isDaily() || isWeekly()) {
        		
        		Date startTime = event.getStartDate();
        		Date endTime = event.getEndDate();
        		
        		Calendar t = Calendar.getInstance();
        		t.setTime(event.getStartDate());
        		
        		if (isDaily()) {
        			// Integer.MAX_VALUE for the remainder of days melts the laptop
        			for (int i = 0; i < 1000; i++) {
        				
        				Calendar x = (Calendar) t.clone();
        				x.set(Calendar.AM_PM, Calendar.AM);
        		        x.set(Calendar.DATE, t.get(Calendar.DATE) + i);
        		        x.set(Calendar.HOUR, startTime.getHours());
        		        
        		        Calendar y = (Calendar) t.clone();
        				y.set(Calendar.AM_PM, Calendar.AM);
        		        y.set(Calendar.DATE, t.get(Calendar.DATE) + i);
        		        y.set(Calendar.HOUR, endTime.getHours());
        		        
        		        if (patternEndDate != null) {
        		        	if(y.getTime().before(patternEndDate)) {
        		        		ScheduleEvent repeatedEvent = new DefaultScheduleEvent(event.getTitle(), x.getTime() , y.getTime());
            					eventModel.addEvent(repeatedEvent);
            		        } else {
            		        	
            		        }   
        		        } else {
        		        	ScheduleEvent repeatedEvent = new DefaultScheduleEvent(event.getTitle(), x.getTime() , y.getTime());
        					eventModel.addEvent(repeatedEvent);
        		        }
        			}    
        		} else if (isWeekly()) {
        			for (int i = 1; i <= 1000; i++) {
        				Calendar x = (Calendar) t.clone();
        				x.set(Calendar.AM_PM, Calendar.AM);
        		        x.set(Calendar.DATE, t.get(Calendar.DATE) + 7*i);
        		        x.set(Calendar.HOUR, startTime.getHours());
        		        
        		        Calendar y = (Calendar) t.clone();
        				y.set(Calendar.AM_PM, Calendar.AM);
        		        y.set(Calendar.DATE, t.get(Calendar.DATE) + 7*i);
        		        y.set(Calendar.HOUR, endTime.getHours());
        		        
        		        if (patternEndDate != null) {
        		        	if(y.getTime().before(patternEndDate)) {
        		        		ScheduleEvent repeatedEvent = new DefaultScheduleEvent(event.getTitle(), x.getTime() , y.getTime());
            					eventModel.addEvent(repeatedEvent);
            		        } else {
            		        	break;
            		        } 
        		        } else {
        		        	ScheduleEvent repeatedEvent = new DefaultScheduleEvent(event.getTitle(), x.getTime() , y.getTime());
        					eventModel.addEvent(repeatedEvent);
        		        }
        			}
        			
        		}
        			
        	} else {
        		eventModel.addEvent(event);
        	}
            
        } else
            eventModel.updateEvent(event);
    }
     
    public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}

	public boolean isDaily() {
		return daily;
	}

	public void setDaily(boolean daily) {
		this.daily = daily;
	}
	
	public boolean isWeekly() {
		return weekly;
	}

	public void setWeekly(boolean weekly) {
		this.weekly = weekly;
	}

	public void onEventSelect(SelectEvent selectEvent) {
    	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "String 1", "String 2");
        
        addMessage(message);
        
        event = (DefaultScheduleEvent) selectEvent.getObject();
    }
    
    public void addRepeatingEvent() {
//    	event = new DefaultScheduleEvent(title, start, end, data)
    }
     
    public void onDateSelect(SelectEvent selectEvent) {
    	this.selectedDate = (Date) selectEvent.getObject();
    	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, this.selectedDate.toString(), "String 2");
        
        addMessage(message);
    
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    }
     
    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event Moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
         
        addMessage(message);
    }
     
    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
         
        addMessage(message);
    }
     
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}