#############################################
#		Gourp: Qinyi Yan & Haiman Duan		# 
#############################################

Part B Solutions: 
1)Tables: 

Item{ *ItemID*, Name,  SellerID, Currently,  Buy_price, First_bid，Location, Latitude, Longitude, Country, Started, Ends, Description }

Seller{ *UserID*, Rating}

Bidder {*BidderID*, Rating, Location, Country}

BidInfo {*ItemID*, *BidderID*, *Time*, Amount}

Category { *Category*, *ItemID*}

note: * * denotes primary keys

2) functional dependencies: 
All of non-trivial functinal dependencies specify keys in my relations

Item:
	ItemID -> Name,  SellerID, Currently,  Buy_price, First_bid，Location, Latitude, Longitude, Country, Started, Ends, Description 
Seller:
	UserID -> rating
Bidder:
	BidderID -> Rating, Location, Country
BidInfo:
	ItemID, BidderID, Time -> Amount
Category:
	Category, ItemID

3) All my relations are in BCNF
4) All my relations are in 4NF