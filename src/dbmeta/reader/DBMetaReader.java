package dbmeta.reader;
import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;
import dbmeta.data.DBTable;
import oracle.jdbc.OracleDatabaseMetaData;

public class DBMetaReader {
	private Vector <String> tables = null;
	private final int COLDATATYPES = 0, COLNAMES = 1;
	private Hashtable <Integer, DBTable> ht;
	
	/**
     * holt Metadaten der Tabellen der Datenbank
     * @@param dbName Datenbankname
     * @@param con Datenbankverbindung
	 */
    DBMetaReader(String dbName, Connection con) 
        throws SQLException {
	  	try {
            ht = new Hashtable <Integer, DBTable> ();
            tables = getTables(con);
			System.out.println("Tabellen geholt");
            int tableCount = tables.size();
	  		String table;
	  		tableCount = 2;
	  		Vector <String> colNames, colTypes;
	  		for(int i=0; i<tableCount; i++) {
	  			table = (String) tables.get(i); 
	  			colNames = getColumnNames(con, table);
	  			colTypes = getColumnTypes(con, table);
	  			ht.put(table.hashCode(), new DBTable(dbName, 
                        table, colNames, colTypes));
	  		} // for (tabellenweise)
	  		con.close();
		} catch (SQLException sqlE) {
			throw new SQLException("Konnte Tabellen nicht " + 
                    "holen");
		} // try-catch
	} // Konstruktor
	
	/**
     * holt Tabellennamen 
     * @@param con Datenbankverbindung
     * @@return Tabellennamen
     * @@throws SQLException Abfragefehler
	 */
    private Vector <String> getTables(Connection con) 
        throws SQLException {
		Vector <String> tables = new Vector<String>();
		String table = null;
		OracleDatabaseMetaData dbmd = (OracleDatabaseMetaData)
      	con.getMetaData(); 
		ResultSet rs = dbmd.getSchemas();
		ResultSet rs1 = null;
		while(rs.next()) {
			rs1 = dbmd.getTables(null,rs.getString(1),"%",null);
			while(rs1.next()) {
				table = rs1.getString(3);
				if(!table.contains("$")) {
					tables.add(table);
				} // if Metatabellen auslassen
			} // für alle Tabellen
			rs1.close();
		}
		rs.close();
		return tables;
	} // getTables	
	
	/**
     * holt Spaltennamen der gegebenen Tabelle 
     * @@param con Datenbankverbindung
     * @@param table gegebenen Tabelle
     * @@return Spaltennamen
     * @@throws SQLException Abfragefehler
	 */
    private Vector <String> getColumnNames(Connection con, 
            String table) throws SQLException {
		return getColumnMetaData(con, table, COLNAMES);
	} // getColumnNames
	
    /**
     * holt Spaltentypen der gegebenen Tabelle
     * @@param con Datenbankverbindung
     * @@param table gegebene Tabelle
     * @@return Spaltentypen
     * @@throws SQLException Abfragefehler
     */
	private Vector <String> getColumnTypes(Connection con, 
            String table) throws SQLException {
		return getColumnMetaData(con, table, COLDATATYPES);
	} // getColumnTypes	
	
    /**
     * holt Spaltennamen bzw Spaltentypen
     * @@param con Datenbankverbindung
     * @@param table gegebene Tabelle
     * @@param META Name oder Typ abfragen
     * @@return Spaltennamen bzw Spaltentypen
     * @@throws SQLException Abfragefehler
     */
	private Vector <String> getColumnMetaData(Connection con, 
            String table, int META) throws SQLException {
		try {
		Vector <String> metaData = new Vector<String>();
		String data = null;
		int bounds = 0;
		OracleDatabaseMetaData dbmd = (OracleDatabaseMetaData)
      	con.getMetaData(); 
		ResultSet rs = dbmd.getSchemas();
		while(rs.next()) {
			ResultSet results = dbmd.getColumns(null, rs.getString(1), table, null);
			if(META == 0) {
				while(results.next()) {
					data = results.getString(6);
					bounds = results.getInt(7);
					metaData.add(getDataType(data, bounds));
				} // für alle Spalten Typen abfragen
			} else if(META == 1) {
				while(results.next()) {
					System.out.println(results.getString(4));
					metaData.add(results.getString(4));
				} // für alle Spalten Namen abfragen
			} // if-else (Typ oder Name)
			results.close();
		}
		rs.close();
		return metaData;
		} catch (SQLException ex) {
			System.out.println("Spaltenfehler: " + ex.getMessage());
			ex.printStackTrace();
			throw new SQLException();
		}
	} // getColumnMetaData
	
	/**
     * Zeichenkette Datentyp (Wertebereich) erstellen  
     * @@param data Datentyp
     * @@param bounds Wertebereich des Datentyp 
     * @@return Typ und Wertebereich als eine Zeichenkette
	 */
    private String getDataType(String data, int bounds) {
		System.out.println(data.concat("(") + bounds + ")");
    	return data.concat("(") + bounds + ")";
	} // getDataType
	
	/**
     * @@return Tabellennamen
	 */
    Vector <String> getTables() {
		return tables;
	} // getTables
	
	/**
     * @@return alle Tabellen als Datentyp Tabelle
	 */
    Hashtable <Integer, DBTable> getMetaData() {
		return ht;
	} // getMetaData
} // DBMetaReader@
