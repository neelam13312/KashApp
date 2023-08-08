package in.games.GamesSattaBets.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
//import com.onesignal.OSDeviceState;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.SessionMangement;

import static in.games.GamesSattaBets.Config.BaseUrls.CREATE_MPIN;
import static in.games.GamesSattaBets.Config.BaseUrls.MPIN_LOGIN;
import static in.games.GamesSattaBets.Config.Constants.KEY_ID;
import static in.games.GamesSattaBets.Config.Constants.KEY_MOBILE;

public class MpinLoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText et_mpin;
Button btn_login;
LinearLayout lin_forgot,lin_logout;
LoadingBar loadingBar;
SessionMangement sessionMangement;
Module module;
TextView tv_forgotmpin;

    ImageView img_back;
    TextView tv_maintitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpin_login);
        initView();
        module.generateToken();
    }

    private void initView() {


        loadingBar = new LoadingBar(MpinLoginActivity.this);
        module = new Module(MpinLoginActivity.this);
        sessionMangement = new SessionMangement(MpinLoginActivity.this);
        lin_forgot = findViewById(R.id.lin_forgot);
        et_mpin = findViewById(R.id.et_mpin);
        btn_login = findViewById(R.id.btn_login);
        lin_logout = findViewById(R.id.lin_logout);
        tv_forgotmpin=findViewById(R.id.tv_forgotmpin);

        btn_login.setOnClickListener(this);
        lin_forgot.setOnClickListener(this);
        lin_logout.setOnClickListener(this);
        tv_forgotmpin.setOnClickListener(this);

        et_mpin.addTextChangedListener (new TextWatcher ( ) {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String mpin = et_mpin.getText ().toString ();
                if(et_mpin.getText ().toString ().length ()==4){
                    mpinLogin(mpin);
                }


            }
        });
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
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btn_login:
                onValidation();
                break;
            case R.id.tv_forgotmpin:
                Intent intent = new Intent(MpinLoginActivity.this,ForgotMpinActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.lin_logout:
                sessionMangement.logoutSession ();
                break;
        }

    }

    private void onValidation() {
        String mpin = et_mpin.getText ().toString ();
        if(et_mpin.getText ().toString ().isEmpty ()){
            et_mpin.setError ("MPIN Required");
            et_mpin.requestFocus ();
        }else if (mpin.length()!=4)
        {
            et_mpin.setError ("4 Digit MPIN Required");
            et_mpin.requestFocus ();
        } else {

            mpinLogin(mpin);
        }
    }

    private void mpinLogin(final String mpin) {
        loadingBar.show();

        HashMap<String, String> params = new HashMap<>();
        params.put("mpin",mpin);
        params.put("imei",sessionMangement.getDeviceId());
//        params.put("user_id", sessionMangement.getUserDetails().get(KEY_ID));
        params.put("mobile",sessionMangement.getUserDetails().get(KEY_MOBILE));
        params.put("token",sessionMangement.getToken());
        params.put("mobile_name",getDeviceName());


        module.postRequest(MPIN_LOGIN, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("create_mpin",response.toString());
                loadingBar.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("success"))
                    {
                        Intent i = new Intent(MpinLoginActivity.this,MainActivity.class);
                        startActivity(i);
                    }else {
                        module.errorToast(object.getString("data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                module.showVolleyError(error);
            }
        });
    }

    private void getMPINData(final String mpin, EditText et_mpin) {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("mpin",mpin);
        params.put("user_id", sessionMangement.getUserDetails().get(KEY_ID));
        params.put("mobile",sessionMangement.getUserDetails().get(KEY_MOBILE));

        module.postRequest(CREATE_MPIN, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("create_mpin",response.toString());
                loadingBar.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("success"))
                    {
                        module.successToast(object.getString("message"));
//                        sessionMangement.updateMpin(mpin);
                    }else {
                        module.errorToast("Something went wrong");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                module.showVolleyError(error);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}