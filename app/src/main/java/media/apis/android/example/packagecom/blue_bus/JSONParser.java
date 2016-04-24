package media.apis.android.example.packagecom.blue_bus;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ali on 22/04/2016.
 */
public class JSONParser extends AppCompatActivity {
    String next;
    private ArrayList items = null;
    private  static  final String RIDE_URL = "http://halfbloodprince.16mb.com/food.php";
    UserSessionManager session;
    ListView listView;
    /**
     * Called when the activity is first created.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        session = new UserSessionManager(getApplicationContext());
        populate(session.getUserDetails().get(UserSessionManager.KEY_EMAIL));
        listView = (ListView) findViewById(R.id.list);
    }

    private  void populate(final String user) {

         class Test extends AsyncTask<String, Void, String> {
             ProgressDialog loading;
             @Override
             protected void onPreExecute() {
                 super.onPreExecute();
                 loading = ProgressDialog.show(JSONParser.this, "Please Wait", null, true, true);
             }
            @Override
            protected String doInBackground(String... params) {
                try {

                    //to fetch data from database
                    HashMap<String,String> strData = new HashMap<>();
                    strData.put("user", params[0]);

                    URL url = new URL(RIDE_URL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(15000);
                    con.setConnectTimeout(15000);
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);

                    OutputStream os = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(strData));
                    writer.flush();
                    writer.close();
                    os.close();

                    BufferedReader bufferedReader =
                            new BufferedReader(new InputStreamReader(
                                    con.getInputStream()));
                    next=bufferedReader.readLine();

                }catch (Exception e) {
                    e.printStackTrace();
                }
                return " ";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                items = new ArrayList();
                try {
                    JSONArray ja = new JSONArray(next);

                    for (int i = 0; i < ja.length(); i++) {
                        //  JSONObject jo = (JSONObject) ja.get(i);
                        //  items.add(jo.getString("food_name"));

                        JSONObject jsonObj = ja.getJSONObject(i);
                        items.add(jsonObj.getString("start")+"\n"+">>"+"\n"+jsonObj.getString("destination")+
                                                    "\n"+jsonObj.getString("depart_time")+"\n"+
                                                     "Spare seats "+jsonObj.getString("seats"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                listView.setAdapter(new ArrayAdapter(
                        JSONParser.this, android.R.layout.simple_list_item_1,
                        items));
            }
         }
        Test test = new Test();
        test.execute(user);
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(java.util.Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}