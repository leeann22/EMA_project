package edu.monash.fit2081a1.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import edu.monash.fit2081a1.storage.EventCategory;

public class CategoryRepository {
    // private class variable to hold reference to DAO
    private EmaDAO emaDAO;
    // private class variable to temporary hold all the items retrieved and pass outside of this class
    private LiveData<List<EventCategory>> allEventCategoriesLiveData;

    // constructor to initialise the repository class
    CategoryRepository(Application application) {
        // get reference/instance of the database
        EmaDatabase db = EmaDatabase.getDatabase(application);

        // get reference to DAO, to perform CRUD operations
        emaDAO = db.emaDAO();

        // once the class is initialised get all the items in the form of LiveData
        allEventCategoriesLiveData = emaDAO.getAllEventCategories();
    }

    /**
     * Repository method to get all cards
     * @return LiveData of type List<Item>
     */
    LiveData<List<EventCategory>> getAllEventCategories() {
        return allEventCategoriesLiveData;
    }

    /**
     * Repository method to insert one single item
     * @param category object containing details of new Item to be inserted
     */
    void insertCategory(EventCategory category) {
        EmaDatabase.databaseWriteExecutor.execute(() -> emaDAO.addEventCategory(category));
    }

    /**
     * Repository method to delete all records
     */
    void deleteAllCategory() {
        EmaDatabase.databaseWriteExecutor.execute(() -> emaDAO.deleteAllCategories());
    }

    List<EventCategory> getCategory(String catID) {
        return emaDAO.getCategory(catID);
    }

    void updateCategory(EventCategory eventCategory){
        EmaDatabase.databaseWriteExecutor.execute(() -> emaDAO.updateEventCategory(eventCategory));
    }
}
