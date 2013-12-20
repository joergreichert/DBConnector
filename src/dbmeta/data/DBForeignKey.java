package dbmeta.data;
/**
 * Datenobjekt f�r einen Verweis eines Fremdschl�ssels
 * auf einen Prim�rschl�ssel einer anderen Tabelle  
 * @author J�rg Reichert
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
