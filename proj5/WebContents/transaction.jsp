<!DOCTYPE html>


<html>
<head>
	<title>Ebay transaction page</title>
	 <meta charset="utf-8">
</head>
<body>
	<div class="wrapper">
		<div>
			<h1>Item Transaction</h1>
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
			</table>
			<% String URL = "https://" + request.getServerName() + ":8443" + request.getContextPath() + "/confirmation"; %>
			<form methd="POST" action="<%= URL %>">Card Number:
				<input type="text" name="card_num">
				<input type ="submit" value="pay!">
			</form>
		</div>
	</div>
</body>
</html>