package edu.ucla.cs.cs144;

public class Location{
	private String location;
	private String longitude="";
	private String latitude="";
	public Location (String location){
		this.location = location;
	}
	public Location(String location,String longitude, String latitude ){
		this.location = location;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public String getLocation(){
		return this.location;
	}
	public String getLongitude(){
		return this.longitude;
	}
	public String getLatitude(){
		return this.latitude;
	}
}