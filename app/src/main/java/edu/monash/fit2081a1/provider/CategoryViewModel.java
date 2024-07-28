package edu.monash.fit2081a1.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.monash.fit2081a1.storage.EventCategory;

public class CategoryViewModel extends AndroidViewModel {
    // reference to CardRepository
    private CategoryRepository repository;
    // private class variable to temporary hold all the items retrieved and pass outside of this class
    private LiveData<List<EventCategory>> allEventCategoriesLiveData;

    public CategoryViewModel(@NonNull Application application) {
        super(application);

        // get reference to the repository class
        repository = new CategoryRepository(application);

        // get all items by calling method defined in repository class
        allEventCategoriesLiveData = repository.getAllEventCategories();
    }

    /**
     * ViewModel method to get all cards
     * @return LiveData of type List<Item>
     */
    public LiveData<List<EventCategory>> getAllEventCategories() {
        return allEventCategoriesLiveData;
    }

    /**
     * ViewModel method to insert one single item,
     * usually calling insert method defined in repository class
     * @param category object containing details of new Item to be inserted
     */
    public void insertCategory(EventCategory category) {
        repository.insertCategory(category);
    }

    /**
     * ViewModel method to delete all records
     */
    public void deleteAllCategory() {
        repository.deleteAllCategory();
    }

    public List<EventCategory> getCategory(String catID) {
        return repository.getCategory(catID);
    }

    public void updateCategory(EventCategory eventCategory) {
        repository.updateCategory(eventCategory);
    }
}
