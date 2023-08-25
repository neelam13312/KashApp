package in.games.Gameskash.Activity;

import static in.games.Gameskash.Activity.SplashActivity.msg_status;
import static in.games.Gameskash.Config.BaseUrls.CREATE_MPIN;
import static in.games.Gameskash.Config.BaseUrls.VERIFY_CREATE_MPIN;
import static in.games.Gameskash.Config.Constants.KEY_ID;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mukesh.OtpView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.games.Gameskash.Config.Module;
import in.games.Gameskash.Config.SmsReceiver;
import in.games.Gameskash.Interfaces.SmsListener;
import in.games.Gameskash.R;
import in.games.Gameskash.Util.LoadingBar;
import in.games.Gameskash.Util.SessionMangement;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_submit,btn_resend;
    TextView tv_timer;
    Module module;
    SessionMangement sessionMangement;
    LoadingBar loadingBar;
    CountDownTimer countDownTimer,cTimer ;
    OtpView et_otp;
    String mobile="",myOtp="",mpin="";
    String OTP_REGEX = "[0-9]{3,6}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);
        mobile = getIntent().getStringExtra("mobile");
        //mpin = getIntent().getStringExtra("mpin");
        initview();
//        module.generateToken();
        setCounterTimer(120000,tv_timer);
//        otp=module.getRandomKey(6);
        sendOtpforPass(mobile,"g");
    }

    private void initview() {
        module = new Module(OTPActivity.this);
        sessionMangement = new SessionMangement(OTPActivity.this);
        loadingBar = new LoadingBar(OTPActivity.this);
        tv_timer=findViewById (R.id.tv_timer);
        et_otp=findViewById (R.id.otp_view);
        btn_submit=findViewById (R.id.btn_submit);
        btn_resend=findViewById (R.id.btn_resend);

        btn_submit.setOnClickListener(this);
        btn_resend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_resend:
                sendOtpforPass(mobile, "r");
                break;
            case R.id.btn_submit:
                String otp= et_otp.getText().toString();
                if (module.checkNull(otp).equals("")){
                    et_otp.setError("Please enter otp");
                } else if (!myOtp.equals(otp)) {
                    module.errorToast(OTPActivity.this,"Invalid otp");
                }else {
                    sendOtpforPass(mobile, "v");
                }
                break;

        }
    }
    public void  setCounterTimer(long diff,final TextView txt_timer)
    {
        txt_timer.setTextColor(getResources().getColor(R.color.red));

        if(cTimer!=null){
            cTimer.cancel ();
        }
        cTimer = new CountDownTimer(diff, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), " %02d : %02d : %02d ",

                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60, TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                txt_timer.setText(text);


            }

            @Override
            public void onFinish() {
                //otp="";
                myOtp="";
                et_otp.setText("");
                txt_timer.setText("Timeout");
                txt_timer.setTextColor(getResources().getColor(R.color.white));
                if(btn_resend.getVisibility() == View.GONE)
                {
                    btn_resend.setVisibility(View.VISIBLE);
                }

            }
        }.start();

    }
    private void sendOtpforPass(String mobile, String type ) {
        loadingBar.show();
        HashMap<String, String> params=new HashMap<>();
        params.put("mobile",mobile);
        if (type.equals("v")){
            params.put("otp",et_otp.getText().toString());
        }
        if(!et_otp.getText ().toString().isEmpty ()){
            et_otp.getText ().clear ();
        }
        Log.e ( "sendOtpforPass: ",params.toString () );
        module.postRequest (VERIFY_CREATE_MPIN, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                loadingBar.dismiss();
                Log.e ( "forgotMpin",response.toString() );
                loadingBar.dismiss();
                JSONObject jsonObject= null;
                try
                {
                    jsonObject = new JSONObject (String.valueOf (response));
                    String res= jsonObject.getString ("status");

                    if(res.equalsIgnoreCase("success")) {
                        try {
                            myOtp = jsonObject.getString("otp");
                        }catch (Exception exception){
                            exception.printStackTrace();
                        }
                        Log.d ("type", "onResponse: "+type);
                        if (type.equals("v")) {
                            updatePassword(mobile, mpin,CREATE_MPIN);
                        }
                        else {
//                            String otp = jsonObject.getString("otp");
                            btn_submit.setVisibility(View.VISIBLE);
                            if (msg_status.equals("0")) {
                                if (countDownTimer != null) {
                                    countDownTimer.cancel();
                                }
                                countDownTimer = new CountDownTimer(5000, 1000) {
                                    @Override
                                    public void onTick(long l) {

                                    }

                                    @Override
                                    public void onFinish() {
//                                       et_otp.setOtp (otp);
                                        Log.e("sdfrgt", myOtp);
                                        et_otp.setText(myOtp);
//                                        myOtp = otp;
                                    }
                                }.start();

                            } else {
                                getSmsOtp();
                            }
                            setCounterTimer(120000, tv_timer);
                        }
                        module.successToast(OTPActivity.this,jsonObject.getString("message"));
                        loadingBar.dismiss();
                    }
                    else
                    {

                       module.errorToast (OTPActivity.this,jsonObject.getString("message"));
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    module.showToast ("Something went wrong");
                }
            }

        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                error.printStackTrace();
                module.showVolleyError(error);
            }
        });
    }
    public void getSmsOtp()
    {
        try
        {
            SmsReceiver.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String messageText) {
                    Log.e("Message",messageText);
                    Pattern pattern = Pattern.compile(OTP_REGEX);
                    Matcher matcher = pattern.matcher(messageText);
                    String otp="";
                    while (matcher.find())
                    {
                        otp = matcher.group();
                    }

                    if(!(otp.isEmpty() || otp.equals("")))
                    {
                        et_otp.setText (otp);
                    }
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void updatePassword(String mobile, String npass, String url) {
        loadingBar.show();
        HashMap<String, String> params=new HashMap<>();
        params.put("mpin",npass);
        params.put("mobile",mobile);
        params.put("user_id",sessionMangement.getUserDetails().get(KEY_ID));

        module.postRequest (url, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {

                Log.e("ChangeMPINPassword",response.toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject (String.valueOf (response));
                    String res = jsonObject.getString ("status");

                    if (res.equalsIgnoreCase ("success")) {
                        loadingBar.dismiss();
//                        module.successToast(jsonObject.getString("message"));
                        Intent intent = new Intent(OTPActivity.this,NewLoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        loadingBar.dismiss();
                       module.errorToast (OTPActivity.this,jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    loadingBar.dismiss();
                    e.printStackTrace ( );

                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                module.showVolleyError(error);
                loadingBar.dismiss();
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}