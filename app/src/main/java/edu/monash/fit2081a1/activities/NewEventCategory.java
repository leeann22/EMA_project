package edu.monash.fit2081a1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

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

import edu.monash.fit2081a1.provider.CategoryViewModel;
import edu.monash.fit2081a1.storage.EventCategory;
import edu.monash.fit2081a1.R;
import edu.monash.fit2081a1.SMSReceiver;

public class NewEventCategory extends AppCompatActivity {

    TextView tvCatId;
    TextView tvCatName;
    TextView tvCatCount;
    Switch tvIsActive;
    TextView tvCatLoc;
    private CategoryViewModel categoryViewModel;
    MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_category);

        tvCatId = findViewById(R.id.editTextCatId);
        tvCatName = findViewById(R.id.editTextCatName);
        tvCatCount = findViewById(R.id.editTextNumber);
        tvIsActive = findViewById(R.id.isActive);
        tvCatLoc = findViewById(R.id.editTextLocation);


        /*
         * Register the broadcast handler with the intent filter that is declared in
         * class SMSReceiver @line 11
         */
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER), RECEIVER_EXPORTED);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
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

    public void onSaveCatButtonClick(View view){
//        Intent intent = new Intent(this, DashboardActivity.class);
        EventCategory cs;
        int catCountInt;

        String catName = tvCatName.getText().toString();
        String catCount = tvCatCount.getText().toString();
        String catLoc = tvCatLoc.getText().toString();
        boolean isActive = tvIsActive.isChecked();

        // check for required fields
        if (!catName.isEmpty()){
            // check catName is only alphabet / alphanumeric
            if (catName.matches("^[a-zA-Z0-9 ]+$")){
                if (catName.matches("^[0-9 ]+$")){
                    Toast.makeText(this, "Please ensure Category Name only contains alphanumeric characters", Toast.LENGTH_SHORT).show();
                } else {
                    String msg = null;
                    if (catCount.isEmpty()) {
                        // set default value if event count is empty
                        tvCatCount.setText("0");
                        catCountInt = 0;
                        msg = "Event count defaulted to 0";
                    } else {
                        catCountInt = Integer.parseInt(catCount);
                        if (catCountInt < 0) {
                            Toast.makeText(this, "Event Count must be a positive integer", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (catLoc.isEmpty()){
                        catLoc = "";
                    }
                    // generate category ID
                    String catID = catIdGenerator();
                    tvCatId.setText(catID);
                    String message;
                    if (msg != null){
                        message = "Category saved successfully: " + catID + ", " + msg;
                    } else{
                        message = "Category saved successfully: " + catID;
                    }

                    // store category into storage
                    cs = new EventCategory(catID, catName, catCountInt, catCountInt, isActive, catLoc);
                    categoryViewModel.insertCategory(cs);

                    // toast for successful save and return to dashboard
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(this, "Please ensure Category Name only contains alphanumeric characters", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Please ensure Category Name is filled", Toast.LENGTH_SHORT).show();
        }

    }

    private String catIdGenerator(){
        String idNum;
        Random random = new Random();
        int randNum = random.nextInt(9999);

        if (randNum < 10){
            idNum = "000" + randNum;
        } else if (randNum < 100){
            idNum = "00" + randNum;
        } else if (randNum < 1000){
            idNum = "0" + randNum;
        } else {
            idNum = String.valueOf(randNum);
        }

        char randChar = (char) (random.nextInt(26) + 'A');
        char randChar2 = (char) (random.nextInt(26) + 'A');

        return "C" + randChar + randChar2 + "-" + idNum;
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

            String cat = sT.nextToken();

            // check if first word is "category"
            if (Objects.equals(cat, "category")) {
                String catDeets = sT.nextToken();

                // split remaining details with ";" delimiter
                String[] details = catDeets.split(";", -1);

                // check if number of details is correct
                if (details.length == 3){
                    String catName = details[0];
                    String catCount = details[1];
                    String catIsActive = details[2];
                    String catIsActiveUpper = catIsActive.toUpperCase();

                    // check for required field
                    if (catName.isEmpty()){
                        Toast.makeText(context, errorToast, Toast.LENGTH_SHORT).show();
                    } else{
                        if (!catCount.isEmpty()){

                            // check that event count contains only numbers and parse to integer
                            if (catCount.matches("[0-9]+")){
                                if (Integer.parseInt(catCount) > 0){
                                    // check that isActive is only True, False or empty (non-required)
                                    if (catIsActiveUpper.equals("TRUE") || catIsActiveUpper.equals("FALSE") || catIsActive.isEmpty()){
                                        tvCatName.setText(catName);
                                        tvCatCount.setText(catCount);
                                        tvIsActive.setChecked(catIsActiveUpper.equals("TRUE"));
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
                            catCount = tvCatCount.getText().toString();
                            if (catIsActiveUpper.equals("TRUE") || catIsActiveUpper.equals("FALSE") || catIsActive.isEmpty()){
                                tvCatName.setText(catName);
                                tvCatCount.setText(catCount);
                                tvIsActive.setChecked(catIsActiveUpper.equals("TRUE"));
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