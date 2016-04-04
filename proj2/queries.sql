-- queries.sql

-- 1.find the number of users in the database
-- answer should be 13422
SELECT COUNT(*) FROM (
	SELECT UserID FROM Seller UNION 
	SELECT UserID FROM Bidder)T ;

-- 2. Find the number of items in "New York"
-- answer should be 103
SELECT COUNT(*) FROM Item WHERE BINARY Location = 'New York';

-- 3. Find the number of auctions belonging to exactly four categories.
-- answer should be 8365
SELECT COUNT(*) FROM (SELECT COUNT(*) as count FROM Category
	GROUP BY ItemID HAVING count = 4) as b;

-- 4. Find the ID(s) of current (unsold) auction(s) with the highest bid.
-- answer should be 1046740686
SELECT BidInfo.ItemID FROM BidInfo INNER JOIN Item ON BidInfo.ItemID = Item.ItemID
	WHERE Item.Ends > '2001-12-20 00:00:01'
	AND BidInfo.Amount = (
		SELECT MAX(Amount) FROM BidInfo);

-- 5. Find the number of sellers whose rating is higher than 1000.
-- answer should be 3130
SELECT COUNT(*) FROM Seller WHERE Rating > 1000;

-- 6. Find the number of users who are both sellers and bidders.
-- answer should be 6717
SELECT COUNT(*) FROM Seller WHERE UserID IN (
		SELECT UserID FROM Bidder);

-- 7. Find the number of categories that include at least one item with a bid of more than $100.
-- answer should be 150
SELECT COUNT(DISTINCT Category) FROM Category WHERE ItemID IN (
	SELECT ItemID FROM BidInfo WHERE Amount > 100.00);