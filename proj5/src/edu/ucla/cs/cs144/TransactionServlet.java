package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Date;

public class TransactionServlet extends HttpServlet implements Servlet{
	public TransactionServlet(){}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession(true);
		if (session == null){
			request.getRequestDispatcher("/errorSession.html").forward(request,response);
		}else{
			String itemID = (String)session.getAttribute("itemId");
			String itemName = (String)session.getAttribute("name");
			String buyPrice = (String)session.getAttribute("buy_price");
			if (false){
				request.getRequestDispatcher("/errorSession.html").forward(request,response);
			}
			else{
				request.setAttribute("itemId", itemID);
				request.setAttribute("name", itemName);
				request.setAttribute("buy_price", buyPrice);
				request.getRequestDispatcher("/transaction.jsp").forward(request,response);
			}
		}
	}
}