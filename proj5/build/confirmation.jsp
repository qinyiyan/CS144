<%@ page import="java.util.Date" %>

<!DOCTYPE html>
<html>
<head>
	<title>Confirmation page</title>
	 <meta charset="utf-8">
	 
</head>
<body>
	<h1>Confirmation Page</h1>
	<div>
		<table>
				<tr>
					<td>itemID: <%= request.getAttribute("itemId") %><td>
				</tr>
				<tr>
					<td>Name: <%= request.getAttribute("name") %></td>
				</tr>
				<tr>
					<td>Buy-now Price: <%= request.getAttribute("buy_price") %></td>
				</tr>
				<tr>
					<td>Credit Card number: <%= request.getAttribute("card_num") %></td>
				</tr>
				<tr>
					<td>Data & time: <%= new Date(request.getSession().getLastAccessedTime()).toString() %></td>
				</tr>
			</table>

	</div>
</body>
</html>