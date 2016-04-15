package media.apis.android.example.packagecom.blue_bus;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Ali on 05/12/2015.
 */
public class Add extends ActionBarActivity {

    private Calendar myCalendar1;
    private Calendar myCalendar2;
    private EditText returnT, depart, departTime, returnTime;

    public static final String START = "final";
    public static final String DESTINATION ="destination";// DEPART_DATE, RETURN_DATE, DEPART_TIME, RETURN_TIME;

    Context context = this;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPref2;
    private SharedPreferences.Editor editor2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        depart = (EditText) findViewById(R.id.editText4);
        returnT = (EditText) findViewById(R.id.editText5);
        returnT.setVisibility(View.GONE);
        departTime = (EditText) findViewById(R.id.editText6);
        returnTime = (EditText) findViewById(R.id.editText7);
        returnTime.setVisibility(View.GONE);

        final ImageButton returnCalendar = (ImageButton) findViewById(R.id.imageButton4);
        returnCalendar.setVisibility(View.GONE);

        ImageButton departCalendar = (ImageButton) findViewById(R.id.imageButton3);

        final AutoCompleteTextView from = (AutoCompleteTextView) findViewById(R.id.from);
        final AutoCompleteTextView to = (AutoCompleteTextView) findViewById(R.id.to);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        from.setAdapter(adapter);
        to.setAdapter(adapter);

        Switch returnTrip = (Switch) findViewById(R.id.switch1);

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

        myCalendar1 = Calendar.getInstance();
        myCalendar2 = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateReturnLabel();

               // Calendar mcurrentTime = Calendar.getInstance();
                int hour = myCalendar2.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar2.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Add.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        returnTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                // mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        };

        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDepartLabel();

                // Calendar mcurrentTime = Calendar.getInstance();
                int hour = myCalendar1.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar1.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Add.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        departTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                // mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        };

        returnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Add.this, date1, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        departTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Add.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        departTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                // mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        returnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Add.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        returnTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                //    mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        departCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Add.this, date2, myCalendar2
                        .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                        myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        returnT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Add.this, date1, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        depart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Add.this, date2, myCalendar2
                        .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                        myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
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

        ImageButton go = (ImageButton) findViewById(R.id.imageButton5);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(getApplicationContext(), RideOfferDetails.class));
                Intent intent = new Intent(getApplicationContext(), RideOfferDetails.class);
                intent.putExtra(START, String.valueOf(from.getText()));
                intent.putExtra(DESTINATION, String.valueOf(to.getText()));
               // intent.putExtra(DEPART_DATE, String.valueOf(depart.getText()));
                //intent.putExtra(RETURN_DATE, String.valueOf(returnT.getText()));
               // intent.putExtra(DEPART_TIME, String.valueOf(departTime.getText()));
               // intent.putExtra(RETURN_TIME, String.valueOf(returnTime.getText()));
                startActivity(intent);
               // START = String.valueOf(from.getText());
              //  DESTINATION = String.valueOf(to.getText());
            }
        });
    }

    private void updateReturnLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        returnT.setText(sdf.format(myCalendar1.getTime()));
    }

    private void updateDepartLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        depart.setText(sdf.format(myCalendar2.getTime()));
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
}
