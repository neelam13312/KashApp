package in.games.GamesSattaBets.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import in.games.GamesSattaBets.Config.BaseUrls;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.ConnectivityReceiver;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.SessionMangement;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG=RegisterActivity.class.getSimpleName();
    EditText et_fname, et_lname, et_mobile, et_email, et_userid, et_password,et_MPIN;
    Button btn_register;
    Module module;
    LoadingBar loadingBar;
    ImageView img_back;
    TextView tv_maintitle;
    String deviceId,android_id;
    SessionMangement pref;
    String deviceIMEI="";
    SessionMangement sessionMangement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_register);
        initview ( );
        CheckPermissionAndStartIntent();
    }

    private void CheckPermissionAndStartIntent() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            //SEY SOMTHING LIKE YOU CANT ACCESS WITHOUT PERMISSION
        } else {
            doSomthing();
        }
    }

    private void doSomthing() {
        deviceIMEI = getDeviceIMEI(RegisterActivity.this);
        //andGoToYourNextStep
    }
    @SuppressLint("HardwareIds")
    public static String getDeviceIMEI(Activity activity) {

        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            else
                //deviceUniqueIdentifier = tm.getDeviceId();
                if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length())
                    deviceUniqueIdentifier = "0";
        }
        return deviceUniqueIdentifier;
    }

    public String getDeviceName() {
        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) getSystemService(Context.
                TELEPHONY_SERVICE);
        /*
         * getDeviceId() returns the unique device ID.
         * For example,the IMEI for GSM and the MEID or ESN for CDMA phones.
         */
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String id=Build.ID;
        String idd= Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);;
        String serial=Build.SERIAL;
        if (model.startsWith(manufacturer)) {
            return model;
        }
        return manufacturer + " " + model;
    }

    private void initview() {

        et_fname = findViewById (R.id.et_fname);
        et_lname = findViewById (R.id.et_lname);
        et_mobile = findViewById (R.id.et_mobile);
        et_email = findViewById (R.id.et_email);
        et_userid = findViewById (R.id.et_userid);
        et_password = findViewById (R.id.et_password);
        et_MPIN = findViewById (R.id.et_MPIN);
        et_MPIN.setText("1234");
        if (getIntent().getExtras().containsKey("mobile")){
            et_mobile.setText(getIntent().getStringExtra("mobile"));
            et_mobile.setEnabled(false);
        }

        module=new Module (RegisterActivity.this);
        pref = new SessionMangement(this);
        sessionMangement = new SessionMangement(this);

        btn_register = findViewById (R.id.btn_register);
        btn_register.setOnClickListener (this);
        loadingBar=new LoadingBar (this);
        android_id = Settings.Secure.getString(RegisterActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        pref.addDeviceId(android_id);
    }


    @Override
    public void onClick(View v) {
        String number=et_mobile.getText ().toString () ;
        if (v.getId ( ) == R.id.btn_register) {
            String phone_value=et_mobile.getText().toString().trim();

            int len=phone_value.length();

            if (et_fname.getText ( ).toString ( ).isEmpty ( )) {
                et_fname.setError ("First Name Required");
                et_fname.requestFocus ();
            }
            else if (et_lname.getText ( ).toString ( ).isEmpty ( )) {
                et_lname.setError ("Last Name Required");
                et_lname.requestFocus ();
            }
            else if (et_mobile.getText ( ).toString ( ).isEmpty ( )) {
                et_mobile.setError ("Mobile No. Required");
                et_mobile.requestFocus ();
            }

            else if (Integer.parseInt (String.valueOf (number.charAt (0))) < 6) {
                et_mobile.setError ("Invalid Mobile No.");
                et_mobile.requestFocus ( );
            }

            else if(len!=10)
            {
                et_mobile.setError ("Invalid Number");
                et_mobile.requestFocus ();
            }

            else if (et_password.getText ( ).toString ( ).isEmpty ( )) {
                et_password.setError ("Password Required");
                et_password.requestFocus ();
            }else if (et_MPIN.getText().toString().isEmpty()){
                et_MPIN.setError ("Mpin Required");
                et_MPIN.requestFocus ();
            }else if(et_MPIN.getText().toString().length()!=4){
                et_MPIN.setError ("4 Digit Mpin Required");
                et_MPIN.requestFocus ();
            }
            else
            {
                if(ConnectivityReceiver.isConnected ()){
                    register(phone_value);
                }
                else {
                    module.noInternet ();
                }

            }

        }
    }



    private void register(String phone_value) {

        loadingBar.show();
        final String fname=et_fname.getText().toString().trim();
        final String lname=et_lname.getText().toString().trim();
        final String fmobile=phone_value;
        final String fpass=et_password.getText().toString().trim();

        HashMap<String,String> params=new HashMap<>();
        params.put("key","1");
        params.put("username",fname);
        params.put("name",lname);
        params.put("mobile",fmobile);
        params.put("imei",android_id);
        params.put("password",fpass);

        params.put("device_imei",deviceIMEI);
        params.put("serial",getDeviceSerial());
        params.put("mobile_name",getDeviceName());

        params.put("mpin",et_MPIN.getText().toString());
        Log.d ("req", "register: "+params);


        module.postRequest (BaseUrls.URL_REGISTER, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    boolean resp = object.getBoolean("responce");
                    Log.e (TAG, "onResponse: "+response);
                    if (resp) {

                        loadingBar.dismiss();

                            module.successToast(object.getString("message"));
                            JSONObject obj = object.getJSONObject("userdata");
                            String id=module.checkNull(obj.getString("id").toString());
                            String name=module.checkNull(obj.getString("name").toString());
                            String username=module.checkNull(obj.getString("username").toString());
                            String mobile=module.checkNull(obj.getString("mobileno").toString());
                            String email=module.checkNull(obj.getString("email").toString());
                            String address=module.checkNull(obj.getString("address").toString());
                            String city=module.checkNull(obj.getString("city").toString());
                            String pincode=module.checkNull(obj.getString("pincode").toString());
                            String accno=module.checkNull(obj.getString("accountno").toString());
                            String bank=module.checkNull(obj.getString("bank_name").toString());
                            String ifsc=module.checkNull(obj.getString("ifsc_code").toString());
                            String holder=module.checkNull(obj.getString("account_holder_name").toString());
                            String paytm=module.checkNull(obj.getString("paytm_no").toString());
                            String tez=module.checkNull(obj.getString("tez_no").toString());
                            String phonepay=module.checkNull(obj.getString("phonepay_no").toString());
//                            String wallet=module.checkNull(object.getString("wallet").toString());
                            String dob=module.checkNull(obj.getString("dob").toString());
                            //String gender=module.checkNull(jsonObject.getString("gender").toString());
                            String p = obj.getString("password");
                            sessionMangement.createLoginSession (id,name,username,mobile,email,dob,address
                                    ,city,pincode,accno,bank,ifsc,holder,paytm,tez,phonepay,"0","");

                        sessionMangement.setIsLoginSuccess();
                        module.successToast (object.getString("message").toString());
                        Intent intent = new Intent (RegisterActivity.this, ChangeMpinActivity.class);
                        intent.putExtra("type","r");
                        intent.putExtra("mobile",fmobile);
                        startActivity (intent);
                        finish ( );

                    } else {
                        loadingBar.dismiss ();
                        module.errorToast (object.getString("error").toString());
                    }
                } catch (Exception ex) {
                    loadingBar.dismiss ();
                    ex.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                module.showVolleyError(error);
                loadingBar.dismiss ();
            }
        });


    }


        public String getDeviceSerial() {
            TelephonyManager telephonyManager;
            telephonyManager = (TelephonyManager) getSystemService(Context.
                    TELEPHONY_SERVICE);
            /*
             * getDeviceId() returns the unique device ID.
             * For example,the IMEI for GSM and the MEID or ESN for CDMA phones.
             */
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            String serial=Build.SERIAL;
            if (model.startsWith(manufacturer)) {
                return model;
            }
            return serial;
        }


}