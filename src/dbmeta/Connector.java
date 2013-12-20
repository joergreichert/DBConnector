package dbmeta;
import java.sql.*;
import java.util.Hashtable;
import java.util.Vector;
/**
 * kann Verbindung zu MYSQL-, Postgres-, Oracle- Excel- und
 * Access-Datenbanksysteme herstellen  
 * @author Jörg Reichert
 *
 */
public class Connector {
	private Connection con = null;
	public final static String MY_SQL   = "MySQL-DB-Treiber";
	public final static String OFFICE   = "MS-Office-DB-Treiber";
	public final static String POSTGRES = "Postgres-DB-Treiber";
	public final static String ORACLE   = "Oracle-DB-Treiber";
	private Hashtable <Integer, DBDriver> ht;
	private final Vector <String> dbDriverNames;
	private final String ORACLE_PORT = "1521";
	
	/**
     * Datenbanktreiber-Datentyp  
     * @author Jörg Reichert
	 */
    class DBDriver {
		final String name, driver, url;
	
		DBDriver(String aName, String aDriver, String aUrl) {
			name = aName;  driver = aDriver;  url = aUrl;
		} // Konstruktor
	} // DBDRiver
	
	public Connector() {
        // registriert Datenbanktreiber unter ihrem Namen
        ht = new Hashtable<Integer, DBDriver>();
		dbDriverNames = new Vector<String>();
		ht.put(ORACLE.hashCode(), new DBDriver(ORACLE, 
				"oracle.jdbc.driver.OracleDriver", 
				"jdbc:oracle:thin:@"));
		dbDriverNames.add(ORACLE);
		ht.put(MY_SQL.hashCode(), new DBDriver(MY_SQL, 
				"org.gjt.mm.mysql.Driver", 
				"jdbc:mysql:"));
		dbDriverNames.add(MY_SQL);
		ht.put(OFFICE.hashCode(), new DBDriver(OFFICE, 
				"sun.jdbc.odbc.JdbcOdbcDriver", 
				"jdbc:odbc:"));
		dbDriverNames.add(OFFICE);		
		ht.put(POSTGRES.hashCode(), new DBDriver(POSTGRES, 
				"org.postgresql.Driver", 
				"jdbc:postgresql:"));
		dbDriverNames.add(POSTGRES);		
	} // Konstruktor
	
	/**
     * stellt Verbindung zu Datenbank her
     * @param dbDriverName Datenbanksystemnamenkonstante
     * @param host lokale oder entfernte Adresse des DB-Systems
     * @param dbName Datenbankname im DB-System
     * @param user Benutzername für Anmeldung zu DB
     * @param pword Passwort für Anmeldung zu DB
     * @return Verbindung zur Datenbank
     * @throws Exception Treiber nicht gefunden / keine 
     *                   Verbindung mit dieses Parametern  
	 */
    public Connection getConnection(String dbDriverName, String host,
			String dbName, String user, String pword) 
		throws Exception {
		if(con != null) {
            throw new Exception("Es existiert bereits eine " + 
                    "Verbindung");
		} else {
			String driver = ht.get(dbDriverName.hashCode()).
								driver;
            try {
                if(dbDriverName.equals(ORACLE)) {
                    dbName    = host.concat(":").concat(ORACLE_PORT).concat(":").concat(dbName);
                    DriverManager.registerDriver(
                        new oracle.jdbc.driver.OracleDriver());					
                } else if(!dbDriverName.equals(OFFICE)) {
                    dbName    = "//".concat(host).concat("/").
                        concat(dbName);
                } // if         
                String url    = ht.get(dbDriverName.hashCode()).
                    url.concat(dbName);
                Class.forName(driver);
                con = DriverManager.getConnection(url, user, pword);
				return con;
			} catch (ClassNotFoundException cnfE) {
				throw new Exception("Treiber nicht gefunden");
			} catch (SQLException sqlE) {
				throw new Exception("Keine Verbindung mit " + 
                        "den angegebenen Parametern möglich");
			} // try-catch (Verbindungsaufbau)
		} // if-else (Verbindung existiert bereits oder nicht
	} // getConnection
	
    /**
     * schließt aktuelle Datenbankverbindung
     * @throws Exception Verbindung konnte nicht geschlossen
     *                   werden 
	 */
    public void closeConnection() throws Exception {
		try {
			if(con != null) {
				con.close();
				con = null;
			} // wenn Verbindung nicht null, sie schließen
		} catch(SQLException sqlE) {
			throw new Exception("Verbindung konnte nicht geschlossen werden");
		} // try-catch
	} // closeConnection
	
	/**
     * @return Namen der Datenbanksysteme
	 */
    public Vector <String> getDBDrivers() {
		return dbDriverNames;
	} // getDBDrivers
} // Connector