package dbmeta.view;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.Vector;
/**
 * in linker Liste stehen die in der Datenbank vorhandenen
 * Schemata, �ber Auswahl eines Schemas werden in der 
 * rechten Liste die in dem Schema enthaltenen Tabellen
 * angezeigt (f�r ohne Schema kommen alle Tabellen, die
 * keinem Schema zugeordnet sind) 
 * @author J�rg Reichert
 */
public class DBMetaViewLists extends JPanel {
    public final static long serialVersionUID = 0;
    private JList schemaList, tableList;
    private DefaultListModel schemaListModel, tableListModel;    
    
    /**
     * Liste f�r Schemata, Liste f�r Tabellen 
     * @param aView Fenster, in das diese Komponente kommt
     */
    DBMetaViewLists(DBMetaView aView) {
        // Gr��e von Liste abh�ngig von Vaterview einstellen
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int listWidth = (int) (width * 0.15);
        int listHeight = (int) (width * 0.1);
        schemaList = new JList(schemaListModel = 
            new DefaultListModel());
        schemaList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane p1 = new JScrollPane(schemaList);
        p1.setPreferredSize(new Dimension(listWidth, listHeight));        
        JPanel schemaP = new JPanel(new BorderLayout());
        schemaP.add(new JLabel("Schemata in Datenbank"), BorderLayout.NORTH);
        schemaP.add(p1, BorderLayout.CENTER);
        tableList = new JList(tableListModel = 
            new DefaultListModel());        
        JScrollPane p2 = new JScrollPane(tableList);        
        p2.setPreferredSize(new Dimension(listWidth, listHeight));
        JPanel tableP = new JPanel(new BorderLayout());
        tableP.add(new JLabel("Tabellen im Schema"), BorderLayout.NORTH);
        tableP.add(p2, BorderLayout.CENTER);        
        add(schemaP);
        add(tableP);
    } // Konstruktor   
    
    /**
     * setzt Ereignisbehandlung undkennzeichnet Liste f�r
     * sp�tere Identifizierung in der Ereignissenke
     * @param listener Ereignissenke
     * @param schema Name f�r linke Tabelle
     * @param table Name f�r rechte Tabelle
     */
    void setListener(ListSelectionListener listener,
            String schema, String table) {
        schemaList.setName(schema);
        schemaList.addListSelectionListener(listener);
        tableList.setName(table);
        tableList.addListSelectionListener(listener);
    } // setListener

    /**
     * f�gt gegebene Datenbankschemanamen in linke Liste ein
     * @param schemas Datenbankschemanamen
     */
    void addSchemas(Vector <String> schemas) {
        schemaListModel.removeAllElements();
        // Auswahleintrag f�r Tabellen ohne Schema
        schemaListModel.addElement("ohne Schema");
        if(schemas != null) {
            int len = schemas.size();
            for(int i=0; i<len; i++) {
                schemaListModel.addElement(schemas.get(i));    
            } // for (schemaweise)
        } // if (schemas vorhanden)
    } // addSchema
    
    /**
     * f�gt gegebene Tabellennamen in rechte Liste ein
     * @param tables Tabellennamen
     */
    void addTables(Vector <String> tables) {
        tableListModel.removeAllElements();
        int len = tables.size();
        for(int i=0; i<len; i++) {
            tableListModel.addElement(tables.get(i));
        } // for (tabellenweise)
    } // addTable    
    
    /**
     * @return gew�hlte Eintr�ge in der rechten Liste
     */
    Object [] getSelectedTables() {
        return tableList.getSelectedValues();
    } // getSelectedTables
    
    /**
     * @return aktuell angew�hlter Eintrag in linker Liste
     */
    String getSelectedSchema() {
        String schema = (String) schemaList.getSelectedValue(); 
        if(schema.equalsIgnoreCase("ohne Schema")) {
            schema = "";
        } // (ohne Schema in "" f�r SQL-Anfrage umwandeln)
        return schema;
    } // getSelectedSchema
    
    /**
     * @return in rechter Liste gew�hlter Eintrag
     */
    String getSelectedTable() {
        return (String) tableList.getSelectedValue();
    } // getSelectedTable
} // DBMetaViewLists
