package dbmeta.data;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import java.util.Vector;
import java.util.Enumeration;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.jdom.output.Format;

/**
 * erstellt XML-Dokument mit Metadaten der Tabellen einer
 * Datenbank
 * @author J�rg Reichert
 *
 */
public class DBMetaXML {
    private Element database; // Wurzelelement
    private Document doc; // XML-Dokument
    
    /**
     * @param dbName Datenbankname
     * @param enu Sammlung der komplexen Tabellendatenobjekte
     */
    public DBMetaXML(String dbName, Enumeration <DBTable> enu) {
        Element root = new Element("Datenbanken");
        doc = new Document(root);
        database = new Element("Datenbank");
        database.setAttribute("dbName", dbName);
        root.addContent(database);
        while(enu.hasMoreElements()) {
            addTable(enu.nextElement());
        } // while (tabellenweise)
    } // Konstruktor
    
    /**
     * f�gt Unterelement Tabelle hinzu
     * @param dbt Datentyp Tabelle
     */
    private void addTable(DBTable dbt) {
        Element table = new Element("Tabelle");
        table.setAttribute("tName", dbt.getTableName());
        table.setAttribute("Schema", dbt.getSchemaName());
        Element primaryKeys = getPrimaryKeys(dbt); 
        Element foreignKeys = getForeignKeys(dbt);        
        Element columns = getColumns(dbt);
        table.addContent(primaryKeys);
        table.addContent(foreignKeys);        
        table.addContent(columns);
        database.addContent(table);
    } // addTable
    
    /**
     * Prim�rschl�ssel (Spaltenname)
     * @param dbt Datenobjekt mit Metadaten 
     * @return Prim�rschl�ssel�bersicht
     */
    private Element getPrimaryKeys(DBTable dbt) {
        Element primaryKeys = new Element("Prim�rschl�ssel");        
        Element primaryKey;
        Vector <String> keys = dbt.getPrimaryKeys();
        int len = keys.size();
        for(int i=0; i<len; i++) {
            primaryKey = new Element("Schl�ssel");
            primaryKey.setAttribute("Spalte", keys.get(i));
            primaryKeys.addContent(primaryKey);
        } // for (schl�sselweise)
        return primaryKeys;
    } // getPrimaryKeys
    
    /**
     * Fremdschl�ssel mit ihren Verweisen auf andere Schl�ssel
     * @param dbt Datenobjekt mit Metadaten
     * @return Fremdschl�ssel�bersicht
     */
    private Element getForeignKeys(DBTable dbt) {
        Element foreignKeys = new Element("Fremdschl�ssel");
        Enumeration <Vector<DBForeignKey>> keysEnu = 
            dbt.getForeignKeys();
        Vector <DBForeignKey> keys;
        String foreignKeyName;
        Element key, reference;
        DBForeignKey foreignKey;
        int len;
        while(keysEnu.hasMoreElements()) {
            keys = keysEnu.nextElement();
            len = keys.size();
            if(len > 0) {
                foreignKeyName = keys.get(0).foreignKey;
                key = new Element("Schl�ssel");
                key.setAttribute("Spalte", foreignKeyName);
                for(int i=0; i<len; i++) {
                    foreignKey = keys.get(i);
                    reference = new Element("Verweis");
                    reference.setAttribute("Schema", foreignKey.schema);
                    reference.setAttribute("Tabelle", foreignKey.table);
                    reference.setAttribute("Spalte", foreignKey.primaryKey);
                    key.addContent(reference);
                } // for (Verweiseigenschaften)
                foreignKeys.addContent(key);
            } // (mindestens ein Fremdschl�ssel)
        } // while (Fremschl�ssel)
        return foreignKeys;
    } // getForeignKeys
    
    /**
     * �bersicht der Spalten mit ihren Datentypen
     * @param dbt Datenobjekt mit Metadaten
     * @return Spalten�bersichtselement
     */
    private Element getColumns(DBTable dbt) {
        Element columns = new Element("Spalten�bersicht");
        int colCount = dbt.getColCount();
        for(int i=0; i<colCount; i++) {
            Element column  = new Element("Spalte");
            column.setAttribute("Spaltenname", 
                    dbt.getColNameAt(i));
            column.setAttribute("Spaltentyp", 
                    dbt.getColTypeAt(i));            
            columns.addContent(column);
        } // for (spaltenweise)
        return columns;
    } // getColumns
    
    /**
     * gibt XML-Dokument in gegebene Datei aus
     */
    public void print(File f) throws IOException {
        try { 
            FileWriter fw = new FileWriter(f);
            XMLOutputter out = new XMLOutputter(
                    Format.getPrettyFormat().setEncoding(
                            "ISO-8859-1"));
            out.output(doc, fw);
        } catch(IOException ioe) {
            throw new IOException("Konnte XML-Datei " + 
                    "nicht schreiben.");
        } // try-catch
    } // print
} // DBMetaXML