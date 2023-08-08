package in.games.GamesSattaBets.Util;

import static in.games.GamesSattaBets.Config.Constants.END_NUMBER;
import static in.games.GamesSattaBets.Config.Constants.IS_LOGIN_SUCCESS;
import static in.games.GamesSattaBets.Config.Constants.NUMBER;
import static in.games.GamesSattaBets.Config.Constants.NextDay;
import static in.games.GamesSattaBets.Config.Constants.NextToNextDay;
import static in.games.GamesSattaBets.Config.Constants.SESSION_LOGOUT_TIME;
import static in.games.GamesSattaBets.Config.Constants.START_NUMBER;
import static in.games.GamesSattaBets.Config.Constants.STATUS;
import static in.games.GamesSattaBets.Config.Constants.TYPE;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

import in.games.GamesSattaBets.Config.Constants;

public class SessionMangement {

    private static final String DEVICE_ID = " ";
    private static final String DEVICE_TOKEN = "device_token";
    SharedPreferences prefs;

    SharedPreferences.Editor editor;

    Context context;

    int PRIVATE_MODE = 0;

    public SessionMangement(Context context)
    {
        this.context = context;
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    public void createLoginSession(String id, String name, String username
            , String mobile, String email,String dob, String address, String city, String pincode, String accountno,
                                   String bank_name, String ifsc, String holder, String paytm, String tez, String phonepay,
                                   String wallet,String branch) {

        editor.putBoolean(Constants.IS_LOGIN, true);
        editor.putString(Constants.KEY_ID, id);
        editor.putString(Constants.KEY_NAME, name);
        editor.putString(Constants.KEY_USER_NAME, username);
        editor.putString(Constants.KEY_MOBILE, mobile);
        editor.putString(Constants.KEY_EMAIL, email);
        editor.putString(Constants.KEY_DOB, dob);
        editor.putString(Constants.KEY_ADDRESS, address);
        editor.putString(Constants.KEY_CITY, city);
        editor.putString(Constants.KEY_PINCODE, pincode);
        editor.putString(Constants.KEY_ACCOUNNO, accountno);
        editor.putString(Constants.KEY_BANK_NAME, bank_name);
        editor.putString(Constants.KEY_IFSC, ifsc);
        editor.putString(Constants.KEY_HOLDER, holder);
        editor.putString(Constants.KEY_PAYTM, paytm);
        editor.putString(Constants.KEY_TEZ, tez);
        editor.putString(Constants.KEY_PHONEPAY, phonepay);
        editor.putString(Constants.KEY_WALLET, wallet);
        editor.putString (Constants.KEY_BRANCH,branch);
//        editor.putString(Constants.KEY_MPIN,"");
        editor.putBoolean(Constants.KEY_DIALOG, false);

        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(Constants.KEY_ID, prefs.getString(Constants.KEY_ID, ""));
        user.put(Constants.KEY_NAME, prefs.getString(Constants.KEY_NAME, ""));
        user.put(Constants.KEY_USER_NAME, prefs.getString(Constants.KEY_USER_NAME, ""));
        user.put(Constants.KEY_MOBILE, prefs.getString(Constants.KEY_MOBILE, ""));
        user.put(Constants.KEY_EMAIL, prefs.getString(Constants.KEY_EMAIL, ""));
        user.put(Constants.KEY_DOB, prefs.getString(Constants.KEY_DOB, ""));
        user.put(Constants.KEY_ADDRESS, prefs.getString(Constants.KEY_ADDRESS, ""));
        user.put(Constants.KEY_CITY, prefs.getString(Constants.KEY_CITY, ""));
        user.put(Constants.KEY_PINCODE, prefs.getString(Constants.KEY_PINCODE, ""));
        user.put(Constants.KEY_ACCOUNNO, prefs.getString(Constants.KEY_ACCOUNNO, ""));
        user.put(Constants.KEY_BANK_NAME, prefs.getString(Constants.KEY_BANK_NAME, ""));
        user.put(Constants.KEY_IFSC, prefs.getString(Constants.KEY_IFSC, ""));
        user.put(Constants.KEY_HOLDER, prefs.getString(Constants.KEY_HOLDER, ""));
        user.put(Constants.KEY_PAYTM, prefs.getString(Constants.KEY_PAYTM, ""));
        user.put(Constants.KEY_TEZ, prefs.getString(Constants.KEY_TEZ, ""));
        user.put(Constants.KEY_PHONEPAY, prefs.getString(Constants.KEY_PHONEPAY, ""));
        user.put(Constants.KEY_WALLET, prefs.getString(Constants.KEY_WALLET, ""));
        user.put(Constants.KEY_WALLET, prefs.getString(Constants.KEY_WALLET, ""));
        user.put(Constants.KEY_BRANCH, prefs.getString(Constants.KEY_BRANCH, ""));
//        user.put(Constants.KEY_MPIN, prefs.getString(Constants.KEY_MPIN, ""));

        return user;
    }

    public void setIsLoginSuccess() {
        editor.putBoolean(IS_LOGIN_SUCCESS,true);
        editor.commit();
    }


    public boolean isLoggedInSuccess() {
        return prefs.getBoolean(Constants.IS_LOGIN_SUCCESS, false);
    }

    public void addDeviceId(String android_id) {
        editor.putString(DEVICE_ID,android_id);
        editor.commit();
    }

    public String getDeviceId() {
        return prefs.getString(DEVICE_ID,"");
    }

    public void addToken(String token) {
        editor.putString(DEVICE_TOKEN,token);
        editor.commit();
    }

    public String getToken() {
        return prefs.getString(DEVICE_TOKEN,"");
    }

    public void setSessionLogouttime(String sessionLogouttime ) {
        editor.putString(SESSION_LOGOUT_TIME,sessionLogouttime);
        editor.commit();
    }
    public void setnumValue(String start,String num,String end,String type ,String status,String next,String nextToNext) {
        editor.putString(START_NUMBER,start);
        editor.putString(NUMBER,num);
        editor.putString(END_NUMBER,end);
        editor.putString(TYPE,type);
        editor.putString(STATUS,status);
        editor.putString(NextDay,next);
        editor.putString(NextToNextDay,nextToNext);
        editor.commit();
    }
    public HashMap<String, String> getnum() {
        HashMap<String, String> numbers = new HashMap<String, String>();

        numbers.put(Constants.START_NUMBER, prefs.getString(Constants.START_NUMBER, ""));
        numbers.put(Constants.NUMBER, prefs.getString(Constants.NUMBER, ""));
        numbers.put(Constants.END_NUMBER, prefs.getString(Constants.END_NUMBER, ""));
        numbers.put(Constants.TYPE, prefs.getString(Constants.TYPE, ""));
        numbers.put(Constants.STATUS, prefs.getString(Constants.STATUS, ""));
        numbers.put(NextDay, prefs.getString(NextDay, ""));
        numbers.put(NextToNextDay, prefs.getString(NextToNextDay, ""));

        return numbers;
    }

    public String getSessionLogouttime() {
        return prefs.getString(SESSION_LOGOUT_TIME,"");
    }

    public void updateAccSection(String acc_no, String bank_name, String ifsc, String holder,String branch)
    {
        editor.putString(Constants.KEY_ACCOUNNO, acc_no);
        editor.putString(Constants.KEY_BANK_NAME, bank_name);
        editor.putString(Constants.KEY_IFSC, ifsc);
        editor.putString(Constants.KEY_HOLDER, holder);
        editor.putString (Constants.KEY_BRANCH,branch);
        editor.apply();
    }

    public void updateWallet(String wallet)
    {
        editor.putString(Constants.KEY_WALLET, wallet);
        editor.apply();
    }

    public void updateDilogStatus(boolean flag)
    {
        editor.putBoolean(Constants.KEY_DIALOG, flag);
        editor.apply();
    }

    public void logoutSession() {
        editor.clear();
        editor.commit();
    }
    public boolean isLoggedIn() {
        return prefs.getBoolean(Constants.IS_LOGIN, false);
    }

}
