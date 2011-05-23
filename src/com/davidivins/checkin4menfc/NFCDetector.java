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
		
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) 
		{
			// read TagTechnology object...
			Log.i(TAG, "Found Tag");
			NdefMessage[] msgs = NFCUtils.getNdefMessages(intent);
			NFCUtils.dumpMessages(msgs);
		}
		else if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) 
		{
			// read NDEF message...
			Log.i(TAG, "Found Ndef");
			NdefMessage[] msgs = NFCUtils.getNdefMessages(intent);
			NFCUtils.dumpMessages(msgs);
		} 
		else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) 
		{
			Log.i(TAG, "Found Tech");
			NdefMessage[] msgs = NFCUtils.getNdefMessages(intent);
			NFCUtils.dumpMessages(msgs);
	    }
	}
}