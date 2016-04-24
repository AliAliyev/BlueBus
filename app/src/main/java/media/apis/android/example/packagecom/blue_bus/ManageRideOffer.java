package media.apis.android.example.packagecom.blue_bus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;

/**
 * Created by Ali on 24/04/2016.
 */
public class ManageRideOffer extends AppCompatActivity {
    Button edit, delete;
    TextView contentView;
    UserSessionManager session;
    private static final String RIDE_DELETE_URL = "http://halfbloodprince.16mb.com/deleteRide.php";
    public static final String START ="start";
    public static final String DESTINATION ="destination";
    public static final String TIME ="time";
    String start, destination, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_ride_offer);

        Intent intent = getIntent();
        final String content = intent.getStringExtra(Myrides.CONTENT);

        edit = (Button) findViewById(R.id.edit);
        delete = (Button) findViewById(R.id.delete);
        contentView = (TextView) findViewById(R.id.content);
        contentView.setText(content);
        session = new UserSessionManager(getApplicationContext());
        String[] contentParts = content.split("\n");
        start = contentParts[0];
        destination = contentParts[2];
        time = contentParts[3];
        final String user = session.getUserDetails().get(UserSessionManager.KEY_EMAIL);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ManageRideOffer.this);
                alertDialog.setTitle("Delete");
                alertDialog.setMessage("Delete ride?");

                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRide(start, destination, time, user);
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

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editRide();
            }
        });

    }

    public void editRide(){
        Intent intent = new Intent(getApplicationContext(), Add.class);
        intent.putExtra(START, start);
        intent.putExtra(DESTINATION, destination);
        intent.putExtra(TIME, time);
        startActivity(intent);
        finish();
    }


    public void deleteRide(final String start, final String destination, final String time, final String user){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ManageRideOffer.this,"Please Wait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                s = s.trim();
                if (s.equalsIgnoreCase("success")){
                    Intent intent = new Intent(getApplicationContext(), Myrides.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "ride deleted", Toast.LENGTH_LONG);
                    finish();
                }else{
                    Toast.makeText(ManageRideOffer.this, "something went wrong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("start",params[0]);
                data.put("destination",params[1]);
                data.put("time",params[2]);
                data.put("user",params[3]);

                RegisterUserClass ruc = new RegisterUserClass();
                String result = ruc.sendPostRequest(RIDE_DELETE_URL,data);

                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(start,destination,time, user);
    }
}