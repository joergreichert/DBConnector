package dbmeta;
import java.io.File;
import java.awt.event.*;

import javax.swing.JTextArea;
import javax.swing.event.*;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import dbmeta.view.DBMetaView;
import dbmeta.data.DBMetaXML;
import dbmeta.data.DBTable;
import dbmeta.reader.DBReaderLevel1;
import dbmeta.data.DBConnection;

/**
 * lässt die Metadaten der Tabellen einer Datenbank 
 * auslesen, lässt sie anzeigen und speichert sie in
 * einer XML-Datei 
 * @author Jörg Reichert
 *
 */
public class DBMetaController extends WindowAdapter 
    implements ActionListener, ListSelectionListener {
	private DBReaderLevel1 reader;
	private DBMetaView view;
	private final String OPEN = "OPEN", CANCEL = "CANCEL", 
        CONNECT = "CONNECT", XML = "XML", FINISH = "FINISH", 
        SCHEMA = "SCHEMA", TABLE = "TABLE", 
        SQL_OPEN = "SQL_OPEN", SQL = "SQL";
    private String dbName;
	
	/**
     * @param aView GUI
	 */
    DBMetaController(DBMetaView aView) {
        view = aView;
        view.setListener(this, CONNECT, XML, FINISH, SCHEMA, 
                TABLE, SQL_OPEN);
        reader = new DBReaderLevel1();
        
	} // Konstruktor
	
	public void actionPerformed(ActionEvent ae) {
		String command = ae.getActionCommand();
        try {
            if(command.equals(OPEN)) {
                // zeigt verfügbare Schemas in Datenbank
                view.closeConnector();
                dbName = connectToDatabase();
                readOutSchemas();
            } else if(command.equals(CONNECT)) {
                // Formular zur Datenbankverbindung
                Vector <String> dbDriverNames = 
                    reader.getDBDrivers();
                view.openConnector(dbDriverNames, this, OPEN, 
                        CANCEL);
            } else if(command.equals(XML)) {
                // ausgewählte Tabellen samt Metadaten in XML
                writeXMLFile();
            } else if(command.equals(CANCEL)) {
                view.closeConnector();
            } else if(command.equals(SQL_OPEN)) {
                view.openSQLer(this, SQL);
            } else if(command.equals(FINISH)) {
                closeProgram();
            } else if(command.equals(SQL)) {
            	String query = view.getSQLQuery();
            	JTextArea area = view.getTextArea();
            	reader.makeQuery(query, area);
            } // if-else (verschiedene Action-Quellen)
            
        } catch(Exception e) {
            view.openMessageDialog("Fehler", e.getMessage());
            e.printStackTrace();
		} // try-catch
	} // actionPerformed
    
    public void valueChanged(ListSelectionEvent lse) {
        javax.swing.JList list = 
            ((javax.swing.JList) lse.getSource());
        String name = list.getName();
        boolean goOn = !list.getValueIsAdjusting() 
            && (list.getSelectedIndex() >= 0);
        if(goOn) {
            String schema = view.getSelectedSchema();
            try {
                if(name.equals(SCHEMA)) {
                    // für Schema verfügbare Tabellen anzeigen 
                    setTables(schema);
                } else if(name.equals(TABLE)) {
                    // Metadaten der Tabelle anzeigen
                    String tableName = view.getSelectedTable();
                    Hashtable <String, String> ht = 
                        reader.getMetaTable(dbName, schema, 
                                tableName);
                    view.showTable(tableName, ht);
                } // if-else (linke oder rechte Liste)
            } catch(Exception e) {
                view.openMessageDialog("Fehler", e.getMessage());
            } // try-catch
        }        
    } // valueChanged
    
    public void windowClosing(WindowEvent we) {
        closeProgram();
    } // windowClosing
    
    /**
     * Datenbankverbindung, Fenster und Programm schließen
     */
    private void closeProgram() {
        try {
            reader.closeConnection();
        } catch(Exception e) {
            view.openMessageDialog("Fehler", e.getMessage());
        } // try-catch
        view.dispose();
        System.exit(-1);        
    } // closeProgram
    
    private void readOutSchemas() throws Exception {
        view.addSchemas(reader.getSchemas());
    } // readOutSchemas
    
    /**
     * setzt für gegebenes Schema verfügbare Tabellen in
     * rechte Liste   
     * @throws Exception Verbindungsfehler, Auslesefehler
     */
    private void setTables(String schema) throws Exception {
        Vector <String> tables = reader.getTableNames(schema);
        if(tables != null) {
            view.addTables(tables);
        } else {
            view.openMessageDialog("Warnung", 
                "Die Datenbank enthält keine " + 
                "Tabellen");
        } // if-else (Anzahl Tabellen in Datenbank)        
    } // readOutTables
    
    /**
     * stellt Verbindung zu Datenbank her
     * @return Datenbankname
     * @throws Exception Verbindungsfehler
     */
    private String connectToDatabase() throws Exception {
        try {
            DBConnection con = view.getDBConnection();
            con.testValues();
            String dbDriverName = con.dbDriverName;
            String host         = con.host;
            String dbName       = con.dbName;
            String userName     = con.userName;
            String password     = con.password;
            reader.connect(dbDriverName, host, dbName, 
                    userName, password);
            return dbName;
        } catch(Exception e) {
        	throw new Exception("Eine Verbindung zu einer " + 
                "Datenbank mit diesen Parametern konnte " +
                "nicht aufgebaut werden.");
        } // try-catch
    } // connectToDatabase
    
    /**
     * schreibt Metadaten der in der GUI ausgewählten
     * Tabellen in eine XML-Datei
     */
    private void writeXMLFile() throws Exception {
        String schema = view.getSelectedSchema();
        Vector <String> tables = view.getSelectedTables();
        Enumeration <DBTable> enu = reader.getTables(schema, 
                tables);
        DBMetaXML xml = new DBMetaXML(dbName, enu);
        File file = view.openFileChooser();
        String fileName = file.getAbsolutePath();
        if(!fileName.endsWith(".xml")) {
            fileName = fileName.concat(".xml");
            file = new File(fileName);
        } // if (fügt ggf .xml an Dateinamen an)
        xml.print(file);    
        view.openMessageDialog("Hinweis", "XML-Datei " + 
                "erfolgreich unter " + fileName + " gespeichert");
    } // writeXMLFile
} // DBMetaController