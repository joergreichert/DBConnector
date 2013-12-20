package dbmeta.data;
/**
 * Datenobjekt für einen Verweis eines Fremdschlüssels
 * auf einen Primärschlüssel einer anderen Tabelle  
 * @author Jörg Reichert
 */
public class DBForeignKey {
    public String foreignKey, schema, table, primaryKey; 
    
    public DBForeignKey(String aForeignKey, String aSchema,
            String aTable, String aPrimaryKey) {
        foreignKey = aForeignKey; 
        schema = aSchema;
        table = aTable; 
        primaryKey = aPrimaryKey;
    } // Konstruktor
} // DBForeignKey
