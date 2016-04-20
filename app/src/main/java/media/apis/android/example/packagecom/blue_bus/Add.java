package media.apis.android.example.packagecom.blue_bus;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

/**
 * Created by Ali on 05/12/2015.
 */
public class Add extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Calendar myCalendar;
    private EditText returnT, depart, departTime, returnTime;
    private AutoCompleteTextView from, to;
    private Switch returnTrip;
    public static final String START = "final";
    public static final String DESTINATION ="destination";//  RETURN_DATE, RETURN_TIME;
    public static final String DEPART_DATE ="depart_date";
    public static final String RETURN_DATE ="return_date";

    Context context = this;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    UserSessionManager session;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
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

        depart = (EditText) findViewById(R.id.editText4);
        returnT = (EditText) findViewById(R.id.editText5);
        returnT.setVisibility(View.GONE);
        departTime = (EditText) findViewById(R.id.editText6);
        returnTime = (EditText) findViewById(R.id.editText7);
        returnTime.setVisibility(View.GONE);

        final ImageButton returnCalendar = (ImageButton) findViewById(R.id.imageButton4);
        returnCalendar.setVisibility(View.GONE);

        final ImageButton departCalendar = (ImageButton) findViewById(R.id.imageButton3);

        from = (AutoCompleteTextView) findViewById(R.id.from);
        to = (AutoCompleteTextView) findViewById(R.id.to);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, OFFICES);

      //final burtuAdapteris adapter = new burtuAdapteris(this, android.R.layout.simple_dropdown_item_1line, OFFICES);

        from.setAdapter(adapter);
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

        returnTrip = (Switch) findViewById(R.id.switch1);
        returnTrip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    returnT.setVisibility(View.VISIBLE);
                    returnCalendar.setVisibility(View.VISIBLE);
                    returnTime.setVisibility(View.VISIBLE);
                } else {
                    returnT.setVisibility(View.GONE);
                    returnCalendar.setVisibility(View.GONE);
                    returnTime.setVisibility(View.GONE);
                }
            }
        });

        myCalendar = getInstance();

        returnCalendar.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               DatePickerDialog datePickerDialog = new DatePickerDialog(Add.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(YEAR, year);
                        myCalendar.set(MONTH, monthOfYear);
                        myCalendar.set(DAY_OF_MONTH, dayOfMonth);
                        new TimePickerDialog(Add.this,new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                myCalendar.set(HOUR_OF_DAY, selectedHour);
                                myCalendar.set(MINUTE, selectedMinute);
                                String myTimeFormat = "HH:mm";
                                SimpleDateFormat sd = new SimpleDateFormat(myTimeFormat, Locale.ENGLISH);
                                returnTime.setText(sd.format(myCalendar.getTime()));
                            }
                        },myCalendar.get(HOUR_OF_DAY), myCalendar.get(MINUTE), true).show();
                        updateReturnLabel();
                    }
                }, myCalendar.get(YEAR), myCalendar.get(MONTH),
                        myCalendar.get(DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        departTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(Add.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        myCalendar.set(HOUR_OF_DAY, selectedHour);
                        myCalendar.set(MINUTE, selectedMinute);
                        String myTimeFormat = "HH:mm";
                        SimpleDateFormat sd = new SimpleDateFormat(myTimeFormat, Locale.ENGLISH);
                        departTime.setText(sd.format(myCalendar.getTime()));
                    }
                },myCalendar.get(HOUR_OF_DAY), myCalendar.get(MINUTE), true).show();
            }
        });

        returnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(Add.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        myCalendar.set(HOUR_OF_DAY, selectedHour);
                        myCalendar.set(MINUTE, selectedMinute);
                        String myTimeFormat = "HH:mm";
                        SimpleDateFormat sd = new SimpleDateFormat(myTimeFormat, Locale.ENGLISH);
                        returnTime.setText(sd.format(myCalendar.getTime()));
                    }
                },myCalendar.get(HOUR_OF_DAY), myCalendar.get(MINUTE), true).show();
            }
        });

        departCalendar.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

             DatePickerDialog  datePickerDialog =  new DatePickerDialog(Add.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(YEAR, year);
                        myCalendar.set(MONTH, monthOfYear);
                        myCalendar.set(DAY_OF_MONTH, dayOfMonth);
                        new TimePickerDialog(Add.this,new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                myCalendar.set(HOUR_OF_DAY, selectedHour);
                                myCalendar.set(MINUTE, selectedMinute);
                                String myTimeFormat = "HH:mm";
                                SimpleDateFormat sd = new SimpleDateFormat(myTimeFormat, Locale.ENGLISH);
                                departTime.setText(sd.format(myCalendar.getTime()));
                            }
                        },myCalendar.get(HOUR_OF_DAY), myCalendar.get(MINUTE), true).show();
                        updateDepartLabel();
                    }
                }, myCalendar.get(YEAR), myCalendar.get(MONTH),
                        myCalendar.get(DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        returnT.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new DatePickerDialog(Add.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(YEAR, year);
                        myCalendar.set(MONTH, monthOfYear);
                        myCalendar.set(DAY_OF_MONTH, dayOfMonth);
                        new TimePickerDialog(Add.this,new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                myCalendar.set(HOUR_OF_DAY, selectedHour);
                                myCalendar.set(MINUTE, selectedMinute);
                                String myTimeFormat = "HH:mm";
                                SimpleDateFormat sd = new SimpleDateFormat(myTimeFormat, Locale.ENGLISH);
                                returnTime.setText(sd.format(myCalendar.getTime()));
                            }
                        },myCalendar.get(HOUR_OF_DAY), myCalendar.get(MINUTE), true).show();
                        updateReturnLabel();
                    }
                }, myCalendar.get(YEAR), myCalendar.get(MONTH),
                        myCalendar.get(DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        depart.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new DatePickerDialog(Add.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(YEAR, year);
                        myCalendar.set(MONTH, monthOfYear);
                        myCalendar.set(DAY_OF_MONTH, dayOfMonth);
                        new TimePickerDialog(Add.this,new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                myCalendar.set(HOUR_OF_DAY, selectedHour);
                                myCalendar.set(MINUTE, selectedMinute);
                                String myTimeFormat = "HH:mm";
                                SimpleDateFormat sd = new SimpleDateFormat(myTimeFormat, Locale.ENGLISH);
                                departTime.setText(sd.format(myCalendar.getTime()));
                            }
                        },myCalendar.get(HOUR_OF_DAY), myCalendar.get(MINUTE), true).show();
                        updateDepartLabel();
                    }
                }, myCalendar.get(YEAR), myCalendar.get(MONTH),
                        myCalendar.get(DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        sharedPref = context.getSharedPreferences("address", MODE_WORLD_READABLE);

        //receive data from Map page
        Bundle extras = getIntent().getExtras();
        if (extras!=null&&getIntent()!=null){
            String address =(String)extras.get("selectedAddress");
            Boolean type = (Boolean)extras.get("t");

            if (type==true) {
                //save the option as start point
                from.setText(address, TextView.BufferType.EDITABLE);
                editor = sharedPref.edit();
                editor.putString("SP", address);
                editor.commit();

                //if terminal point is previously set
                if (sharedPref.contains("TP")){
                    String TP = sharedPref.getString("TP",null);
                    to.setText(TP, TextView.BufferType.EDITABLE);
                    System.out.println("TP is " + TP);
                }
            } else {
                //save the option as terminal point
                to.setText(address, TextView.BufferType.EDITABLE);
                editor = sharedPref.edit();
                editor.putString("TP", address);
                editor.commit();

                //if start point is previously set
                if (sharedPref.contains("SP")){
                    String SP = sharedPref.getString("SP",null);
                    from.setText(SP, TextView.BufferType.EDITABLE);
                    System.out.println("SP is "+SP);}
            }
        } else {
            sharedPref.edit().clear().commit();
        }

        ImageButton addMapFrom = (ImageButton) findViewById(R.id.imageButton10);
        addMapFrom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Boolean start = true;
                Boolean searchPage = false;
                //sending two pieces of message
                Intent intent = new Intent(Add.this, Map.class);
                intent.putExtra("startPoint or terminal", start);
                intent.putExtra("searchPage or addPage", searchPage);
                startActivity(intent);
            }
        });

        ImageButton addMapTo = (ImageButton) findViewById(R.id.imageButton11);
        addMapTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean start = false;
                Boolean searchPage = false;
                //sending two pieces of message
                Intent intent = new Intent(Add.this, Map.class);
                intent.putExtra("startPoint or terminal", start);
                intent.putExtra("searchPage or addPage", searchPage);
                startActivity(intent);
            }
        });

        depart.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                depart.setError(null);
                String departText = depart.getText().toString();
                boolean cancel = false;
                View focusView = null;
                if (TextUtils.isEmpty(departText)) {
                    depart.setError(getString(R.string.error_field_required));
                    focusView = depart;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        departTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                departTime.setError(null);
                String departTimeText = departTime.getText().toString();
                boolean cancel = false;
                View focusView = null;
                if (TextUtils.isEmpty(departTimeText)) {
                    departTime.setError(getString(R.string.error_field_required));
                    focusView = departTime;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        returnT.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                returnT.setError(null);
                String returnText = returnT.getText().toString();
                boolean cancel = false;
                View focusView = null;
                if(returnTrip.isChecked()) {
                    if (TextUtils.isEmpty(returnText)) {
                        returnT.setError(getString(R.string.error_field_required));
                        focusView = returnT;
                        cancel = true;
                    }
                }
                if (cancel) {
                    focusView.requestFocus();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        returnTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                returnTime.setError(null);
                String returnTimeText = returnTime.getText().toString();
                boolean cancel = false;
                View focusView = null;
                if(returnTrip.isChecked()) {
                    if (TextUtils.isEmpty(returnTimeText)) {
                        returnTime.setError(getString(R.string.error_field_required));
                        focusView = returnTime;
                        cancel = true;
                    }
                }
                if (cancel) {
                    focusView.requestFocus();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        from.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                from.setError(null);
                String fromText = from.getText().toString();
                boolean cancel = false;
                View focusView = null;
                if (TextUtils.isEmpty(fromText)) {
                    from.setError(getString(R.string.error_field_required));
                    focusView = from;
                    cancel = true;
                } else if(!isValidLocation(fromText)) {
                    from.setError("Please choose one of the provided ATOS offices");
                    focusView = from;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        to.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                to.setError(null);
                String toText = to.getText().toString();
                String fromText = from.getText().toString();
                boolean cancel = false;
                View focusView = null;
                if (TextUtils.isEmpty(toText)) {
                    to.setError(getString(R.string.error_field_required));
                    focusView = to;
                    cancel = true;
                } else if(!isValidLocation(toText)) {
                    to.setError("Please choose one of the provided ATOS offices");
                    focusView = to;
                    cancel = true;
                }else if(fromText.equalsIgnoreCase(toText)){
                    to.setError("Start and destination points must be different");
                    focusView = to;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        ImageButton go = (ImageButton) findViewById(R.id.imageButton5);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset errors.
                from.setError(null);
                to.setError(null);
                depart.setError(null);
                departTime.setError(null);
                returnT.setError(null);
                returnTime.setError(null);

                // Store values at the time of the login attempt.
                String fromText = from.getText().toString();
                String toText = to.getText().toString();
                String departText = depart.getText().toString();
                String departTimeText = departTime.getText().toString();
                String returnText = returnT.getText().toString();
                String returnTimeText = returnTime.getText().toString();

                boolean cancel = false;
                View focusView = null;

                if(returnTrip.isChecked()) {
                    if (TextUtils.isEmpty(returnTimeText)) {
                        returnTime.setError(getString(R.string.error_field_required));
                        focusView = returnTime;
                        cancel = true;
                    }
                    if (TextUtils.isEmpty(returnText)) {
                        returnT.setError(getString(R.string.error_field_required));
                        focusView = returnT;
                        cancel = true;
                    }else if(!isValidDate(returnText)) {
                        returnT.setError("please enter a valid date");
                        focusView = returnT;
                        cancel = true;
                    }
                }
                if (TextUtils.isEmpty(departTimeText)) {
                    departTime.setError(getString(R.string.error_field_required));
                    focusView = departTime;
                    cancel = true;
                }
                if (TextUtils.isEmpty(departText)) {
                    depart.setError(getString(R.string.error_field_required));
                    focusView = depart;
                    cancel = true;
                } else if(!isValidDate(departText)) {
                    depart.setError("please enter a valid date");
                    focusView = depart;
                    cancel = true;
                }
                if (TextUtils.isEmpty(toText)) {
                    to.setError(getString(R.string.error_field_required));
                    focusView = to;
                    cancel = true;
                }else if(!isValidLocation(toText)) {
                    to.setError("Please choose one of the provided ATOS offices");
                    focusView = to;
                    cancel = true;
                }
                else if(fromText.equalsIgnoreCase(toText)){
                    to.setError("Start and destination points must be different");
                    focusView = to;
                    cancel = true;
                }
                if (TextUtils.isEmpty(fromText)) {
                    from.setError(getString(R.string.error_field_required));
                    focusView = from;
                    cancel = true;
                } else if(!isValidLocation(fromText)) {
                    from.setError("Please choose one of the provided ATOS offices");
                    focusView = from;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                } else {
                    String[] departTimeParts = departTimeText.split(":");
                    String departTimePart1 = departTimeParts[0];
                    String departTimePart2 = departTimeParts[1];
                    String[] departDateParts = departText.split("/");
                    String departDatePart1 = departDateParts[0];
                    String departDatePart2 = departDateParts[1];
                    String departDatePart3 = departDateParts[2];

                    Calendar c = Calendar.getInstance();
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    int month = c.get(Calendar.MONTH);
                    int year = c.get(Calendar.YEAR);
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);

                    if (returnTrip.isChecked()) {
                        if(Integer.parseInt(departDatePart1)==day && Integer.parseInt(departDatePart2)==month+1 &&
                                Integer.parseInt(departDatePart3)==year) {
                            if(Integer.parseInt(departTimePart1)==hour && Integer.parseInt(departTimePart2) < minute)
                                departTime.setError("please select a future time");
                            else if(Integer.parseInt(departTimePart1) < hour)
                                departTime.setError("please select a future time");
                             else {
                                if (departText.equalsIgnoreCase(returnText)) {
                                    String[] departParts = departTimeText.split(":");
                                    String departPart1 = departParts[0];
                                    String departPart2 = departParts[1];
                                    String[] returnParts = returnTimeText.split(":");
                                    String returnPart1 = returnParts[0];
                                    String returnPart2 = returnParts[1];

                                    if (Integer.parseInt(departPart1) == Integer.parseInt(returnPart1)) {
                                        if (Integer.parseInt(departPart2) > Integer.parseInt(returnPart2))
                                            returnTime.setError("Please choose a return time after the depart time");
                                        else goToRideDetailsPage();
                                    } else if (Integer.parseInt(departPart1) > Integer.parseInt(returnPart1))
                                        returnTime.setError("Please choose a return time after the depart time");
                                    else goToRideDetailsPage();
                                } else {
                                    String[] departParts = departText.split("/");
                                    String departPart1 = departParts[0];
                                    String departPart2 = departParts[1];
                                    String departPart3 = departParts[2];
                                    String[] returnParts = returnText.split("/");
                                    String returnPart1 = returnParts[0];
                                    String returnPart2 = returnParts[1];
                                    String returnPart3 = returnParts[2];

                                    if (Integer.parseInt(departPart3) < Integer.parseInt(returnPart3))
                                        goToRideDetailsPage();
                                    else if (Integer.parseInt(departPart3) > Integer.parseInt(returnPart3))
                                        returnT.setError("Please choose a return date after the depart date");
                                    else if (Integer.parseInt(departPart3) == Integer.parseInt(returnPart3)) {
                                        if (Integer.parseInt(departPart2) < Integer.parseInt(returnPart2))
                                            goToRideDetailsPage();
                                        else if (Integer.parseInt(departPart2) > Integer.parseInt(returnPart2))
                                            returnT.setError("Please choose a return date after the depart date");
                                        else if (Integer.parseInt(departPart2) == Integer.parseInt(returnPart2)) {
                                            if (Integer.parseInt(departPart1) < Integer.parseInt(returnPart1))
                                                goToRideDetailsPage();
                                            else if (Integer.parseInt(departPart1) > Integer.parseInt(returnPart1))
                                                returnT.setError("Please choose a return date after the depart date");
                                        }
                                    }
                                }
                            }
                        } else {
                            if (departText.equalsIgnoreCase(returnText)) {
                                String[] departParts = departTimeText.split(":");
                                String departPart1 = departParts[0];
                                String departPart2 = departParts[1];
                                String[] returnParts = returnTimeText.split(":");
                                String returnPart1 = returnParts[0];
                                String returnPart2 = returnParts[1];

                                if (Integer.parseInt(departPart1) == Integer.parseInt(returnPart1)) {
                                    if (Integer.parseInt(departPart2) > Integer.parseInt(returnPart2))
                                        returnTime.setError("Please choose a return time after the depart time");
                                    else goToRideDetailsPage();
                                } else if (Integer.parseInt(departPart1) > Integer.parseInt(returnPart1))
                                    returnTime.setError("Please choose a return time after the depart time");
                                else goToRideDetailsPage();
                            } else {
                                String[] departParts = departText.split("/");
                                String departPart1 = departParts[0];
                                String departPart2 = departParts[1];
                                String departPart3 = departParts[2];
                                String[] returnParts = returnText.split("/");
                                String returnPart1 = returnParts[0];
                                String returnPart2 = returnParts[1];
                                String returnPart3 = returnParts[2];

                                if (Integer.parseInt(departPart3) < Integer.parseInt(returnPart3))
                                    goToRideDetailsPage();
                                else if (Integer.parseInt(departPart3) > Integer.parseInt(returnPart3))
                                    returnT.setError("Please choose a return date after the depart date");
                                else if (Integer.parseInt(departPart3) == Integer.parseInt(returnPart3)) {
                                    if (Integer.parseInt(departPart2) < Integer.parseInt(returnPart2))
                                        goToRideDetailsPage();
                                    else if (Integer.parseInt(departPart2) > Integer.parseInt(returnPart2))
                                        returnT.setError("Please choose a return date after the depart date");
                                    else if (Integer.parseInt(departPart2) == Integer.parseInt(returnPart2)) {
                                        if (Integer.parseInt(departPart1) < Integer.parseInt(returnPart1))
                                            goToRideDetailsPage();
                                        else if (Integer.parseInt(departPart1) > Integer.parseInt(returnPart1))
                                            returnT.setError("Please choose a return date after the depart date");
                                    }
                                }
                            }
                        }
                    }
                    else {
                        if(Integer.parseInt(departDatePart1)==day && Integer.parseInt(departDatePart2)==month+1 &&
                                Integer.parseInt(departDatePart3)==year) {
                            if(Integer.parseInt(departTimePart1)==hour && Integer.parseInt(departTimePart2) < minute)
                                departTime.setError("please select a future time");
                            else if(Integer.parseInt(departTimePart1) < hour)
                                departTime.setError("please select a future time");
                            else //depart.setText(String.valueOf(minute));
                                goToRideDetailsPage();
                        } else
                             goToRideDetailsPage();
                    }
                }
            }
        });
    }

    private void goToRideDetailsPage() {
        final ProgressDialog loading;
        loading = ProgressDialog.show(Add.this, "Please Wait", null, true, true);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(getApplicationContext(), RideOfferDetails.class);
                intent.putExtra(START, String.valueOf(from.getText()));
                intent.putExtra(DESTINATION, String.valueOf(to.getText()));
                intent.putExtra(DEPART_DATE, String.valueOf(departTime.getText()).concat(" ").
                        concat(String.valueOf(depart.getText())));
                if(returnTrip.isChecked())
                intent.putExtra(RETURN_DATE, String.valueOf(returnTime.getText()).concat(" ").
                        concat(String.valueOf(returnT.getText())));
                else intent.putExtra(RETURN_DATE, " ");
                // intent.putExtra(DEPART_TIME, String.valueOf(departTime.getText()));
                // intent.putExtra(RETURN_TIME, String.valueOf(returnTime.getText()));
                startActivity(intent);
                loading.dismiss();
            }
        }, 2000);

       // loading.dismiss();
    }

    private boolean isValidDate(String date) {

        if (dateIsValid(date)) {

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

    private boolean isValidLocation(String location) {
       for(String i: OFFICES) {
           if (location.equalsIgnoreCase(i))
               return true;
       }
        return false;
    }

    private void updateReturnLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        returnT.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateDepartLabel() {
        String myDateFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myDateFormat, Locale.ENGLISH);
        depart.setText(sdf.format(myCalendar.getTime()));
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
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Add.this);
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

/*
class burtuAdapteris extends ArrayAdapter<String> implements Filterable {

    ArrayList<String> _items = new ArrayList<String>();
    ArrayList<String> orig = new ArrayList<String>();

    public burtuAdapteris(Context context, int resource, String[] items) {
        super(context, resource, items);
        for (int i = 0; i < items.length; i++) {
            orig.add(items[i]);
        }
    }

    @Override
    public int getCount() {
        if (_items != null)
            return _items.size();
        else return 0;
    }

    @Override
    public String getItem(int arg0) {
        return _items.get(arg0);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                if(constraint != null)
                    Log.d("Constraints", constraint.toString());
                FilterResults oReturn = new FilterResults();

                /*  if (orig == null){
                    for (int i = 0; i < items.size(); i++) {
                        orig.add(items.get(i));
                    }
                  }*/ /*
                String temp;
                int counters = 0;
                if (constraint != null){
                    _items.clear();
                    if (orig != null && orig.size() > 0) {
                        for(int i=0; i<orig.size(); i++)
                        {
                            temp = orig.get(i).toUpperCase();
                            if(temp.startsWith(constraint.toString().toUpperCase()))
                            {
                                _items.add(orig.get(i));
                                counters++;
                            }
                        }
                    }
                    Log.d("REsult size:" , String.valueOf(_items.size()));
                    if(counters==0)
                    {
                        _items.clear();
                        _items = orig;
                    }
                    oReturn.values = _items;
                    oReturn.count = _items.size();
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results != null && results.count > 0) notifyDataSetChanged();
                else notifyDataSetInvalidated();
            }
        };
        return filter;
    }
}
*/