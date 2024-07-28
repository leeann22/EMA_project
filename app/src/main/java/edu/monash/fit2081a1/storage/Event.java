package edu.monash.fit2081a1.storage;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "events")
public class Event {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "eventID")
    private String eventID;

    @ColumnInfo(name = "eventName")
    private String eventName;

    @ColumnInfo(name = "catID")
    private String catID;

    @ColumnInfo(name = "ticket")
    private int ticket;

    @ColumnInfo(name = "isActive")
    private boolean isActive;

    public Event(String eventID, String catID, String eventName, int ticket, boolean isActive) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.catID = catID;
        this.ticket = ticket;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getCatID() {
        return catID;
    }

    public void setCatID(String catID) {
        this.catID = catID;
    }

    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }
}
