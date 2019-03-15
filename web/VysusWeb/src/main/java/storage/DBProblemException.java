package storage;

/* *********************************************************
 *                  DBProblemException                     *
 * Tells application that an SQLException happened         *
 * SQLException needs to be handled by who tries the query *
 * but the application needs to be notified                *
 * *********************************************************/
 
import storage.StorageException;
import java.sql.SQLException;

 
public class DBProblemException extends StorageException {
	public DBProblemException(SQLException e) { nestedException = e; }
}