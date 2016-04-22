package media.apis.android.example.packagecom.blue_bus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by romil_000 on 2016/4/22.
 */
public class Notification extends AppCompatActivity {


    public void showNotification() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        System.out.println("intent created");
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.drawable.bb);
        builder.setContentTitle("Blue Bus");
        builder.setContentText("People come to your ride!");
        builder.setContentIntent(pendingIntent).getNotification();
        builder.setAutoCancel(true);
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        System.out.println("Start building");
        nm.notify(0,builder.build());
    }
}
