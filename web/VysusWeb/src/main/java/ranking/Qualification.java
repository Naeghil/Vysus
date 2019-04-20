package ranking;

//Used to store and manipulate qualification data in the ranking process
public class Qualification {
	public String type;
	public String startDate;
	public String endDate;
	public String mainSubj;
	
	public Qualification(String initType, String mainSubj, String startDate, String endDate) {
		this.type = initType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.mainSubj = mainSubj;
	}
	
	
	public float addWorkYears(String subject) {
		if(!type.equals("Work experience")) return 0;
		float start = Float.parseFloat(startDate.substring(0, 4));
		float end = Float.parseFloat(endDate.substring(0, 4));
		float mod = mainSubj.equals(subject) ? (float)1.0 : (float)0.25;
		return (end-start)*mod;
	}
	
	public float addAcademic(String subject) {
		float mod = mainSubj.equals(subject) ? (float)1.0 : (float)0.25;
		float value = (float)(	
			type.equals("Undergraduate")? 1 	:
			type.equals("Postgraduate")	? 1.5	:
			type.equals("PHD")			? 2		: 
			0);
		return mod*value;
	}
}