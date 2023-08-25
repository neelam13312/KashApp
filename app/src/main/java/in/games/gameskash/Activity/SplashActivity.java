package in.games.gameskash.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.gameskash.Config.Module;
import in.games.gameskash.R;
import in.games.gameskash.Util.ConnectivityReceiver;
import in.games.gameskash.Util.SessionMangement;
//import in.games.SattaBetsGame.R;
//import in.games.SattaBetsGame.R;

import static in.games.gameskash.Config.BaseUrls.URL_GET_STATUS;
import static in.games.gameskash.Config.BaseUrls.URL_INDEX;

public class SplashActivity extends AppCompatActivity {
    int limit=4000,startTime=5000;
    TextView tv_vername;
    String is_mpin="",is_pass="",new_app_link="";
    Module module;
    float version_code ;
    public  static CountDownTimer sessionCountDownTimer;
    public  int ver_code=0;
    SessionMangement sessionMangement;
    public static String home_text ="",message="",admin_email="",whatsapp_no="", withdraw_text="",tagline= "" ,min_add_amount= "" ,app_link="",share_link="",msg_status="",withdraw_no="",
            indexId="",indexMinAmount="",index_w_saturday="",index_w_sunday="",index_w_amount="",index_withdraw_limit="",index_min_wallet_msg="",index_device_config="",index_notice="";
    Animation animBlink;
    ImageView img_logo;
    public static int  max_bet_amount=0,min_bet_amount=0,bank_change_charge=0;
    public static final int UNINSTALL_APP = 101;
//    String oldPackageName ="in.games.SattaBetsGame";
    public static final int MY_PERMISSIONS_REQUEST_WRITE_FIELS = 102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initview();
//        89789b85-34de-4526-99ab-ca845c576dca ///appid one signal

        OneSignal.clearOneSignalNotifications();//to clear default notification

