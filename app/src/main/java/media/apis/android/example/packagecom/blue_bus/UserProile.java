package media.apis.android.example.packagecom.blue_bus;

/**
 * Created by Ali on 11/03/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class UserProile extends AppCompatActivity {

    // User Session Manager Class
    UserSessionManager session;

    // Button Logout
    Button btnLogout;

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Session class instance
        session = new UserSessionManager(getApplicationContext());

        // Button logout
        btnLogout = (Button) findViewById(R.id.button);

        textView = (TextView) findViewById(R.id.textViewUserName);

        // Check user login (this is the important point)
        // If User is not logged in , This will redirect user to LoginActivity
        // and finish current activity from activity stack.
        if(session.checkLogin())
            finish();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // get name
        String name = user.get(UserSessionManager.KEY_NAME);

        // get email
        String email = user.get(UserSessionManager.KEY_EMAIL);

        // Show user data on activity
     //   lblName.setText(Html.fromHtml("Name: <b>" + name + "</b>"));
        textView.setText(Html.fromHtml("Email: <b>" + email + "</b>"));

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Clear the User session data
                // and redirect user to LoginActivity
                session.logoutUser();
            }
        });

        Intent intent = getIntent();

        String username = intent.getStringExtra(Login.USER_NAME);

      //  textView.setText("Welcome User "+username);
    }

}
