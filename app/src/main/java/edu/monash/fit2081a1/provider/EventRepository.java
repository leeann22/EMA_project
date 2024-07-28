package edu.monash.fit2081a1.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import edu.monash.fit2081a1.storage.Event;
import edu.monash.fit2081a1.storage.EventCategory;

public class EventRepository {
    // private class variable to hold reference to DAO
    private EmaDAO emaDAO;
    // private class variable to temporary hold all the items retrieved and pass outside of this class
    private LiveData<List<Event>> allEventsLiveData;


    // constructor to initialise the repository class
    EventRepository(Application application) {
        // get reference/instance of the database
        EmaDatabase db = EmaDatabase.getDatabase(application);

        // get reference to DAO, to perform CRUD operations
        emaDAO = db.emaDAO();

        // once the class is initialised get all the items in the form of LiveData
        allEventsLiveData = emaDAO.getAllEvents();
    }

    /**
     * Repository method to get all cards
     * @return LiveData of type List<Item>
     */
    LiveData<List<Event>> getAllEvents() {
        return allEventsLiveData;
    }

    /**
     * Repository method to insert one single item
     * @param event object containing details of new Item to be inserted
     */
    void insertEvent(Event event) {
        EmaDatabase.databaseWriteExecutor.execute(() -> emaDAO.addEvent(event));
    }

    /**
     * Repository method to delete all records
     */
    void deleteAllEvents() {
        EmaDatabase.databaseWriteExecutor.execute(() -> emaDAO.deleteAllEvents());
    }

    LiveData<List<String>> getCatIDs(){
        return emaDAO.getCatIDs();
    }

    void deleteEvent(String event){
        EmaDatabase.databaseWriteExecutor.execute(() -> emaDAO.deleteEvent(event));
    }
}
