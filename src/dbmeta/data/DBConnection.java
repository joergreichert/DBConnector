package dbmeta.data;

public class DBConnection {
    public final String dbDriverName, host, dbName, 
                        userName, password;    
    
    /**
     * @param aDbDriverName Datenbanksystemname
     * @param aHost Adresse
     * @param aDbName Datenbankname
     * @param aUserName Benutzername für DB
     * @param aPassword Passwort für DB
     */
    public DBConnection(String aDbDriverName, String aHost, 
            String aDbName, String aUserName, 
            String aPassword) {
        dbDriverName = aDbDriverName;
        host = aHost;
        dbName = aDbName;
        userName = aUserName;
        password = aPassword;
    } // Konstruktor
    
    /**
     * testet, ob die Werte für Host, Datenbankname und
     * Benutzername leer sind
     * @throws NullPointerException wenn Werte leer sind
     */
    public void testValues() throws NullPointerException {
        int len = host.trim().length();
        if(len == 0) {
            throw new NullPointerException("Sie haben keinen " + 
                    "Host angegeben");
        } // if leerer Wert für Host
        len = dbName.trim().length();
        if(len == 0) {
            throw new NullPointerException("Sie haben keinen " + 
                    "Datenbanknamen angegeben");
        } // if leerer Wert für Datenbankname
        len = userName.trim().length();
        if(len == 0) {
            throw new NullPointerException("Sie haben keinen " + 
                    "Benutzernamen angegeben");
        } // if leerer Wert für Benutzername
    } // testValues
} // DBConnection