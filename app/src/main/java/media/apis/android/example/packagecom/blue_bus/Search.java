package media.apis.android.example.packagecom.blue_bus;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
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

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        } else
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_log_drawer);
        }

        sharedPref = context.getSharedPreferences("address", MODE_WORLD_READABLE);
        startPoint = (EditText) findViewById(R.id.editText);
        terminalPoint = (EditText) findViewById(R.id.editText2);

        //receive data from Map page
        Bundle extras = getIntent().getExtras();
        if (extras!=null&&getIntent()!=null){
            String address =(String)extras.get("selectedAddress");
            Boolean type = (Boolean)extras.get("t");

            //Check whether it is a start point or terminal point
            if (type==true) {
                startPoint.setText(address, TextView.BufferType.EDITABLE);
                editor = sharedPref.edit();
                editor.putString("SP", address);
                editor.commit();

                //if terminal point is previously set
                if (sharedPref.contains("TP")){
                    String TP = sharedPref.getString("TP",null);
                    terminalPoint.setText(TP, TextView.BufferType.EDITABLE);
                    System.out.println("TP is " + TP);
                }
            } else {
                terminalPoint.setText(address, TextView.BufferType.EDITABLE);
                editor = sharedPref.edit();
                editor.putString("TP", address);
                editor.commit();

                //if start point is previously set
                if (sharedPref.contains("SP")){
                    String SP = sharedPref.getString("SP",null);
                    startPoint.setText(SP, TextView.BufferType.EDITABLE);
                    System.out.println("SP is "+SP);}
            }
        } else {
            sharedPref.edit().clear().commit();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, OFFICES);
        final AutoCompleteTextView from = (AutoCompleteTextView)
                findViewById(R.id.editText);
        from.setAdapter(adapter);

        final AutoCompleteTextView to = (AutoCompleteTextView)
                findViewById(R.id.editText2);
        to.setAdapter(adapter);

        from.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                from.showDropDown();

            }
        });

        to.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                to.showDropDown();

            }
        });

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

        dateText.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new DatePickerDialog(Search.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }
                }, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        calen.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Search.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
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

        startPoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                startPoint.setError(null);
                String departText = startPoint.getText().toString();
                boolean cancel = false;
                View focusView = null;
                if (TextUtils.isEmpty(departText)) {
                    startPoint.setError(getString(R.string.error_field_required));
                    focusView = startPoint;
                    cancel = true;
                } else if (!isValidLocation(departText)) {
                    startPoint.setError("Please choose one of the provided ATOS offices");
                    focusView = startPoint;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        terminalPoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                terminalPoint.setError(null);
                String termText = terminalPoint.getText().toString();
                boolean cancel = false;
                View focusView = null;
                if (TextUtils.isEmpty(termText)) {
                    terminalPoint.setError(getString(R.string.error_field_required));
                    focusView = terminalPoint;
                    cancel = true;
                } else if (!isValidLocation(termText)) {
                    terminalPoint.setError("Please choose one of the provided ATOS offices");
                    focusView = terminalPoint;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        ImageButton search = (ImageButton) findViewById(R.id.imageButton2);
        search.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                //to save the addresses typed or selected by user
                startPoint.setError(null);
                terminalPoint.setError(null);
                dateText.setError(null);
                String sPoint = startPoint.getText().toString();
                String tPoint = terminalPoint.getText().toString();
                String depart_time = dateText.getText().toString();
                sharedPref = context.getSharedPreferences("address", MODE_WORLD_READABLE);
                editor = sharedPref.edit();
                editor.putString("SP", sPoint);
                editor.putString("TP", tPoint);
                editor.putString("date", depart_time);
                System.out.println("time: " + depart_time);
                editor.commit();

                boolean cancel = false;
                View focusView = null;
                //Check whether the addresses are valid
                if (!TextUtils.isEmpty(sPoint) && !TextUtils.isEmpty(tPoint)) {
                    if (sPoint.equals(tPoint)) {
                        terminalPoint.setError("Start point and terminal point are the same place");
                        focusView = terminalPoint;
                        cancel = true;
                    }
                } else if (TextUtils.isEmpty(sPoint)) {
                    startPoint.setError("Start point needs to be set");
                    focusView = startPoint;
                    cancel = true;
                } else {
                    terminalPoint.setError("Terminal point needs to be set");
                    focusView = terminalPoint;
                    cancel = true;
                }

                //Check whether the depart time is valid
                if (!isValidDate(depart_time)) {
                    dateText.setError("Please enter a valid date");
                    focusView = dateText;
                    cancel = true;
                }

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

    private boolean isValidLocation(String location) {
        for(String i: OFFICES) {
            if (location.equalsIgnoreCase(i))
                return true;
        }
        return false;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        dateText.setText(sdf.format(myCalendar.getTime()));

    }

    private static final String[] OFFICES = new String[] {
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

    private boolean isValidDate(String date) {

        if (dateIsValid(date)) {

            // from.setText(clone1);

            String[] parts = date.split("/");
            String part1 = parts[0];
            String part2 = parts[1];
            String part3 = parts[2];
            boolean valid=true;
            for(char c: part3.toCharArray()) {
                if(!Character.isDigit(c))
                {
                    valid = false;
                    break;
                }
            }
            if (valid && Integer.parseInt(part1) >= 1 && Integer.parseInt(part1) <= 31 &&
                    Integer.parseInt(part2) >= 1 && Integer.parseInt(part2) <= 12 &&
                    Integer.parseInt(part3) >= 2016 && Integer.parseInt(part3) <= 2099)
                return true;
        }
        return false;
    }

    private boolean dateIsValid(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }


}
