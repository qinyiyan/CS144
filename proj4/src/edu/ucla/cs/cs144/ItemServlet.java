package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;


public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

     /*credited from MyParser.java*/
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

    /*credited from MyParser.java*/

    public static Document xmlToDocument(String xmlData) throws ParserConfigurationException, SAXException ,IOException {
    	//System.out.println(xmlData);
    	if (xmlData==null || xmlData =="")
    		return null;
    	StringReader reader = new StringReader(xmlData);
    	InputSource source = new InputSource(reader);
    	DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder dbuilder = dbfactory.newDocumentBuilder();
    	Document doc = dbuilder.parse(source);
    	return doc;
    }
    public static ItemBean makeItem(Document doc){
    	if (doc==null ) 
    		return null;
    	Element itemElement =  doc.getDocumentElement();
    	String itemId = itemElement.getAttribute("ItemID");
    	if (itemId == "" || itemId ==null)
    		return null;
    	else{
	    	ItemBean item = new ItemBean(itemId);
	    	//obtain each item's elements
	    	item.setName(getElementTextByTagNameNR(itemElement, "Name"));
	    	item.setCurrently(getElementTextByTagNameNR(itemElement, "Currently"));
	    	item.setBuyPrice(getElementTextByTagNameNR(itemElement, "Buy_Price"));
	    	item.setFristBid(getElementTextByTagNameNR(itemElement, "First_Bid"));
	    	item.setCountry(getElementTextByTagNameNR (itemElement, "Country"));
	    	item.setStartTime(getElementTextByTagNameNR (itemElement, "Started"));
	    	item.setEndTime(getElementTextByTagNameNR (itemElement, "Ends"));
	    	item.setDescription(getElementTextByTagNameNR (itemElement, "Description"));
	    	//location
	    	Element locationElement = getElementByTagNameNR (itemElement, "Location");
	    	String latitude = locationElement.getAttribute ("Latitude");
	    	String longitude = locationElement.getAttribute ("Longitude");
	    	String location = getElementTextByTagNameNR (itemElement, "Location");
	    	item.setLocation(new Location(location,longitude,latitude));
	    	//seller
	    	Element sellerElement = getElementByTagNameNR (itemElement, "Seller");
	    	String sellerID = sellerElement.getAttribute("UserID");
	    	String rating = sellerElement.getAttribute("Rating");
	    	item.setSeller(new User(sellerID, rating));
	    	//bids
	    	Element[] bidsElement = getElementsByTagNameNR(itemElement,"Bids");
	    	ArrayList bidsList = new ArrayList();
	    	for (int i=0; i<bidsElement.length; i++)
	        {
	            Element bidder = getElementByTagNameNR(bidsElement[i], "Bidder");
	            String bidderID = bidder.getAttribute ("UserID");
	            String bidderRating = bidder.getAttribute("Rating");
	            String bidderLocation = getElementTextByTagNameNR(bidder,"Location");
	            String bidderCountry = getElementTextByTagNameNR(bidder, "Country");
	            String time = getElementTextByTagNameNR(bidsElement[i], "Time");
	            String amount = getElementTextByTagNameNR(bidsElement[i], "Amount");
	            User newBidder = new User(bidderID, bidderRating, bidderLocation, bidderCountry);
	            bidsList.add(new Bid(newBidder, time, amount));
	        }
	        Bid [] mybids = (Bid[])bidsList.toArray(new Bid[bidsList.size()]);
	        item.setBids(mybids);
	        //categories
	        Element[] categories = getElementsByTagNameNR (itemElement, "Category");
	        ArrayList catList = new ArrayList();
	        for (int i=0; i<categories.length; i++)
	        {
	            String category = getElementText (categories[i]);
	            //String formated_category = formatString(itemID, category);
	            catList.add(category);
	        }
	        String [] categoryArr = (String[])catList.toArray(new String [catList.size()]);
	        item.setCategories(categoryArr);
	        return item;
    }
}
 
    protected void doGet (HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
    	AuctionSearchClient asClient = new AuctionSearchClient();
        String itemId = request.getParameter("itemID");
        String xmlData = asClient.getXMLDataForItemId(itemId);
        if (xmlData == null)
        	request.getRequestDispatcher("/getEmptyItem.html").forward(request, response);
        else{
        	try{
	        	Document doc = xmlToDocument(xmlData);
	        	ItemBean item = makeItem(doc);
	        	if (item ==null  )
		    		request.getRequestDispatcher("/getEmptyItem.html").forward(request, response);
		    	else if (item.getItemId() == "")
		    		request.getRequestDispatcher("/getEmptyItem.html").forward(request, response);
		    	else{
			        //request.setAttribute
			        request.setAttribute("itemId", itemId);
			        request.setAttribute("name", item.getName());
			        request.setAttribute("sellerId", item.getSeller().getUserId());
			        request.setAttribute("sellerRating", item.getSeller().getRating());
			        request.setAttribute("start_time", item.getStartTime());
			        request.setAttribute("end_time", item.getEndTime());
			        request.setAttribute("currently", item.getCurrently());
			        request.setAttribute("buy_price", item.getBuyPrice());
			        request.setAttribute("first_bid", item.getFirstBid());
			        request.setAttribute("num_of_bids", item.getNumOfBids());
			        request.setAttribute("location", item.getLocation());
			        request.setAttribute("country", item.getCountry());
			        request.setAttribute("description", item.getDescription());
			        request.setAttribute("categories", item.getCategories());
			        request.setAttribute("bids", item.getBids());
			        request.setAttribute("xml", xmlData);
			        request.getRequestDispatcher("/getItemResult.jsp").forward(request, response);
			    }
	        }catch(ParserConfigurationException| SAXException e){
	        	e.printStackTrace();
	        }

	    }
    }
}
