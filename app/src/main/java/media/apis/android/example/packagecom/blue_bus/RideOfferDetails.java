package media.apis.android.example.packagecom.blue_bus;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class RideOfferDetails extends AppCompatActivity {

    private static final String RIDES_URL = "http://halfbloodprince.16mb.com/rides.php";
    NumberPicker np;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_offer_details);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        np = (NumberPicker) findViewById(R.id.numberPicker);
        np.setMinValue(0);
        np.setMaxValue(8);
        np.setWrapSelectorWheel(true);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub

            }
        });

/*
       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        final TextView start = (TextView) findViewById(R.id.textView5);
        final TextView dest =  (TextView) findViewById(R.id.textView7);
        final EditText comment = (EditText) findViewById(R.id.editText8);
        Intent intent = getIntent();
        final String startT = intent.getStringExtra(Add.START);// + " " + Add.DEPART_DATE + " " + Add.DEPART_TIME);
        final String destT = intent.getStringExtra(Add.DESTINATION);// + " " + Add.DEPART_DATE + " " + Add.DEPART_TIME);
        start.setText(startT);
        dest.setText(destT);

        Button publish = (Button) findViewById(R.id.button2);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            register(start.getText().toString().replace(" ", "_"), dest.getText().toString().replace(" ", "_"), "depart", "return", 5, comment.getText().toString().replace(" ", "_"));
            }
        });
    }

    private void register(String start, String destination, String depart_time, String return_time, int seats, String comment) {
        String urlSuffix = "?start="+start+"&destination="+destination+"&depart_time="+depart_time+"&return_time="+return_time+"&seats="+seats+"&comment="+comment;
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
