package techmarket.uno.mymovies.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {// будет наследоваться от

    private LiveData<List<Movie>> movies;//создаем список фильмов
    private static MovieDatabase database;//создаем объект базы данных

    //конструктор
    public MainViewModel(@NonNull Application application) {
        super(application);
        database = MovieDatabase.getInstance(getApplication());//присваиваем знвчение в конструкторе
        movies = database.movieDAO().getAllMovies();//присваиваем знвчениев конструкторе
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }//геттер на List
    //методы для доступа к данным
    //возврат объекта movie
    public Movie getMovieById (int id) throws ExecutionException, InterruptedException {
        // все действия должны быть в другом программном потоке
        // - создаем класс GetMovieTask
        return new GetMovieTask().execute(id).get();
    }                                                        //принимает
    private static class GetMovieTask extends AsyncTask<Integer, Void, Movie> {

        @Override
        protected Movie doInBackground(Integer... integers) {
            if (integers != null && integers.length > 0)
            {
                return database.movieDAO().getMovieById(integers[0]);
            }
            return null;
        }
    }

    //метод для вставки элемента
    public void insertMovies(Movie movie){
        new InsertTask().execute(movie);
    }
    private static class InsertTask extends AsyncTask<Movie, Void, Void> {

        @Override
        protected Void doInBackground(Movie... movies) {
            if (movies != null && movies.length > 0)
            {
                database.movieDAO().insertMovie(movies[0]);
            }
            return null;
        }
    }

    public void deleteAllMovies(){
        new DeleteMoviesTask().execute();
    }  //метод для удаления элементов
    private static class DeleteMoviesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... integers) {
            database.movieDAO().deleteAllMovies();
            return null;
        }
    }
    public void deleteMovie(Movie movie){
        new DeleteMovieTask().execute();
    }
    private static class DeleteMovieTask extends AsyncTask<Movie, Void, Void> {

        @Override
        protected Void doInBackground(Movie... movies) {//проверка не нужна
               database.movieDAO().deleteMovie(movies[0]);
               return null;
        }
    }
}
