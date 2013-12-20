package dbmeta.reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
//import java.io.*;
import java.text.DecimalFormat;


/**
 * SQL-Abfragen der normalen Daten in den Tabellen 
 * @author Jörg Reichert
 */
public class DBReaderLevel3 {
	private DecimalFormat format;
	
	DBReaderLevel3() {
		format = new DecimalFormat();
		format.applyPattern("0000");
	} // Konstruktor
	
	
    /**
     * Abrechnungsdaten von bestimmtem Kunden
     * @param con bestehende Datenverbindung
     */
    public void makeQuery(Connection con, String query, javax.swing.JTextArea area) {
        try {
        	Statement sta = con.createStatement();
        	ResultSet rs = sta.executeQuery(query);        	
        	ResultSetMetaData md = rs.getMetaData();
        	int colCount = md.getColumnCount();
        	for(int i=1; i<=colCount; i++) {
        		area.append(md.getColumnLabel(i));
        		area.append(" | ");
        	}
        	area.append("\n");  area.append("\n");
            /*File file = new File("objekt_id.txt");
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            int i=0;*/
            while(rs.next()) {
                    //fw.write(format.format(i++) + " :");
            	for(int i=1; i<=colCount; i++) {
            		area.append(rs.getString(i));
            		area.append(" | ");
            		try { 
            			//fw.write(rs.getObject(1).toString());
            		} catch(Exception ex) {  }
            		// fw.write("\n");
            	}
            	area.append("\n");
            } // while (datensatzweise)
            //fw.close();
            System.out.println("Fertig");
            sta.close();
            rs.close();
        } catch(Exception e) {
            e.printStackTrace();
        } // try-catch
    } // makeQuery
} // DBReaderLevel3
