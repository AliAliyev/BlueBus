package media.apis.android.example.packagecom.blue_bus;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by romil_000 on 2016/3/9.
 */

public class SearchResult extends ActionBarActivity{

    private EditText startPoint;
    private EditText terminalPoint;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresult);

        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText("You are searching for a journey from Cambridge - Discovery House to Guildford - Deacon Field.", TextView.BufferType.NORMAL);
    }

}

