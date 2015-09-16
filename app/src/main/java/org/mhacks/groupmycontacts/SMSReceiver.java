package org.mhacks.groupmycontacts;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


/**
 * Created by morganhy on 9/12/15.
 */
public class SMSReceiver extends BroadcastReceiver {

    double longitude;
    double latitude;

    public SMSReceiver(){}

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(final Context context, Intent intent) {
        // Retrieves a map of extended data from the intent.

        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null)
            {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++)
                {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    final String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
                    SharedPreferences prefs = context.getSharedPreferences("MORGANPREFS", Context.MODE_PRIVATE);
                    if (senderNum.equals(prefs.getString("NUMBER", "NOPE"))) {
                        Toast.makeText(context, "SAME NUMBER", Toast.LENGTH_SHORT).show();

                        if (System.currentTimeMillis() - prefs.getLong("Sent Time", 0) > 60 * 60 * 1000) {
                            Util.sendSMS(phoneNumber, "An hour has passed. Time to respond has exceeded.", context);
                            // unsubscribe friend
                        } else {
                            if (message.toLowerCase().equals("no")) {
                            } else if (message.toLowerCase().equals("yes")) {
                                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
                                    @Override
                                    public void onLocationChanged(Location location) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                        String latitude = String.valueOf(location.getLatitude());
                                        String longitude = String.valueOf(location.getLongitude());
                                        Util.sendSMS(phoneNumber, "Here is the location. We recommend that you call your friend in advance to check up." + latitude + "," + longitude, context);

                                    }

                                    @Override
                                    public void onStatusChanged(String provider, int status, Bundle extras) {

                                    }

                                    @Override
                                    public void onProviderEnabled(String provider) {

                                    }

                                    @Override
                                    public void onProviderDisabled(String provider) {

                                    }
                                }, null);

                            } else {
                                Util.sendSMS(phoneNumber, "Please reply with yes or no only", context);
                            }
                        }
                    }
                    else {
                        // Not from who we texted
                    }
                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            "Caller ID: "+ senderNum + message, duration);
                    toast.show();

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }
}

