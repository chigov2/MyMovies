package techmarket.uno.mymovies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import techmarket.uno.mymovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //String url = NetworkUtils.buildURL(NetworkUtils.POPULARITY,1).toString();
        JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(NetworkUtils.TOP_RATED,3);
        if (jsonObject == null){
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
        }
        Log.i("test",jsonObject.toString());
    }
}