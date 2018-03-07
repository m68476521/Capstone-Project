package com.m68476521.mymexico.fcm;

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
import com.m68476521.mymexico.MainActivity;
import com.m68476521.mymexico.R;
import com.m68476521.mymexico.data.TaskContract;

import java.net.URL;
import java.util.Map;

/**
 * It contains an overridden method onMessageReceived(RemoteMessage remoteMessage).
 * If you want foregrounded (still running) apps to receive notification messages or data messages,
 * you’ll need to write code to handle the onMessageReceived callback
 */

public class MyMexicoFirebaseMessageService extends FirebaseMessagingService {

    private static final String JSON_KEY_QUESTION = TaskContract.TaskEntry.COLUMN_FCM_QUESTION;
    private static final String JSON_KEY_ANSWER = TaskContract.TaskEntry.COLUMN_FCM_ANSWER;
    private static final String JSON_KEY_FAKE_A = TaskContract.TaskEntry.COLUMN_FCM_FAKE_ANS_A;
    private static final String JSON_KEY_FAKE_B = TaskContract.TaskEntry.COLUMN_FCM_FAKE_ANS_B;
    private static final String JSON_KEY_HINT = TaskContract.TaskEntry.COLUMN_FCM_HINT;

    private static final int NOTIFICATION_MAX_CHARACTERS = 30;
    private static final String LOG_TAG = MyMexicoFirebaseMessageService.class.getSimpleName();

    private static Map<String, String> data;
    private static Context context;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with FCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options\

        // The Squawk server always sends just *data* messages, meaning that onMessageReceived when
        // the app is both in the foreground AND the background
        context = getApplicationContext();

        Log.d(LOG_TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.

        Map<String, String> data = remoteMessage.getData();

        if (data.size() > 0) {
            Log.d(LOG_TAG, "Message data payload: " + data);

            // Send a notification that you got a new message
            sendNotification(data);
            insertTrick(data);
        }
    }

    /**
     * Inserts a single squawk into the database;
     *
     * @param data Map which has the message data in it
     */
    private void insertTrick(final Map<String, String> data) {
        // Database operations should not be done on the main thread
        this.data = data;
        URL url = null;
        context = getApplicationContext();
        new InsertTrickSync().execute(url);
    }

    /**
     * Create and show a simple notification containing the received FCM message
     *
     * @param data Map which has the message data in it
     */
    private void sendNotification(Map<String, String> data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Create the pending intent to launch the activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String author = data.get(JSON_KEY_QUESTION);
        String message = data.get(JSON_KEY_HINT);

        // If the message is longer than the max number of characters we want in our
        // notification, truncate it and add the unicode character for ellipsis
        if (message.length() > NOTIFICATION_MAX_CHARACTERS) {
            message = message.substring(0, NOTIFICATION_MAX_CHARACTERS) + "\u2026";
        }

        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                //TODO change the icon
                .setSmallIcon(R.drawable.ic_m)
                .setContentTitle(String.format(getString(R.string.notification_message), author))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private static class InsertTrickSync extends AsyncTask<URL, Void,
            String> {

        @Override
        protected String doInBackground(URL... voids) {
            ContentValues newMessage = new ContentValues();
            newMessage.put(TaskContract.TaskEntry.COLUMN_ID, data.get(TaskContract.TaskEntry.COLUMN_ID));
            newMessage.put(TaskContract.TaskEntry.COLUMN_FCM_QUESTION, data.get(JSON_KEY_QUESTION));
            newMessage.put(TaskContract.TaskEntry.COLUMN_FCM_ANSWER, data.get(JSON_KEY_ANSWER));
            newMessage.put(TaskContract.TaskEntry.COLUMN_FCM_FAKE_ANS_A, data.get(JSON_KEY_FAKE_A));
            newMessage.put(TaskContract.TaskEntry.COLUMN_FCM_FAKE_ANS_B, data.get(JSON_KEY_FAKE_B));
            newMessage.put(TaskContract.TaskEntry.COLUMN_FCM_HINT, data.get(JSON_KEY_HINT));
            context.getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI_TRICKS, newMessage);
            return null;
        }
    }
}