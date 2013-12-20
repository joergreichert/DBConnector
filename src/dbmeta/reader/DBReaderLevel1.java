package dbmeta.reader;
import java.sql.Connection;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import dbmeta.Connector;
import dbmeta.data.DBTable;
/**
 * Verbindung zu Datenbank herstellen und Metadaten der
 * Tabellen auslesen lassen
 * @author Jörg Reichert
 *
 */
public class DBReaderLevel1 extends Connector {
    public final static long serialVersionUID = 0;
	private Connection connection      = null;
    private String dbName              = null;
	private DBReaderLevel2 tableReader = null;
	private DBReaderLevel3 reader2;
	
    public DBReaderLevel1() {
        tableReader = new DBReaderLevel2();
        reader2 = new DBReaderLevel3();        
    } // Konstruktor
    
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
		this.dbName = dbName;
	} // connect
	
    /**
     * @return alle Schemata in der Datenbank
     */
    public Vector <String> getSchemas() throws Exception {
        Vector <String> schemas = new Vector<String>();
        if(tableReader != null) {
            schemas = tableReader.getSchemas(connection);
        } // if (Verbindung wurde hergestellt)
        return schemas;
    } // getTables    
    
	/**
     * @param schema Schemaname
     * @return Tabellennamen aus diesem Schema
	 */
    public Vector <String> getTableNames(String schema) 
        throws Exception {
		Vector <String> tables = new Vector<String>();
        if(tableReader != null) {
            tables = tableReader.getTableNames(connection, 
                    schema);
		} // if (Verbindung wurde hergestellt)
		return tables;
	} // getTables
    
    /**
     * @param schema Schemaname
     * @param tables Tabellennamen
     * @return alle Tabellenobjekte für die gegebenen Parameter
     * @throws Exception
     */
    public Enumeration <DBTable> getTables(String schema,
            Vector <String> tables) throws Exception {
        return tableReader.createDBTableTypes(connection, 
                dbName, schema, tables);
    } // getMetaTables

    /**
     * @param dbName Datenbankname
     * @param schema Schemaname
     * @param tableName Tabellenname
     * @return Hashtable mit Spaltennamen und -typen für
     *         Einsatz in JTable
     * @throws Exception
     */
    public Hashtable <String, String> getMetaTable(
            String dbName, String schema, String tableName) 
            throws Exception {
        return tableReader.createDBTable(connection, 
                dbName, schema, tableName).getMetaTable();
	} // getMetaTable
    
    /**
     * schließt aktuelle Datenbankverbindung 
     * @throws Exception Fehler beim Schließen der Verbindung
     */
    public void disconnect() throws Exception {
        if(connection != null) {
            super.closeConnection();
        } // if (keine leere Verbindung)
    } // disconnect
    
    public void makeQuery(String query, javax.swing.JTextArea ta) {
        reader2.makeQuery(connection, query, ta);
    }
} // DBReaderLevel1