package techmarket.uno.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import techmarket.uno.mymovies.R;
import techmarket.uno.mymovies.data.Movie;

public class MovieAdapter extends RecyclerView.Adapter <MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;

    //создаем объект интерфейсного типа
    private OnPosterClickListener onPosterClickListener;
    private OnReachEndListener onReachEndListener;

    //создаем пустой конструктор
    public MovieAdapter() {
        movies = new ArrayList<>();
    }


    //интерфейс для нажатия на маленькие картинки
    public interface OnPosterClickListener{
        //в нём будет один метод и принимает позицию
        void onPosterClick(int position);
    }
    //setter  - потом оидем в конструктор MovieViewHolder
    public void setOnPosterClickListener(OnPosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }


    //интерфейс для подгрузки контента
    public interface OnReachEndListener{
        void onReachEnd();
    }

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item,parent,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        //проверка где находимся
        if (movies.size() >=20 && position > movies.size() - 2 && onReachEndListener != null){ ///onPosterClickListener //onReachEndListener
          onReachEndListener.onReachEnd();
        }
    // надо взять imageView и установить у него фото из фильма
        Movie movie = movies.get(position);
        //ImageView imageView = holder.imageViewSmallPoster;
        Picasso.get().load(movie.getPosterPath()).into(holder.imageViewSmallPoster);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewSmallPoster;//нужен доступ к элементу из movie_item.xml

        //конструктор MovieViewHolder
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewSmallPoster = itemView.findViewById(R.id.imageViewSmallPoster);
            itemView.setOnClickListener(new View.OnClickListener() {//анонимный класс
                @Override
                public void onClick(View v) {
                    //переопреляем метол onClick
                    if (onPosterClickListener != null){
                        onPosterClickListener.onPosterClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public void clear(){
        this.movies.clear();
        notifyDataSetChanged();
    }

    //чтобы можно было устанавливать новый массив - создается сеттер и геттер
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
    //getter
    public List<Movie> getMovies() {
        return movies;
    }
    //чтобы не обновлять постоянно массив

    public void addMovies(List<Movie> movies){
        this.movies.addAll(movies);                 /////////////////////////////////////
        notifyDataSetChanged();
    }
}
