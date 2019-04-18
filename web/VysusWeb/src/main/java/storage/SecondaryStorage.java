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

public abstract class SecondaryStorage extends StorageAbstract {
//Constructors and initialisers:
	public SecondaryStorage(Object id) { super(id);	}
	public SecondaryStorage(Object id, Connection connection) throws DBProblemException, InvalidDataException {
		super(id, connection);
	}
	public SecondaryStorage(Object id, Map<String, Object> data, Connection connection) throws DBProblemException {
		super(id, data, connection);
	}
	
//Static methods
	public void delete(Connection connection) throws DBProblemException, InvalidDataException {
		this.delete(connection);
	}
	
	protected static List<SecondaryStorage> all(String className, Object queryBy, Connection connection, String idName, String table, String foreignKey)
		throws DBProblemException, InvalidDataException {
		String retrieveIDs = "SELECT "+idName+" FROM "+table+" WHERE "+foreignKey+"=?";
		System.out.println("SecondaryStorage.all: "+className+" "+queryBy.toString()+" "+retrieveIDs);
		List<Object> list = new ArrayList<Object>();
		List<SecondaryStorage> all = new ArrayList<SecondaryStorage>();
		try(PreparedStatement retList = connection.prepareStatement(retrieveIDs);) {
			retList.setObject(1, queryBy);
			try(ResultSet records = retList.executeQuery();) {
				while(records.next()) list.add(records.getObject(idName));
			}
			for(Object id : list) {
				Object [] params = {id, connection };
				System.out.println(id.toString());
				SecondaryStorage toAdd = (SecondaryStorage) Class.forName(className)
					.getConstructor(Object.class, Connection.class)
					.newInstance(params);
				all.add(toAdd);
			}
		} catch (SQLException e) { 
			throw new DBProblemException(e); 
		} catch(NoSuchMethodException | ClassNotFoundException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
			System.out.println("This is an error: "+e.getMessage());
		}
		
		return all;
	}
}
