package techmarket.uno.mymovies.data;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favourite_movies")
public class FavouriteMovie extends Movie{//переопределяем коструктор с параметрами

    public FavouriteMovie(int uniqueId,int id, int voteCount, String title, String originalTitle, String overview, String posterPath, String bigPosterPath, String releaseDate, String backdropPath, double voteAverage) {
        super(uniqueId,id, voteCount, title, originalTitle, overview, posterPath, bigPosterPath, releaseDate, backdropPath, voteAverage);
    }
    //конструктор
    @Ignore
    public FavouriteMovie (Movie movie){
        //возвращает этотже фильм, но преобразованный в дочерний класс
        super(movie.getUniqueId(),movie.getId(),movie.getVoteCount(),movie.getTitle(), movie.getTitle(), movie.getOverview(), movie.getPosterPath(),
                movie.getBigPosterPath(), movie.getReleaseDate(),movie.getBackdropPath(), movie.getVoteAverage());
    }
}
