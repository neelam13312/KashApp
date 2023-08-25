package in.games.gameskash.Util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import in.games.gameskash.Activity.SplashActivity;

public class NotificationClickHandler implements OneSignal.NotificationOpenedHandler {
    Context context2;

    public NotificationClickHandler(Context context) {
        context2 = context;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        try
        {
            Intent intent=new Intent(context2, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context2.startActivity(intent);

        }
        catch ( Exception ex)
        {
            ex.printStackTrace();
        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken) {
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
        }
    }
}
