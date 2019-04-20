package storage;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import exceptions.DBProblemException;
import exceptions.InvalidDataException;

/*********************************************
 * 			SecondaryStorage			 	 *
 * Special class of storage objects that are *
 * not queried by id (often auto-incremented)*
 * and are inserted by foreign key			 *
 ********************************************/

public abstract class SecondaryStorage extends StorageAbstract {
//Constructors and initialisers:
	public SecondaryStorage(Object id) { super(id);	}
	public SecondaryStorage(Object id, Connection connection) throws DBProblemException, InvalidDataException {
		super(id, connection);
	}
	public SecondaryStorage(Object id, Map<String, Object> data, Connection connection) throws DBProblemException {
		super(id, data, connection);
	}
	
//"interface" methods
	//Makes delete public to allow for deletion from external contexts
	public void delete(Connection connection) throws DBProblemException, InvalidDataException {
		super.delete(connection);
	}
	//Retrieves all objects of a certain "className" class, with foreign key "queryBy"
	//Also includes information to build the proper query (idName, table, foreignKey)
	protected static List<SecondaryStorage> all(
		String className, Object queryBy, Connection connection, String idName, String table, String foreignKey)
		throws DBProblemException, InvalidDataException {
		String retrieveIDs = "SELECT "+idName+" FROM "+table+" WHERE "+foreignKey+"=?";
		//List of IDs of the objects to be returned
		List<Object> list = new ArrayList<Object>();
		//List to return
		List<SecondaryStorage> all = new ArrayList<SecondaryStorage>();
		try(PreparedStatement retList = connection.prepareStatement(retrieveIDs);) {
			retList.setObject(1, queryBy);
			try(ResultSet records = retList.executeQuery();) {
				while(records.next()) list.add(records.getObject(idName));
			}
			for(Object id : list) {
				Object [] params = {id, connection };
				//Builds an object of class "className" cast to its superclass SecondaryStorage
				SecondaryStorage toAdd = (SecondaryStorage) Class.forName(className)
					.getConstructor(Object.class, Connection.class)
					.newInstance(params);
				all.add(toAdd);
			}
		} catch (SQLException e) { throw new DBProblemException(e); 
		} catch(NoSuchMethodException | ClassNotFoundException | InvocationTargetException | 
				IllegalAccessException | InstantiationException e) {
			//These exceptions should not happen, as only
			//SecondaryStorage children can be retrieved by this method
			System.out.println("SecondaryStorage.all: "+e.getMessage());
		}
		return all;
	}
}
