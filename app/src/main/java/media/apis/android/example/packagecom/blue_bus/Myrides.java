package media.apis.android.example.packagecom.blue_bus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
 * Created by Ali on 20/04/2016.
 */
public class Myrides extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String next;
    private ArrayList items = null;
    private  static  final String RIDE_URL = "http://halfbloodprince.16mb.com/food.php";
    UserSessionManager session;
    RideAdapter rideAdapter;
    ArrayList<RideItem> myListItems;
    ListView listView;
    public static final String CONTENT ="content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrides);
        session = new UserSessionManager(getApplicationContext());
        items = new ArrayList();
        populate(session.getUserDetails().get(UserSessionManager.KEY_EMAIL));
        listView = (ListView) findViewById(R.id.list);

        myListItems = new ArrayList<RideItem>();
        populateListView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView text = (TextView) header.findViewById(R.id.user);

        if (session.isUserLoggedIn())
            text.setText(session.getUserDetails().get(UserSessionManager.KEY_EMAIL));

        if (session.isUserLoggedIn()) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer);
        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_log_drawer);
        }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), ManageRideOffer.class);
                    intent.putExtra(CONTENT, listView.getItemAtPosition(position).toString());
                 if(!listView.getItemAtPosition(position).toString().equalsIgnoreCase("No rides to show")) {
                     startActivity(intent);
                     finish();
                 }
                }
            });
    }

        private void populate(final String user) {

            class Test extends AsyncTask<String, Void, String> {
                ProgressDialog loading;
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(Myrides.this, "Please Wait", null, true, true);
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
                 //   items = new ArrayList();
                    try {
                        JSONArray ja = new JSONArray(next);

                        for (int i = 0; i < ja.length(); i++) {
                            //  JSONObject jo = (JSONObject) ja.get(i);
                            // items.add(jo.getString("food_name"));

                            JSONObject jsonObj = ja.getJSONObject(i);
                            items.add(jsonObj.getString("start")+"\n"+">>"+"\n"+jsonObj.getString("destination")+
                                    "\n"+jsonObj.getString("depart_time")+"\n"+
                                    "Spare seats "+jsonObj.getString("seats"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                if (items.isEmpty())    items.add("No rides to show");
                    listView.setAdapter(new ArrayAdapter(
                            Myrides.this, android.R.layout.simple_list_item_1,
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


    private void populateListView() {
        rideAdapter = new RideAdapter (Myrides.this, 0, myListItems);
      //  ListView listview = (ListView) findViewById(R.id.list);
      //  listview.setAdapter(rideAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else if (id == R.id.nav_inbox) {
                startActivity(new Intent(getApplicationContext(), Inbox.class));
            } else if (id == R.id.nav_rides) {
                startActivity(new Intent(getApplicationContext(), Myrides.class));
            } else if (id == R.id.nav_register) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            } else if (id == R.id.nav_login) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            } else if (id == R.id.nav_send) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Myrides.this);
                alertDialog.setTitle("Log out");
                alertDialog.setMessage("Are you sure to log out?");
                alertDialog.setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        session.logoutUser();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.create().dismiss();
                    }
                });
                alertDialog.show();
            } else if (id == R.id.nav_search) {
                startActivity(new Intent(getApplicationContext(), Search.class));
            } else if (id == R.id.nav_offer) {
                startActivity(new Intent(getApplicationContext(), Add.class));
            }else if (id == R.id.nav_help) {
                startActivity(new Intent(getApplicationContext(), Help.class));
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
    }
}
