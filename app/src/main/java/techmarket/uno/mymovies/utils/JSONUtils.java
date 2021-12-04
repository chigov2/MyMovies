package techmarket.uno.mymovies.utils;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import techmarket.uno.mymovies.data.Movie;

public class JSONUtils {//после создания классав муви необходимо разобрать JSON

    //первым делом получить result
    private static final String KEY_RESULTS = "results";
    private static final String KEY_id = "id";
    private static final String KEY_VOTE_COUNT = "vote_count";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_POSTER_PASS = "poster_path";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_BACKDROP_PASS = "backdrop_path";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    private static final String SMALL_POSTER_SIZE = "w185";
    private static final String BIG_POSTER_SIZE = "w780";

    //сделав запрос к базе - должны получить массив с фильмами
    //возвращать будет ArrayList<Movie>, а принимать JSONObject jsonObject
    public static ArrayList<Movie> getMoviesFromJSON(JSONObject jsonObject) throws JSONException {//////////
        ArrayList<Movie> result = new ArrayList<>();
        //jsonObject может быть null - check
        if (jsonObject == null){
            return  result;}
        JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);                 /////////////////////
        //получили массив с фильмами
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject objectMovie = jsonArray.optJSONObject(i);
            int id = objectMovie.getInt(KEY_id);
            int voteCount = objectMovie.getInt(KEY_VOTE_COUNT);
            String title = objectMovie.getString(KEY_TITLE);
            String originalTitle = objectMovie.getString(KEY_ORIGINAL_TITLE);
            String overview = objectMovie.getString(KEY_OVERVIEW);
            String posterPass = BASE_POSTER_URL + SMALL_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PASS);
            String bigPosterPass = BASE_POSTER_URL + BIG_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PASS);  /////////!!!!!!!!!!!!!
            String releaseDate = objectMovie.getString(KEY_RELEASE_DATE);
            String backdropPass = objectMovie.getString(KEY_BACKDROP_PASS);
            double voteAverage = objectMovie.getDouble(KEY_VOTE_AVERAGE);
            // после получения данных - создается объект Movie
            Movie movie = new Movie(id,voteCount,title,originalTitle,
                    overview,posterPass, bigPosterPass, releaseDate,backdropPass,voteAverage);
            //полученный фильм добавляем в result
            result.add(movie);
        }

        return result;
    }
}