        module.generateToken();
        if (!sessionMangement.getToken().equals("")){
            startTime=0;
        }
        loginStatus();
        PackageManager pm = getApplicationContext().getPackageManager();
        String pkgName = getApplicationContext().getPackageName();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String ver = pkgInfo.versionName;
        tv_vername.setText("App Version : " +ver);

    }

    private void initview() {
        tv_vername = findViewById(R.id.tv_vername);
        module=new Module (SplashActivity.this);

        sessionMangement =new SessionMangement (SplashActivity.this);
    }


    public void getApiData() {
        HashMap<String,String> params = new HashMap<String, String> ( );
        module.getRequest (URL_INDEX, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                Log.e ("getApiData", "onResponse: " + response.toString ( ));
                try {
                    JSONObject jsonObject = new JSONObject (response);
                    Boolean result = Boolean.valueOf (jsonObject.getString ("response"));

                    if (result) {
                        JSONArray arr=jsonObject.getJSONArray("data");
                        JSONObject dataObj=arr.getJSONObject(0);

                        indexId = dataObj.getString("id");
                        indexMinAmount = dataObj.getString("min_amount");
                        index_w_saturday = dataObj.getString("w_saturday");
                        index_w_sunday = dataObj.getString("w_sunday");
                        index_w_amount = dataObj.getString("w_amount");
                        index_withdraw_limit = dataObj.getString("withdraw_limit");
                        index_min_wallet_msg = dataObj.getString("min_wallet_msg");
                        index_device_config = dataObj.getString("device_config");
                        tagline = dataObj.getString ("tag_line");
                        withdraw_text = dataObj.getString ("withdraw_text");
                        withdraw_no = dataObj.getString ("withdraw_no");
                        whatsapp_no= dataObj.getString ("mobile");
                        home_text = dataObj.getString ("home_text").toString ( );
                        min_add_amount = dataObj.getString ("min_wallet");
                        msg_status = dataObj.getString ("msg_status");
                        app_link = dataObj.getString ("app_link");
                        share_link = dataObj.getString ("share_link");
                        ver_code = Integer.parseInt (dataObj.getString ("version"));
                        message = dataObj.getString ("message");
                        index_notice = dataObj.getString("notice");
                        admin_email = dataObj.getString("adm_email");
                        max_bet_amount = Integer.parseInt(module.checkNullNumber(dataObj.getString("max_bet_amount")));
                        min_bet_amount = Integer.parseInt(module.checkNullNumber(dataObj.getString("min_bet_amt")));
                        new_app_link=dataObj.getString("new_app_link");

                        checkAppPermissions();
                        go_next();
                    } else {
                        module.errorToast (SplashActivity.this,"Something Went Wrong");
                    }
                } catch (JSONException e) {
                    e.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg = module.VolleyErrorMessage (error);
                if (!msg.isEmpty ( )) {
                    module.errorToast (SplashActivity.this,"" + msg);
                }
            }
        });
    }

    public void checkAppPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED  ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.INTERNET) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_NETWORK_STATE)) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        go_next();
                    }
                },startTime);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                android.Manifest.permission.INTERNET,
                                android.Manifest.permission.ACCESS_NETWORK_STATE
                        },
                        MY_PERMISSIONS_REQUEST_WRITE_FIELS);
            }
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    go_next();
                }
            },startTime);
        }
    }
    public void go_next() {
        module.generateToken();

        if(sessionMangement.isLoggedIn())
        {
            sessionMangement.updateDilogStatus(false);
            Intent intent = null;

            if ( sessionMangement.isLoggedInSuccess()) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                intent = new Intent(SplashActivity.this,NewLoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else
        {
            Intent intent = new Intent(SplashActivity.this,NewLoginActivity.class);
           // intent.putExtra("type","r");
            startActivity(intent);
            finish();

        }
    }

    private void getStatus()
    {
        String android_id = Settings.Secure.getString(SplashActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        HashMap<String,String> params = new HashMap<>();
        params.put("device_id",android_id);
//        params.put(VERSION_CODE, module.getAppVersion());
//        params.put(API_KEY, module.getApiKey());

        module.postRequest(URL_GET_STATUS, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("URL_GET_STATUS",response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("response")){
                        JSONArray array = object.getJSONArray("data");

                        JSONObject obj = array.getJSONObject(0);
                        is_mpin=obj.getString ("is_mpin");
                        is_pass=obj.getString ("is_password");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                module.VolleyErrorMessage (error);
            }
        });
    }


    public void loginStatus() {
//        HashMap<String, String> params = new HashMap<> ( );
//        params.put ("user_id",sessionMangement.getUserDetails ().get (KEY_ID));
//        module.postRequest (URL_GETSTATUS, params, new Response.Listener<String> ( ) {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    Log.e ("logonstatus", "onResponse: " + response);
//                    JSONObject jsonObject = new JSONObject (String.valueOf (response));
//                    jsonObject.getString ("login_status");
//                    if (jsonObject.getBoolean("responce")) {
//                        if (jsonObject.getString("login_status").equals("1")) {
//
//                            //errorToast (jsonObject.getString ("message"));
//                            Dialog dialog = new Dialog (SplashActivity.this);
//                            dialog.requestWindowFeature( Window.FEATURE_NO_TITLE);
//                            dialog.getWindow();
//                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                            dialog.getWindow().setGravity( Gravity.CENTER);
//                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable (0));
//                            dialog.show();
//                            dialog.setCancelable(false);
//                            dialog.setContentView(R.layout.dialog_block);
//                            TextView tv_block=dialog.findViewById (R.id.tv_block);
//                            tv_block.setText (jsonObject.getString ("message"));
//                            Button btn_block=dialog.findViewById (R.id.btn_block);
//                            btn_block.setOnClickListener (new View.OnClickListener ( ) {
//                                @Override
//                                public void onClick(View v) {
//                                    try {
//                                        dialog.dismiss ( );
//                                        moveTaskToBack (true);
//                                        android.os.Process.killProcess (android.os.Process.myPid ( ));
//                                        System.exit (1);
//                                    }catch (Exception e)
//                                    {
//                                        e.printStackTrace ();
//                                    }
//                                }
//                            });
//
//                        }
//                        else {
//                            Handler handler = new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
        if (ConnectivityReceiver.isConnected()) {
            getApiData();
        } else {
            module.noInternet ();
        }
//                                }
//                            },limit);
        // go_next();
//                        }
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace ( );
//                }
//            }
//        }, new Response.ErrorListener ( ) {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                module.showToast ("" + error);
//            }
//        });

    }

}