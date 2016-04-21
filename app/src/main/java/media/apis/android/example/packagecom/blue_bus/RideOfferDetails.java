package media.apis.android.example.packagecom.blue_bus;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RideOfferDetails extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    private static final String RIDES_URL = "http://halfbloodprince.16mb.com/rides.php";
    private EditText seats;
    UserSessionManager session;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_offer_details);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        session = new UserSessionManager(getApplicationContext());
        final TextView start = (TextView) findViewById(R.id.textView5);
        final TextView dest = (TextView) findViewById(R.id.textView7);
        final TextView returnStart = (TextView) findViewById(R.id.textView12);
        final TextView returnDest = (TextView) findViewById(R.id.textView14);
        final TextView returnDate = (TextView) findViewById(R.id.textView15);
        final TextView arrow = (TextView) findViewById(R.id.textView13);
        final TextView border = (TextView) findViewById(R.id.textView16);
        returnStart.setVisibility(View.GONE);
        returnDest.setVisibility(View.GONE);
        returnDate.setVisibility(View.GONE);
        arrow.setVisibility(View.GONE);
        border.setVisibility(View.GONE);
        final TextView date = (TextView) findViewById(R.id.textView10);
        final EditText comment = (EditText) findViewById(R.id.editText8);
        Intent intent = getIntent();
        final String startT = intent.getStringExtra(Add.START);
        final String destT = intent.getStringExtra(Add.DESTINATION);
        final String dateT = intent.getStringExtra(Add.DEPART_DATE);
        final String returnDateT = intent.getStringExtra(Add.RETURN_DATE); //Add.DEPART_DATE + " " + Add.DEPART_TIME);
        if (!returnDateT.equalsIgnoreCase(" ")) {
            returnStart.setVisibility(View.VISIBLE);
            returnDest.setVisibility(View.VISIBLE);
            returnDate.setVisibility(View.VISIBLE);
            arrow.setVisibility(View.VISIBLE);
            border.setVisibility(View.VISIBLE);
        }
        start.setText(startT);
        dest.setText(destT);
        date.setText(dateT);
        returnStart.setText(destT);
        returnDest.setText(startT);
        returnDate.setText(returnDateT);

        Button publish = (Button) findViewById(R.id.button2);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seats.setError(null);
                comment.setError(null);
                String seatsText = seats.getText().toString();
                String commentText = comment.getText().toString();
                boolean cancel = false;
                View focusView = null;
                if (TextUtils.isEmpty(seatsText)) {
                    seats.setError(getString(R.string.error_field_required));
                    focusView = seats;
                    cancel = true;
                }
                if (TextUtils.isEmpty(commentText)) {
                    comment.setError(getString(R.string.error_field_required));
                    focusView = comment;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                } else {
                    if(!session.isUserLoggedIn()) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RideOfferDetails.this);

                        // Setting Dialog Title
                        alertDialog.setTitle("Almost there");

                        // Setting Dialog Message
                        alertDialog.setMessage("You need to log in to publish a ride");

                        alertDialog.setPositiveButton("Log in", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                startActivity(new Intent(getApplicationContext(), Login.class));
                            }
                        });

                        alertDialog.setNegativeButton("Register", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getApplicationContext(), Register.class));
                            }
                        });
                        alertDialog.show();
                    }
                    else {
                        if(haveNetworkConnection()) {

                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(RideOfferDetails.this);
                            alertDialog.setMessage("You are about to publish this ride offer. Are you sure?");
                            alertDialog.setPositiveButton("Publish", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    publishRide(start.getText().toString().replace(" ", "_"), dest.getText().toString().replace(" ", "_"),
                                            date.getText().toString().replace(" ", "_"), Integer.parseInt(seats.getText().toString()),
                                            comment.getText().toString().replace(" ", "_"));
                                    if (arrow.getVisibility() == View.VISIBLE)
                                        publishRide(dest.getText().toString().replace(" ", "_"), start.getText().toString().replace(" ", "_"),
                                                returnDate.getText().toString().replace(" ", "_"), Integer.parseInt(seats.getText().
                                                        toString()), comment.getText().toString().replace(" ", "_"));
                                    startActivity(new Intent(getApplicationContext(), RidePublished.class));
                                }
                            });

                            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.create().dismiss();
                                }
                            });
                            alertDialog.show();
                        }
                        else Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        seats = (EditText) findViewById(R.id.editText9);
        seats.setFocusable(false);
        seats.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                show();
            }
        });

        seats.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
              if(seats.hasFocus())  show();
            }
        });
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is", "" + newVal);
    }

    public void show() {

        final Dialog d = new Dialog(RideOfferDetails.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(8);   // max value 8
        np.setMinValue(0);   // min value 0
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seats.setText(String.valueOf(np.getValue())); //set the value to textview
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();
    }

    private void publishRide(String start, String destination, String depart_time, int seats, String comment) {
        String urlSuffix = "?start="+start+"&destination="+destination+"&depart_time="+depart_time+"&seats="
                +seats+"&comment="+comment;
        class RegisterUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RideOfferDetails.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader;
                try {
                    URL url = new URL(RIDES_URL+s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String result;
                    result = bufferedReader.readLine();
                    return result;
                }catch(Exception e){
                    return null;
                }
            }
        }
        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
        // showProgress(true);
    }
}
