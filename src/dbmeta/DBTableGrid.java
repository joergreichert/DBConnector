package dbmeta;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class DBTableGrid extends JPanel {
    public final static long serialVersionUID = 0;
	private DBTableMetaData data = null;
	private JTable table         = null;
	
	DBTableGrid(String title, String [] colNames, Hashtable <String, String> ht, 
	            int rowCount) {
        data  = new DBTableMetaData(colNames, ht, rowCount);
		table = new JTable(data, null);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(true);
        Dimension dim = table.getPreferredSize();
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension((int)(1.5*dim.width), 
                (int)(1.5*dim.height)));
		Border etch  = BorderFactory.createEtchedBorder();
		TitledBorder titledBorder = 
			new TitledBorder(etch, title);
		pane.setBorder(titledBorder);
        add(pane, BorderLayout.NORTH);
	}
	
	public void insertValue(Object value, int row, int col) {
		table.setValueAt(value, row, col);
	}
}
