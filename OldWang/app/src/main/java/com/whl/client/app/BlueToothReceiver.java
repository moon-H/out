

package com.whl.client.app;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.whl.client.R;

public class BlueToothReceiver extends BroadcastReceiver
{
	private String btMessage="";

	@Override
	public void onReceive(Context context, Intent intent)
	{
        String action = intent.getAction();

        Log.i("TAG---BlueTooth", action);
        if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
            if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR) == BluetoothAdapter.STATE_ON){
                BizApplication.getInstance().setBluetoothState(true);
                Toast.makeText(context.getApplicationContext(), R.string.bluetooth_on, Toast.LENGTH_SHORT).show();
            }else if(BluetoothAdapter.STATE_OFF == intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)){
                BizApplication.getInstance().setBluetoothState(false);
            }

        }


	}

}
