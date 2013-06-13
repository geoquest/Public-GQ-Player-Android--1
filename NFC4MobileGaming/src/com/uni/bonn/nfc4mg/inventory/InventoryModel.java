package com.uni.bonn.nfc4mg.inventory;

/**
 * This represents the model of items present in inventory. All the items in the
 * inventory must be unique, but in future we can also introduce item count
 * parameter to maintain the number of items.
 * 
 * @author shubham
 * 
 */
public class InventoryModel {

	//id
	
	private String id;
	
	// name of item
	private String name;

	public InventoryModel(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}
}
