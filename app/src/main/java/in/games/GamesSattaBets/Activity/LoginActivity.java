package in.games.GamesSattaBets.Activity;

import static in.games.GamesSattaBets.Config.Constants.KEY_USER_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
//import com.onesignal.OSDeviceState;

import org.json.JSONObject;

import java.util.HashMap;

import in.games.GamesSattaBets.Config.BaseUrls;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Interfaces.GetAppSettingData;
import in.games.GamesSattaBets.Model.IndexResponse;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.ConnectivityReceiver;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.SessionMangement;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG=LoginActivity.class.getSimpleName();
TextView tv_register,tv_forget,tv_email,tv_adminnumber,tv_forgetUserName;
Button btn_login;
ImageView img_back;
TextView tv_maintitle,tv_mpin;
TextInputEditText et_number,et_pass;
Module module;
SessionMangement sessionMangement;
LoadingBar loadingBar;
String admin_email,whatsapp_no,msg_status,android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);
        initview();
        module.generateToken();
    }

    private void initview() {
        module=new Module (LoginActivity.this);
        sessionMangement=new SessionMangement (LoginActivity.this);
        tv_email=findViewById (R.id.tv_email);
        tv_mpin = findViewById(R.id.tv_mpin);
        if ( sessionMangement.isLoggedInSuccess()) {
            tv_mpin.setVisibility(View.VISIBLE);
        }else {
            tv_mpin.setVisibility(View.GONE);
        }
        tv_adminnumber=findViewById (R.id.tv_adminnumber);
        tv_register=findViewById (R.id.tv_register);

        tv_register.setOnClickListener (this);
        tv_forget=findViewById (R.id.tv_forget);
        tv_forget.setOnClickListener (this);
        btn_login=findViewById (R.id.btn_login);
        btn_login.setOnClickListener (this);
        tv_forgetUserName = findViewById(R.id.tv_forgetUserName);
        tv_forgetUserName.setOnClickListener(this);
        tv_mpin.setOnClickListener(this);
        et_number=findViewById (R.id.et_number);
        if (sessionMangement.isLoggedIn()){
            et_number.setText(sessionMangement.getUserDetails().get(KEY_USER_NAME));
        }
        et_pass=findViewById (R.id.et_pass);
        loadingBar=new LoadingBar (LoginActivity.this);
        module.getConfigData(new GetAppSettingData() {
             @Override
             public void getAppSettingData(IndexResponse model) {
                  admin_email = model.getAdm_email();
                  whatsapp_no = model.getMobile();
                  msg_status= model.getMsg_status();
                 tv_email.setText ("Email : "+admin_email);
                 tv_adminnumber.setText ("Number : "+whatsapp_no);
             }
         });
    }

    @Override
    public void onClick(View v) {
        if(v.getId ()==R.id.tv_register){
            Intent intent=new Intent ( LoginActivity.this,RegisterActivity.class );
            startActivity (intent);

        }else if (v.getId()==R.id.tv_mpin){
            Intent intent = new Intent(LoginActivity.this,MpinLoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if(v.getId ()==R.id.tv_forget){
            Log.e("status",msg_status);
            if (msg_status.equals("0"))
            {
                Intent intent=new Intent ( LoginActivity.this,ForgetActivity.class );
                intent.putExtra("type","f");
                startActivity (intent);
            }
            else {
                Intent intent=new Intent (LoginActivity.this,ForgotPasswordWhatsppActivity.class );
                startActivity (intent);
            }
        }
        else if(v.getId ()==R.id.btn_login){
            validation();

        }else if (v.getId()==R.id.tv_forgetUserName){
            module.showToast("something went wrong");
        }
    }

    private void validation() {
        String number=et_number.getText ().toString () ;
        String password=et_pass.getText ().toString () ;
        if(et_number.getText ().toString ().isEmpty ()){
            et_number.setError ("UserName Required");
            et_number.requestFocus ();

        } else if(et_pass.getText ().toString ().isEmpty ()){
            et_pass.setError ("Password Required");
            et_pass.requestFocus ();

        } else {
            if (ConnectivityReceiver.isConnected()) {
                getUserLoginRequest (number, password);
                } else {
                    module.noInternet ();
                }
        }
    }
    public String getDeviceName() {
        /*
         * getDeviceId() returns the unique device ID.
         * For example,the IMEI for GSM and the MEID or ESN for CDMA phones.
         */
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        }
        return manufacturer + " " + model;
    }

    private void getUserLoginRequest(final String mobile, final String Pass) {
        loadingBar.show ( );

        HashMap<String, String> params = new HashMap<> ( );
        params.put("imei",sessionMangement.getDeviceId());
        params.put("token", sessionMangement.getToken());
//        "atoken" = "db043646-ace0-11ec-b274-4e54eef4664f"
//       "android_id" -- "3471842a0c9f2c20"
        params.put ("username", mobile);
        params.put ("password", Pass);
        params.put("mobile_name",getDeviceName());
        Log.e("params",params.toString());
        module.postRequest (BaseUrls.URL_LOGIN, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                loadingBar.dismiss ( );
                Log.e (TAG, response.toString ( ));
                try {

                    JSONObject object = new JSONObject(response);

                    boolean resp=object.getBoolean("responce");
                    if (resp)
                    {
                        JSONObject jsonObject = object.getJSONObject("data");
                        String id=module.checkNull(jsonObject.getString("id").toString());
                        String name=module.checkNull(jsonObject.getString("name").toString());
                        String username=module.checkNull(jsonObject.getString("username").toString());
                        String mobile=module.checkNull(jsonObject.getString("mobileno").toString());
                        String email=module.checkNull(jsonObject.getString("email").toString());
                        String address=module.checkNull(jsonObject.getString("address").toString());
                        String city=module.checkNull(jsonObject.getString("city").toString());
                        String pincode=module.checkNull(jsonObject.getString("pincode").toString());
                        String accno=module.checkNull(jsonObject.getString("accountno").toString());
                        String bank=module.checkNull(jsonObject.getString("bank_name").toString());
                        String ifsc=module.checkNull(jsonObject.getString("ifsc_code").toString());
                        String holder=module.checkNull(jsonObject.getString("account_holder_name").toString());
                        String paytm=module.checkNull(jsonObject.getString("paytm_no").toString());
                        String tez=module.checkNull(jsonObject.getString("tez_no").toString());
                        String phonepay=module.checkNull(jsonObject.getString("phonepay_no").toString());
                        String wallet=module.checkNull(jsonObject.getString("wallet").toString());
                        String dob=module.checkNull(jsonObject.getString("dob").toString());
                        //String gender=module.checkNull(jsonObject.getString("gender").toString());
                        String p = jsonObject.getString("password");
                        if (Pass.equals(p)) {
                            sessionMangement.setIsLoginSuccess();
                            sessionMangement.createLoginSession (id,name,username,mobile,email,dob,address
                                    ,city,pincode,accno,bank,ifsc,holder,paytm,tez,phonepay,wallet,"");

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("username", jsonObject.getString("username").toString());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            module.errorToast ("Password is not correct ");
                        }

                    }
                    else {
                        module.errorToast (object.getString("error").toString());
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    loadingBar.dismiss();
                }

            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ( );
                String msg = module.VolleyErrorMessage (error);
                module.showToast (msg);

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}