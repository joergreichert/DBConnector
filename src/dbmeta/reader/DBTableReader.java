package dbmeta.reader;
import java.sql.Connection;
import java.util.Hashtable;
import java.util.Vector;
import java.sql.SQLException;
import dbmeta.data.DBTable;
/**
 * holt Metadaten der Tabellen
 * @author Jörg Reichert
 *
 */
public class DBTableReader {
	private DBMetaReader meta;
	private Hashtable <Integer, DBTable> ht;
	
	/**
     * @param dbName Datenbankname
     * @param con Datenbankverbindung
     * @throws SQLException Abfragefehler
	 */
    DBTableReader(String dbName, Connection con) 
        throws SQLException {
		meta = new DBMetaReader(dbName, con);
		ht = meta.getMetaData();
	} // Konstruktor
	
	/**
     * @return Tabellennamen
	 */
    Vector <String> getTables() {
		return meta.getTables(); 
	} // getTables
	
	/**
     * @param table Tabellenname
     * @return Metadaten der Tabelle
	 */
    DBTable getMetaDataForTable(String table) {
		return ht.get(table.hashCode());
	} // getMetaDataForTable
	
	/**
     * @param table Tabellenname
     * @return Spaltenanzahl der Tabelle
	 */
    int getColumnCount(String table) {
		DBTable dbt = (DBTable) ht.get(table.hashCode());
		return dbt.getColCount();
	} // getColumnCount
	
    /**
     * @param table Tabellenname
     * @return Spaltennamen der Tabelle
     */
    Vector <String> getColumnNames(String table) {
		DBTable dbt = (DBTable) ht.get(table.hashCode());
		System.out.println(dbt.getTableName());
		System.out.println(dbt == null);
		return dbt.getColNames();
	} // getColumnNames	
	
    /**
     * @param table Tabellennamen
     * @param index Position der Spalte
     * @return Spaltenname
     */
	String getColumnNameAt(String table, int index) {
		DBTable dbt = (DBTable) ht.get(table.hashCode());
		return dbt.getColNameAt(index);
	} // getColumnNameAt
	
    /**
     * @param table Tabellennamen
     * @param index Position der Spalte
     * @return Spaltentyp
     */    
	String getColumnTypeAt(String table, int index) {
		DBTable dbt = (DBTable) ht.get(table.hashCode());
		return dbt.getColTypeAt(index);
	} // getColumnTypeAt		
	
    /**
     * @param table Tabellenname
     * @return Spaltentypen
     */
    Vector <String> getColumnDataTypes(String table) {
		DBTable dbt = (DBTable) ht.get(table.hashCode());
		return dbt.getColTypes();
	} // getColumnDataTypes
	
    /**
     * @param table Tabellenname
     * @return Metadaten der Spalten der Tabelle
     */
    Hashtable <String, String> getMetaTable(String table) {
		DBTable dbt = (DBTable) ht.get(table.hashCode());
		return dbt.getMetaTable();		
	} // getMetaTable
    
    /**
     * @param tNames Tabellennamen
     * @return Metadaten der Spalten der Tabellen
     */
    Vector <DBTable> getMetaTables(Vector <String> tNames) {
        int len = tNames.size();
        Vector <DBTable> tables = new Vector<DBTable>(len);
        System.out.println(ht == null);
        for(int i=0; i<len; i++) {
            tables.add(ht.get(tNames.get(i).hashCode()));
        } // for (tabellenweise)
        return tables;
    } // getMetaTables
    
    /*public void abfragen(Connection con) 
    	throws SQLException {
    	meta.abfragen(con);
    }*/
} // DBTableReader