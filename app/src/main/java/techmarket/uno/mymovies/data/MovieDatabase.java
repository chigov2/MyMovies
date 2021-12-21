package techmarket.uno.mymovies.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class, FavouriteMovie.class},version = 9, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    //используем паттерн singletone
    // создаем объект класса
    private static MovieDatabase database;
    private static final String DB_NAME = "movies.db";
    //добавить блок синхронизации
    private static final Object LOCK = new Object();

    //получаем экземпляр БД
    public static MovieDatabase getInstance(Context context) {
        synchronized (LOCK) {                                           //4 добавить блок синхронизаци
            if (database == null) {//создается таблица?
                database = Room.databaseBuilder(context, MovieDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
            }
            return database;
        }
    }
    //метод, который возвращает Dao
    public abstract MovieDAO movieDAO();                                //5 создаем объект view model MainViewModel.java

}
