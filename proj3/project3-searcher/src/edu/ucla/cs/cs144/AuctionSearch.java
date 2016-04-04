package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.text.SimpleDateFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.*;
import javax.xml.parsers.ParserConfigurationException;

import java.io.StringWriter;


public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	//the searchEngine is credited to the tutorial given by the instructor
public class SearchEngine {
    private IndexSearcher searcher = null;
    private QueryParser parser = null;
    
    /** Creates a new instance of SearchEngine */
    public SearchEngine() throws IOException {
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index"))));
        parser = new QueryParser("content", new StandardAnalyzer());
    }
    
    public TopDocs performSearch(String queryString, int n)
    throws IOException, ParseException {
        Query query = parser.parse(queryString);        
        return searcher.search(query, n);
    }

    public Document getDocument(int docId)
    throws IOException {
        return searcher.doc(docId);
    }
}


	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		// TODO: Your code here!
		ArrayList<SearchResult> resultList = new ArrayList<SearchResult>();
		//SearchResult[] result = new SearchResult[numResultsToReturn];
		try{
		SearchEngine se = new SearchEngine();
		TopDocs topdocs =se.performSearch(query,numResultsToReturn+numResultsToSkip);	//what is the second argument??
		System.out.println("Results found: " + topdocs.totalHits);
		ScoreDoc[] hits = topdocs.scoreDocs;
        //if (numResultsToSkip>hit.length)
		for (int i = numResultsToSkip; i < hits.length; i++) {
            Document doc = se.getDocument(hits[i].doc);
            SearchResult newSR = new SearchResult(doc.get("itemID"), doc.get("name"));
            //result[i]=newSR;
            resultList.add(newSR);
        }

    }
        catch (IOException|ParseException e) {
        System.out.println("Exception caught.\n");
        e.printStackTrace();
    	}
    	System.out.println("performSearch done");
        return resultList.toArray(new SearchResult[0]);
        
      }


	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		//get another ResultSet that satisfy the region constraint
		ArrayList<SearchResult> resultList = new ArrayList<SearchResult>();
		try{
			SearchResult[] basicResult = basicSearch(query, 0, Integer.MAX_VALUE);
			Connection conn = null;
            conn = DbManager.getConnection(true);
            Statement stmt = conn.createStatement();
            String s = ""+region.getLx()+" "+region.getLy()+", "+region.getRx()+" "+region.getLy()+", "+region.getRx()+" "+region.getRy()+", "+ region.getLx()+" "+region.getRy()+", "+region.getLx()+" "+region.getLy();
            ResultSet rs = stmt.executeQuery("SELECT ItemID FROM ItemLocation WHERE MBRContains(GeomFromText ('Polygon((" + s + "))'), Location)");
            HashSet<String> itemIDhs = new HashSet<String> ();
            while (rs.next())
            {
                String theItemID = rs.getString("ItemID");
                itemIDhs.add (theItemID);
            }
            rs.close();
            conn.close();

            
            for (int i=0; i<basicResult.length; i++)
            {
                String itemID_basic = basicResult[i].getItemId();
                if (itemIDhs.contains(itemID_basic))
                {
                    resultList.add(basicResult[i]);
                }
            }
            
		}catch (SQLException e){
			e.printStackTrace();
		}
        if (resultList.size()<numResultsToSkip)
                return new SearchResult[0];
            else if (numResultsToReturn>resultList.size()-numResultsToSkip){
                return (resultList.subList(numResultsToSkip, resultList.size())).toArray(new SearchResult[0]);
            }
            else  {
                return (resultList.subList(numResultsToSkip, numResultsToSkip+numResultsToReturn)).toArray(new SearchResult[0]);
            }
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
        String xmlData = "";
        Connection conn = null;
        try{
            conn = DbManager.getConnection(true);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Item WHERE ItemID ="+ itemId);
            if (!rs.first()) return xmlData;
            else
            {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.newDocument();
                //DTD format element 
                //get everything from Item Table
                String name = rs.getString("Name");
                String sellerID = rs.getString("Seller");
                String currently = rs.getString("Currently");
                String buy_price = rs.getString("Buy_price");
                String first_bid = rs.getString("First_bid");
                String location = rs.getString("Location");
                String latitude = rs.getString("Latitude");
                String longitude = rs.getString("Longitude");
                String country = rs.getString("Country");
                String started = rs.getString("Started");
                String ended = rs.getString("Ends");
                String description = rs.getString("Description");
                SimpleDateFormat date_in_format = new SimpleDateFormat ("YYYY-MM-dd HH:mm:ss");
                SimpleDateFormat date_out_format = new SimpleDateFormat ("MMM-dd-yy HH:mm:ss");
               /* try {
                started = date_out_format.format(date_in_format.parse(started));
                ended = date_out_format.format(date_in_format.parse(ended));
                }catch(ParseException e1){
                    e1.printStackTrace();
                }*/
                started = timestamp(started);
                ended = timestamp(ended);

                //Seller table
                ResultSet sellerRS = stmt.executeQuery("SELECT * FROM Seller WHERE UserID ='" + sellerID + "'");
                sellerRS.first();
                String rating = sellerRS.getString("Rating");
                //category table
                ResultSet categoryRS = stmt.executeQuery("SELECT Category FROM Category WHERE ItemID = "+itemId);
                ArrayList<String> category = new ArrayList<String>();
                while (categoryRS.next()){
                    category.add(categoryRS.getString("Category"));
                }
                //BidInfo table
                ResultSet bidCountRS = stmt.executeQuery("SELECT COUNT(*) FROM BidInfo WHERE ItemID=" +itemId);
                bidCountRS.first();
                String numberofbids = bidCountRS.getString("COUNT(*)");
                //append element to doc
                Element root = doc.createElement("Item");
                root.setAttribute("ItemID", itemId);
                doc.appendChild(root);

                Element nameEle = doc.createElement("Name");
                nameEle.appendChild(doc.createTextNode(name));
                root.appendChild(nameEle);

                Element sellerEle = doc.createElement("Seller");
                sellerEle.setAttribute("UserID", sellerID);
                sellerEle.setAttribute("Rating", rating);
                root.appendChild(sellerEle);

                Element currentlyEle = doc.createElement("Currently");
                currentlyEle.appendChild(doc.createTextNode(currently));
                root.appendChild(currentlyEle);
                Element element_buyprice = doc.createElement("Buy_Price");
                element_buyprice.appendChild(doc.createTextNode(buy_price));
                root.appendChild(element_buyprice);
                Element element_firstbid = doc.createElement("First_Bid");
                element_firstbid.appendChild(doc.createTextNode(first_bid));
                root.appendChild(element_firstbid);
                Element element_numberofbids = doc.createElement("Number_of_Bids");
                element_numberofbids.appendChild(doc.createTextNode(numberofbids));
                root.appendChild(element_numberofbids);

                Element locationEle = doc.createElement("Location");
                if (!latitude.equals(0))
                    locationEle.setAttribute("Latitude", latitude);
                if (!longitude.equals(0))
                    locationEle.setAttribute("Longitude", longitude);
                root.appendChild (locationEle);
                Element countryEle = doc.createElement("Country");
                countryEle.appendChild(doc.createTextNode(country));
                root.appendChild(countryEle);
                Element element_started = doc.createElement("Started");
                element_started.appendChild(doc.createTextNode(started));
                root.appendChild(element_started);
                Element element_ends = doc.createElement("Ends");
                element_ends.appendChild(doc.createTextNode(ended));
                root.appendChild(element_ends);
                Element element_description = doc.createElement("Description");
                element_description.appendChild(doc.createTextNode(description));
                root.appendChild(element_description);

                //category elements
                for (int i=0; i<category.size();i++){
                    Element categoryEle = doc.createElement("Category");
                    categoryEle.appendChild(doc.createTextNode(category.get(i)));
                    root.appendChild(categoryEle);
                }
                //bids elements
                ResultSet bidInfoRS = stmt.executeQuery("SELECT * FROM BidInfo WHERE ItemID ="+ itemId);
                while (bidInfoRS.next())
                {
                    Element bidEle = doc.createElement("Bid");
                    String bidderID = bidInfoRS.getString("BidderID");
                    String time = bidInfoRS.getString("Time");
                    String amount = bidInfoRS.getString("Amount");
                    ResultSet bidderRS = stmt.executeQuery("SELECT * FROM Bidder WHERE BidderID="+ bidderID);
                    String rating_bidder = bidInfoRS.getString("Rating");
                    String location_bidder = bidInfoRS.getString("Location");
                    String country_bidder = bidInfoRS.getString("Country");
                    Element bidderEle = doc.createElement("Bidder");
                    bidderEle.setAttribute("UserID", bidderID);
                    bidderEle.setAttribute("Rating", rating_bidder);
                    if (location_bidder!=""){
                        Element location_bidder_ele = doc.createElement("Location");
                        location_bidder_ele.appendChild(doc.createTextNode(location_bidder));
                        bidderEle.appendChild(location_bidder_ele);
                    }
                    if (country_bidder!=""){
                        Element country_bidder_ele = doc.createElement("Country");
                        country_bidder_ele.appendChild(doc.createTextNode(country_bidder));
                        bidderEle.appendChild(country_bidder_ele);
                    }
                    bidEle.appendChild(bidderEle);
                    Element timeEle = doc.createElement("Time");
                    timeEle.appendChild(doc.createTextNode(time));
                    bidEle.appendChild(timeEle);
                    Element amountEle = doc.createElement("Amount");
                    amountEle.appendChild(doc.createTextNode(amount));
                    bidEle.appendChild(amountEle);
                    
                    root.appendChild(bidEle);

                    
                }
                //transfer to string
                    TransformerFactory tfFactory = TransformerFactory.newInstance();
                    Transformer transformer = tfFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                    StringWriter swriter = new StringWriter();
                    transformer.transform(new DOMSource(doc), new StreamResult(swriter));
                    xmlData = swriter.getBuffer().toString();

            }
        }catch (SQLException|ParserConfigurationException|TransformerException e){
            e.printStackTrace();
        }

		return xmlData;
	}

    private static String timestamp(String date) {
        SimpleDateFormat format_in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format_out = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        StringBuffer buffer = new StringBuffer();
        
        try {
            Date parsedDate = format_in.parse(date);
            return "" + format_out.format(parsedDate);
        }
        catch(Exception pe) {
            System.err.println("Parse error");
            return "Parse error";
        }
    }
    /*
	public static String dateFormat(String time_raw) throws IOException, ParseException
    {
        String formated_time = "";
        try{
        SimpleDateFormat date_in_format = new SimpleDateFormat ("YYYY-MM-dd HH:mm:ss");
        SimpleDateFormat date_out_format = new SimpleDateFormat ("MMM-dd-yy HH:mm:ss");
        formated_time = date_out_format.format(date_in_format.parse(time_raw));
        }catch(ParseException e){
            e.printStackTrace();
        }
        return formated_time;
    }*/
	public String echo(String message) {
		return message;
	}

}
