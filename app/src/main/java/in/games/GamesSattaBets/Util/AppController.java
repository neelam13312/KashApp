package in.games.GamesSattaBets.Util;

import static in.games.GamesSattaBets.Config.Constants.CHANNEL_ID;
import static kotlin.random.RandomKt.Random;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import android.widget.RemoteViews;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import in.games.GamesSattaBets.Activity.SplashActivity;
import in.games.GamesSattaBets.R;


public class AppController extends Application {
    public static final String TAG = in.games.GamesSattaBets.Util.AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;

    private static in.games.GamesSattaBets.Util.AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.WARN);


        OneSignal.startInit(this)
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
                .setNotificationOpenedHandler(new NotificationClickHandler(mInstance))
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.None)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        OneSignal.setSubscription(true);
        cresteNotificationChannel();
        EmojiManager.install(new GoogleEmojiProvider());
    }

    public static synchronized in.games.GamesSattaBets.Util.AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public void cresteNotificationChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"My Notification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            manager.deleteNotificationChannel("restored_OS_notifications");
            manager.deleteNotificationChannel("fcm_fallback_notification_channel");

        }
    }

    public class ExampleNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void notificationReceived(OSNotification notification) {
            int color = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
            JSONObject data = notification.payload.additionalData;
            String notificationID = notification.payload.notificationID;
            String title =String.valueOf(HtmlCompat.fromHtml("<font color=\"" + color + "\">" + notification.payload.title + "</font>", HtmlCompat.FROM_HTML_MODE_LEGACY));
            String body = notification.payload.body;
            String rawPayload = notification.payload.rawPayload;

            String customKey;

            Log.e("ASDfgrtadsefr234",title+"++"+body+"++"+rawPayload);


            Log.i("OneSignalExample43545", "NotificationID received: " + notificationID);

            if (data != null) {
                customKey = data.optString("customkey", null);
                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: " + customKey);
            }

            showNotification(title,body,rawPayload);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNotification(String title1, String body, String rawPayload) {
        String title = title1;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");
        LocalDateTime now = LocalDateTime.now();
        String time = dtf.format(now);
        String alert = body;
        String type="";
        try {
            JSONObject object1= new JSONObject(rawPayload);
            String custom = object1.getString("custom");
            JSONObject object = new JSONObject(custom);
            JSONObject obj = object.getJSONObject("a");
            type = obj.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
            type="1";
        }
        try {
            @SuppressLint("RemoteViewLayout") RemoteViews notificationLayout = null;
            if (type.equals("2")) {
                notificationLayout = new RemoteViews(getPackageName(), R.layout.row_push_notification);
                notificationLayout.setTextViewText(R.id.tv_name, Html.fromHtml(title));
                notificationLayout.setTextViewText(R.id.tv_result, Html.fromHtml(alert));
                notificationLayout.setTextViewText(R.id.tv_time, time);
                notificationLayout.setImageViewResource(R.id.img_time, R.drawable.card);
            }
            else {
                Log.e("erftg", type);
                notificationLayout = new RemoteViews(getPackageName(), R.layout.push_notification_two);
                notificationLayout.setTextViewText(R.id.tv_name, Html.fromHtml(title));
                notificationLayout.setTextViewText(R.id.tv_result, Html.fromHtml(alert));


            }
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
//you can use your launcher Activity insted of SplashActivity, But if the Activity you used here is not launcher Activty than its not work when App is in background.
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//Add Any key-value to pass extras to intent
//            intent.putExtra("pushnotification", "yes");
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Notification customNotification;
            if (type.equals("2")) {
                 customNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.card)
                        .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                        .setCustomContentView(notificationLayout)
                        .setCustomBigContentView(notificationLayout)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setGroup("GROUP_KEY_EMAILS")
                        .setGroupSummary(true)
                        .build();
            }else {
                 customNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.card)
                        .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                        .setCustomContentView(notificationLayout)
                        .setCustomBigContentView(notificationLayout)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .build();
            }



            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);

            int NOTI_ID= Random(System.currentTimeMillis()).nextInt(1000);
            managerCompat.notify(NOTI_ID, customNotification);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.getNotificationChannels();
                manager.deleteNotificationChannel("restored_OS_notifications");
                manager.deleteNotificationChannel("fcm_fallback_notification_channel");
//                manager.cancel(NOTI_ID);
                Log.e("getNotificationChannels", String.valueOf(manager.getNotificationChannels()));
//         Channel ids
//         1. fcm_fallback_notification_channel
//         2. restored_OS_notifications
//         3. exampleChannel
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


}

