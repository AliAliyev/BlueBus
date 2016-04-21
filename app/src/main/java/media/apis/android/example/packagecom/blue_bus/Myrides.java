package media.apis.android.example.packagecom.blue_bus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ali on 20/04/2016.
 */
public class Myrides extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    UserSessionManager session;

    RideAdapter rideAdapter;
    ArrayList<RideItem> myListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrides);

        myListItems  = new ArrayList<RideItem>();

        populateListView();

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

        if (session.isUserLoggedIn())
            text.setText(session.getUserDetails().get(UserSessionManager.KEY_EMAIL));

        if (session.isUserLoggedIn()) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer);
        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_log_drawer);
        }
    }

    private void populateListView() {
        rideAdapter = new RideAdapter (Myrides.this, 0, myListItems);
        ListView listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(rideAdapter);
    }

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
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Myrides.this);
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
