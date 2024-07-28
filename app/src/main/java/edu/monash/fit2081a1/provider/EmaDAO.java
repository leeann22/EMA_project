package edu.monash.fit2081a1.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.monash.fit2081a1.storage.Event;
import edu.monash.fit2081a1.storage.EventCategory;

@Dao
public interface EmaDAO {

    @Query("select * from events")
    LiveData<List<Event>> getAllEvents();

    @Query("select * from eventCategories")
    LiveData<List<EventCategory>> getAllEventCategories();

    @Insert
    void addEvent(Event event);

    @Insert
    void addEventCategory(EventCategory eventCategory);

    @Query("DELETE FROM events")
    void deleteAllEvents();

    @Query("DELETE FROM eventCategories")
    void deleteAllCategories();

    @Query("select catID from eventCategories")
    LiveData<List<String>> getCatIDs();

    @Query("delete from events where eventID = :eventID")
    void deleteEvent(String eventID);

    @Query("select * from eventCategories where catID = :catID")
    List<EventCategory> getCategory(String catID);

    @Update
    void updateEventCategory(EventCategory eventCategory);

}
