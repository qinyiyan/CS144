package edu.ucla.cs.cs144;

import java.io.Serializable;
import java.util.*;
import java.text.*;

public class ItemBean implements Serializable {
	
	public class BidBean {
		protected String bidderID;
		protected String bidderRating;
		protected String bidderLocation;
		protected String bidderCountry;
		protected String time;
		protected String amount;

		public BidBean(){}

		public String getBidderID() {
			return bidderID;
		}

		public String getBidderRating() {
			return bidderRating;
		}

		public String getTime() {
			return time;
		}

		public String getAmount() {
			return amount;
		}

		public String getBidderLocation() {
			return bidderLocation;
		}

		public String getBidderCountry() {
			return bidderCountry;
		}

		public void setBidderID(String bidderID) {
			this.bidderID = bidderID;
		}

		public void setBidderRating(String bidderRating) {
			this.bidderRating = bidderRating;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		} 

		public void setBidderLocation (String bidderLocation) {
			this.bidderLocation = bidderLocation;
		}

		public void setBidderCountry (String bidderCountry) {
			this.bidderCountry = bidderCountry;
		}
	}

	protected String itemID;
	protected String name;
	protected ArrayList<String> categories;
	protected String currently;
	protected String buyPrice;
	protected String firstBid;
	protected String numberOfBids;
	protected ArrayList<BidBean> bids;
	protected String location;
	protected String latitude;
	protected String longitude;
	protected String country;
	protected String started;
	protected String ends;
	protected String sellerID;
	protected String sellerRating;
	protected String description;

	public ItemBean(){ }

	public String getItemID(){
		return itemID;
	}

	public void setItemID(String itemID){
		this.itemID = itemID;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public ArrayList<String> getCategories(){
		return categories;
	}

	public void setCategories(ArrayList<String> categories){
		this.categories = categories;
	}

	public String getCurrently(){
		return currently;
	}

	public void setCurrently(String currently){
		this.currently = currently;
	}

	public String getBuyPrice(){
		return buyPrice;
	}

	public void setBuyPrice(String buyPrice){
		this.buyPrice = buyPrice;
	}

	public String getFirstBid(){
		return firstBid;
	}

	public void setFirstBid(String firstBid){
		this.firstBid = firstBid;
	}

	public String getNumberOfBids(){
		return numberOfBids;
	}

	public void setNumberOfBids(String numberOfBids){
		this.numberOfBids = numberOfBids;
	}

	public ArrayList<BidBean> getBids() {
		return bids;
	}

	public void setBids(ArrayList<BidBean> bids){
		this.bids = bids;
		//这样真的能有效排序吗
		Collection.sort(this.bids, new Comparator<BidBean>(){
			public compare(BidBean bid1, BidBean bid2){
				Date date1, date2;
				try {
					date1 = new SimpleDateFormat("MMM-dd-yy HH:mm:ss").parse(bid1.getBidTime());
				} catch (ParseException e) {
					return -1;
				}

				try {
					date2 = new SimpleDateFormat("MMM-dd-yy HH:mm:ss").parse(bid2.getBidTime());
				} catch(ParseException e) {
					return 1;
				}

				return date2.compareTo(date1);
			}
		});
	}

	public String getLocation(){
		return location;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setLatiture(String latitude){
		this.latitude = latitude;
	}

	public String getLongitude(){
		return longitude;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getCountry(){
		return country;
	}

	public void setCountry(String country){
		this.country = country;
	}

	public String getStarted(){
		return started;
	}

	public void setStarted(String started){
		this.started = started;
	}

	public String getEnds(){
		return ends;
	}

	public void setEnds(String ends){
		this.ends = ends;
	}

	public String getSellerID(){
		return sellerID;
	}

	public void setSellerID(String sellerID){
		this.sellerID = sellerID;
	}

	public String getSellerRating(){
		return sellerRating;
	}

	public void setSellerRating(String sellerRating){
		this.sellerRating = sellerRating;
	}

	public String getDescription(){
		return description;
	}

	public void setDescription(String description){
		this.description = description;
	}
}