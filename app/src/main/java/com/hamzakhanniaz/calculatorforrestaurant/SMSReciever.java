package com.hamzakhanniaz.calculatorforrestaurant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Hamza Khan Niaz on 4/30/2016.
 */
public class SMSReciever extends BroadcastReceiver {
    EditText rec;

    @Override
    public void onReceive(Context context, Intent intent) {


        //---get the SMS message passed in---
        //get message passed in
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages= null;
        String str = "";

        if(bundle != null)
        {
            Object[] pdus = (Object[]) bundle.get("pdus");
            messages = new SmsMessage[pdus.length];

            for(int i=0; i<messages.length; i++)
            {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    String format =  bundle.getString("format");
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i],format);
                }
                else
                {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                str = "Message from " + messages[i].getOriginatingAddress();
                str += " :";
                str += messages[i].getMessageBody().toString();
                str += "\n";

                Log.e("SMS", str);
                //display the message
                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

            }

            //send a broatcast intent to update the SMS recieved in a textview
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("sms", str);
            context.sendBroadcast(broadcastIntent);

            //this will update the UI with message
            MainActivity inst = MainActivity.instance();
            inst.updateList(str);

        }
    }
}
