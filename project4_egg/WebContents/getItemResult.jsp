<%@ page import = "edu.ucla.cs.cs144.ItemBean"%>
<%@ page import ="edu.ucla.cs.cs144.User"%>
<%@ page import ="edu.ucla.cs.cs144.Location"%>
<%@ page import ="edu.ucla.cs.cs144.Bid"%>


<!DOCTYPE html>
<html>
<head>
	<title>Ebay Item Search</title>
	<meta charset="utf-8">
	
	
</head>
<body>
	<h1>Item Page</h1>
	<div>
		<form action="item">
			<input type="text" name="itemID">
			<input type="submit" value = "get Item">
		</form>
		<table>
			<tr>
				<td>ID: <%= request.getAttribute("itemId") %></td>
			</tr>
			<tr>
				<td>Name: <%= request.getAttribute("name") %></td>
			</tr>
			<% 
				String[]categories = (String[])request.getAttribute("categories");
				for (int i=0; i<categories.length; i++){
					String categ = categories[i];
			%>
			<tr>
				<td>category: <%= categ %></td>
			</tr>
			<%
			}
			%>
			<tr>
				<td>Currently: <%= request.getAttribute("currently") %></td>
			</tr>
			<tr>
				<td>First_bid: <%= request.getAttribute("first_bid") %></td>
			</tr>
			<tr>
				<td>Buy_price: <%= request.getAttribute("buy_price") %></td>
			</tr>
			<tr>
				<td>Started_time: <%= request.getAttribute("start_time") %></td>
			</tr>
			<tr>
				<td>End_time: <%= request.getAttribute("end_time") %></td>
			</tr>
			<tr>
				<td>Location: </td>
			</tr>
			<%
				Location theLocation = (Location)request.getAttribute("location");
				String locationName = theLocation.getLocation();
				String longitude = theLocation.getLongitude();
				String latitude = theLocation.getLatitude();
				if (longitude != "" && latitude!=""){
			%>
			<tr>
				<td>Location: <%= locationName %></td>
			</tr>
			<tr>
				<td>Longitude: <%= longitude %></td>
			</tr>
			<tr>
				<td>Latitude: <%= latitude %></td>
			</tr>
			<%
				}
			%>
			<tr>
				<td>Country: <%= request.getAttribute("country") %></td>
			</tr>
			<tr>
				<td>Seller:</td>
			</tr>
			<tr>
				<td>Seller ID: <%= request.getAttribute("sellerId") %></td>
			</tr>
			<tr>
				<td>Seller Rating: <%= request.getAttribute("sellerRating") %></td>
			</tr>
			<tr>
				<td>Description: <%= request.getAttribute("description") %></td>
			</tr>
			
			<tr>
				<td>Bids: </td>
			</tr>
			<tr>
				<td>Number of bids: <%= request.getAttribute("num_of_bids") %></td>
			</tr>

			<% 
				Bid[]bids = (Bid[])request.getAttribute("bids");
				for (int i=0; i<bids.length; i++){
					User bidder = bids[i].getBidder();
					String bidderID = bidder.getUserId();
					String bidderRating = bidder.getRating();
					String bidderLocation = bidder.getLocation();
					String bidderCountry = bidder.getCountry();
					String time = bids[i].getTime();
					String amount = bids[i].getAmount();
			%>
			<tr>
				<td>Bid info: </td>
			</tr>
			<tr>
				<td>Bidder ID: <%= bidderID %> </td>
			</tr>
			<tr>
				<td>Bidder Rating: <%= bidderRating %> </td>
			</tr>
			<tr>
				<td>Bidder Location: <%= bidderLocation %> </td>
			</tr>
			<tr>
				<td>Bidder country: <%= bidderCountry %> </td>
			</tr>
			<tr>
				<td>Time: <%= time %> </td>
			</tr>
			<tr>
				<td> Amount : <%= amount %> </td>
			</tr>
			<%
			}
			%>
		</table>
		<div class = "col">
			<div alt="Map" id="map-canvas"></div>
			<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
		<script type="text/javascript">
		var geoCoder;
		var itemAddress;
		var map;
		var marker;
		var myOptions;
		<%
			Location thislocation = (Location)request.getAttribute("location");
			String latitude = thislocation.getLongitude();
			String longitude = thislocation.getLatitude();
			String address;

			if ((latitude != "" && longitude != "") && (latitude != null && longitude != null)) {
				address = latitude + "" + longitude;
			} else {
				address = thislocation.getLocation();
			}
		%>
		function initialiaze() 
		{
			itemAddress = "<% out.print(address); %>";
			geoCoder = new google.maps.Geocoder();
			geoCoder.geocode({'address' : address}, function(results, status) 
			{
				if (status == google.maps.GeocoderStatus.OK)
				{
					myOptions = {zoom: 14, center:results[0].geometry.location}
					map = new google.maps.Map(document.getElementById('map-canvas'), myOptions);
					marker = new google.maps.Marker({position: results[0].geometry.location, 
						map: map});
				} else 
				{
					//if no location data found
					var ucla = new google.maps.LatLng(34.063509,-118.44541);
					myOptions = {zoom: 5, center = ucla};
					map = new google.maps.Map(document.getElementById('map-canvas'), myOptions);
				}
			});
		}
	</script>
	</div>
</body>
</html>