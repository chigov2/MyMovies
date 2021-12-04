package techmarket.uno.mymovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import techmarket.uno.mymovies.data.Movie;
import techmarket.uno.mymovies.utils.JSONUtils;
import techmarket.uno.mymovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPosters;
    private MovieAdapter movieAdapter;
    private Switch switchSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchSort = findViewById(R.id.switchSort);
        recyclerViewPosters = findViewById(R.id.recyclerViewPosters);

        recyclerViewPosters.setLayoutManager(new GridLayoutManager(this,3));
        //получаем список фильмов
        movieAdapter = new MovieAdapter();
        recyclerViewPosters.setAdapter(movieAdapter);
        //чтобы фильмы сразу загрузились
        switchSort.setChecked(true);
        //и добавляем к нему слушатель....
        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int methodOfSort;
                if (isChecked){
                    methodOfSort = NetworkUtils.TOP_RATED;
                }else{
                    methodOfSort = NetworkUtils.POPULARITY;
                }
                //copied
                JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(methodOfSort,1);
                //после этого получим список фильмов
                ArrayList<Movie> movies = null;
                try {
                    movies = JSONUtils.getMoviesFromJSON(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                movieAdapter.setMovies(movies);
            }
        });
        //и потом установили его обратно
        switchSort.setChecked(false);
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