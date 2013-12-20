package dbmeta;
import dbmeta.view.DBMetaView;

public class DBMetaMain {
    private final String TITLE = 
        "Datenbank - Metadaten - Leser";
    
	DBMetaMain() {
        DBMetaView view = new DBMetaView(TITLE);
        new DBMetaController(view);
	} // Konstruktor
	
	public static void main(String[] args) {
		new DBMetaMain();
	} // main
} // DBMetaMain