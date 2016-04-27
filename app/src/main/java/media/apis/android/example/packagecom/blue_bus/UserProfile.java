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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;

/**
 * Created by Ali on 25/04/2016.
 */
public class UserProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    UserSessionManager session;
    private static final String USER_URL = "http://halfbloodprince.16mb.com/userDetails.php";
    private static final String USER_DELETE_URL = "http://halfbloodprince.16mb.com/userDelete.php";
    public static String NAME = "name";
    public static String SURNAME = "surname";
    public static String AGE = "age";
    public static String GENDER = "gender";


    String next;
    TextView textView, emailV;
    Button delete, edit, add_car;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        session = new UserSessionManager(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView text = (TextView) header.findViewById(R.id.user);

        delete = (Button) findViewById(R.id.delete);
        add_car = (Button) findViewById(R.id.add_car);
        add_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "to be added on further development", Toast.LENGTH_LONG).show();
            }
        });
        edit = (Button) findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditProfile.class));
            }
        });

        textView = (TextView) findViewById(R.id.textView18);
        emailV = (TextView) findViewById(R.id.email);
        if (session.isUserLoggedIn())
            text.setText(session.getUserDetails().get(UserSessionManager.KEY_EMAIL));
        ImageView image = (ImageView) header.findViewById(R.id.imageView);
        TextView textUser = (TextView) header.findViewById(R.id.user);
        if(session.isUserLoggedIn())
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), UserProfile.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            });
            textUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), UserProfile.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            });
        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_log_drawer);
        }

        populate(session.getUserDetails().get(UserSessionManager.KEY_EMAIL));

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserProfile.this);
                alertDialog.setTitle("Delete");
                alertDialog.setMessage("Delete account?");

                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount(session.getUserDetails().get(UserSessionManager.KEY_EMAIL));
                        session.logoutUser();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                });

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.create().dismiss();
                    }
                });
                alertDialog.show();

            }
        });

        NAME = "name";
    }


    private void populate(final String email) {

        class Test extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UserProfile.this, "Please Wait", null, true, true);
            }
            @Override
            protected String doInBackground(String... params) {
                try {
                    //to fetch data from database
                    HashMap<String,String> strData = new HashMap<>();
                    strData.put("email", params[0]);

                    URL url = new URL(USER_URL);
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

                        JSONObject jsonObj = ja.getJSONObject(i);
                        textView.setText(jsonObj.getString("firstName")+" "+jsonObj.getString("familyName")+"\n"+
                        jsonObj.getString("gender")+"\n"+jsonObj.getString("age")+ " " + "years old");
                        emailV.setText(jsonObj.getString("email"));

                        NAME = jsonObj.getString("firstName");
                        SURNAME = jsonObj.getString("familName");
                        GENDER = jsonObj.getString("gender");
                        AGE = jsonObj.getString("age");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        Test test = new Test();
        test.execute(email);
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

    public void deleteAccount(final String email){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UserProfile.this,"Please Wait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                s = s.trim();
                if (s.equalsIgnoreCase("success")){
                    Toast.makeText(getApplicationContext(), "account deleted", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(UserProfile.this, "something went wrong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("email",params[0]);

                RegisterUserClass ruc = new RegisterUserClass();
               String result = ruc.sendPostRequest(USER_DELETE_URL,data);

                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(email);
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
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserProfile.this);
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