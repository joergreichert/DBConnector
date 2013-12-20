package dbmeta;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Vector;

public class DBConnectorView extends JDialog {
    public final static long serialVersionUID = 0;
    private JTextField [] fields;
	private JComboBox box;
    private JButton openBtn, cancelBtn;
    
	DBConnectorView(JFrame owner, Vector <String> boxContent) {
        super(owner, "Verbinder", true);
        BorderLayout bl = new BorderLayout();
		bl.setHgap(10);  bl.setVgap(10);
		setLayout(bl);
		Border border = BorderFactory.createEmptyBorder(10, 10, 10,10);
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(border);
		JPanel inputP = createInputPanel(boxContent);
		p.add(inputP, BorderLayout.NORTH);
		JPanel btnP = new JPanel();
		openBtn = new JButton("Verbindung öffnen");
		btnP.add(openBtn);
        cancelBtn = new JButton("Abbrechen");
        btnP.add(cancelBtn);        
		p.add(btnP, BorderLayout.CENTER);
		add(p, BorderLayout.CENTER);
        pack();
        WindowSetter.openWindow(this);
	}
	
	private JPanel createInputPanel(Vector <String> boxContent) {
		String [] lbls = {"Datenbank-Treiber", "Host/IP",  
				"DatenbankName", "Benutzername", "Passwort"};
		int len = lbls.length;
		JPanel p = new JPanel(new GridLayout(len, 1));
        p.add(new JLabel(lbls[0]));
        p.add(box = new JComboBox(boxContent));
        fields = new JTextField[len-1];
        for(int i=1; i<len-1; i++) {
            p.add(new JLabel(lbls[i]));
			p.add(fields[i-1] = new JTextField(13));
		}
		p.add(new JLabel(lbls[len-1]));
		JPasswordField field = new JPasswordField(13);
		p.add(field);
		fields[len-2] = field;
		return p;
	}
    
    void setListener(ActionListener al, String open, 
            String cancel) {
        openBtn.setActionCommand(open);
        openBtn.addActionListener(al);
        cancelBtn.setActionCommand(cancel);
        cancelBtn.addActionListener(al);         
    }
	
    DBConnection getDBConnection() {
        String dbDriverName = (String) box.getSelectedItem();
        String host = fields[0].getText().trim();
        String dbName = fields[1].getText().trim();
        String userName = fields[2].getText().trim();
        String password = fields[3].getText().trim();
        return new DBConnection(dbDriverName, host, dbName, 
                userName, password);
    }
}
