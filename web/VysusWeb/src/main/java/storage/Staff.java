package storage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Staff extends User {
	public Staff(String username, Connection connection)
			throws DBProblemException, InvalidDataException {
		super(username, connection);
	}
	public Staff(Connection connection, String username, String password, Map<String, Object> data, String accountID)
			throws DBProblemException, InvalidDataException {
		super(username);
		this.data = data;
		this.data.put("accountID", accountID);
		create(connection);
	}
	
//Static show methods to be used by the Institution object
	public static List<Staff> allStaff(String accountID, Connection connection) throws DBProblemException, InvalidDataException {
		List<Staff> allStaff = new ArrayList<Staff>();
		for(String staffID : Staff.staffList(accountID, connection)) {
			allStaff.add(new Staff(staffID, connection));
		}
		return allStaff;
	}
	public static List<String> staffList(String accountID, Connection connection) 
		throws DBProblemException {
		List<String> staffList = new ArrayList<String>();
		try(PreparedStatement retList = connection.prepareStatement("SELECT userID FROM User WHERE accountID=?");) {
			retList.setString(1, accountID);
			try(ResultSet records = retList.executeQuery();) {
				while(records.next()) staffList.add(records.getString("userID"));
			}
		} catch (SQLException e) { throw new DBProblemException(e); }
		return staffList;
	}
}