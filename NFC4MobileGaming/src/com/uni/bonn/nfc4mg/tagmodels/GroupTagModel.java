package com.uni.bonn.nfc4mg.tagmodels;

/**
 * This class represents the model of Group. NFC Tag which is storing this
 * structure is called GroupTag.
 * 
 * @author shubham
 * 
 */

public class GroupTagModel {

	// Represents the id of a group. Every id value is prefixed by 'grp_'
	private String id;

	private int occupied;

	// Represents capacity of a group. MAX CAPACITY = 4
	private int capacity;

	// Data shared among group users
	private String data;

	public GroupTagModel(){
		
	}
	
	public GroupTagModel(String id, int capacity, int occupied, String data) {
		super();
		this.id = id;
		this.occupied = occupied;
		this.capacity = capacity;
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getOccupied() {
		return occupied;
	}

	public void setOccupied(int occupied) {
		this.occupied = occupied;
	}
}
