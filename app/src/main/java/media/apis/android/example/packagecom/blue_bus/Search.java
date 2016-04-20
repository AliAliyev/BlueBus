package media.apis.android.example.packagecom.blue_bus;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Ali on 03/12/2015.
 */


public class Search extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private EditText dateText;
    private Calendar myCalendar;
    private EditText startPoint;
    private EditText terminalPoint;

    Context context = this;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPref2;
    private SharedPreferences.Editor editor2;
    UserSessionManager session;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        session = new UserSessionManager(getApplicationContext());
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

        if(session.isUserLoggedIn())
            text.setText(session.getUserDetails().get(UserSessionManager.KEY_EMAIL));

        if(session.isUserLoggedIn())
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer);
        } else
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_log_drawer);
        }

        sharedPref = context.getSharedPreferences("address", MODE_WORLD_READABLE);
        EditText editText = (EditText) findViewById(R.id.editText);
        EditText editText2 = (EditText) findViewById(R.id.editText2);

        //receive data from Map page
        Bundle extras = getIntent().getExtras();
        if (extras!=null&&getIntent()!=null){
            String address =(String)extras.get("selectedAddress");
            Boolean type = (Boolean)extras.get("t");


            if (type==true) {
                //save the option as start point

                editText.setText(address, TextView.BufferType.EDITABLE);
                editor = sharedPref.edit();
                editor.putString("SP", address);
                editor.commit();

                //if terminal point is previously set
                if (sharedPref.contains("TP")){
                    String TP = sharedPref.getString("TP",null);
                    editText2.setText(TP, TextView.BufferType.EDITABLE);
                    System.out.println("TP is " + TP);
                }
            } else {
                //save the option as terminal point
                editText2.setText(address, TextView.BufferType.EDITABLE);
                editor = sharedPref.edit();
                editor.putString("TP", address);
                editor.commit();

                //if start point is previously set
                if (sharedPref.contains("SP")){
                    String SP = sharedPref.getString("SP",null);
                    editText.setText(SP, TextView.BufferType.EDITABLE);
                    System.out.println("SP is "+SP);}
            }
        } else {
            sharedPref.edit().clear().commit();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        AutoCompleteTextView from = (AutoCompleteTextView)
                findViewById(R.id.editText);
        from.setAdapter(adapter);

        AutoCompleteTextView to = (AutoCompleteTextView)
                findViewById(R.id.editText2);
        to.setAdapter(adapter);

        dateText = (EditText) findViewById(R.id.editText3);

        myCalendar = Calendar.getInstance();
        ImageButton calen = (ImageButton) findViewById(R.id.imageButton);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        calen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Search.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        dateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Search.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        ImageButton addMapFrom = (ImageButton) findViewById(R.id.imageButton6);
        addMapFrom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Boolean start = true;
                Boolean searchPage = true;

                //sending two pieces of message
                Intent intent = new Intent(Search.this, Map.class);
                intent.putExtra("startPoint or terminal", start);
                intent.putExtra("searchPage or addPage", searchPage);
                startActivity(intent);
            }
        });

        ImageButton addMapTo = (ImageButton) findViewById(R.id.imageButton7);
        addMapTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean start = false;
                Boolean searchPage = true;

                //sending two pieces of message
                Intent intent = new Intent(Search.this, Map.class);
                intent.putExtra("startPoint or terminal", start);
                intent.putExtra("searchPage or addPage", searchPage);
                startActivity(intent);
            }
        });

        startPoint = (EditText) findViewById(R.id.editText);
        terminalPoint = (EditText) findViewById(R.id.editText2);
        final String SP = startPoint.getText().toString();
        final String TP = terminalPoint.getText().toString();

        ImageButton search = (ImageButton) findViewById(R.id.imageButton2);
        search.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v) {
                //to save the addresses typed or selected by user
                EditText editText = (EditText) findViewById(R.id.editText);
                EditText editText2 = (EditText) findViewById(R.id.editText2);
                String sPoint=editText.getText().toString();
                String tPoint=editText2.getText().toString();
                sharedPref = context.getSharedPreferences("address", MODE_WORLD_READABLE);
                editor = sharedPref.edit();
                editor.putString("SP", sPoint);
                editor.putString("TP", tPoint);
                editor.commit();

                boolean cancel = false;
                View focusView = null;
                if (!TextUtils.isEmpty(sPoint)&&!TextUtils.isEmpty(tPoint)){
                    System.out.println("both not empty");
                    if (!sPoint.equals(tPoint)){
                        //System.out.println("not equal");
                        //Intent intent = new Intent(Search.this, SearchResult.class);
                        //System.out.println("SearchResult page is going to be launched");
                        //intent.putExtra("startPoint or terminal", start);
                        //intent.putExtra("searchPage or addPage", searchPage);
                        //startActivity(intent);
                    } else {
                        terminalPoint.setError("Start point and terminal point are the same place");
                        focusView = terminalPoint;
                        cancel = true;
                    }
                } else if (TextUtils.isEmpty(sPoint)){
                    startPoint.setError("Start point needs to be set");
                    focusView = startPoint;
                    cancel = true;
                } else {
                    terminalPoint.setError("Terminal point needs to be set");
                    focusView = terminalPoint;
                    cancel = true;}

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    System.out.println("Intent created");
                    Intent intent = new Intent(Search.this, SearchResult.class);
                    System.out.println("SearchResult page is going to be launched");
                    startActivity(intent);
                }
            }
        });



    }



    private void updateLabel() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateText.setText(sdf.format(myCalendar.getTime()));

    }

    private static final String[] COUNTRIES = new String[] {
            "Andover - Kingsgate House", "Birmingham - Business Park", "Bristol - Beta Building",
            "Cambridge - Discovery House", "Crewe - Rail House", "Darlington - Equinox House",
            "Dublin - Ireland","Dundee - Maryfield House", "Guildford - Deacon Field",
            "Leeds - Wellington Place", "Linwood - St James Business Park", "Liverpool - Old Hall Street",
            "Livingston - Appleton Parkway", "London, Triton Square", "Nottingham - Building 10.1",
            "Runcorn - Daresbury Court", "Westbury - Heywood House", "Winnersh – 1020 Eskdale Road",
            "Wolverhampton - Trinity Court"
    };

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
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Search.this);
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
