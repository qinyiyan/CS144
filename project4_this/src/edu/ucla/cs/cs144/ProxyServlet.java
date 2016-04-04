package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try{
        	String q = request.getParameter("q");
        	URL newURL = new URL("http://google.com/complete/search?output=toolbar&q=" + URLEncoder.encode(q, "UTF-8"));
        	HttpURLConnection con = (HttpURLConnection)newURL.openConnection();
        	con.connect();
        	//con.setRequestMethod("GET");

        	InputStreamReader isr= new InputStreamReader(con.getInputStream());
        	BufferedReader bReader = new BufferedReader(isr);
        	String inputLine = bReader.readLine();
        	StringBuffer buffer = new StringBuffer();
        	while (inputLine != null){
        		buffer.append(inputLine);
        		inputLine = bReader.readLine();
        	}
        	
        	bReader.close();
        	con.disconnect();

        	response.setContentType("text/xml");
        	response.getWriter().println(buffer.toString());
        } catch (Exception e){
        	e.printStackTrace();
        }

    }
}
