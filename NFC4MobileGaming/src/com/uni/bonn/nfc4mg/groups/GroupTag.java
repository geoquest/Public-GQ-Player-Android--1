package com.uni.bonn.nfc4mg.groups;

import java.io.IOException;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;

import com.uni.bonn.nfc4mg.TextRecord;
import com.uni.bonn.nfc4mg.constants.TagConstants;
import com.uni.bonn.nfc4mg.exception.NfcTagException;
import com.uni.bonn.nfc4mg.exception.TagModelException;
import com.uni.bonn.nfc4mg.tagmodels.GroupTagModel;
import com.uni.bonn.nfc4mg.utility.NfcReadWrite;

/**
 * Class to deal with Group Tag type. User has to create object of this
 * class in order to deal with any operation related to group tags.
 * 
 * @author shubham
 * 
 */
public class GroupTag {

	protected static boolean write2Tag(GroupTagModel model, Tag tag)
			throws TagModelException, IOException, FormatException,
			NfcTagException {

		// check id uniqueness
		if (null == model)
			throw new TagModelException("GroupTagModel is not initialized");

		String id = model.getId();

		// throw exception in case user has not defined tag id.
		if (null == id || "".equals(model.getId()))
			throw new TagModelException("Tag Id is not defined.");

		// id prefix check
		if (!id.startsWith(TagConstants.TAG_TYPE_GROUP_PREFIX))
			model.setId(TagConstants.TAG_TYPE_GROUP_PREFIX + model.getId());

		//check for group capacity
		int capacity = model.getCapacity();
		if(capacity <= 0 || capacity > TagConstants.MAX_GROUP_CAPACITY){
			throw new TagModelException("Fatal Exception : Group Capacity is 4");
		}
		
		//finally create group tag
		NdefRecord records[] = new NdefRecord[4];
		records[0] = TextRecord.createRecord(model.getId());
		records[1] = TextRecord.createRecord(""+model.getCapacity());//explicitly converting integer to string to store into tags
		records[2] = TextRecord.createRecord(""+model.getOccupied());//explicitly converting integer to string to store into tags
		records[3] = TextRecord.createRecord(model.getData());
		
		NdefMessage group_msg = new NdefMessage(records);
		NfcReadWrite.writeToNfc(group_msg, tag);
		return true;
	}

	
	protected static GroupTagModel readTagData(Tag tag) throws IOException,
			FormatException {

		GroupTagModel model = new GroupTagModel();
		NdefMessage msg = NfcReadWrite.readNfcData(tag);
		NdefRecord records[] = msg.getRecords();

		if (null != records && 4 == records.length) {
			model.setId(TextRecord.parseNdefRecord(records[0]).getData());
			model.setCapacity(Integer.parseInt(TextRecord.parseNdefRecord(records[1]).getData()));
			model.setOccupied(Integer.parseInt(TextRecord.parseNdefRecord(records[2]).getData()));
			model.setData(TextRecord.parseNdefRecord(records[3]).getData());
			return model;
		}
		return null;
	}
}
