package media.apis.android.example.packagecom.blue_bus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by romil_000 on 2016/3/9.
 */

public class SearchResult extends ActionBarActivity{

    private static final String SEARCH_URL = "http://halfbloodprince.16mb.com/search.php";

    Context context = this;
    private SharedPreferences sharedPref;
    ArrayList listviewData = new ArrayList();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresult);

        sharedPref = context.getSharedPreferences("address", MODE_WORLD_READABLE);
        String SP = sharedPref.getString("SP",null);
        String TP = sharedPref.getString("TP", null);
        String date = sharedPref.getString("date", null);
        TextView textView = (TextView)findViewById(R.id.textView10);
        textView.setText(SP + "\n>>\n" + TP);
        searchForRide(SP.replace(" ", "_"), TP.replace(" ", "_"));
    }
    private String searchForRide(final String startPoint, final String destPoint){
        class Search extends AsyncTask<String,Void, String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(SearchResult.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //to inflate the listview
                ListView lv = (ListView) findViewById(R.id.showData);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchResult.this, R.layout.single_row, R.id.details, listviewData);
                lv.setAdapter(adapter);
            }

            @Override
            protected String doInBackground(String... params){
                try {
                    //to fetch data from database
                    HashMap<String,String> strData = new HashMap<>();
                    strData.put("start", params[0]);
                    strData.put("destination", params[1]);;
                    URL url = new URL(SEARCH_URL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(15000);
                    con.setConnectTimeout(15000);
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    System.out.println("URL connected");

                    OutputStream os = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(strData));
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode=con.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        String line;
                        String strResult = "";
                        BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                        int i = 1;
                        while ((line=br.readLine()) != null) {
                            int j = i % 3;
                            strResult+=line;
                            switch (j){
                                case 0: listviewData.add(strResult);
                                    strResult = "";
                                    break;
                                case 1: strResult=strResult+"\n";
                                    break;
                                case 2: strResult=strResult+"\n";
                            }
                            i++;
                        }
                        strResult = strResult.substring(0,strResult.length()-2);
                        System.out.println(strResult);
                        listviewData.add(strResult);
                    }else {
                        Toast.makeText(getApplicationContext(), "Connection error!", Toast.LENGTH_LONG).show();
                    }

                    con.disconnect();
                    return null;

                }catch (MalformedURLException e){
                    e.printStackTrace();
                    return null;
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }

            }
        }
        Search search = new Search();
        search.execute(startPoint,destPoint);
        return null;
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

