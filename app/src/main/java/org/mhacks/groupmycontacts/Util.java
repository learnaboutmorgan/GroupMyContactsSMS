package org.mhacks.groupmycontacts;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class Util {

    public static void sendSMS (String phoneNumber, String message, Context context)
    {
        Log.i("Send SMS", "");


        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(context.getApplicationContext(), "Your friends have gotten your text.", Toast.LENGTH_LONG).show();

            SharedPreferences prefs = context.getSharedPreferences("MORGANPREFS", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("NUMBER", phoneNumber);
            editor.putLong("Sent Time", System.currentTimeMillis());
            editor.commit();
        }

        catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }



    }
}
