package dbmeta.data;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;
/**
 * Datenobjekt Tabelle mit Spalten (Name, Typ), 
 * Primärschlüssel, Fremdschlüssel
 * @author Jörg Reichert
 *
 */
public class DBTable {
    private String dbName, schemaName, tableName;
    private final Vector <String> colNames, colTypes, 
        primaryKeys;
    private Enumeration <Vector<DBForeignKey>> foreignKeys;
	private int colCount;
	private Hashtable <String, String> ht = null;
    
    /**
     * @param aDbName      Datenbankname
     * @param aSchemaName  Schemaname
     * @param aTableName   Tabellenname
     * @param aColNames    Spaltennamen
     * @param aColTypes    Spaltentypen
     * @param aPrimaryKeys Primärschlüssel (Plural)
     * @param aForeignKeys Fremdschlüssel  (Plural) 
     */
     public DBTable(String aDbName, String aSchemaName, 
			String aTableName, Vector <String> aColNames,
            Vector <String> aColTypes, 
            Vector <String> aPrimaryKeys,
            Enumeration <Vector<DBForeignKey>> aForeignKeys) {
        dbName      = aDbName;
		schemaName  = aSchemaName;
		tableName   = aTableName;
        colNames    = aColNames;  
		colTypes    = aColTypes;
		colCount    = aColNames.size();
		primaryKeys = aPrimaryKeys;
        foreignKeys = aForeignKeys;
		createDBTableColumnHashtable();
	} // Konstruktor
     
     /**
      * @param aDbName      Datenbankname
      * @param aSchemaName  Schemaname
      * @param aTableName   Tabellenname
      * @param aColNames    Spaltennamen
      * @param aColTypes    Spaltentypen
      * @param aPrimaryKeys Primärschlüssel (Plural)
      * @param aForeignKeys Fremdschlüssel  (Plural) 
      */
      public DBTable(String aDbName, String aTableName, 
    		  Vector <String> aColNames, 
    		  Vector <String> aColTypes) {
    	  dbName      = aDbName;
    	  tableName   = aTableName;
    	  schemaName  = "";    	  
    	  colNames    = aColNames;  
    	  colTypes    = aColTypes;
    	  colCount    = aColNames.size();
    	  primaryKeys = null;
    	  foreignKeys = null;    	  
    	  createDBTableColumnHashtable();
 	} // Konstruktor     
	
    public String getDatabaseName() {
        return dbName;
    } // getDatabaseName    
    
    public String getSchemaName() {
        return schemaName;
    } // getSchemaName
    
    public Vector <String> getPrimaryKeys() {
        return primaryKeys;
    } // getPrimaryKeys
    
    public Enumeration <Vector<DBForeignKey>> getForeignKeys() {
        return foreignKeys;
    } // getForeignKeys    
    
    public String getTableName() {
        return tableName;
    } // getTableName
    
	public Vector <String> getColNames() {	
        return colNames;  
    } // getColNames
	
	public String getColNameAt(int index) {	
		return (String) colNames.get(index);  
	} // getColNameAt
	
    public Vector <String> getColTypes() {  
        return colTypes;  
    } // getColTypes
    
    public String getColTypeAt(int index) {	
		return (String) colTypes.get(index);  
	} // getColTypeAt
    
    public int getColCount() {
        return colCount;
    } // getColCount    
	
    /**
     * erstellt Hashtable mit Spaltennamen und 
     * -typen für den Gebrauch durch eine JTable
     */
    private void createDBTableColumnHashtable() {
        ht = new Hashtable <String, String> ();
        String colName, colType;
        String key;
        for(int i=0; i<colCount; i++) {
            colName = colNames.get(i);
            colType = colTypes.get(i);
            key = i + "-" + "0";
            ht.put(key, colName);       
            key = i + "-" + "1";
            ht.put(key, colType);       
        } // for (spaltenweise)
    } // createDBTableColumnHashtable    
    
	public Hashtable <String, String> getMetaTable() {
		return ht;
	} // getMetaTable
} // DBTable