package edu.ucla.cs.cs144;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class ItemBean {
	private String itemId;
	private String name;
	private User seller;
	private String[] categories;
	private String startTime;
	private String endTime;
	private String currently;
	private String buy_price;
	private String first_bid;
	private String num_of_bids;
	private Location location;
	private String country;
	private String description;
	private Bid[] bids;

	public ItemBean (){
		this.itemId = "";
	}
	public ItemBean(String id){
		this.itemId = id;
	}
	public void setName (String name){
		this.name = name;
	}
	public void setSeller(User seller){
		this.seller = seller;
	}
	public void setStartTime(String startTime){
		this. startTime = startTime;
	}
	public void setEndTime (String endTime){
		this.endTime = endTime;
	}
	public void setCurrently (String currently){
		this.currently = currently;
	}
	public void setBuyPrice (String buyPrice){
		this.buy_price = buyPrice;
	}
	public void setFristBid (String first_bid){
		this.first_bid = first_bid;
	}
	public void setNumOfBids (String num_of_bids){
		this.num_of_bids = num_of_bids;
	}
	public void setLocation (Location location){
		this.location = location;
	}
	public void setCountry (String country){
		this.country = country;
	}
	public void setDescription (String description){
		this.description = description;
	}
	public void setCategories (String[] categories){
		this.categories = categories;
	}
	public void setBids(Bid[] bids){
		this. bids = bids;
	}

	public String getItemId(){
		return this.itemId;
	}
	public String getName(){
		return this.name;
	}
	public User getSeller(){
		return this.seller;
	}
	public String getStartTime(){
		return this.startTime;
	}
	public String getEndTime(){
		return this.endTime;
	}
	public String getCurrently(){
		return this.currently;
	}
	public String getBuyPrice(){
		return this.buy_price;
	}
	public String getFirstBid(){
		return this.first_bid;
	}
	public String getNumOfBids(){
		return this.num_of_bids;
	}
	public Location getLocation(){
		return this.location;
	}
	public String getCountry(){
		return this.country;
	}
	public String getDescription(){
		return this.description;
	}
	public String[] getCategories(){
		return this.categories;
	}
	public Bid[] getBids(){
		return this.bids;
	}
}