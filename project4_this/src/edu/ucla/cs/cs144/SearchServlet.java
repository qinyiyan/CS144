package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ucla.cs.cs144.AuctionSearchClient;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
    
        	String q = request.getParameter("q");
        	String numberResultsToReturn = request.getParameter("numResultsToReturn");
    		String numberResultsToSkip = request.getParameter("numResultsToSkip");
    	int nrtSkip_int;
        int nrtReturn_int;
        //if (numberResultsToSkip==""||numberResultsToSkip==null)
         //   nrtSkip_int = 0;
        //else
    	   nrtSkip_int = Integer.parseInt(numberResultsToSkip);
        //if (numberResultsToReturn==""||numberResultsToReturn==null)
         //   nrtReturn_int =20;
        //else
    	   nrtReturn_int = Integer.parseInt(numberResultsToReturn);


    	AuctionSearchClient asclient = new AuctionSearchClient();
    	SearchResult[] searchresult = asclient.basicSearch(q, nrtSkip_int, nrtReturn_int);

    	request.setAttribute("resultArray", searchresult);
        //request.setAttribute("q",q);
        //request.setAttribute("numResultsToSkip", nrtSkip_int);
        //request.setAttribute("numResultsToReturn", nrtReturn_int);
    	request.getRequestDispatcher("./searchResult.jsp").forward(request, response);
    }
}
