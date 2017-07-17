package minasedrak.squawker.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import minasedrak.squawker.MainActivity;
import minasedrak.squawker.R;
import minasedrak.squawker.provider.SquawkContract;
import minasedrak.squawker.provider.SquawkProvider;

/**
 * Created by MinaSedrak on 7/17/2017.
 */

public class SquawkFirebaseMessageService extends FirebaseMessagingService {

    private static final int NOTIFICATION_MAX_LENGTH = 30;
    private static String LOG_TAG = SquawkFirebaseMessageService.class.getSimpleName();

    // The Squawk server always sends just *data* messages, meaning that onMessageReceived when
    // the app is both in the foreground AND the background
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.i(LOG_TAG, "From: "+ remoteMessage.getFrom());

        Map<String, String> JsonData = remoteMessage.getData();


        if(JsonData.size() > 0){
            Log.i(LOG_TAG, "JSON data: " + JsonData);

            sendNotification(JsonData);
            insertSquawkToDatabase(JsonData);
        }
    }

    private void insertSquawkToDatabase(final Map<String, String> jsonData) {

        AsyncTask<Void, Void, Void> insertSquawk = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ContentValues newSquawk = new ContentValues();
                newSquawk.put(SquawkContract.COLUMN_AUTHOR_KEY, jsonData.get(SquawkContract.COLUMN_AUTHOR_KEY));
                newSquawk.put(SquawkContract.COLUMN_AUTHOR, jsonData.get(SquawkContract.COLUMN_AUTHOR));
                newSquawk.put(SquawkContract.COLUMN_DATE, jsonData.get(SquawkContract.COLUMN_DATE));
                newSquawk.put(SquawkContract.COLUMN_MESSAGE, jsonData.get(SquawkContract.COLUMN_MESSAGE).trim());
                getContentResolver().insert(SquawkProvider.SquawkMessages.CONTENT_URI, newSquawk);

                return null;
            }
        };

        insertSquawk.execute();
    }

    private void sendNotification(Map<String, String> jsonData) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Create the pending intent to launch the activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String message = jsonData.get(SquawkContract.COLUMN_MESSAGE);
        if ( message.length() > NOTIFICATION_MAX_LENGTH){
            message = message.substring(0, NOTIFICATION_MAX_LENGTH) + "…";
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(String.format(getString(R.string.notification_message), jsonData.get(SquawkContract.COLUMN_AUTHOR)))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }
}
