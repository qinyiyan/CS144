
package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParserPrint {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    private static BufferedWriter itemFileWriter;
    private static BufferedWriter sellerFileWriter;
    private static BufferedWriter bidderFileWriter;
    private static BufferedWriter categoryFileWriter;
    private static BufferedWriter bidInfoFileWriter; 
    static final String[] typeName = {
    "none",
    "Element",
    "Attr",
    "Text",
    "CDATA",
    "EntityRef",
    "Entity",
    "ProcInstr",
    "Comment",
    "Document",
    "DocType",
    "DocFragment",
    "Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        Element[] items = getElementsByTagNameNR (doc.getDocumentElement(), "Item");    //arg: root, tag
        try {
            //System.out.println("test1");
            for (int i=0; i< items.length; i++)
            {
                //System.out.println("test2");
                extractItem(items[i]);
                extractSeller(items[i]);
                extractBidder(items[i]);
                extractCategory(items[i]);
                extractBidInfo(items[i]);
            }
        }
        catch (IOException error) {
            System.out.println ("error using extract functions");
            error. printStackTrace ();
        }
        /**************************************************************/
    }
    //auxiliary methods:
    /* extractItem(Element item)
    given one Item, extract it's elements/attibutes from the file: 
    ItemID*, name,  currently,  buy_price, first_bid，location, latitude, longitude, country, started, ends, description, sellerID
    */
    public static void extractItem (Element theItem) throws IOException
    {
        //System.out.println("test3");
        try {
            String itemID =theItem.getAttribute ("ItemID");
            String name = getElementTextByTagNameNR (theItem, "Name");
            String currently = strip (getElementTextByTagNameNR(theItem, "Currently"));
            String buy_price = strip (getElementTextByTagNameNR(theItem, "Buy_Price"));
            String first_bid = strip (getElementTextByTagNameNR(theItem, "First_Bid"));
            String location = getElementTextByTagNameNR (theItem, "Location");
            String latitude = "";
            String longitude = "";
            if (location == null)
            {
                location = "";
            }
            else 
            {
                Element theLocation = getElementByTagNameNR (theItem, "Location");
                latitude = theLocation.getAttribute ("Latitude");
                if (latitude == null) 
                    latitude = "";
                longitude = theLocation.getAttribute ("Longitude");
                if (longitude == null)
                    longitude = "";
            }
            String country = getElementTextByTagNameNR (theItem, "Country");
            if (country == null)
                country = "";
            String started_raw = getElementTextByTagNameNR (theItem, "Started");
            String ends_raw = getElementTextByTagNameNR (theItem, "Ends");
            //transform to format the data/time string
            String started = dateFormat (started_raw);
            String ends = dateFormat (ends_raw);
            String description = getElementTextByTagNameNR (theItem, "Description");
            if (description.length()>4000)
                description = description.substring (0,4000);
            Element theSeller = getElementByTagNameNR (theItem, "Seller");
            String sellerID = theSeller.getAttribute("UserID");
            //format the strings, and load to the file
            String formated_item = formatString (itemID, name,  sellerID, currently, buy_price, first_bid, location, latitude,longitude, country, started, ends, description );
            //System.out.println("before load, the string is"+formated_item);
            loadFile (itemFileWriter, formated_item);
        }
        catch (ParseException e1){
            e1.printStackTrace();
            System.out.println("parse error");
        }
        catch (IOException e2){
            e2.printStackTrace();
        }
        
    }

    /*extractSeller(Element theItem)
    given one Seller, extract it's attributes:
    sellerID*, rating
    */
    public static void extractSeller (Element theItem) throws IOException
    {
        Element theSeller = getElementByTagNameNR (theItem, "Seller");
        String sellerID = theSeller.getAttribute("UserID");
        String rating = theSeller.getAttribute("Rating");
        //format string, and load
        String formated_seller = formatString (sellerID, rating);
        loadFile (sellerFileWriter, formated_seller);
    }

    /*extractBidder(Element theBidder)
    given one Seller, extract it's attributes:
    bidder ID*, rating, location, country
    */
    public static void extractBidder (Element theItem) throws IOException
    {
        Element[] bids = getElementsByTagNameNR(getElementByTagNameNR (theItem, "Bids"),"Bid");
        for (int i=0; i<bids.length; i++)
        {
            Element bidder = getElementByTagNameNR(bids[i], "Bidder");
            String bidderID = bidder.getAttribute ("UserID");
            String rating = bidder.getAttribute("Rating");
            String location = getElementTextByTagNameNR(bidder,"Location");
            if (location == null)
                location = "";
            String country = getElementTextByTagNameNR(bidder, "Country");
            if (country == null)
                country = "";
            String formated_bidder = formatString(bidderID, rating, location, country);
            loadFile (bidderFileWriter, formated_bidder);
        }
    }

    /*extractBidInfo(Element theItem)
    given one Seller, extract it's attributes:
    itemID, bidderID, time, amount
    */
    public static void extractBidInfo (Element theItem) throws IOException
    {
        try{
            String itemID = theItem.getAttribute("ItemID");
            Element[] bids = getElementsByTagNameNR(getElementByTagNameNR (theItem, "Bids"), "Bid");
            for (int i=0; i<bids.length; i++)
            {
                Element bidder = getElementByTagNameNR(bids[i], "Bidder");
                String bidderID = bidder.getAttribute ("UserID");
                String time_raw = getElementTextByTagNameNR(bids[i], "Time");
                String time = dateFormat(time_raw);
                String amount = strip (getElementTextByTagNameNR(bids[i], "Amount"));
                String formated_bidInfo = formatString (itemID, bidderID, time, amount);
                loadFile (bidInfoFileWriter, formated_bidInfo); 
            }
        }
        catch (ParseException e1){
            e1.printStackTrace();
            System.out.println("parse error");
        }
        
    }
     /*extractCategory(Element theItem)
    given one category, extract it's attributes:
    category_name*, itemID*
    */
    public static void extractCategory (Element theItem) throws IOException
    {
        String itemID = theItem.getAttribute("ItemID");
        Element[] categories = getElementsByTagNameNR (theItem, "Category");
        for (int i=0; i<categories.length; i++)
        {
            String category = getElementText (categories[i]);
            String formated_category = formatString(itemID, category);
            loadFile (categoryFileWriter, formated_category);
        }
    }
    /*dateFormat(String time_raw)
    format the date/time string to standard format
    */
    public static String dateFormat(String time_raw) throws IOException, ParseException
    {
        SimpleDateFormat date_in_format = new SimpleDateFormat ("MMM-dd-yyy HH:mm:ss");
        SimpleDateFormat date_out_format = new SimpleDateFormat ("YYYY-MM-dd HH:mm:ss");
        String formated_time = date_out_format.format(date_in_format.parse(time_raw));
        return formated_time;
    }
    /*formatString (String[] list)
    make the strings into string stream, seperated by the seperator
    */
    public static String formatString (String... list) throws IOException
    {
        String formated_string = "";
        int i=0;
        for (; i< list.length-1; i++)
        {
            formated_string += (list[i] + columnSeparator);
        }
        formated_string += list[i];
        return formated_string;
    }
    /*
    load the string into the file using fileWriter
    */
    public static void loadFile (BufferedWriter fileWriter, String formated_string) throws IOException
    {
        //System.out.println("before loadfile");
        fileWriter.write (formated_string);
        //System.out.println("the formated_string is"+formated_string);
        fileWriter.newLine();
    }
    
    public static void recursiveDescent(Node n, int level) {
        // adjust indentation according to level
        for(int i=0; i<4*level; i++)
            System.out.print(" ");
        
        // dump out node name, type, and value  
        String ntype = typeName[n.getNodeType()];
        String nname = n.getNodeName();
        String nvalue = n.getNodeValue();
        
        System.out.println("Type = " + ntype + ", Name = " + nname + ", Value = " + nvalue);
        
        // dump out attributes if any
        org.w3c.dom.NamedNodeMap nattrib = n.getAttributes();
        if(nattrib != null && nattrib.getLength() > 0)
            for(int i=0; i<nattrib.getLength(); i++)
                recursiveDescent(nattrib.item(i),  level+1);
        
        // now walk through its children list
        org.w3c.dom.NodeList nlist = n.getChildNodes();
        
        for(int i=0; i<nlist.getLength(); i++)
            recursiveDescent(nlist.item(i), level+1);
    }  
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        try{
            itemFileWriter  = new BufferedWriter(new FileWriter ("item.dat", true));
            sellerFileWriter = new BufferedWriter(new FileWriter ("seller.dat", true));
            bidderFileWriter = new BufferedWriter(new FileWriter ("bidder.dat", true));
            categoryFileWriter = new BufferedWriter(new FileWriter ("category.dat",true));
            bidInfoFileWriter = new BufferedWriter(new FileWriter ("bidInfo.dat", true));
        /* Process all files listed on command line. */
            for (int i = 0; i < args.length; i++) {
                File currentFile = new File(args[i]);
                processFile(currentFile);
            }
            itemFileWriter.close();
            sellerFileWriter.close();
            bidderFileWriter.close();
            categoryFileWriter.close();
            bidInfoFileWriter.close();
        }
        catch (IOException error2)
        {
            error2.printStackTrace();
            System.out.println ("error last lines");
        }
    }
}
