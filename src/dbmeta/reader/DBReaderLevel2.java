package dbmeta.reader;
import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import dbmeta.data.DBTable;
import dbmeta.data.DBForeignKey;
/**
 * liest mit SQL-Anfragen Metadaten (Schemata, Tabellen,
 * Spalten, Primär- und Gremschlüssel) aus der 
 * Datenbankverbindung aus 
 * @author Jörg Reichert
 */
public class DBReaderLevel2 {
	
    /**
     * @param con bestehende Datenbankverbindung
     * @return vorhandene Schemata in Datenbank
     * @throws SQLException
     */
    Vector <String> getSchemas(Connection con) 
        throws SQLException {
        Vector <String> schemas = new Vector<String>();
        DatabaseMetaData dbmd = con.getMetaData(); 
        ResultSet schemaRs = dbmd.getSchemas();
        while(schemaRs.next()) {
            schemas.add(schemaRs.getString("TABLE_SCHEM"));
        } // für alle Schemas
        schemaRs.close();
        return schemas;
    } // getSchemas
    
    /**
     * @param con bestehende Datenbankverbindung
     * @param schema Schemaname
     * @return Namen der Tabellen in diesem Schema
     * @throws SQLException
     */
    Vector <String> getTableNames(Connection con, 
            String schema) throws SQLException {
        Vector <String> tables = new Vector<String>();
        String table = null, tableType = null;
        DatabaseMetaData dbmd = con.getMetaData();        
        ResultSet tableRs = dbmd.getTables(null, schema, 
                    "%", null);
        while(tableRs.next()) {
            table = tableRs.getString("TABLE_NAME");
            tableType = tableRs.getString("TABLE_TYPE");
            if(tableType.equals("TABLE")) {
                tables.add(table);
            } // sortiert z.B. Views aus, nur Tabellen
        } // für alle Tabellen
        tableRs.close();
        return tables;        
    } // getTableNames
    
    /**
     * @param con bestehende Datenbankverbindung
     * @param dbName Datenbankname
     * @param schema Schemaname
     * @param tables Tabellennamen
     * @return Tabellenobjekte für diese Parameter 
     * @throws SQLException
     */
    public Enumeration <DBTable> createDBTableTypes(
            Connection con, String dbName, String schema,
            Vector <String> tables) throws SQLException {
        Hashtable <Integer, DBTable> ht = 
            new Hashtable<Integer, DBTable>();
        int len = tables.size();
        String tableName;
        for(int i=0; i<len; i++) {
            tableName = tables.get(i);
            DBTable dbTable = createDBTable(con, dbName, 
                schema, tableName);
            String key = getKey(dbName, schema, tableName);
            ht.put(key.hashCode(), dbTable);
        } // for (tabellenweise)
        return ht.elements();
    } // createDBTableTypes

    /**
     * @param con bestehende Datenbankverbindung
     * @param dbName Datenbankname
     * @param schema Schemaname
     * @param tableName Tabellenname
     * @return komplexen Datentyp für gegebenen Tabelle
     * @throws SQLException
     */
    public DBTable createDBTable(Connection con, 
        String dbName, String schema, String tableName) 
        throws SQLException {
        DatabaseMetaData dbmd = con.getMetaData();
        ResultSet colRs = dbmd.getColumns(null, schema, 
                tableName, "%");
        Vector <String> colNames = new Vector<String>();
        Vector <String> colTypes = new Vector<String>();
        String name, type;
        while(colRs.next()) {
            name = colRs.getString("COLUMN_NAME");
            type = colRs.getString(6);
            type = type.concat("(") + 
                   colRs.getInt("COLUMN_SIZE") + ")";
            colNames.add(name);
            colTypes.add(type);
        } // while (spaltenweise)
        colRs.close();
        Vector <String> primaryKeys = getPrimaryKeys(
                tableName, schema, dbmd);         
        Enumeration <Vector<DBForeignKey>> foreignKeys =
            getForeignKeys(tableName, schema, dbmd);
        return new DBTable(dbName, schema, tableName, 
                colNames, colTypes, primaryKeys, foreignKeys);
    } // createDBTable
    
	/**
     * @param table Tabellenname
     * @param schema Schemaname
     * @param dbmd SQL-Datenbankmetadatencontainer
     * @return Primärschlüssel der gegebenen Tabelle
     * @throws SQLException
	 */
    private Vector<String> getPrimaryKeys(String table,
            String schema, DatabaseMetaData dbmd) 
            throws SQLException {
		Vector <String> keys = new Vector<String>();
		ResultSet keyRs = dbmd.getPrimaryKeys(null, 
                schema, table);
		while(keyRs.next()) {
			keys.add(keyRs.getString("COLUMN_NAME"));
		} // (schlüsselweise)
        keyRs.close();
        return keys;
    } // getPrimaryKeys
    
    /**
     * @param table Tabellenname
     * @param schema Schemaname
     * @param dbmd SQL-Datenbankmetadatencontainer
     * @return Fremdschlüssel der gegebenen Tabelle
     * @throws SQLException
     */
    private Enumeration <Vector<DBForeignKey>> getForeignKeys(
            String table, String schema,
            DatabaseMetaData dbmd) throws SQLException {        
        ResultSet keyRs = dbmd.getImportedKeys(null, 
                schema, table);
        Hashtable <Integer, Vector<DBForeignKey>> ht = 
            new Hashtable<Integer, Vector<DBForeignKey>>();
        Vector <DBForeignKey> foreignKeys;
        String foreignKey;
        int hashCode;
        while(keyRs.next()) {
            foreignKey = keyRs.getString("FKCOLUMN_NAME");
            hashCode = foreignKey.hashCode();
            foreignKeys = ht.get(hashCode);
            if(foreignKeys == null) {
                foreignKeys = new Vector<DBForeignKey>();
            } /* (für Fremdschlüssel wurden noch keine 
                 Verweise erfasst) */
            foreignKeys.add(new DBForeignKey(
                    foreignKey,
                    keyRs.getString("PKTABLE_SCHEM"),
                    keyRs.getString("PKTABLE_NAME"),
                    keyRs.getString("PKCOLUMN_NAME")
                )
            );
            ht.put(hashCode, foreignKeys);
		} // while (fremdschlüsselweise)
        keyRs.close();
		return ht.elements();
	} // getForeignKeys
    
    /**
     * @param dbName Datenbankname
     * @param schema Schemaname
     * @param tableName Tabellenname
     * @return Schlüssel für Hashtable 
     *         (Datenbank§Schema§Tabelle)
     */
    private String getKey(String dbName, 
            String schema, String tableName) {
        StringBuffer buf = new StringBuffer();
        buf.append(dbName);  buf.append("§");
        buf.append(schema);  buf.append("§");
        buf.append(tableName);
        return buf.toString();
    } // getKeys            
} // DBReaderLevel3