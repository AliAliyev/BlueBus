package media.apis.android.example.packagecom.blue_bus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Ali on 27/04/2016.
 */
public class EditProfile extends AppCompatActivity {
    Button edit;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account);

        AutoCompleteTextView name = (AutoCompleteTextView) findViewById(R.id.FirstName);
        name.setText(UserProfile.NAME);

        AutoCompleteTextView surname = (AutoCompleteTextView) findViewById(R.id.FamilyName);
        surname.setText(UserProfile.SURNAME);

        AutoCompleteTextView age = (AutoCompleteTextView) findViewById(R.id.age);
        age.setText(UserProfile.AGE);

        edit = (Button) findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditProfile.this, "profile updated", Toast.LENGTH_LONG).show();
            }
        });
    }
}
