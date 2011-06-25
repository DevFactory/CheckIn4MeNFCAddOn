//*****************************************************************************
//    This file is part of CheckIn4Me.  Copyright © 2010  David Ivins
//
//    CheckIn4Me is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    CheckIn4Me is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with CheckIn4Me.  If not, see <http://www.gnu.org/licenses/>.
//*****************************************************************************
package com.davidivins.checkin4menfc;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.util.Log;

/**
 * NFCUtils
 * 
 * Various utility methods for interacting with nfc intents and ndef messages.
 * 
 * @author david
 */
public class NFCUtils 
{
	private static final String TAG = NFCUtils.class.getName();
	
	/**
	 * getNdefMessages
	 * 
	 * returns a pending nfc intent as an array of ndef messages
	 * 
	 * @param intent
	 * @return
	 */
	public static NdefMessage[] getNdefMessages(Intent intent)
	{
		NdefMessage[] msgs = null;

		Parcelable[] raw_msgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			
		if (raw_msgs != null) 
		{
			msgs = new NdefMessage[raw_msgs.length];
				
			for (int i = 0; i < raw_msgs.length; i++) 
			{
				msgs[i] = (NdefMessage)raw_msgs[i];
			}
		}
		else
		{
			msgs = new NdefMessage[0];
		}
		
		return msgs;		
	}
	
	/**
	 * dumbNdefMessages
	 * 
	 * logs the contents of an array of ndef messages.
	 * 
	 * @param msgs
	 */
	public static void dumpNdefMessages(NdefMessage[] msgs)
	{
		Log.i(TAG, "number of ndef messages = " + msgs.length);
		
		for (int i = 0; i < msgs.length; i++)
		{
			NdefRecord[] records = msgs[i].getRecords();
			Log.i(TAG, "number of records = " + records.length);
			
			for (int j = 0; j < records.length; j++)
			{
				String id      = new String(records[j].getId());
				String type    = new String(records[j].getType());
				String payload = new String(records[j].getPayload());
				
				Log.i(TAG, "record number " + (j + 1));
				Log.i(TAG, "id = " + id);
				Log.i(TAG, "type = " + type);
				Log.i(TAG, "payload = " + payload);
			}
		}
	}
}
