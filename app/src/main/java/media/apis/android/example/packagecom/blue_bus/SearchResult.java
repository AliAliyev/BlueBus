package media.apis.android.example.packagecom.blue_bus;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.params.HttpParams;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by romil_000 on 2016/3/9.
 */

public class SearchResult extends ActionBarActivity{

    private static final String RIDES_URL = "http://halfbloodprince.16mb.com/rides.php";

    Context context = this;
    private SharedPreferences sharedPref;

    private EditText startPoint;
    private EditText terminalPoint;

    String[] data = {"one","two","three"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresult);

        sharedPref = context.getSharedPreferences("address", MODE_WORLD_READABLE);
        String SP = sharedPref.getString("SP",null);
        String TP = sharedPref.getString("TP", null);
        System.out.println(SP);

        //to fetch data from database
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try{
            URL url = new URL(RIDES_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(con.getInputStream());


        }catch (Exception e){

        }


        //to set up the listview with template
        ListView lv = (ListView) findViewById(R.id.showData);
        System.out.println("1 "+lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.single_row, R.id.details, data);
        System.out.println("adapter created");
        lv.setAdapter(adapter);
        System.out.println("setting adapter");


    }

}

