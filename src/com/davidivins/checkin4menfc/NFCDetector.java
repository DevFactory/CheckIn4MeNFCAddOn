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
 * @author david ivins
 */
public class NFCDetector extends Activity
{
	private static final String TAG = "NFCDetector";

	@Override
	public void onCreate(Bundle bundle) 
	{
		super.onCreate(bundle);
		this.setContentView(R.layout.main);
		Log.i(TAG, "NFC Detected");
		
		handleIntent(getIntent());
	}
	
	public void handleIntent(Intent intent) 
	{
		String action = intent.getAction();
		NdefMessage[] msgs = null;
		
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) ||
				NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) ||
				NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) 
		{
			Log.i(TAG, "Found Tag");
			
			msgs = NFCUtils.getNdefMessages(intent);
			NFCUtils.dumpMessages(msgs);
			
			if (msgs.length > 0)
			{
				NdefRecord[] records = msgs[0].getRecords();
				
				if (records.length > 0)
				{
					//String id = new String(records[0].getId());
					//String type = new String(records[0].getType());
					String payload = new String(records[0].getPayload());
				
					Intent checkin4me = new Intent("com.davidivins.checkin4me.action.NFC");
					checkin4me.putExtra("url", "http://" + payload);
//		        checkin4me.setPackage("com.davidivins.checkin4me.debug");
//		        //checkin4me.putExtra("SCAN_MODE", "QR_CODE_MODE");
					startActivityForResult(checkin4me, 0);
				
//				Intent intent = new Intent();
//				intent.setComponent(new ComponentName("com.davidivins.checkin4me.debug", "com.example.MyExampleActivity"));
//				startActivity(intent)
				}
			}
		}
		else
		{
			Intent main = new Intent(this, CheckIn4MeNFCAddOn.class);
	        startActivity(main);
		}

	}
}