package com.gravity.chatme.app.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.gravity.chatme.R;
import com.gravity.chatme.app.ChatApplication;
import com.gravity.chatme.app.ui.signing.GoogleSignInActivity;

import java.util.concurrent.ExecutionException;

public class FCMService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (ChatApplication.isInBackground) {

            Intent intent = new Intent(this, GoogleSignInActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            String body = remoteMessage.getData().get("body");
            String title = remoteMessage.getData().get("title");
            String imageUrl = remoteMessage.getData().get("icon");

            Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification);

            try {
                Bitmap bitmap = Glide
                        .with(this)
                        .load(imageUrl)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(100, 100).get();

                NotificationCompat.Builder notificationBuilder =
                        (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                                .setLargeIcon(bitmap)
                                .setSmallIcon(R.mipmap.ic_launcher_round)
                                .setContentTitle(title)
                                .setContentText(body)
                                .setContentIntent(pendingIntent)
                                .setVibrate(new long[]{1000, 1000, 1000, 1000})
                                .setSound(sound)
                                .setAutoCancel(true);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notificationBuilder.build());

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

    }
}
