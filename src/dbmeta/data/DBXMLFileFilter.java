package dbmeta.data;
import java.io.File;
import javax.swing.filechooser.*;
/**
 * filtert alle Dateien aus, die nicht auf .xml enden 
 * @author Jörg Reichert
 */
public class DBXMLFileFilter extends FileFilter {

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        } // if (Verzeichnis)
        String name = f.getName();        
        if (name.endsWith(".xml")) {
            return true;
        } else {
            return false;
        } // if-else (endet auf .xml)
    } // accept

    public String getDescription() {
        return "*.xml";
    } // getDescription
} // DBXMLFileFilter
