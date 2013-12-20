/*
 * Created on 18.05.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package dbmeta.view;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DBMetaSQLView extends JDialog {
	public static final long serialVersionUID = 0;
	private JTextField field;
	private JTextArea area;
	private JButton btn;
	
	DBMetaSQLView(JFrame owner) {
        super(owner, "SQLer", true);
        this.setModal(false);
		field = new JTextField(20);
		btn = new JButton("Start");
		area = new JTextArea();
		JScrollPane pane = new JScrollPane(area);
		setLayout(new BorderLayout());
		Container c = this.getContentPane();
		JPanel top = new JPanel();
		top.add(field);
		top.add(btn);
		c.add(top, BorderLayout.NORTH);
		c.add(pane, BorderLayout.CENTER);
		pack();
		int w = this.getPreferredSize().width;
		this.setPreferredSize(new Dimension(w,w));
		WindowSetter.openWindow(this);
	}
	
	public void setListener(EventListener el, String cmd) {
		btn.setActionCommand(cmd);
		btn.addActionListener((ActionListener) el);
	}
	
	public String getSQLQuery() {
		return field.getText().trim();
	}
	
	public JTextArea getTextArea() {
		area.setText("");
		return area;
	}
}
