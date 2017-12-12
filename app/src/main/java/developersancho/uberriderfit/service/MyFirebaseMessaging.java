package developersancho.uberriderfit.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import developersancho.uberriderfit.R;


/**
 * Created by developersancho on 11.12.2017.
 */

public class MyFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification().getTitle().equals("Cancel")) {
            // because this outside of main thread, so if you want to run Toast, you need create Handler to do that
            // choose Handler from android.os
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MyFirebaseMessaging.this, "" + remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (remoteMessage.getNotification().getTitle().equals("Arrived")) {
            showArrivedNotification(remoteMessage.getNotification().getBody());
        }


    }

    private void showArrivedNotification(String body) {
        // this code only work for android api 26 below
        // from android api 26 or higher, you need create Notification Channel
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(),
                0, new Intent(), PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Arrived")
                .setContentText(body)
                .setContentIntent(contentIntent);
        NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }
}
