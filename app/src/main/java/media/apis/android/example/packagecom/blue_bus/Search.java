package media.apis.android.example.packagecom.blue_bus;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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


public class Search extends ActionBarActivity {

    private EditText dateText;
    private Calendar myCalendar;
    private EditText startPoint;
    private EditText terminalPoint;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        //receive data from Map page
        Bundle extras = getIntent().getExtras();
        if (extras!=null&&getIntent()!=null){
            String address =(String)extras.get("selectedAddress");
            Boolean type = (Boolean)extras.get("t");
            System.out.println("receiving ArrayList, "+type);
            if (type=true) {
                //save the option as start point
                EditText editText = (EditText) findViewById(R.id.editText);
                editText.setText(address, TextView.BufferType.EDITABLE);
            } else {
                //save the option as terminal point
                EditText editText = (EditText) findViewById(R.id.editText2);
                editText.setText(address, TextView.BufferType.EDITABLE);
            }
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
                boolean cancel = false;
                View focusView = null;
                if (!TextUtils.isEmpty(SP)&&!TextUtils.isEmpty(TP)){
                    //Intent intent = new Intent(Search.this, SearchResult.class);
                    //intent.putExtra("startPoint or terminal", start);
                    //intent.putExtra("searchPage or addPage", searchPage);
                    //startActivity(intent);
                } else if (TextUtils.isEmpty(SP)){
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
                    // Show a progress spinner, and kick off a background task to
                    // perform the user login attempt.
                    //showProgress(true);
                    //mAuthTask = new UserLoginTask(email, password);
                    //mAuthTask.execute((Void) null);
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


}
