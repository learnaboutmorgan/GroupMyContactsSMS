package org.mhacks.groupmycontacts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

 private Button btnSendSMS;
    private Firebase mFirebaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase("https://GroupMyContacts.firebaseio.com");

        btnSendSMS = (Button) this.findViewById(R.id.btn_SendSMS);
        btnSendSMS.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Log.d("SMS", "Clicked the button!!");
                String numbers[] = {"+19195970133", "+12407784399", "+17345459884"
                };
                for(String number : numbers) {
                    Util.sendSMS(number, "Your friend needs your help. Can you come? Respond with Yes or No. Yes will also provide his/her location", MainActivity.this);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
