package media.apis.android.example.packagecom.blue_bus;

/**
 * Created by Ali on 11/03/2016.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class UserProile extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        textView = (TextView) findViewById(R.id.textViewUserName);

        Intent intent = getIntent();

        String username = intent.getStringExtra(Login.USER_NAME);

        textView.setText("Welcome User "+username);
    }

}
