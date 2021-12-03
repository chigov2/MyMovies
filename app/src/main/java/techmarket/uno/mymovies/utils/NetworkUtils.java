package techmarket.uno.mymovies.utils;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {

    //https://api.themoviedb.org/3/discover/movie?api_key=4142aedbdf1201d4bf23dbba3b1515b8&
    // language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=2&with_watch_monetization_types=flatrate

    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";

    private static final String PARAMS_API_KEY = "api_key";
    private static final String PARAMS_LANGUAGE = "language";
    private static final String PARAMS_SORT_BY = "sort_by";
    private static final String PARAMS_PAGE = "page";

    private static final String API_KEY = "4142aedbdf1201d4bf23dbba3b1515b8";
    private static final String LANGUAGE_VALUE = "ru-RU";
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_TOP_RATED = "vote_average.desc";

    public static final int POPULARITY = 0;
    public static final int TOP_RATED = 1;

    //метод, который будет формировать запрос - возвращать будет URL
    private static URL buildURL(int sortBy, int page) {
        //получили https://api.themoviedb.org/3/discover/movie в виде адреса и
        // можем прикреплять к нему параметры
        URL result = null;
        String methodOfSort;
        if (sortBy == POPULARITY) {
            methodOfSort = SORT_BY_POPULARITY;
        } else {
            methodOfSort = SORT_BY_TOP_RATED;
        }
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .appendQueryParameter(PARAMS_SORT_BY, methodOfSort)
                .appendQueryParameter(PARAMS_PAGE, Integer.toString(page))
                .build();
        //вернуть URL
        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    //метод, который будет получать из JSON сети
    public static JSONObject getJSONFromNetwork(int sortBy, int page){
        JSONObject result = null;
        URL url = buildURL(sortBy,page);
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    //метод - загружает данные из интернета
    private static class JSONLoadTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... urls) {
            //переменная, которую будем возвращать
            JSONObject result = null;
            //проверка
            if (urls == null || urls.length == 0){
                return null;
            }
            //соединение
            HttpURLConnection connection = null;
            //открываем соединение
            try {
                connection = (HttpURLConnection) urls[0].openConnection();
                //соединение открыто - создаем поток ввода
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                //чтобы читать сразу строками
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder builder = new StringBuilder();
                //читаем данные
                String line = reader.readLine();
                while (line != null){
                    builder.append(line);
                    line = reader.readLine();
                }
                result = new JSONObject(builder.toString());

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null){
                connection.disconnect();
                }
            }

            return result;
        }
    }
}

