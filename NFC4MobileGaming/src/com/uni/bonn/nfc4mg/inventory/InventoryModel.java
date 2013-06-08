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

	// name of item
	private String name;

	// This field indicate auto deletion of resource after use.
	private boolean autoRemove;

	public InventoryModel(String name, boolean autoRemove) {
		this.name = name;
		this.autoRemove = autoRemove;
	}

	public String getName() {
		return name;
	}

	public boolean isAutoRemove() {
		return autoRemove;
	}
}
