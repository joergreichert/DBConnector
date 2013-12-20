package dbmeta;
import java.sql.Connection;
import java.util.Hashtable;
import java.util.Vector;

public class DBTableReader {
	private DBMetaReader meta;
	private Hashtable <Integer, DBTable> ht;
	
	DBTableReader(Connection con) {
		meta = new DBMetaReader(con);
		ht = meta.getMetaData();
	}
	
	Vector <String> getTables() {
		return meta.getTables(); 
	}
	
	DBTable getMetaDataForTable(String table) {
		return ht.get(table.hashCode());
	}	
	
	int getColumnCount(String table) {
		DBTable dbt = (DBTable) ht.get(table.hashCode());
		return dbt.getColCount();
	}		
	
	Vector <String> getColumnNames(String table) {
		DBTable dbt = (DBTable) ht.get(table.hashCode());
		return dbt.getColNames();
	}	
	
	String getColumnNameAt(String table, int index) {
		DBTable dbt = (DBTable) ht.get(table.hashCode());
		return dbt.getColNameAt(index);
	}
	
	String getColumnTypeAt(String table, int index) {
		DBTable dbt = (DBTable) ht.get(table.hashCode());
		return dbt.getColTypeAt(index);
	}		
	
	Vector <String> getColumnDataTypes(String table) {
		DBTable dbt = (DBTable) ht.get(table.hashCode());
		return dbt.getColTypes();
	}
	
	Hashtable <String, String> getMetaTable(String table) {
		DBTable dbt = (DBTable) ht.get(table.hashCode());
		return dbt.getMetaTable();		
	}		
}