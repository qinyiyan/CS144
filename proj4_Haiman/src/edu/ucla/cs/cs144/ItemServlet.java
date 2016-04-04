package edu.ucla.cs.cs144;

import java.util.*;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.*;


public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    public ItemBin parseXMLString(String xml){
        Document doc = null
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        try {
        	doc = dBuilder.parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
        	e.printStackTrace();
        }

        if (doc == null) return null;

        Element item = doc.getDocumentElement();

        String itemID = item.getAttribute("")

        return itemBin;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String itemID = request.getParameter("id");
        String xml = AuctionSearchClient.getXMLDataForItemId(itemID);
        ItemBin itemBin = parseXMLString(xml);
    }
}
