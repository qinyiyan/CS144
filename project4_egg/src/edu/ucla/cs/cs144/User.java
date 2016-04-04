package edu.ucla.cs.cs144;

public class User{
	private String userId;
	private String rating;
	private String location;
	private String country;
	public User(String userId, String rating){
		this.userId = userId;
		this.rating = rating;
	}
	public User(String userId, String rating, String location, String country){
		this.userId = userId;
		this.rating = rating;
		this.location = location;
		this.country = country;
	}
	public String getUserId(){
		return this.userId;
	}
	public String getRating(){
		return this.rating;
	}
	public String getLocation(){
		return this.location;
	}
	public String getCountry(){
		return this.country;
	}
}