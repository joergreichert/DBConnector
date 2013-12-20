package dbmeta;
import java.sql.Connection;
import java.util.Vector;
import java.util.Hashtable;

public class DBReader extends Connector {
    public final static long serialVersionUID = 0;
	private Connection connection = null;
	private DBTableReader tableReader = null;
	
	class DBDriver {
		final String name, driver, url;
	
		DBDriver(String aName, String aDriver, String aUrl) {
			name = aName;  driver = aDriver;  url = aUrl;
		}
	}
	
	void connect(String dbType, String host, String dbName, String user, 
		String pword) throws Exception {

		if(dbType.equals(Connector.MY_SQL)) {
			connection = super.getConnection(
					Connector.MY_SQL, host, dbName, user, pword);
		} else if(dbType.equals(Connector.OFFICE)) {
			connection = super.getConnection(
					Connector.OFFICE, host, dbName, user, pword);
		} else if(dbType.equals(Connector.POSTGRES)) {
			connection = super.getConnection(
					Connector.POSTGRES, host, dbName, user, pword);			
		} else if(dbType.equals(Connector.ORACLE)) {
			connection = super.getConnection(
					Connector.ORACLE, host, dbName, user, pword);			
		} else {
			throw new Exception("Datenbanktyp existiert nicht");
		}
		tableReader = new DBTableReader(connection);
	}
	
	Vector <String> getTables() {
		Vector <String> tables = new Vector<String>();
		if(tableReader != null) {
			tables = tableReader.getTables();
		}
		return tables;
	}
	
	Vector <String> getColNames(String table) {
		Vector <String> colNames = new Vector<String>();
		if(tableReader != null) {
			colNames = tableReader.getColumnNames(table);
		}
		return colNames;
	}	
	
	Vector <String> getDataTypes(String table) {
		Vector <String> dataTypes = new Vector<String>();
		if(tableReader != null) {
			dataTypes = tableReader.getColumnDataTypes(table);
		}
		return dataTypes;
	}
	
	Hashtable <String, String> getMetaTable(String table) {
		return tableReader.getMetaTable(table);
	}
}