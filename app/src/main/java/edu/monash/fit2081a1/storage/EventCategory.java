package edu.monash.fit2081a1.storage;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "eventCategories")
public class EventCategory {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "catID")
    private String catID;

    @ColumnInfo(name = "catName")
    private String catName;

    @ColumnInfo(name = "categoryLocation")
    private String categoryLocation;

    @ColumnInfo(name = "eventCount")
    private int eventCount;

    @ColumnInfo(name = "initEventCount")
    private final int initEventCount;

    @ColumnInfo(name = "isActive")
    private boolean isActive;

    public EventCategory(String catID, String catName, int eventCount, int initEventCount, boolean isActive, String categoryLocation) {
        this.catName = catName;
        this.catID = catID;
        this.eventCount = eventCount;
        this.isActive = isActive;
        this.initEventCount = initEventCount;
        this.categoryLocation = categoryLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String eventName) {
        this.catName = eventName;
    }

    public String getCatID() {
        return catID;
    }

    public String getCategoryLocation() {
        return categoryLocation;
    }

    public void setCategoryLocation(String categoryLocation) {
        this.categoryLocation = categoryLocation;
    }

    public void setCatID(String catID) {
        this.catID = catID;
    }

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public int getInitEventCount() {
        return initEventCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
