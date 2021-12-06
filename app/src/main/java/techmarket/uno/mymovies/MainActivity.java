package techmarket.uno.mymovies;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import techmarket.uno.mymovies.data.MainViewModel;
import techmarket.uno.mymovies.data.Movie;
import techmarket.uno.mymovies.utils.JSONUtils;
import techmarket.uno.mymovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPosters;
    private MovieAdapter movieAdapter;
    private Switch switchSort;
    private TextView textViewPopularity;
    private TextView textViewTopRated;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        textViewPopularity = findViewById(R.id.textViewPopularity);
        textViewTopRated = findViewById(R.id.textViewTopRated);
        switchSort = findViewById(R.id.switchSort);
        recyclerViewPosters = findViewById(R.id.recyclerViewPosters);

        recyclerViewPosters.setLayoutManager(new GridLayoutManager(this, 2));
        //получаем список фильмов
        movieAdapter = new MovieAdapter();
        recyclerViewPosters.setAdapter(movieAdapter);
        //чтобы фильмы сразу загрузились
        switchSort.setChecked(true);
        //и добавляем к нему слушатель....
        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setMethodOfSort(isChecked);
            }
        });
        //и потом установили его обратно
        switchSort.setChecked(false);
        //устанавливаем слушатель адаптера нажатия на картинку
        movieAdapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {//создаем новый  анаонимный класс
            @Override
            public void onPosterClick(int position) {//преопреляем метод, который объявили в интерфейсе
                //Toast.makeText(MainActivity.this, "Clicked" + position, Toast.LENGTH_SHORT).show();
                //получаем фильм, на который нажали

            }
        });

        //устанавдиваем слушатель у movieAdapter - окончание окна
        movieAdapter.setOnReachEndListener(new MovieAdapter.OnReachEndListener() {
            @Override
            public void onReachEnd() {
                Toast.makeText(MainActivity.this, "End of list", Toast.LENGTH_SHORT).show();
            }
        });
        LiveData<List<Movie>> moviesFromLiveData = viewModel.getMovies();

        //добавляем обсервер
        moviesFromLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                //когда данные в базе будут меняться - будем устанавливыать их у адаптера
                movieAdapter.setMovies(movies);
            }
        });

    }

    public void onClickSetPopularity(View view) {
        setMethodOfSort(false);
        switchSort.setChecked(false);
    }

    public void onClickTopRated(View view) {
        setMethodOfSort(true);
        switchSort.setChecked(true);
    }

    private void setMethodOfSort(boolean isTopRated) {//метод сделан для того, чтобы использоваться в трёх местах
        int methodOfSort;
        if (isTopRated) {
            methodOfSort = NetworkUtils.TOP_RATED;
            textViewTopRated.setTextColor(getResources().getColor(R.color.rose));
            textViewPopularity.setTextColor(getResources().getColor(R.color.white));
        } else {
            methodOfSort = NetworkUtils.POPULARITY;
            textViewPopularity.setTextColor(getResources().getColor(R.color.rose));
            textViewTopRated.setTextColor(getResources().getColor(R.color.white));
        }
        downloadData(methodOfSort,1);
    }

    //выносим загрузку в отдельный метод                                    ///////////   -7
    private void downloadData(int methodOfSort, int page){
        JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(methodOfSort, 1);
        //после этого получим список фильмов
        ArrayList<Movie> movies = null;
        try { movies = JSONUtils.getMoviesFromJSON(jsonObject);} catch (JSONException e) {e.printStackTrace();}
        if (movies != null && !movies.isEmpty()){
            //очистим предцдущие данные
            viewModel.deleteAllMovies();
            //после вставляем новые данные
            for (Movie movie : movies){
                viewModel.insertMovies(movie);
            }
        }
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