package techmarket.uno.mymovies.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//преобразуем класс, чтобы он был таблицей в БД
@Entity(tableName = "movies")                           //1
public class Movie {
    @PrimaryKey(autoGenerate = true)
    private  int uniqueId;
                                     //2, создаем интерфейс DAO - MovieDAO
    private  int id;
    private int voteCount;
    private String title;
    private String originalTitle;
    private String overview;
    private String posterPath; //картинка маленькая
    private String bigPosterPath; //картинка маленькая
    private String releaseDate;
    private String backdropPath;// poster
    private double voteAverage;

    //создаем конструктор(shift + ins)
    public Movie(int uniqueId, int id, int voteCount, String title, String originalTitle, String overview,
                 String posterPath, String bigPosterPath,String releaseDate, String backdropPath, double voteAverage) {
        this.uniqueId = uniqueId;
        this.id = id;
        this.voteCount = voteCount;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.posterPath = posterPath;
        this.bigPosterPath = bigPosterPath;
        this.releaseDate = releaseDate;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
    }

    //создаем конструктор(shift + ins)
    @Ignore
    public Movie(int id, int voteCount, String title, String originalTitle, String overview,
                 String posterPath, String bigPosterPath,String releaseDate, String backdropPath, double voteAverage) {
        this.id = id;
        this.voteCount = voteCount;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.posterPath = posterPath;
        this.bigPosterPath = bigPosterPath;
        this.releaseDate = releaseDate;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    //создаем getters and setters для того, чтобы этот класс можно было использовать в БД
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int vote_count) {
        this.voteCount = voteCount;
    }

    public String getTitle() {
        return title;
    }

    public String getBigPosterPath() {return bigPosterPath; }

    public void setBigPosterPath(String bigPosterPath) {this.bigPosterPath = bigPosterPath;}

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }
}
