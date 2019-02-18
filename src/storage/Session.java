package storage;

/* *******************************************************************************
 *                                    Session                                    *
 * Users logging in establish a session.                                         *
 * A session lasts maxInactivity if no requests are made.                        *
 * Mobile app has infinite maxInactivity                                         *
 * A session is not updated for automatic requests (e.g. messaging system)       *
 *********************************************************************************/
import java.time.Duration;
import java.security.SecureRandom;

//maxInactivity = 15min = 900s = 900000ms
//Usage for operations: if(!(Session(seskey, con).checkKey())) goto login page 
//Usage for login session = new Session(user, dev, con)
//Usage for logout if(ses.delete()) ses = null; else dosmt

public class Session implements StorageInterface {
	String userId;	

	private Timestamp timeIn;
	private Timestamp timeOut;
	private String device; //max 64 characters

	private final Random random;

	Session(String usr, String dev, Connection con) { 
		userId = usr;
		device = dev; 
		this.con = con;
		timeIn = null; timeOut = null;
		create();
	}

	Session(String key, Connection con) { 
		id.add(key); 
		this.con = con;
	}
	
	private String generateKey() {
		String pool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		String key = "";
		random = Object.requireNonNull(random);
		if(random == null) random = new SecureRandom();

		for(int i=0; i<64; i++){
			char newChar = pool.charAt(random.nextInt(64)); 
			key += Character.toString(newChar);		
		}

		return key;
	}

	//Interface implementation:
	public boolean create() {
		boolean success = true;
		try{ 
			id.add(generateKey);
			timeIn = new Timestamp(System.currentTimeMillis());
			timeOut = new Timestamp(timeIn.getTime() + 900000);
 
			String ins = "INSERT INTO Session(sessionID, userID, timeIn, timeOut, device) VALUES(?, ?, ?, ?, ?)";
			PreparedStaement insert = con.prepareStatement(ins);
			insert.setString(1, id.get(0));
			insert.setString(2, userId);
			insert.setTimestamp(3, timeIn);
			insert.setTimestamp(4, timeOut);
			insert.setString(5, device);
			if(insert.executeUpdate() != 1) success = false;
		} catch (Exception e) {
			success = false;
		} finally {
			if(insert!=null) insert.close());
			return success;
		}
	}

	public boolean retrieve() {
		boolean success = true;
		try{
			String ret = "SELECT * FROM Session WHERE SessionId=?";
			PreparedStatement retrieve = con.prepareStatement(ret);
			retrieve.setString(1, id.get(0));
			ResultSet result = retrieve.executeQuery();
			if(result.next()) {
				userId = result.getString("userId");
				timeIn = result.getTimestamp("timeIn");
				timeOut = result.getTimestamp("timeOut");
				device = result.getString("device");
			} else success = false;	
			
		} catch (Exception e) {
			success = false;
		} finally {
			if(retrieve!=null) retrieve.close();
			return success;
		}
	}	

	public boolean update() {
		timeOut = new Timestamp(System.currentTimeMillis() + 900000);
		boolean success = true;
		try{
			String ret = "UPDATE Session SET timeOut=? WHERE SessionID = ?";
			PreparedStatement update = con.prepareStatement(ret);
			update.setTimestamp(1, timeOut);
			update.setString(2, id.get(0));
			if(update.executeUpdate() != 1) success = false;
		} catch (Exception e) {
			success = false;
		} finally { 
			if(update!=null) update.close();
			return success;
		}
	}
	
	public boolean delete() {
		success = true;
		try{
			String lg = "INSERT INTO SessionLog(userId, start, end, device) VALUES(?, ?, ?, ?)";
			String cls = "DELETE FROM Session WHERE SessionId=?";
			PreparedStatement log = con.prepareStatement(lg);
			PreparedStatement del = con.prepareStatement(cls);
			del.setString(1, id.get(0));
			log.setString(1, userId);
			log.setTimestamp(2, timeIn);
			if(del.executeUpdate != 1) success = false;
			else {
				log.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
				if(log.executeUpdate != 1) success = false; //or somehow use an exception to save the log somewhere else?
			}
		} catch (Exception e) {
			success = false;
		} finally {
			if(log!=null) log.close();
			if(del!=null) del.close();
			return success;
		}
	}
	
	
	//Getters:
	public String getUser() { return userId; }
	public String getDevice() { return device; }
	
	//Verifications:
	public boolean checkKey(String usr, String dev) { 
		if(retrieve()) {
			if(timeOut.before(new Timestamp(System.currentTimeMillis())) return false;
			if(!userId.equals(usr) || !device.equals(dev)) return false;
			update(); //??
			return true;
		} else return false;
	
	}

	
}