# Event Management Application
## Overview
A basic events management app coded in Java using Android Studio. 
The app features several UIs which include:
* Registration Page
* Login Page
* Dashboard with New Event Form
* New Event Category Form
* View All Event Page
* View All Event Category Page
* Navigation Drawer and Toolbar
* Google Map Display
* Web Browser Display

## Data Storage Components
Event and Event Category are implemented as Room Databases entities while 
User Details are stored in SharedPreferences. The app only allows for the registration of 
one user. LiveData is used to  observe changes and receive multiple events & event 
categories to display.

## APIs
Google Maps API was used to display the location of the event category. 

## Additional Widgets
* Floating Action Button (FAB) for saving events on the dashboard
* Touchpad for Gesture detection on dashboard (Long press to clear all fields; Double Tap to save event)
* WebView to launch web result for event name when clicked on
* RecyclerView to display all saved Events and Event Categories 
* Fragments to display the respective RecyclerViews
