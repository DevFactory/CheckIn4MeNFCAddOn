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
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.util.Log;

/**
 * CheckIn4MeNFCAddOn
 * 
 * Main activity for providing nfc functionality in CheckIn4Me.
 * Reads an nfc tag and attempts to forward the data (if valid) to
 * the main CheckIn4Me app.
 * 
 * @author david ivins
 */
public class CheckIn4MeNFCAddOn extends Activity
{
	private static final String TAG = "CheckIn4MeNFCAddOn";
	private NfcAdapter adapter;
	
	private static final String[][] techList = new String[][] { 
		new String[] { 
			NfcA.class.getName(),
			NfcB.class.getName(), 
			NfcF.class.getName(),
			NfcV.class.getName(), 
			IsoDep.class.getName(),
			MifareClassic.class.getName(),
			MifareUltralight.class.getName(), 
			Ndef.class.getName() 
		} 
	};
	
	private PendingIntent pendingIntent;
	private IntentFilter[] intentFiltersArray;
	
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
		
		Log.i(TAG, "reading pending intent");
		
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
			getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
		IntentFilter tag  = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		IntentFilter tech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		
		// add data type to  ndef
		try 
		{
			String mine_type = "*/*";
			
			if (null != ndef)
				ndef.addDataType(mine_type);
			
			if (null != tech)
				tech.addDataType(mine_type);
		} 
		catch (MalformedMimeTypeException e) 
		{
			throw new RuntimeException("invalid mine-type", e);
		}
		
		intentFiltersArray = new IntentFilter[] { tag, ndef, tech };
	}
	
	/**
	 * onNewIntent
	 * 
	 * called when a new intent is matched to this activity.
	 * 
	 * @param intent
	 */
	@Override
	public void onNewIntent(Intent intent) 
	{
		String action = intent.getAction();

		// if this is an nfc action
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) ||
			NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)  ||
			NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) 
		{
			Log.i(TAG, "Found Tag");

			// read NDEF message...
			NdefMessage[] msgs = NFCUtils.getNdefMessages(intent);
			NFCUtils.dumpNdefMessages(msgs);
		}
	}
	
	/**
	 * onResume
	 * 
	 * executed when this activity is resumed
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		
		// enable foreground dispatch for nfc
		NfcManager manager = (NfcManager)getSystemService(Context.NFC_SERVICE);
		adapter = manager.getDefaultAdapter();
		adapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techList);
	}
	
	/**
	 * onPause
	 * 
	 * executed when this activity is paused.
	 */
	@Override
	protected void onPause() 
	{
		super.onPause();
		
		// disable foreground dispatch for nfc
		adapter.disableForegroundDispatch(this);
	}
}