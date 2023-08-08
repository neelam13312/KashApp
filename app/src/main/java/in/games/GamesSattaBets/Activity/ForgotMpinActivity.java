package in.games.GamesSattaBets.Activity;

import static in.games.GamesSattaBets.Activity.ForgetActivity.OTP_REGEX;
import static in.games.GamesSattaBets.Config.BaseUrls.FORGOT_MPIN;
import static in.games.GamesSattaBets.Config.Constants.KEY_MOBILE;

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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Config.SmsReceiver;
import in.games.GamesSattaBets.Interfaces.GetAppSettingData;
import in.games.GamesSattaBets.Interfaces.SmsListener;
import in.games.GamesSattaBets.Model.IndexResponse;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.SessionMangement;

public class ForgotMpinActivity extends AppCompatActivity implements View.OnClickListener {
OtpView otp_view;
TextView tv_timer;
Button btn_submit,btn_resend;
CountDownTimer countDownTimer,cTimer ;
LoadingBar loadingBar;
SessionMangement sessionMangement;
Module module;
String msg_status="";
String myOtp="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_mpin);
        initView();
    }

    private void initView() {
        module = new Module(ForgotMpinActivity.this);
        loadingBar = new LoadingBar(ForgotMpinActivity.this);
        sessionMangement = new SessionMangement(ForgotMpinActivity.this);
        otp_view = findViewById(R.id.otp_view);
        tv_timer = findViewById(R.id.tv_timer);
        btn_submit = findViewById(R.id.btn_submit);
        btn_resend = findViewById(R.id.btn_resend);

        btn_resend.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        module.getConfigData(new GetAppSettingData() {
            @Override
            public void getAppSettingData(IndexResponse model) {
                msg_status = model.getMsg_status();
            }
        });
        setCounterTimer(120000,tv_timer);
        sendOtpforPass(sessionMangement.getUserDetails().get(KEY_MOBILE),"r");


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
                otp_view.setText("");
                txt_timer.setText("Timeout");
                txt_timer.setTextColor(getResources().getColor(R.color.white));
                if(btn_resend.getVisibility() == View.GONE)
                {
                    btn_submit.setVisibility (View.VISIBLE);
                    btn_resend.setVisibility(View.VISIBLE);
                }

            }
        }.start();

    }
    private void sendOtpforPass(String mobile, String type ) {
        loadingBar.show();
        HashMap<String, String> params=new HashMap<>();
        params.put("mobile",sessionMangement.getUserDetails().get(KEY_MOBILE));
        if (type.equals("v")){
            params.put("otp",otp_view.getText().toString());
        }
        Log.e ( "sendOtpforPass234567: ",params.toString () );
        module.postRequest (FORGOT_MPIN, params, new Response.Listener<String> ( ) {
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
                        Log.d ("ch_type", "onResponse: "+type);
                        try {
                             myOtp = jsonObject.getString("otp");
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        if (type.equals("v")) {
                            Intent intent = new Intent(ForgotMpinActivity.this,ChangeMpinActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else {
                        btn_submit.setVisibility(View.VISIBLE);
//                        myOtp = otp;
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
                                    otp_view.setText(myOtp);
//                                    myOtp = otp;
                                }
                            }.start();

                        } else {
                            getSmsOtp();
                        }
                        setCounterTimer(120000, tv_timer);
                    }
                        module.successToast(jsonObject.getString("message"));
                        loadingBar.dismiss();
                    }
                    else
                    {

                        module.errorToast (jsonObject.getString("message"));
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
                        otp_view.setText (otp);
                    }
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_submit:
                String etOtp = otp_view.getText().toString ();
                if(etOtp.isEmpty ()){
                    otp_view.setError ("Otp Required");
                    otp_view.requestFocus ();

                }
                else if(etOtp.length()<4)
                {
                    module.errorToast ("OTP is too short");
                    otp_view.requestFocus();
                }
                else {
                    Log.e("dfghjk",etOtp+"+++"+myOtp);
                    if (etOtp.equals(myOtp)) {
                        sendOtpforPass(sessionMangement.getUserDetails().get(KEY_MOBILE),"v");
                    } else {
                        module.errorToast("Wrong Otp Entered");
                    }
                }
                break;
            case R.id.btn_resend:
                sendOtpforPass(sessionMangement.getUserDetails().get(KEY_MOBILE),"r");
                break;
        }
    }
}