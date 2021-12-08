package techmarket.uno.mymovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

import techmarket.uno.mymovies.data.FavouriteMovie;
import techmarket.uno.mymovies.data.MainViewModel;
import techmarket.uno.mymovies.data.Movie;

public class DetailActivity extends AppCompatActivity {
    private ImageView imageViewBigPoster;

    private TextView textViewTitle;
    private TextView textViewOriginalTitle;
    private TextView textViewRating;
    private TextView textViewReleaseDate;
    private TextView textViewOverview;
    private ImageView imageViewAddToFavorite;
    private FavouriteMovie favouriteMovie;

    private int id;
    private Movie movie;
    //надо получить фильм из БД – создадим объект
    private MainViewModel viewModel;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imageViewBigPoster=findViewById(R.id.imageViewBigPoster);//не было Caused by: java.lang.IllegalArgumentException: Target must not be null.
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        textViewRating = findViewById(R.id.textViewRating);
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
        textViewOverview = findViewById(R.id.textViewOverview);
        imageViewAddToFavorite = findViewById(R.id.imageViewAddToFavorite);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")){
            //присваиваем значение
            id = intent.getIntExtra("id",-1);

        }else {
            finish();
        }//закрыть активность
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        try {
            movie = viewModel.getMovieById(id);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //после этого устанавливаем у всех элементов нужные значения
        Picasso.get().load(movie.getBigPosterPath()).into(imageViewBigPoster);
        textViewTitle.setText(movie.getTitle());
        textViewOriginalTitle.setText(movie.getOriginalTitle()); //Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'void android.widget.TextView.setText(java.lang.CharSequence)' on a null object reference
        textViewOverview.setText(movie.getOverview());
        textViewReleaseDate.setText(movie.getReleaseDate());
        textViewRating.setText(Double.toString(movie.getVoteAverage()));
        setFavourite();
    }

    public void onClickChangeFavorite(View view) throws ExecutionException, InterruptedException {
        if (favouriteMovie == null){// в базе фильиа нет - надо добавить
            viewModel.insertFavouriteMovie(new FavouriteMovie(movie));
            Toast.makeText(this, "Object added to favourite", Toast.LENGTH_SHORT).show();
        }else{
            viewModel.deleteFavouriteMovie(favouriteMovie);
            Toast.makeText(this, "Object deleted from favourite", Toast.LENGTH_SHORT).show();
        }
        setFavourite();
    }

    private void setFavourite() {
        try {
            favouriteMovie = viewModel.getFavouriteMovieById(id);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (favouriteMovie == null){
            imageViewAddToFavorite.setImageResource(R.drawable.favourite_add_to);
        }else{
            imageViewAddToFavorite.setImageResource(R.drawable.favourite_remove);
        }
    }
}