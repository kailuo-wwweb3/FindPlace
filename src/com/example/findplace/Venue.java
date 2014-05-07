package com.example.findplace;

public class Venue {
	public Venue(String id, String name, String location) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
	}
	private String id;
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getLocation() {
		return location;
	}
	private String name;
	private String location;
	
	
}
