package vysus;

import java.util.Map;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Job extends StorageAbstract{
	
	protected Map<String, String> jobData = new HashMap<String, String>();
	
	// eventID relates to calendar
	// not sure what oneOff is
	protected static List<String> keys = new ArrayList<String>(Arrays.asList(
			"accountID", "subjectID", "title", "startDate", "endDate", "eventID", "oneOff", "subjectID", "requirements", "ratePerHour", "pending", 
			"accepted", "comment"));
	protected Map<String, String> changes = new HashMap<String, String>();
	
	//Queries:
		// incremental number 
		protected String deleteJob = "DELETE FROM Job WHERE jobID=?";
		protected String retrieveJob = "SELECT * FROM Job WHERE jobID=?";
		protected String createJob = "INSERT INTO Job(jobID, accountID, subjectID, title, startDate, endDate, eventID, oneOff, subjectID, requirements, ratePerHour,"
				+ " pending, accepted, comment) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		// isn't there a WHERE missing?
		protected String updateJob(List<String> changed) {
			String upd = "UPDATE Job SET " + changed.get(0);
			for(int i=1; i<changed.size(); i++) upd += "=?, " + changed.get(i);
			upd += "=? WHERE jobID=?";
			return upd;
		}
	
	protected Boolean verified;

	@Override
	public void create(Connection con) throws StorageException, DBProblemException {
		try(PreparedStatement insert = con.prepareStatement(createJob);) {
			//the qualification
			insert.setString(1, id.get("job"));
			insert.setString(2, id.get("accountID"));
			insert.setString(2, id.get("subjectID"));
			for(int i=0; i<keys.size(); i++) insert.setString(i+3, jobData.get(keys.get(i)));	
		} catch (SQLException e) { throw new DBProblemException(e); }
		
	}

	@Override
	public void retrieve() throws StorageException, InvalidDataException, DBProblemException {
		try (PreparedStatement retrieve = con.prepareStatement(retrieveJob);) {
			retrieve.setString(1, id.get("job"));
			try(ResultSet record = retrieve.executeQuery();){
				if(record.next()){
					for(String key : keys)jobData.put(key, record.getString(key));
					id.put("account", record.getString("accountID"));
				} else throw InvalidDataException.invalidJob();
			}
		} catch (SQLException e) { throw new DBProblemException(e); }
	}

	@Override
	public void delete() throws StorageException, InvalidDataException, DBProblemException {
		try(PreparedStatement delete = con.prepareStatement(deleteJob);) {
			delete.setString(1, id.get("job"));
			if(delete.executeUpdate() != 1) throw InvalidDataException.invalidQualification();
		} catch (SQLException e) { throw new DBProblemException(e); }
	}

	@Override
	public void update() throws StorageException, InvalidDataException, DBProblemException {
		List<String> changedFields = new ArrayList<String>(changes.keySet());
		try(PreparedStatement update = con.prepareStatement(updateJob(changedFields));) {
			//Setting up the statement:
			int i;
			for(i=0; i<changedFields.size(); i++) update.setString(i+1, changes.get(changedFields.get(i)));
			update.setString(i, id.get("job"));
			//Execution:
			if(update.executeUpdate() != 1) throw InvalidDataException.invalidQualification();
			else changes = new HashMap<String, String>();
		} catch (SQLException e) { throw new DBProblemException(e); }		
	}

	@Override
	public HashMap<String, Object> showMini() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Object> show() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Object> showFull() {
		// TODO Auto-generated method stub
		return null;
	}

}
