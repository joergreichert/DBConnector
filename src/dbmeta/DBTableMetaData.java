package dbmeta;
import java.util.*;
import javax.swing.table.*;

public class DBTableMetaData extends AbstractTableModel {
    public final static long serialVersionUID = 0;
	private Hashtable <String, String> ht = null;
	private int rowCount = 0, colCount = 0;
	private String [] colNames; 
	
	DBTableMetaData(String [] cols, Hashtable <String, String> hash, int rows) {
		rowCount = rows;
		colCount = cols.length;
		colNames = cols;
		ht = hash;
	}
	
	public int getColumnCount() {
		return colCount;
	}
	
	public int getRowCount() {
		return rowCount;
	}
	
	public String getColumnName(int col) {
		return colNames[col];
	}
	
	public Class <String> getColumnClass(int columnIndex) {
		return String.class;
	}
	
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return rowIndex < rowCount && columnIndex < colCount;
	}		
	
	public Object getValueAt(int row, int col) {
		String key = row + "-" + col;
		return (String)ht.get(key);
	}
	
	public void setValueAt(Object obj, int row, int col) {
		String key = row + "-" + col;
		String value = (String) obj;
		if(obj == null) {
			ht.remove(key);
		} else {
			ht.put(key, value);
		}
	}
}
