package dbmeta.view;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;
import dbmeta.data.DBConnection;
import java.io.File;
import dbmeta.data.DBXMLFileFilter;
/**
 * Grafische Oberfläche für das Abrufen der Tabellen in
 * einer gegebenen Datenbank, die Anzeige ihrer Spalten
 * (Name, Datentyp) und die Speicherung dieser Daten 
 * in einer XML-Datei   
 * @author Jörg Reichert
 */
public class DBMetaView extends JFrame {
    public final static long serialVersionUID = 0;
    private DBConnectorView conView;
    private JButton dbConnectBtn, xmlBtn, finishBtn, 
    	sqlOpenBtn;
    private CardLayout layout;
    private JPanel tablePanel;
    private DBMetaViewLists listsView;
    private DBTableGrid grid = null;
    private DBMetaSQLView sqlView;
    
    /**
     * Liste links zeigt vorhandene Schemas, Liste rechts
     * für Schema verfügbare Tabellen, Knopf für
     * Dialog zum Datenbankverbindungsaufbau, Knopf für 
     * Speichern der Metadaten der ausgewählten Tabellen in
     * der Liste rechts in einer XML-Datei, Anzeige einer 
     * aktuell ausgewählten Tabelle in der rechten Liste
     */
    public DBMetaView(String title) {
        // Überschrift
        Border empty = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        JLabel titleL = new JLabel(title, JLabel.CENTER);
        titleL.setFont(new Font("Times New Roman", Font.BOLD, 24));
        titleL.setForeground(new Color(200, 50, 50));
        JPanel centerPanel = new JPanel(new BorderLayout());
        // Listen für Schemas und Tabellen
        centerPanel.add(listsView = 
            new DBMetaViewLists(this), BorderLayout.NORTH);
        // Knöpfe für Verbindung und XML-Speicherung
        centerPanel.add(createIOPanel(), BorderLayout.CENTER);        
        // Anzeige der Spalten der Tabelle
        tablePanel = new JPanel(layout = new CardLayout());
        JScrollPane p = new JScrollPane(tablePanel);
        p.setPreferredSize(new Dimension(100, 200));
        // Beenden-Knopf
        JPanel finishPanel = new JPanel(new BorderLayout());
        sqlOpenBtn = new JButton("SQL-Anfrage");
        finishBtn = new JButton("Beenden");
        finishPanel.add(sqlOpenBtn, BorderLayout.BEFORE_LINE_BEGINS);
        finishPanel.add(finishBtn, BorderLayout.AFTER_LINE_ENDS);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(p, BorderLayout.NORTH);
        finishPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        bottomPanel.add(finishPanel, BorderLayout.SOUTH);
        bottomPanel.setBorder(empty);
        JPanel dbMetaPanel = new JPanel(new BorderLayout());
        dbMetaPanel.add(titleL, BorderLayout.NORTH);
        dbMetaPanel.add(centerPanel, BorderLayout.CENTER);
        dbMetaPanel.add(bottomPanel, BorderLayout.SOUTH);
        dbMetaPanel.setBorder(empty);
        Container c = getContentPane();
        c.add(dbMetaPanel);
        openWindow(title);
    } // Konstruktor
    
    /**
     * Panele mit Knöpfen für Öffnen einer 
     * Datenbankverbindung und das Speichern der Metadaten
     * der ausgewählten Tabellen als XML-Datei
     * @return Knopfpanele
     */
    private JPanel createIOPanel() {
        dbConnectBtn = new JButton("Datenbank öffnen");
        xmlBtn = new JButton("speichern als XML");        
        GridLayout gl = new GridLayout(1,2);
        gl.setHgap(10);  gl.setVgap(10);
        JPanel p = new JPanel(gl);
        p.add(dbConnectBtn);        
        p.add(xmlBtn);
        JPanel panel = new JPanel();
        panel.add(p);
        return panel;
    } // createIOPanel
    
    /**
     * entfernte alte Tabelle aus Anzeige und registriert 
     * neue Tabelle mit ihren Daten für spätere Anzeige   
     * @param title Titel der Tabelle
     * @param ht Spaltenüberschriften und Daten der Tabelle
     */
    public void showTable(String title, 
            Hashtable <String, String> ht) {
        String [] colNames = {"Name", "Typ"} ;
        grid = new DBTableGrid(title, colNames, ht);
        tablePanel.add(grid, title);
        layout.show(tablePanel, title);
        layout.removeLayoutComponent(grid);
    } // addTable

    /**
     * fügt Tabellenname der rechten Liste hinzu
     * @param tableNames Tabellennamen für das in der linken
     *        Liste ausgewählte Schema
     */
    public void addTables(Vector <String> tableNames) {
        listsView.addTables(tableNames);
    } // addTable

    /**
     * @return in rechter Liste ausgewählte Tabelle
     */
    public String getSelectedTable() {
        return listsView.getSelectedTable();
    } // getSelectedTable    
    
