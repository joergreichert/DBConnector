package dbmeta.reader;
import java.sql.Connection;
import java.util.Vector;
import java.util.Hashtable;

import dbmeta.Connector;
import dbmeta.data.DBTable;
/**
 * Verbindung zu Datenbank herstellen und Metadaten der
 * Tabellen auslesen lassen
 * @author Jörg Reichert
 *
 */
public class DBReader extends Connector {
    public final static long serialVersionUID = 0;
	private Connection connection = null;
	private DBTableReader tableReader = null;
	
	/**
     * Datenbank-Treiber-Datentyp 
     * @author Jörg Reichert
     *
	 */
    class DBDriver {
		final String name, driver, url;
	
		DBDriver(String aName, String aDriver, String aUrl) {
			name = aName;  driver = aDriver;  url = aUrl;
		} // Konstruktor
	} // DBDriver
	
	/**
     * stellt Verbindung zu Datenbank her 
     * @param dbType Datenbanksystemnamekonstante
     * @param host Adresse
     * @param dbName Datenbankname
     * @param user Benutzername zur Anmeldung an DB
     * @param pword Passwort zur Anmeldung an DB
     * @throws Exception Verbindungfehler, falscher Treiber
	 */
    public void connect(String dbType, String host, 
            String dbName, String user, String pword) 
        throws Exception {
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
		} // if-else
		tableReader = new DBTableReader(dbName, connection);
	} // connect
	
	/**
     * @return Tabellennamen
	 */
    public Vector <String> getTables() {
		Vector <String> tables = new Vector<String>();
		if(tableReader != null) {
			tables = tableReader.getTables();
		} // if (Verbindung wurde hergestellt)
		return tables;
	} // getTables
    
    /**
     * @param tables Tabellennamen
     * @return alle Tabellen
     */
    public Vector <DBTable> getMetaTables(
            Vector <String> tables) {
        return tableReader.getMetaTables(tables);
    } // getMetaTables
	
	/**
     * @param table Tabellenname
     * @return Spaltennamen dieser Tabelle
	 */
    public Vector <String> getColNames(String table) {
		Vector <String> colNames = new Vector<String>();
		if(tableReader != null) {
			colNames = tableReader.getColumnNames(table);
        } // if (Verbindung wurde hergestellt) 
		return colNames;
	} // getColNames	
	
	/**
     * @param table Tabellenname
     * @return Spaltentypen dieser Tabelle
	 */
    public Vector <String> getDataTypes(String table) {
		Vector <String> dataTypes = new Vector<String>();
		if(tableReader != null) {
			dataTypes = tableReader.getColumnDataTypes(table);
        } // if (Verbindung wurde hergestellt)
		return dataTypes;
	} // getDataTypes
	
	/**
     * @param table Tabellenname
     * @return Hashtable mit Spaltennamen und -typen
	 */
    public Hashtable <String, String> getMetaTable(
            String table) {
        return tableReader.getMetaTable(table);
	} // getMetaTable
    
    /*public void abfragen() 
    	throws Exception {
    	tableReader.abfragen(connection);
    }*/
} // DBReader