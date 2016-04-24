package media.apis.android.example.packagecom.blue_bus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.places.AutocompletePrediction;

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
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by romil_000 on 2016/3/9.
 */

public class SearchResult extends ActionBarActivity{

    private static final String SEARCH_URL = "http://halfbloodprince.16mb.com/search.php";
    private static final String UPDATE_URL = "http://halfbloodprince.16mb.com/updateseats.php";


    Context context = this;
    private SharedPreferences sharedPref;
    ArrayList listviewData = new ArrayList();
    private TextView textView;
    private EditText editText;
    private ListView lv;
    private AlertDialog.Builder dialogBuilder, confirmDialog;
    private String selected, depart_time, seats, comment;
    private int number, noOfSeats;
    private boolean updateError;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresult);

        updateError = false;

        sharedPref = context.getSharedPreferences("address", MODE_WORLD_READABLE);
        String SP = sharedPref.getString("SP",null);
        String TP = sharedPref.getString("TP", null);
        String date = sharedPref.getString("date", null)+" 00:00:00";
        textView = (TextView)findViewById(R.id.textView10);
        textView.setText(SP + "\n>>\n" + TP);
        searchForRide(SP.replace(" ", "_"), TP.replace(" ", "_"), date.replace(" ", "_"));

        lv = (ListView) findViewById(R.id.showData);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //After selecting a ride
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                selected = (String) (lv.getItemAtPosition(position));
                if (!selected.equals(" No trip is found")){
                    String [] records = selected.split("\n");
                    depart_time = records[0].substring(14);
                    seats = records[1].substring(21);
                    comment = records[2].substring(15);
                    noOfSeats = Integer.parseInt(seats);
                    bookingDialog();
                }
            }
        });
    }
    private String searchForRide(final String startPoint, final String destPoint, final String depart_time){
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchResult.this, R.layout.single_row, R.id.details, listviewData);
                lv.setAdapter(adapter);

            }

            @Override
            protected String doInBackground(String... params){
                try {
                    //to fetch data from database
                    HashMap<String,String> strData = new HashMap<>();
                    strData.put("start", params[0]);
                    strData.put("destination", params[1]);
                    strData.put("depart_time", params[2]);
                    URL url = new URL(SEARCH_URL);
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

                    int responseCode=con.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        String line;
                        String strResult = "";
                        BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                        int i = 1;
                        //String [] rec = new String[3];
                        while ((line=br.readLine()) != null) {
                            int j = i % 3;
                            strResult+=line;
                            switch (j){
                                case 0:
                                    listviewData.add(strResult);
                                    strResult = "";
                                    break;
                                case 1: strResult=strResult+"\n";
                                    break;
                                case 2: strResult=strResult+"\n";
                            }
                            i++;
                        }
                        strResult = strResult.substring(0,strResult.length()-1);
                        listviewData.add(strResult);
                        return strResult;
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
        search.execute(startPoint, destPoint, depart_time);
        return null;
    }

    private String updateSeats (final String reducedNumber, final String depart_time, final String seats, final String comment){
        class UpdateSeatsClass extends AsyncTask<String,Void,String>{
            boolean error;
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SearchResult.this, "Please Wait", null, true, true);
                error = true;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (error){
                    updateError = true;
                }
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    //to fetch data from database
                    HashMap<String,String> strData = new HashMap<>();
                    strData.put("updateseats", params[0]);
                    strData.put("depart_time", params[1]);
                    strData.put("seats", params[2]);
                    strData.put("comment", params[3]);
                    URL url = new URL(UPDATE_URL);
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

                    int responseCode=con.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {//make sure no connection error
                        String line;
                        String strResult = "";
                        BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                        while ((line=br.readLine()) != null) {
                            strResult+=line;
                        }
                        error = false;
                        return strResult;
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
        UpdateSeatsClass usc = new UpdateSeatsClass();
        usc.execute(reducedNumber, depart_time, seats, comment);
        return null;
    }




    protected void bookingDialog() {
        editText = new EditText(this);
        dialogBuilder = new AlertDialog.Builder(SearchResult.this)
                .setTitle("Make a booking?")
                .setMessage(selected)
                .setPositiveButton("Booking", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        confirmDialog = new AlertDialog.Builder(SearchResult.this)
                                .setTitle(noOfSeats + " seats available")
                                .setMessage("Please enter the number of seats you need:")
                                .setView(editText)
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        String inputSeats = editText.getText().toString();
                                        Boolean error = true;
                                        //Check whether input number is valid
                                        if (!TextUtils.isEmpty(inputSeats)) {
                                            try {
                                                number = Integer.parseInt(editText.getText().toString());
                                                if (number > 0 && number <= noOfSeats) {
                                                    error = false;
                                                    String reducedSeats = Integer.toString(noOfSeats - number);
                                                    updateSeats(reducedSeats, depart_time.replace(" ", "_"), seats.replace(" ", "_"), comment.replace(" ", "_"));
                                                    if (!updateError) {
                                                        Toast.makeText(getApplicationContext(), "You have booked a ride!", Toast.LENGTH_LONG).show();
                                                        SearchResult.this.finish();
                                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Error from database! Please search again", Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            } catch (NumberFormatException e) {

                                            } catch (NullPointerException e) {

                                            }
                                        }

                                        if (error) {
                                            Toast.makeText(getApplicationContext(), "Please enter a valid number no greater than available seats and try again", Toast.LENGTH_LONG).show();
                                            dialog.dismiss();
                                        }

                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        confirmDialog.create().show();
                    }
                });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });  dialogBuilder.create().show();
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
