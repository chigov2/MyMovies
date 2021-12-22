package techmarket.uno.mymovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import techmarket.uno.mymovies.adapters.MovieAdapter;
import techmarket.uno.mymovies.data.MainViewModel;
import techmarket.uno.mymovies.data.Movie;
import techmarket.uno.mymovies.utils.JSONUtils;
import techmarket.uno.mymovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {
    private RecyclerView recyclerViewPosters;
    private MovieAdapter movieAdapter;
    private Switch switchSort;
    private TextView textViewPopularity;
    private TextView textViewTopRated;
    private MainViewModel viewModel;

    private static final int LOADER_ID = 200;
    private LoaderManager loaderManager;
    private static int page = 1;
    private  static int methodOfSort;
    private static boolean isLoading = false;
    private ProgressBar progressBarLoading;

    private static String lang;// и присвоим значение в методе onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.itemMain:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.itemFavourite:
                Intent intentToFavorite = new Intent(this,FavoriteActivity.class);
                startActivity(intentToFavorite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private int getColumnCount(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return width / 185 > 2 ? width / 185 : 2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //чтобы получить язык, который используется на устройстве
        lang = Locale.getDefault().getLanguage();
        loaderManager = LoaderManager.getInstance(this);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        textViewPopularity = findViewById(R.id.textViewPopularity);
        textViewTopRated = findViewById(R.id.textViewTopRated);
        switchSort = findViewById(R.id.switchSort);
        recyclerViewPosters = findViewById(R.id.recyclerViewPosters);
        progressBarLoading = findViewById(R.id.progressBarLoading);

        recyclerViewPosters.setLayoutManager(new GridLayoutManager(this, getColumnCount()));
        //получаем список фильмов
        movieAdapter = new MovieAdapter();
        recyclerViewPosters.setAdapter(movieAdapter);
        //чтобы фильмы сразу загрузились
        switchSort.setChecked(true);
        //и добавляем к нему слушатель....
        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                page = 1;
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
                Movie movie = movieAdapter.getMovies().get(position);
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("id",movie.getId());
                startActivity(intent);
            }
        });

        //устанавдиваем слушатель у movieAdapter - окончание окна
        movieAdapter.setOnReachEndListener(new MovieAdapter.OnReachEndListener() {
            @Override
            public void onReachEnd() {
                if(!isLoading) {
                    downloadData(methodOfSort,page,lang);
                    //Toast.makeText(MainActivity.this, "End of list", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LiveData<List<Movie>> moviesFromLiveData = viewModel.getMovies();
        //добавляем обсервер
        moviesFromLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                //когда данные в базе будут меняться - будем устанавливыать их в адаптере
                //movieAdapter.setMovies(movies);
                if (page == 1){
                    movieAdapter.setMovies(movies);
                }
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
        if (isTopRated) {
            textViewTopRated.setTextColor(getResources().getColor(R.color.rose));
            textViewPopularity.setTextColor(getResources().getColor(R.color.white));
            methodOfSort = NetworkUtils.TOP_RATED;
        } else {
            methodOfSort = NetworkUtils.POPULARITY;
            textViewPopularity.setTextColor(getResources().getColor(R.color.rose));
            textViewTopRated.setTextColor(getResources().getColor(R.color.white));
        }
        downloadData(methodOfSort,page,lang);
    }

    //выносим загрузку в отдельный метод                                    ///////////   -7
    private void downloadData(int methodOfSort, int page, String lang){
        URL url = NetworkUtils.buildURL(methodOfSort,page,lang);
        Bundle bundle = new Bundle();
        bundle.putString("url",url.toString());
        loaderManager.restartLoader(LOADER_ID,bundle,this);
    }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        NetworkUtils.JSONLoader jsonLoader= new NetworkUtils.JSONLoader(this,args);
        jsonLoader.setOnStartLoadingListener(new NetworkUtils.JSONLoader.OnStartLoadingListener() {
            @Override
            public void onStartLoading() {
                progressBarLoading.setVisibility(View.VISIBLE);
                isLoading =true;
            }
        });
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        ArrayList<Movie> movies = null;
        try { movies = JSONUtils.getMoviesFromJSON(data);} catch (JSONException e) {e.printStackTrace();}
        if (movies != null && !movies.isEmpty()){
        if(page == 1){
            //очистим предыдущие данные
            viewModel.deleteAllMovies();
            movieAdapter.clear();
        }
            //после вставляем новые данные
            for (Movie movie : movies){
                viewModel.insertMovies(movie);
            }
            movieAdapter.addMovies(movies);
            page++;
        }
        isLoading = false;
        progressBarLoading.setVisibility(View.INVISIBLE);
        loaderManager.destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

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