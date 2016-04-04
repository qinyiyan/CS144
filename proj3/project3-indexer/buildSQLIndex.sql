CREATE TABLE IF NOT EXISTS ItemLocation(
ItemID INTEGER NOT NULL,
Location GEOMETRY NOT NULL,
SPATIAL INDEX(Location)) ENGINE=MyISAM;

INSERT INTO ItemLocation (ItemID, Location)
SELECT ItemID, Point(Latitude, Longitude) 
FROM  Item
WHERE Latitude is NOT NULL AND Longitude is NOT NULL;
