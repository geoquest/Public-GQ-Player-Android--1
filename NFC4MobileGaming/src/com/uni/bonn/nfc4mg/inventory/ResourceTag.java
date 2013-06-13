package com.uni.bonn.nfc4mg.inventory;

import java.io.IOException;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;

import com.uni.bonn.nfc4mg.TextRecord;
import com.uni.bonn.nfc4mg.constants.TagConstants;
import com.uni.bonn.nfc4mg.exception.NfcTagException;
import com.uni.bonn.nfc4mg.exception.TagModelException;
import com.uni.bonn.nfc4mg.tagmodels.ResourceTagModel;
import com.uni.bonn.nfc4mg.utility.NfcReadWrite;

/**
 * Class to deal with Group Tag type. User has to create object of this
 * class in order to deal with any operation related to group tags.
 * 
 * @author shubham
 * 
 */
public final class ResourceTag {

	protected static boolean write2Tag(ResourceTagModel model, Tag tag)
			throws TagModelException, IOException, FormatException,
			NfcTagException {

		// check id uniqueness
		if (null == model)
			throw new TagModelException("ResourceTagModel is not initialized");

		String id = model.getId();

		// throw exception in case user has not defined tag id.
		if (null == id || "".equals(model.getId()))
			throw new TagModelException("Tag Id is not defined.");

		// id prefix check
		if (!id.startsWith(TagConstants.TAG_TYPE_RESOURCE_PREFIX))
			model.setId(TagConstants.TAG_TYPE_RESOURCE_PREFIX + model.getId());

				
		//finally create group tag
		NdefRecord records[] = new NdefRecord[3];
		records[0] = TextRecord.createRecord(model.getId());
		records[1] = TextRecord.createRecord(model.getName());
		records[2] = TextRecord.createRecord(""+model.getCount());//explicitly converting integer to string to store into tags
		
		NdefMessage group_msg = new NdefMessage(records);
		NfcReadWrite.writeToNfc(group_msg, tag);
		return true;
	}

	
	protected static ResourceTagModel readTagData(Tag tag) throws IOException,
			FormatException {

		ResourceTagModel model = new ResourceTagModel();
		NdefMessage msg = NfcReadWrite.readNfcData(tag);
		NdefRecord records[] = msg.getRecords();

		if (null != records && 3 == records.length) {
			model.setId(TextRecord.parseNdefRecord(records[0]).getData());
			model.setName(TextRecord.parseNdefRecord(records[1]).getData());
			model.setCount(Integer.parseInt(TextRecord.parseNdefRecord(records[2]).getData()));
			return model;
		}
		return null;
	}
}
