package dbmeta;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;

public class DBMetaView extends JFrame {
    public final static long serialVersionUID = 0;
    private DBConnectorView conView;
    private JPanel tablePanel;
    private JButton closeBtn;
    
    DBMetaView() {
        tablePanel = new JPanel();
        JScrollPane pane = new JScrollPane(tablePanel);
        Panel btnPanel = new Panel();
        btnPanel.add(closeBtn = new JButton("Verbindung schlieﬂen"));
        add(pane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }
    
    void openWindow(String dbName, ActionListener al, 
            String close) {
        setTitle(dbName);
        WindowSetter.openWindow(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        closeBtn.setActionCommand(close);
        closeBtn.addActionListener(al);
        setVisible(true);
    }
    
    void addTable(String title, int rowCount, 
            Hashtable <String, String> ht) {
        String [] colNames = {"Name", "Typ"} ;
        tablePanel.add(new DBTableGrid(title, 
                colNames, ht, rowCount));
    }
    
    void openMessageDialog(String title, String message) {
        JOptionPane pane = new JOptionPane();
        pane.setMessage(message);
        JDialog dialog = pane.createDialog(this, "Fehler");
        dialog.setVisible(true);
    }    
    
    void openConnector(Vector <String> boxContent, 
            ActionListener al, String open, String cancel) {
        conView = new DBConnectorView(this, boxContent);
        conView.setListener(al, open, cancel);
        conView.setVisible(true);
    }
    
    DBConnection getDBConnection() {
        return conView.getDBConnection();
    }
    
    void closeConnector() {
        conView.dispose();
    }
}
