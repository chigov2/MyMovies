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

    @Query("SELECT * FROM favourite_movies")  //////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    LiveData<List<FavouriteMovie>> getAllFavouriteMovies();

    // метод возвращает экземпляр фильма по id
    @Query("SELECT * FROM movies WHERE id == :movieId")
    Movie getMovieById(int movieId);

//    @Query("SELECT * FROM favourite_movies WHERE id == :movieId")
//    Movie getFavouriteMoviesById(int movieId);

    @Query("SELECT * FROM favourite_movies WHERE id == :movieId")
    FavouriteMovie getFavouriteMovieById(int movieId);

    //удалить все данные из таблицы
    @Query("DELETE FROM movies")
    void deleteAllMovies();

    //вставить фильм
    @Insert
    void insertMovie(Movie movie);

    //удалить 1 элемент из базы
    @Delete
    void deleteMovie(Movie movie);

    @Insert
    void insertFavouriteMovie(FavouriteMovie movie);

    //удалить 1 элемент из базы
    @Delete
    void deleteFavouriteMovie(FavouriteMovie movie);

                                                //3 добавляем БД MovieDatabase - in data
}
