package com.uni.bonn.nfc4mg.inventory;

import java.util.HashMap;

/**
 * Inventory Manager class to manage the data about all resources earned during
 * the game. The entire inventory is managed in terms of key and value pairs.
 * 
 * @author shubham
 * 
 */
public class InventoryManager {

	private static InventoryManager INSTANCE = null;
	
	private static HashMap<String, InventoryModel> INVENTORY_REPO = new HashMap<String, InventoryModel>();

	/**
	 * Singleton Class
	 */
	private InventoryManager() {
	}

	/**
	 * Get the instance of inventory manager.
	 * @return
	 */
	public InventoryManager getInventoryManager() {
		
		if (null == INSTANCE) {
			INSTANCE = new InventoryManager();
		}
		return INSTANCE;
	}

	
	/**
	 * Add item to inventory
	 * @param key : Each NFC Tag is initialized with unique id, that id can be treated as the key in inventory.
	 * @param model
	 */
	public void addItem(String key, String name, boolean autoRemove){		
		
		InventoryModel item = new InventoryModel(name, autoRemove);
		INVENTORY_REPO.put(key, item);
	}
	
	
	/**
	 * Add item to inventory
	 * @param key : Each NFC Tag is initialized with unique id, that id can be treated as the key in inventory.
	 * @param model
	 */
	public void addItem(String key, String name){		
		
		InventoryModel item = new InventoryModel(name, false);
		INVENTORY_REPO.put(key, item);
	}
	
	
	/**
	 * Remove Item from inventory
	 * @param key : Each NFC Tag is initialized with unique id, that id can be treated as the key in inventory.
	 */
	public void removeItem(String key){		
		INVENTORY_REPO.remove(key);
	}
	
	/**
	 * To check inventory is empty or not.
	 * @return
	 */
	boolean isInventoryEmpty(){
		return INVENTORY_REPO.isEmpty();
	}
	
	
	/**
	 * To delete entire inventory
	 * NOTE : calling this api will remove all items present in the inventory
	 */
	public void deleteInventory(){
		INVENTORY_REPO.clear();
	}
	
	/**
	 * Check for the specific item present in inventory or not
	 * @param key
	 * @return
	 */
	public boolean isItemPresent(String key){
		return INVENTORY_REPO.containsKey(key);
	}
	
	
	/**
	 * Returns the size of inventory
	 * @return
	 */
	public int inventorySize(){
		return INVENTORY_REPO.size();
	}

	/**
	 * Return the item model for the given key, else this function will return null
	 * @param key
	 * @return
	 */
	public InventoryModel getItem(String key){
		return INVENTORY_REPO.get(key);
		
	}
	
}
