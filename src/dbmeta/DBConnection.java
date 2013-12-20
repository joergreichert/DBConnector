package dbmeta;

public class DBConnection {
    public final String dbDriverName, host, dbName, 
                        userName, password;    
    
    DBConnection(String aDbDriverName, String aHost, 
            String aDbName, String aUserName, 
            String aPassword) {
        dbDriverName = aDbDriverName;
        host = aHost;
        dbName = aDbName;
        userName = aUserName;
        password = aPassword;
    }
    
    void testValues() throws NullPointerException {
        int len = host.trim().length();
        if(len == 0) {
            throw new NullPointerException("Sie haben keinen " + 
                    "Host angegeben");
        }
        len = dbName.trim().length();
        if(len == 0) {
            throw new NullPointerException("Sie haben keinen " + 
                    "Datenbanknamen angegeben");
        }
        len = userName.trim().length();
        if(len == 0) {
            throw new NullPointerException("Sie haben keinen " + 
                    "Benutzernamen angegeben");
        }
    }
}