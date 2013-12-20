package dbmeta;
import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;

public class DBMetaReader {
	private Vector <String> tables = null;
	private final int COLDATATYPES = 0, COLNAMES = 1;
	private Hashtable <Integer, DBTable> ht = null;
	
	DBMetaReader(Connection con) {
	  	try {
	  		tables = getTables(con);
	  		int tableCount = tables.size();
	  		ht = new Hashtable <Integer, DBTable> ();
	  		String table;
	  		Vector <String> colNames, colTypes;
	  		for(int i=0; i<tableCount; i++) {
	  			table = (String) tables.get(i); 
	  			colNames = getColumnNames(con, table);
	  			colTypes = getColumnTypes(con, table);
	  			ht.put(table.hashCode(), new DBTable(colNames, colTypes));
	  		}
	  		con.close();
		} catch (SQLException sqlE) {
			System.out.println(sqlE.getMessage());
		}	  		
	}
	
	private Vector <String> getTables(Connection con) throws SQLException {
		Vector <String> tables = new Vector<String>();
		String table = null;
		DatabaseMetaData dbmd = con.getMetaData(); 
		ResultSet results = dbmd.getTables(null, null, null, null);
	    while(results.next()) {
	    	table = results.getString(3);
	    	if(!table.contains("$")) {
	    		tables.add(table);
	    	}
	    }
		return tables;
	}	
	
	private Vector <String> getColumnNames(Connection con, String table) 
		throws SQLException {
		return getColumnMetaData(con, table, COLNAMES);
	}
	
	private Vector <String> getColumnTypes(Connection con, String table) 
		throws SQLException {
		return getColumnMetaData(con, table, COLDATATYPES);
	}	
	
	private Vector <String> getColumnMetaData(Connection con, String table, 
			int META) throws SQLException {
		Vector <String> metaData = new Vector<String>();
		String data = null;
		int bounds = 0;
		DatabaseMetaData dbmd = con.getMetaData();
		ResultSet results = dbmd.getColumns(null, null, table, null);
		if(META == 0) {
			while(results.next()) {
				data = results.getString(6);
				bounds = results.getInt(7);
				metaData.add(getDataType(data, bounds));
			}
		} else if(META == 1) {
			while(results.next()) {
				metaData.add(results.getString(4));
			}
		}
		return metaData;
	}
	
	private String getDataType(String data, int bounds) {
		return data.concat("(") + bounds + ")";
	}
	
	Vector <String> getTables() {
		return tables;
	}
	
	Hashtable <Integer, DBTable> getMetaData() {
		return ht;
	}
}