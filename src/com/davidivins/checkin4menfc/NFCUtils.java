package com.davidivins.checkin4menfc;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.util.Log;

public class NFCUtils 
{
	private static final String TAG = "NFCUtils";
	
	public static NdefMessage[] getNdefMessages(Intent intent)
	{
		// Parse the intent
		NdefMessage[] msgs = null;
//		String action = intent.getAction();
		
//		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) 
//		{
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			
			if (rawMsgs != null) 
			{
				msgs = new NdefMessage[rawMsgs.length];
				
				for (int i = 0; i < rawMsgs.length; i++) 
				{
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			}
			else 
			{
				// Unknown tag type
				byte[] empty      = new byte[] {};
				NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
				NdefMessage msg   = new NdefMessage(new NdefRecord[] { record });
				msgs              = new NdefMessage[] { msg };
			}
//		}
//		else 
//		{
//			Log.e(TAG, "Unknown intent " + intent);
//		}
		
		return msgs;
	}
	
	public static void dumpMessages(NdefMessage[] msgs)
	{
		for (int i = 0; i < msgs.length; i++)
		{
			NdefRecord[] records = msgs[i].getRecords();
			
			for (int j = 0; j < records.length; j++)
			{
				String id = new String(records[j].getId());
				String type = new String(records[j].getType());
				String payload = new String(records[j].getPayload());
				Log.i(TAG, id);
				Log.i(TAG, type);
				Log.i(TAG, payload);
			}
		}
	}
}
