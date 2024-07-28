package edu.monash.fit2081a1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.StringTokenizer;

import edu.monash.fit2081a1.R;
import edu.monash.fit2081a1.SMSReceiver;
import edu.monash.fit2081a1.storage.Event;

public class NewEvent extends AppCompatActivity {

    TextView tvEventId;
    TextView tvCatId;
    TextView tvEventName;
    TextView tvTicket;
    Switch tvIsActive;
    Gson gson = new Gson();
    ArrayList<Event> db = new ArrayList<Event>();
    MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.READ_SMS
        }, 0);

        tvEventId = findViewById(R.id.editTextEventID);
        tvCatId = findViewById(R.id.editTextCategoryID);
        tvEventName = findViewById(R.id.editTextEventName);
        tvTicket = findViewById(R.id.editTextTicket);
        tvIsActive = findViewById(R.id.isEventActive);

        /*
         * Register the broadcast handler with the intent filter that is declared in
         * class SMSReceiver @line 11
         */
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER), RECEIVER_EXPORTED);
    }

    /*
    * Unregister the receiver when the page is closed
    */
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myBroadCastReceiver);
    }

    /*
     * Register the receiver when the page is reopened
     */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER), RECEIVER_EXPORTED);

    }

    public void onSaveButtonClick(View view){
        Event es;
        int ticketInt;

        String catID = tvCatId.getText().toString();
        String eventName = tvEventName.getText().toString();
        String ticket = tvTicket.getText().toString();
        boolean isActive = tvIsActive.isChecked();

        // check required fields
        if (!eventName.isEmpty() && !catID.isEmpty()){
            // check eventName only has alphabet / alphanumeric
            if (eventName.matches("^[a-zA-Z0-9 ]+$")) {
                if (eventName.matches("^[0-9 ]+$")){
                    Toast.makeText(this, "Please ensure Event Name only contains alphanumeric characters", Toast.LENGTH_SHORT).show();
                } else {
                    // if catID is in shared preferences
                    if (!getDataFromSharedPreference().get(2).equals(catID)){
                        Toast.makeText(this, "Category ID Not Found", Toast.LENGTH_SHORT).show();
                    } else{
                        if (ticket.isEmpty() ){
                            // set default value if ticket field is left empty
                            ticketInt = 0;
                        } else {
                            ticketInt = Integer.parseInt(ticket);
                            // ensure ticket is positive integer
                            if (ticketInt <= 0){
                                Toast.makeText(this, "Ticket Available must be positive integer", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // generate event ID
                        String eventID = eventIdGenerator();

                        // set the event ID
                        tvEventId.setText(eventID);

                        String message = "Event saved: " + eventID + " to " + catID;

                        // store event into storage
                        es = new Event(eventID, catID, eventName, ticketInt, isActive);
                        db.add(es);

                        // store event into shared preferences
                        String dbStr = gson.toJson(db);
                        saveDataToSharedPreference(dbStr);

                        // toast for successful save
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "Please ensure Event Name only contains alphanumeric characters", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please ensure Event Name and Category ID are filled", Toast.LENGTH_SHORT).show();
        }
    }

    private String eventIdGenerator(){
        String idNum = "";
        Random random = new Random();
        int randNum = random.nextInt(99999);

        if (randNum < 10){
            idNum = "0000" + randNum;
        } else if (randNum < 100){
            idNum = "000" + randNum;
        } else if (randNum < 1000){
            idNum = "00" + randNum;
        } else if (randNum < 10000){
            idNum = "0" + randNum;
        } else {
            idNum = String.valueOf(randNum);
        }

        char randChar = (char) (random.nextInt(26) + 'A');
        char randChar2 = (char) (random.nextInt(26) + 'A');

        return "E" + randChar + randChar2 + "-" + idNum;
    }

    private void saveDataToSharedPreference(String dbStr){
        // initialise shared preference class variable to access Android's persistent storage
        SharedPreferences sharedPreferences = getSharedPreferences("EVENT_FILE", MODE_PRIVATE);

        // use .edit function to access file using Editor variable
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // save key-value pairs to the shared preference file
        editor.putString("DB_KEY", dbStr);
        editor.apply();
    }

    private ArrayList<Event> getDataFromSharedPreference(){
        SharedPreferences sharedPreferences = getSharedPreferences("EVENT_FILE", MODE_PRIVATE);

        String stringFromSP = sharedPreferences.getString("DB_KEY", "");

        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        ArrayList<Event> newdb = new ArrayList<Event>();
        newdb = gson.fromJson(stringFromSP, type);

        return newdb;
    }

    class MyBroadCastReceiver extends BroadcastReceiver {
        /*
         * This method 'onReceive' will get executed every time class SMSReceive sends a broadcast
         * */
        @Override
        public void onReceive(Context context, Intent intent) {
            /*
             * Retrieve the message from the intent
             * */
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            String errorToast = "Unrecognized SMS received";

            /*
             * String Tokenizer is used to parse the incoming message
             * */
            StringTokenizer sT = new StringTokenizer(msg, ":");

            String ev = sT.nextToken();

            // check if the first word is "event"
            if (Objects.equals(ev, "event")) {
                String evDeets = sT.nextToken();

                // split the rest of the string with ";" delimiter
                String[] details = evDeets.split(";", -1);

                // check if the number of details is correct
                if (details.length == 4){
                    String eventName = details[0];
                    String categoryId = details[1];
                    String ticket = details[2];
                    String eventIsActive = details[3];
                    String eventIsActiveUpper = eventIsActive.toUpperCase();

                    // check for required fields
                    if (eventName.isEmpty() || categoryId.isEmpty()){
                      Toast.makeText(context, errorToast, Toast.LENGTH_SHORT).show();
                    } else{
                        if (!ticket.isEmpty()){
                            // check if ticket string contains only numbers and convert to int
                            if (ticket.matches("[0-9]+")){
                                if (Integer.parseInt(ticket) > 0){
                                    // check if isActive is only true or false or empty (non-required)
                                    if (eventIsActiveUpper.equals("TRUE") || eventIsActiveUpper.equals("FALSE") || eventIsActive.isEmpty()){
                                        tvCatId.setText(categoryId);
                                        tvEventName.setText(eventName);
                                        tvTicket.setText(ticket);
                                        tvIsActive.setChecked(eventIsActiveUpper.equals("TRUE"));
                                    } else {
                                      Toast.makeText(context, errorToast, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, errorToast, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                            Toast.makeText(context, errorToast, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ticket = tvTicket.getText().toString();
                            if (eventIsActiveUpper.equals("TRUE") || eventIsActiveUpper.equals("FALSE") || eventIsActive.isEmpty()){
                                tvCatId.setText(categoryId);
                                tvEventName.setText(eventName);
                                tvTicket.setText(ticket);
                                tvIsActive.setChecked(eventIsActiveUpper.equals("TRUE"));
                            } else {
                            Toast.makeText(context, errorToast, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, errorToast, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, errorToast, Toast.LENGTH_SHORT).show();
            }
        }
    }
}