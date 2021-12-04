package techmarket.uno.mymovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import techmarket.uno.mymovies.data.Movie;
import techmarket.uno.mymovies.utils.JSONUtils;
import techmarket.uno.mymovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPosters;
    private MovieAdapter movieAdapter;                                      ////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewPosters = findViewById(R.id.recyclerViewPosters);
        recyclerViewPosters.setLayoutManager(new GridLayoutManager(this,3));
        //получаем список фильмов
        movieAdapter = new MovieAdapter();
        JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(NetworkUtils.POPULARITY,1);
        //после этого получим список фильмов
        ArrayList<Movie> movies = null;
        try {
            movies = JSONUtils.getMoviesFromJSON(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        movieAdapter.setMovies(movies);
        recyclerViewPosters.setAdapter(movieAdapter);
    }
}

//String url = NetworkUtils.buildURL(NetworkUtils.POPULARITY,1).toString();
//        JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(NetworkUtils.TOP_RATED,3);
//        if (jsonObject == null){
//            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
//        }
//        Log.i("test",jsonObject.toString());

//    //полученный JSON объект надо преобразовать в объект Film
//    JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(NetworkUtils.POPULARITY,6);
////после этого получим список фильмов
//        try {
//                ArrayList<Movie> movies = JSONUtils.getMoviesFromJSON(jsonObject);
//        StringBuilder builder = new StringBuilder();
//        for (Movie m : movies){
//        builder.append(m.getBackdropPath()).append("\n");
//        }
//        Log.i("test",builder.toString());
//        } catch (JSONException e) {
//        e.printStackTrace();
//        }