package dbmeta;
import java.util.Vector;
import java.util.Hashtable;

public class DBTable {
	private final Vector <String> colNames, colTypes;
	private int colCount;
	private Hashtable <String, String> ht = null;
	
	DBTable(Vector <String> aColNames, Vector <String> aColTypes) {
		colNames = aColNames;  
		colTypes = aColTypes;
		colCount = aColNames.size();
		createDBTableColumnHashtable();
	}
	
	private void createDBTableColumnHashtable() {
		ht = new Hashtable <String, String> ();
		String colName, colType;
		String key;
		for(int i=0; i<colCount; i++) {
			colName = colNames.get(i);
			colType = colTypes.get(i);
			key = (i-1) + "-" + "0";
			ht.put(key, colName);		
			key = (i-1) + "-" + "1";
			ht.put(key, colType);		
		}
	}
	
	int getColCount() {
		return colCount;
	}
	
	Vector <String> getColNames() {	return colNames;  }
	
	Vector <String> getColTypes() {	return colTypes;  }
	
	String getColNameAt(int index) {	
		return (String) colNames.get(index);  
	}
	
	String getColTypeAt(int index) {	
		return (String) colTypes.get(index);  
	}
	
	Hashtable <String, String> getMetaTable() {
		return ht;
	}
}
