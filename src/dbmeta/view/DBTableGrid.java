package dbmeta.view;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import dbmeta.data.DBTableMetaData;
import java.awt.*;
/**
 * tabellarische Anzeige der Spaltenname mit den 
 * dazugehörigen Datentypen  
 * @author Jörg Reichert
 */
public class DBTableGrid extends JPanel {
    public final static long serialVersionUID = 0;
	private DBTableMetaData data = null;
	private JTable table         = null;
	
	/**
     * gerahmte und scrollbare Tabelle die Spaltennamen
     * und Spaltentypen der gegebenen Tabelle listet
     * @param title Name der Tabelle
     * @param colNames Spaltenüberschriften
     * @param ht Daten (Spaltennamen und -typen)
     * @param rowCount Zeilenanzahl
	 */
    DBTableGrid(String title, String [] colNames, 
            Hashtable <String, String> ht) {
        int rowCount = (int) (ht.size() / 2);
        rowCount = (rowCount > 0) ? rowCount : 0;
        data  = new DBTableMetaData(colNames, ht, rowCount);
		table = new JTable(data, null);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(true);
        Dimension dim = table.getPreferredSize();
        int height = (int) ((double) dim.height * 
                (double) (rowCount + 3) / (double) rowCount);
        int width = dim.width + 15; 
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(width, height));
		Border etch  = BorderFactory.createEtchedBorder();
		TitledBorder titledBorder = 
			new TitledBorder(etch, title);
		pane.setBorder(titledBorder);
        add(pane, BorderLayout.NORTH);
	} // Konstruktor
	
	/**
     * Wert in die grafische Tabelle eintragen 
     * @param value Wert
     * @param row Zeilenindex
     * @param col Spaltenindex
	 */
    public void insertValue(Object value, int row, int col) {
		table.setValueAt(value, row, col);
	} // insertValue
} // DBTableGrid
