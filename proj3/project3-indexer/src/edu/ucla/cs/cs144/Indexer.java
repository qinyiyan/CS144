package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    private IndexWriter indexWriter = null;
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
    public IndexWriter getIndexWriter ()throws IOException
    {
        if (indexWriter == null){
        Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index"));
        IndexWriterConfig idconfig = new IndexWriterConfig (Version.LUCENE_4_10_2, new StandardAnalyzer());
        indexWriter = new IndexWriter(indexDir, idconfig);
        }   
        return indexWriter;

    }
    public void closeIndexWriter() throws IOException
    {
        if (indexWriter !=null)
            indexWriter.close();
    }
    public String getCategory (PreparedStatement ps, String ItemID)
    {
        String categories="";
        try{
            ps.setString(1,ItemID);
            ResultSet prs = ps.executeQuery();
            while(prs.next())
            {
                categories+=prs.getString("Category")+" ";
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return categories;
    }
    public void indexItem (ResultSet rs, PreparedStatement ps) throws IOException
    {
        try{
        //System.out.println("Indexing Item: "+ rs.getInt("ItemID"));
        IndexWriter idWirter = getIndexWriter();
        Document doc = new Document();
        String itemID = rs.getString("ItemID");
        String itemName = rs.getString("Name");
        String itemCategory = getCategory(ps, itemID);
        String itemDescription = rs.getString("Description");
        doc.add (new StringField("itemID", itemID, Field.Store.YES));
        doc.add (new TextField("name",itemName, Field.Store.YES));
        doc.add (new TextField("category", itemCategory, Field.Store.YES));
        doc.add (new TextField("description", itemDescription, Field.Store.YES));
        final String searchText =itemName+" "+itemCategory+" "+itemDescription;
        doc.add (new TextField("content", searchText, Field.Store.NO));
        idWirter.addDocument(doc);
    }catch (IOException| SQLException ex){
        ex.printStackTrace();
    }
    }
    public void rebuildIndexes() {

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);
	} catch (SQLException ex) {
	    System.out.println(ex);
	}

    try {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT ItemID, Name, Description FROM Item");
        PreparedStatement ps =conn.prepareStatement("SELECT Category FROM Category WHERE ItemID=?");
        while (rs.next())
        {
            indexItem(rs, ps);
        }
    }
        catch (IOException | SQLException ex){
            ex.printStackTrace();
        }
        
    

	/*
	 * Add your code here to retrieve Items using the connection
	 * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add 
         * new methods and create additional Java classes. 
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
	 * 
	 */


        // close the database connection
	try {
        closeIndexWriter();
	    conn.close();
	} catch (SQLException|IOException ex) {
	    System.out.println(ex);
	}
    }    

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
