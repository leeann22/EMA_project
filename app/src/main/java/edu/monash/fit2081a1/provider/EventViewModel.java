package edu.monash.fit2081a1.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.monash.fit2081a1.storage.Event;
import edu.monash.fit2081a1.storage.EventCategory;

public class EventViewModel extends AndroidViewModel {
    // reference to CardRepository
    private EventRepository repository;
    // private class variable to temporary hold all the items retrieved and pass outside of this class
    private LiveData<List<Event>> allEventsLiveData;

    public EventViewModel(@NonNull Application application) {
        super(application);

        // get reference to the repository class
        repository = new EventRepository(application);

        // get all items by calling method defined in repository class
        allEventsLiveData = repository.getAllEvents();
    }

    /**
     * ViewModel method to get all cards
     * @return LiveData of type List<Item>
     */
    public LiveData<List<Event>> getAllEvents() {
        return allEventsLiveData;
    }


    /**
     * ViewModel method to insert one single item,
     * usually calling insert method defined in repository class
     * @param event object containing details of new Item to be inserted
     */
    public void insertEvent(Event event) {
        repository.insertEvent(event);
    }

    /**
     * ViewModel method to delete all records
     */
    public void deleteAllEvents() {
        repository.deleteAllEvents();
    }

    public LiveData<List<String>> getCatIDs(){
        return repository.getCatIDs();
    }

    public void deleteEvent(String event){
        repository.deleteEvent(event);
    }
}
