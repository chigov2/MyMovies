package techmarket.uno.mymovies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDAO {
    //создаем методы - будет возвращать <List<Movie>>
    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAllMovies();

    // метод возвращает экземпляр фильма по id
    @Query("SELECT * FROM movies WHERE id == :movieId")
    Movie getMovieById(int movieId);

    //удалить все данные из таблицы
    @Query("DELETE FROM movies")
    void deleteAllMovies();

    //вставить фильм
    @Insert
    void insertMovie(Movie movie);

    //удалить 1 элемент из базы
    @Delete
    void deleteMovie(Movie movie);

                                                //3 добавляем БД MovieDatabase - in data
}
