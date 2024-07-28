package edu.monash.fit2081a1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.monash.fit2081a1.R;
import edu.monash.fit2081a1.fragments.FragmentListCategory;
import edu.monash.fit2081a1.provider.CategoryViewModel;
import edu.monash.fit2081a1.provider.EventViewModel;
import edu.monash.fit2081a1.storage.EventCategory;
import edu.monash.fit2081a1.storage.Event;

public class DashboardActivity extends AppCompatActivity {

    TextView tvEventId;
    TextView tvCatId;
    TextView tvEventName;
    TextView tvTicket;
    Switch tvIsActive;
    TextView tvTouchpad;
    private EventViewModel eventViewModel;
    private CategoryViewModel categoryViewModel;
    private DrawerLayout drawerLayout;
    Intent intent;
    Handler uiHandler = new Handler(Looper.getMainLooper());
    List<EventCategory> category;
    // help detect basic gestures like scroll, single tap, double tap, etc
    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView4, new FragmentListCategory()).commit();

        tvEventId = findViewById(R.id.editTextEventID);
        tvCatId = findViewById(R.id.editTextCategoryID);
        tvEventName = findViewById(R.id.editTextEventName);
        tvTicket = findViewById(R.id.editTextTicket);
        tvIsActive = findViewById(R.id.isEventActive);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar dashboardToolbar = findViewById(R.id.toolbar);

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        setSupportActionBar(dashboardToolbar);
        getSupportActionBar().setTitle("Assignment-2");

        View touchpad = findViewById(R.id.touchpad);
        tvTouchpad = findViewById(R.id.tvTouchpad);
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        mDetector = new GestureDetectorCompat(getApplicationContext(), customGestureDetector);
        mDetector.setOnDoubleTapListener(customGestureDetector);
        touchpad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, dashboardToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // get the id of the selected item
        int id = item.getItemId();

        if (id == R.id.op_clear_event) {
            clearAll();
        } else if (id == R.id.op_del_cat) {
            deleteCategory(null);
        } else if (id == R.id.op_refresh) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView4, new FragmentListCategory()).commit();
        } else if (id == R.id.op_del_event) {
            deleteEvent(null);
        }
        return true;
    }

    public void clearAll(){
        tvEventId.setText("");
        tvCatId.setText("");
        tvEventName.setText("");
        tvTicket.setText("");
        tvIsActive.setChecked(false);
    }

    // Option Menu buttons
    public void deleteCategory(View view){
//       delete all saved categories
        categoryViewModel.deleteAllCategory();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView4, new FragmentListCategory()).commit();
    }

    public void deleteEvent(View view){
//        // delete all saved events
        eventViewModel.deleteAllEvents();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView4, new FragmentListCategory()).commit();
    }

    // Navigation Drawer buttons
    public void viewAllCategories(View view){
        // Opens ListCategoryActivity
        intent = new Intent(this, ListCategoryActivity.class);
        startActivity(intent);
    }

    public void viewAllEvents(View view){
        // Opens ListEventActivity
        intent = new Intent(this, ListEventActivity.class);
        startActivity(intent);
    }

    public void onNewEventCatButtonClick(View view) {
        // to new event category page
        intent = new Intent(this, NewEventCategory.class);
        startActivity(intent);
    }

    public void onSaveButtonClick(View view){
        String catID = tvCatId.getText().toString();
        String eventName = tvEventName.getText().toString();
        boolean isActive = tvIsActive.isChecked();

        ExecutorService executor = Executors.newSingleThreadExecutor();

        // check required fields
        if (!eventName.isEmpty() && !catID.isEmpty()){
            executor.execute(() -> {
                String ticket = tvTicket.getText().toString();
                category = categoryViewModel.getCategory(catID);
                if (eventName.matches("^[a-zA-Z0-9 ]+$")) {
                    if (!eventName.matches("^[0-9 ]+$")){
                        for (EventCategory eventCategory : category){
                            if (Objects.equals(eventCategory.getCatID(), catID)) {
                                Event es;
                                int ticketInt;
                                String msg = null;
                                if (ticket.isEmpty()) {
                                    // set default value if ticket field is left empty
                                    runOnUiThread(() -> tvTicket.setText("0")); // Update UI on main thread

                                    ticketInt = 0;
                                    msg = "Tickets available defaulted to 0";
                                } else {
                                    ticketInt = Integer.parseInt(ticket);
                                    // ensure ticket is positive integer
                                    if (ticketInt < 0) {
                                        uiHandler.post(() -> {
                                            Toast.makeText(getApplicationContext(), "Invalid tickets available!", Toast.LENGTH_SHORT).show();
                                        });
                                    }
                                }

                                // generate event ID
                                String eventID = eventIdGenerator();

                                // set the event ID
                                runOnUiThread(() -> tvEventId.setText(eventID));
                                String message;
                                if (msg != null){
                                    message = "Event saved: " + eventID + " to " + catID + ", " + msg;
                                } else{
                                    message = "Event saved: " + eventID + " to " + catID;
                                }

                                // store event into database
                                es = new Event(eventID, catID, eventName, ticketInt, isActive);
                                eventViewModel.insertEvent(es);

                                // update event count
                                int evCount = eventCategory.getEventCount();
                                eventCategory.setEventCount(evCount + 1);
                                categoryViewModel.updateCategory(eventCategory);
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView4, new FragmentListCategory()).commit();

                                // Snackbar for successful save
                                Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Code to undo the user's last action.
                                        eventViewModel.deleteEvent(eventID);
                                        int evCount = eventCategory.getEventCount();
                                        eventCategory.setEventCount(evCount - 1);
                                        categoryViewModel.updateCategory(eventCategory);
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView4, new FragmentListCategory()).commit();
                                    }
                                }).show();

                            }
                        }
                        if (category.size() == 0){
                            uiHandler.post(() -> Toast.makeText(getApplicationContext(), "Category does not exist!", Toast.LENGTH_SHORT).show());
                        }

                    } else {
                        uiHandler.post(() -> {
                            Toast.makeText(getApplicationContext(), "Invalid event name!", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    uiHandler.post(() -> {
                        Toast.makeText(getApplicationContext(), "Invalid event name!", Toast.LENGTH_SHORT).show();
                    });
                }
            });

        } else {
            Toast.makeText(this, "Please ensure Event Name and Category ID are filled", Toast.LENGTH_SHORT).show();
        }
    }

    private String eventIdGenerator(){
        String idNum;
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

    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the id of the selected item
            int id = item.getItemId();

            if (id == R.id.nav_view_cat) {
                viewAllCategories(null);
            } else if (id == R.id.nav_add_cat) {
                onNewEventCatButtonClick(null);
            } else if (id == R.id.nav_view_event) {
                viewAllEvents(null);
            } else if (id == R.id.nav_logout) {
                finish();
            }
            // close the drawer
            drawerLayout.closeDrawers();
            // tell the OS
            return true;
        }
    }

    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            tvTouchpad.setText("onLongPress");
            clearAll();
            super.onLongPress(e);
        }

        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            tvTouchpad.setText("onDoubleTap");
            onSaveButtonClick(getCurrentFocus());
            return super.onDoubleTap(e);
        }

    }
}