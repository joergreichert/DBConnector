package dbmeta.data;
import java.util.*;
import javax.swing.table.*;
/**
 * Tabellen-Modell für Tabelle der Metadaten der Tabellen
 * @author Jörg Reichert
 *
 */
public class DBTableMetaData extends AbstractTableModel {
    public final static long serialVersionUID = 0;
	private Hashtable <String, String> ht = null;
	private int rowCount = 0, colCount = 0;
	private String [] colNames; 
	
	/**
     * @param cols Spaltenüberschriften (Name, Typ)
     * @param hash Tabelle mit Werten   
     * @param rows Zeilenanzahl
	 */
    public DBTableMetaData(String [] cols, Hashtable <String, String> hash, int rows) {
		rowCount = rows;
		colCount = cols.length;
		colNames = cols;
		ht = hash;
	} // Konstruktor
	
	public int getColumnCount() {
		return colCount;
	} // getColumnCount
	
	public int getRowCount() {
		return rowCount;
	} // getRowCount
	
    public String getColumnName(int col) {
		return colNames[col];
	} // getColumnName
	
	public Class <String> getColumnClass(int columnIndex) {
		return String.class;
	} // getColumnClass
	
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return rowIndex < rowCount && columnIndex < colCount;
	} // isCellEditable		
	
    public Object getValueAt(int row, int col) {
		String key = row + "-" + col;
		return (String)ht.get(key);
	} // getValueAt
	
	public void setValueAt(Object obj, int row, int col) {
		String key = row + "-" + col;
		String value = (String) obj;
		if(obj == null) {
			ht.remove(key);
		} else {
			ht.put(key, value);
		} /* if-else (wenn Objekt nicht existiert, Schlüssel
             löschen */
	} // setValueAt
} // DBTableMetaData