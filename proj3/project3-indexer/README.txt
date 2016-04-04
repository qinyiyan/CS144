In the Indexer.java file, we chose "ItemID", "Name" and "Description" from the Item table. 

Since we should also use "Category" as one of the source of search, we build a getCategory method, using the "ItemID" within the Category table to collect all the categories of the item. 