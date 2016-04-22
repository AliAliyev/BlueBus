package media.apis.android.example.packagecom.blue_bus;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ali on 22/04/2016.
 */
public class JSONParser extends ListActivity {
    String next;
    private ArrayList items = null;

    /**
     * Called when the activity is first created.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);



final ArrayList list = new ArrayList();
        list.add("alma");
        list.add("armud");
        list.add("heyva");
        list.add("nar");
        list.add("avokado");

     //   next=null;
        populate();
//System.out.println(next);

       // System.out.println(populate());
    }

    private  void populate() {


         class Test extends AsyncTask<String, Void, String> {
             @Override
             protected void onPreExecute() {
                 super.onPreExecute();
                 //loading = ProgressDialog.show(Register.this, "Please Wait", null, true, true);
             }
            @Override
            protected String doInBackground(String[] params) {
                try {
                    URL url = new URL ("http://halfbloodprince.16mb.com/food.php");
                    HttpURLConnection urlConnection =
                            (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();

                    BufferedReader bufferedReader =
                            new BufferedReader(new InputStreamReader(
                                    urlConnection.getInputStream()));
                    next=bufferedReader.readLine();
                 //   System.out.println(bufferedReader.readLine() + " text");


                   // while ((next = bufferedReader.readLine()) != null) {

                  //  }
                }catch (Exception e) {
                    e.printStackTrace();
                }
                return "some message";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                items = new ArrayList();
                try {
                    JSONArray ja = new JSONArray(next);
                    //   System.out.println(ja + "   jason");
                    for (int i = 0; i < ja.length(); i++) {
                        //  JSONObject jo = (JSONObject) ja.get(i);
                        // items.add(jo.getString("food_name"));

                        JSONObject jsonObj = ja.getJSONObject(i);
                        items.add(jsonObj.getString("food_name"));
                        // System.out.println(jsonObj.getString("food_name"));
                        // System.out.println("\n\n");
                        System.out.println(items);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                setListAdapter(new ArrayAdapter(
                        JSONParser.this, android.R.layout.simple_list_item_1,
                        items));
            }
         }
        Test test = new Test();
        test.execute("http://halfbloodprince.16mb.com/food.php");
        //System.out.println("aftafa");
        //return items;
    }
}