    /**
     * @return in der Liste ausgewählte Tabellen 
     */
    public Vector <String> getSelectedTables() {
        Object [] cs = listsView.getSelectedTables(); 
        int len = cs.length;
        Vector <String> tables = new Vector<String>(len);
        for(int i=0; i<len; i++) {
            tables.add((String) cs[i]);
        } // for (tabellenweise)
        return tables;
    } // getSelectedTables    

    /**
     * @param schemaNames in Datenbank verfügbare Schemata
     */
    public void addSchemas(Vector <String> schemaNames) {
        listsView.addSchemas(schemaNames);
    } // addSchema    
    
    /**
     * @return in linker Liste gewähltes Schema
     */
    public String getSelectedSchema() {
        return listsView.getSelectedSchema();
    } // getSelectedSchema    
    
    /**
     * öffnet Fenster mit gegebenen Titel
     * @param title Titel des Fensters
     */
    private void openWindow(String title) {
        setTitle(title);
        WindowSetter.openWindow(this);
        pack();
        setVisible(true);
    } // openWindow
    
    /**
     * setzt Ereignissenken für die Knöpfe 
     * Datenbankverbindung öffnen, XML-Datei speichern und 
     * für den Beendenbutton, weiterhin noch für die 
     * linke und rechte Liste sowie für das Fensterkreuz
     * zum Beenden des Programms 
     * @param l Ereignissenke (Klasse in der Aktionen je 
     * nach gedrückten Knopf, Listeneintrag ausgeführt werden   
     * @param connect Kommando, an dem der Knopf für die
     *         Datenverbindung in der Senke erkannt wird   
     * @param xml Kommando, an dem der Knopf für die
     *   XML-Datei-Speicherung in der Senke erkannt wird
     * @param finish Kommando, an dem der Knopf für die
     *   Programmbeendingung in der Senke erkannt wird
     * @param schema Name, an dem die Liste für die
     *   Schemata in der Senke erkannt wird
     * @param table Name, an dem die Liste für die
     *   Tabellen in der Senke erkannt wird   
     */
    public void setListener(EventListener l, String connect, 
            String xml, String finish, String schema, 
            String table, String sql) {
        dbConnectBtn.setActionCommand(connect);
        dbConnectBtn.addActionListener((ActionListener) l);
        xmlBtn.setActionCommand(xml);
        xmlBtn.addActionListener((ActionListener) l);
        sqlOpenBtn.setActionCommand(sql);
        sqlOpenBtn.addActionListener((ActionListener) l);
        finishBtn.setActionCommand(finish);
        finishBtn.addActionListener((ActionListener) l);        
        listsView.setListener((ListSelectionListener) l,
                schema, table);
        addWindowListener((WindowListener) l);
    } // setListener        
    
    /**
     * Hinweisfenster öffnen 
     * @param title Titel des Fensters / Dialogs
     * @param message angezeigte Meldung im Fenster
     */
    public void openMessageDialog(String title, String message) {
        JOptionPane pane = new JOptionPane();
        pane.setMessage(message);
        JDialog dialog = pane.createDialog(this, title);
        dialog.setVisible(true);
    } // openMessageDialog    
    
    /**
     * öffnet Dialog für Eingabe der Daten für die 
     * Verbindungsaufnahme zu einer Datenbank 
     * @param boxContent Liste der Datenbanksystem, an die 
     *                   man sich anmelden kann    
     * @param al Ereignissenke
     * @param open Kommando, an dem der Knopf für die
     *   Verbindungsdaten in der Senke erkannt wird
     * @param cancel Kommando, an dem der Knopf für die
     *   Abbruch in der Senke erkannt wird
     */
    public void openConnector(Vector <String> boxContent, 
            ActionListener al, String open, String cancel) {
        conView = new DBConnectorView(this, boxContent);
        conView.setListener(al, open, cancel);
        conView.setVisible(true);
    } // openConnection
    
    /**
     * @return Daten für Verbindungsaufnahme zu Datenbank
     *         als komplexer Datentyp 
     */
    public DBConnection getDBConnection() {
        return conView.getDBConnection();
    } // getDBConnection
    
    /**
     * Dateidialogfenster für Angabe von Pfad und Datei
     * zum Speichern der Daten in XML 
     * @return Pfad zur zu speichernden Datei
     */
    public File openFileChooser() {
        String dir = System.getProperty("user.dir");
        JFileChooser chooser = new JFileChooser(dir);
        chooser.setFileFilter(new DBXMLFileFilter());
        chooser.showSaveDialog(this);
        return chooser.getSelectedFile();
    } // openFileChooser
    
    /**
     * schließt Fenster für Eingabe der Verbindungsdaten
     */
    public void closeConnector() {
        conView.dispose();
    } // closeConnector
    
    public void openSQLer(ActionListener al, String sql) {
        sqlView = new DBMetaSQLView(this);
        sqlView.setListener(al, sql);
        sqlView.setVisible(true);
    } // openSQLer
    
    public String getSQLQuery() {
    	return sqlView.getSQLQuery();
    }
    
    public JTextArea getTextArea() {
    	return sqlView.getTextArea();
    }
} // DBMetaView
