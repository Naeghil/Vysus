package storage;

/* *******************************************************************************
 *                                    Session                                    *
 * Users logging in establish a session.                                         *
 * A session lasts maxInactivity if no requests are made.                        *
 * Mobile app has infinite maxInactivity                                         *
 * A session is not updated for automatic requests (e.g. messaging system)       *
 *********************************************************************************/
import java.sql.*;
import java.text.SimpleDateFormat;
import java.security.SecureRandom;
import java.util.Random;

//Usage for operations: ses = new Session(seskey, con); try ses.checkKey(usr, dev); catch goto(login page);
//Usage for login: session = new Session(user, dev, con)
//Usage for logout try ses.delete(); catch doSmt(); finally ses = null;

//TODO: implement difference from app
//TODO: why does session have a different log from the other stuff?

public class Session extends StorageAbstract {
	private String userId;	

	private Timestamp timeIn;
	private Timestamp timeOut;
	private String device; //max 64 characters

	private Random random;

	//Abstract implementation:
	public void create() throws DBProblemException { 
		//Create has no issue of invalid data; the only thing that can go wrong is the database connection itself
		id.add(generateKey());
		timeIn = new Timestamp(System.currentTimeMillis());
		String noTimeOutIdentifier = device.substring(device.length()-4);
		//If the session is created from an app or the user said so, then the timeout is always null
		if(noTimeOutIdentifier.equals("*app")|| noTimeOutIdentifier.equals("*rmd")) timeOut = null;
		//Otherwise, it expires in 15 mins increments   
		else timeOut = new Timestamp(timeIn.getTime() + 900000);
		try(PreparedStatement insert = con.prepareStatement("INSERT INTO Session(sessionID, userID, timeIn, timeOut, device) VALUES(?, ?, ?, ?, ?)");) {
			insert.setString(1, id.get(0));
			insert.setString(2, userId);
			insert.setTimestamp(3, timeIn);
			if(timeOut==null) insert.setNull(4, java.sql.Types.TIMESTAMP); 
			else insert.setTimestamp(4, timeOut);
			insert.setString(5, device);
			//!!! THE NESTED EXCEPTION CAN BE NULL!!! Additionally, if executeUpdate throws, the catch below should do the job
			if(insert.executeUpdate() != 1) throw new DBProblemException(null);
		} catch (SQLException e) {
			throw new DBProblemException(e);
		}
	}
	
	public void retrieve() throws InvalidSessionException, DBProblemException { 
		//Retrieve can either have a connection problem or the session cannot be found
		try(PreparedStatement retrieve = con.prepareStatement("SELECT * FROM Session WHERE sessionID=?");) {
			retrieve.setString(1, id.get(0));
			try(ResultSet result = retrieve.executeQuery();) {
				if(result.next()) {
				userId = result.getString("userID");
				timeIn = result.getTimestamp("timeIn");
				timeOut = result.getTimestamp("timeOut");
				device = result.getString("device");
				} else throw new InvalidSessionException(null);	
			} catch(SQLException e2) {
				throw new DBProblemException(e2);
			}
		} catch (SQLException e) {
			throw new DBProblemException(e);
		}
	}	

	public void update() throws DBProblemException, InvalidSessionException {
		//The only thing that can go wrong is the db itself, or the session has already been deleted
		//in the last case it's an invalid data causing an InvalidSessionException
		if(timeOut==null) return;
		try(PreparedStatement update = con.prepareStatement("UPDATE Session SET timeOut=? WHERE sessionID = ?");) {
			timeOut = new Timestamp(System.currentTimeMillis() + 900000);
			update.setTimestamp(1, timeOut);
			update.setString(2, id.get(0));
			//if executeUpdate throws, the catch below should do the job
			if(update.executeUpdate() != 1) throw new InvalidSessionException(new InvalidDataException(null, "sessionID"));
		} catch (SQLException e) {
			throw new DBProblemException(e);
		}
	}
	
	public void delete() throws DBProblemException, NoLogException {
		//The only thing that can go wrong is the db, but there is a log to be written	
		try(PreparedStatement log = con.prepareStatement("INSERT INTO SessionLog(userID, start, end, device) VALUES(?, ?, ?, ?)");
			PreparedStatement delete = con.prepareStatement("DELETE FROM Session WHERE sessionID=?");) {
			delete.setString(1, id.get(0));
			log.setString(1, userId);
			log.setTimestamp(2, timeIn);
			//TODO: Does it need to delete the noTimeOutIdentifier?
			log.setString(4, device);
			if(delete.executeUpdate() != 1) throw new DBProblemException(null);
			log.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			//Its request-level handling should temporarily save logs in a file and notify an admin
			if(log.executeUpdate() != 1) {
				String sessionEnd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.S").format(new Timestamp(System.currentTimeMillis()));
				throw new NoLogException(null, userId+" ended session "+id.get(0)+" from "+device+" at "+sessionEnd);
			}
		} catch (SQLException e) { 
			throw new DBProblemException(e);
		}
	}
	
	
	//Establish a new session
	Session(String usr, String dev, Connection con) throws DBProblemException { 
		userId = usr;
		device = dev; 
		this.con = con;
		timeIn = null; timeOut = null;
		create();
	}
	//Use an existing session
	Session(String key, Connection con) { 
		id.add(key); 
		this.con = con;
	}
	
	private String generateKey() {
		String pool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		String key = "";
		random = new SecureRandom();

		for(int i=0; i<64; i++){
			char newChar = pool.charAt(random.nextInt(64)); 
			key += Character.toString(newChar);		
		}

		return key;
	}
	
	
	//Getters:
	public String getUser() { return userId; }
	public String getDevice() { return device; }
	
	//Verifications:
	public void checkKey(String usr, String dev) throws DBProblemException, InvalidSessionException { 
		retrieve();
		if(timeOut.before(new Timestamp(System.currentTimeMillis()))) throw new InvalidSessionException(null);
		if(!userId.equals(usr) || !device.equals(dev)) throw new InvalidSessionException(null);
		update();
	}

	
}