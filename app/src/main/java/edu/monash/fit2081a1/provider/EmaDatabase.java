package edu.monash.fit2081a1.provider;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.monash.fit2081a1.storage.Event;
import edu.monash.fit2081a1.storage.EventCategory;

@Database(entities = {Event.class, EventCategory.class}, version = 1)
public abstract class EmaDatabase extends RoomDatabase{

    // database name, this is important as data is contained inside a file named "card_database"
    public static final String EMA_DATABASE = "ema_database";

    public abstract EmaDAO emaDAO();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile EmaDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Since this class is an absract class, to get the database reference we would need
     * to implement a way to get reference to the database.
     *
     * @param context Application of Activity Context
     * @return a reference to the database for read and write operation
     */
    static EmaDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EmaDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    EmaDatabase.class, EMA_DATABASE)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
