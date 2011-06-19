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

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;

/**
 * NFCDetector
 * 
 * activity that handle nfc-detection when the nfc add-on was not running in the foreground.
 * 
 * @author david ivins
 */
public class NFCDetector extends Activity
{
	private static final String TAG = "NFCDetector";

	/**
	 * onCreate
	 * 
	 * executed at the creation of the activity.
	 * 
	 * @param bundle
	 */
	@Override
	public void onCreate(Bundle bundle) 
	{
		super.onCreate(bundle);
		this.setContentView(R.layout.main);
		Log.i(TAG, "NFC Detected!");
		
		handleIntent(getIntent());
	}
	
	/**
	 * handleIntent
	 * 
	 * handles the incoming nfc intent.
	 * 
	 * @param intent
	 */
	public void handleIntent(Intent intent) 
	{
		String action = intent.getAction();
		Log.i(TAG, "handling intent action = " + action);
		
		// only handle nfc-related intents
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) ||
			NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) ||
			NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) 
		{
			Log.i(TAG, "Found Tag");
			
			NdefMessage[] msgs = NFCUtils.getNdefMessages(intent);
			NFCUtils.dumpNdefMessages(msgs);
			
			// loop through each ndef message
			for (int i = 0; i < msgs.length; i++)
			{
				NdefRecord[] records = msgs[i].getRecords();
			
				// loop through each record in the current ndef message
				for (int j = 0; j < records.length; j++)
				{
					// get type and payload of current record
					String type = new String(records[j].getType());
					Log.i(TAG, "type = " + type);

					String payload = new String(records[j].getPayload());
					Log.i(TAG, "payload = " + payload);
					
					// send to checkin4me app
//					Intent checkin4me = new Intent("com.davidivins.checkin4me.action.NFC");
//					checkin4me.putExtra("url", "http://" + payload);
//					startActivityForResult(checkin4me, 0);
				}
			}
		}
	}
}