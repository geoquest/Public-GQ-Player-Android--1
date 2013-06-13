package com.uni.bonn.nfc4mg.inventory;

import java.io.IOException;
import java.util.HashMap;

import com.uni.bonn.nfc4mg.exception.NfcTagException;
import com.uni.bonn.nfc4mg.exception.TagModelException;
import com.uni.bonn.nfc4mg.tagmodels.ResourceTagModel;

import android.nfc.FormatException;
import android.nfc.Tag;

/**
 * Inventory Manager class to manage the data about all resources earned during
 * the game. The entire inventory is managed in terms of key and value pairs.
 * 
 * @author shubham
 * 
 */
public class InventoryManager {

	private static InventoryManager INSTANCE = null;
	private OnResourceCallback mOnResourceCallback = null;
	private static HashMap<String, InventoryModel> INVENTORY_REPO = new HashMap<String, InventoryModel>();

	/**
	 * Singleton Class
	 */
	private InventoryManager() {
	}

	/**
	 * Get the instance of inventory manager.
	 * 
	 * @return
	 */
	public static InventoryManager getInventoryManager() {

		if (null == INSTANCE) {
			INSTANCE = new InventoryManager();
		}
		return INSTANCE;
	}

	/**
	 * API to interact with resource model. OnResourceCallback has to override
	 * in order to get immediate callback.
	 * 
	 * @param tag
	 * @throws IOException
	 * @throws FormatException
	 * @throws TagModelException
	 * @throws NfcTagException
	 */
	public void handleResourceTag(Tag tag) throws IOException, FormatException,
			TagModelException, NfcTagException {

		ResourceTagModel model = ResourceTag.readTagData(tag);

		int count = model.getCount();

		//check resource is already taken or not.
		if (0 == count) {

			// call callback in case resource is empty
			if (null != mOnResourceCallback) {

				mOnResourceCallback
						.onResourceEmpty(
								InventoryErrors.ErrorCodes.ERROR_RESOURCE_ALREADY_TAKEN,
								InventoryErrors.ErrorMsg.ERROR_RESOURCE_ALREADY_TAKEN);
			} else {

				// update the resource inventory and call success callback
				model.setCount(model.getCount() - 1);
				if (ResourceTag.write2Tag(model, tag)) {

					// add item to inventory
					InventoryModel addedModel = addItem(model.getId(),
							model.getName());
					if (null != mOnResourceCallback) {
						mOnResourceCallback.onEarnedResource(addedModel);
					}
				}
			}
		}
	}

	/**
	 * Add item to inventory
	 * 
	 * @param key
	 *            : Each NFC Tag is initialized with unique id, that id can be
	 *            treated as the key in inventory.
	 * @param model
	 */
	private InventoryModel addItem(String key, String name) {

		InventoryModel item = new InventoryModel(key, name);
		INVENTORY_REPO.put(key, item);
		return item;
	}

	/**
	 * Remove Item from inventory
	 * 
	 * @param key
	 *            : Each NFC Tag is initialized with unique id, that id can be
	 *            treated as the key in inventory.
	 */
	public void removeItem(String key) {
		INVENTORY_REPO.remove(key);
	}

	/**
	 * To check inventory is empty or not.
	 * 
	 * @return
	 */
	boolean isInventoryEmpty() {
		return INVENTORY_REPO.isEmpty();
	}

	/**
	 * To delete entire inventory NOTE : calling this api will remove all items
	 * present in the inventory
	 */
	public void deleteInventory() {
		INVENTORY_REPO.clear();
	}

	/**
	 * Check for the specific item present in inventory or not
	 * 
	 * @param key
	 * @return
	 */
	public boolean isItemPresent(String key) {
		return INVENTORY_REPO.containsKey(key);
	}

	/**
	 * Returns the size of inventory
	 * 
	 * @return
	 */
	public int inventorySize() {
		return INVENTORY_REPO.size();
	}

	/**
	 * Return the item model for the given key, else this function will return
	 * null
	 * 
	 * @param key
	 * @return
	 */
	public InventoryModel getItem(String key) {
		return INVENTORY_REPO.get(key);

	}

	/**
	 * This function will initialize the resource tag first time.
	 * 
	 * @param model
	 * @param tag
	 * @return
	 * @throws TagModelException
	 * @throws IOException
	 * @throws FormatException
	 * @throws NfcTagException
	 */
	public boolean initializeGroupTag(ResourceTagModel model, Tag tag)
			throws TagModelException, IOException, FormatException,
			NfcTagException {

		return ResourceTag.write2Tag(model, tag);
	}

	/**
	 * Resource callback listener. Called when player interacts with resource
	 * tag
	 * 
	 * @param listener
	 */
	public void setOnResourceCallback(OnResourceCallback listener) {

		this.mOnResourceCallback = listener;
	}

	/**
	 * Callback when player interact with the resource tag.
	 * 
	 * @author shubham
	 * 
	 */
	public static interface OnResourceCallback {

		public void onResourceEmpty(int error, String msg);

		public void onEarnedResource(InventoryModel addedModel);
	}
}
