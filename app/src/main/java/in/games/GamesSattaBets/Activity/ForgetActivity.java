package in.games.GamesSattaBets.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mukesh.OtpView;
//import com.onesignal.OSDeviceState;

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

import static in.games.GamesSattaBets.Config.BaseUrls.URL_GENERATE_OTP;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_VERIFY_REGISTRATION;
import static in.games.GamesSattaBets.Config.Constants.KEY_MOBILE;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG=ForgetActivity.class.getSimpleName();
EditText et_number;
OtpView et_otp;
Button btn_mobile,btn_otp,btn_resend;
SessionMangement sessionMangement;
Module module;
ImageView img_back;
TextView tv_maintitle;
LoadingBar loadingBar;
TextView tv_msg,tv_email,tv_timer;
String myOtp="";
public static final String OTP_REGEX = "[0-9]{3,6}";
CountDownTimer countDownTimer,cTimer ;
LinearLayout lin_otp,lin_mobile;
String otp="",mobile="",type="",redirectPage="" ;
String admin_email,message,msg_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_forget);
        initview();
        module.generateToken();
        if (type.equalsIgnoreCase("f")){
            lin_mobile.setVisibility(View.GONE);
            setCounterTimer(120000,tv_timer);
            otp=module.getRandomKey(4);
            sendOtpforPass(mobile, otp, URL_GENERATE_OTP);
        }else {
            lin_mobile.setVisibility(View.VISIBLE);
        }
    }

    private void initview() {
        sessionMangement = new SessionMangement(ForgetActivity.this);
        et_number=findViewById (R.id.et_number);
        et_otp=findViewById (R.id.et_otp);
        btn_mobile=findViewById (R.id.btn_mobile);
        btn_mobile.setOnClickListener (this);
        btn_otp=findViewById (R.id.btn_otp);
        btn_resend=findViewById (R.id.btn_resend);
        lin_mobile=findViewById (R.id.lin_mobile);
        lin_otp=findViewById (R.id.lin_otp);
        tv_email=findViewById (R.id.tv_email);
        tv_msg=findViewById (R.id.tv_msg);
        tv_timer=findViewById (R.id.tv_timer);

        btn_otp.setOnClickListener (this);
        btn_resend.setOnClickListener (this);
        loadingBar=new LoadingBar (this);
        type=getIntent().getStringExtra("type");
        module=new Module (ForgetActivity.this);
        module.getConfigData(new GetAppSettingData() {
            @Override
            public void getAppSettingData(IndexResponse model) {
                admin_email = model.getAdm_email();
                message = model.getMessage();
                msg_status = model.getMsg_status();
                tv_email.setText ("Email : "+admin_email);
                tv_msg.setText (message);
                tv_msg.setVisibility (View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        mobile=et_number.getText ().toString ();
        otp=module.getRandomKey(4);
        if(v.getId ()==R.id.btn_mobile){


            if(et_number.getText ().toString ().isEmpty ()){
                et_number.setError ("Mobile No. Required");
                et_number.requestFocus ();

            }
            else if (mobile.length()!=10) {
                et_number.setError ("Invalid Mobile No.");
                et_number.requestFocus ( );
            }
            else if (Integer.parseInt (String.valueOf (mobile.charAt (0))) < 6) {
                et_number.setError ("Invalid Mobile No.");
                et_number.requestFocus ( );
            }
            else {
                otp=module.getRandomKey(4);
                if (type.equalsIgnoreCase("f")) {
                    sendOtpforPass(mobile, otp, URL_GENERATE_OTP);
                } else {
//                    sendOtpforPass(mobile, otp, URL_VERIFICATION);
                    sendOtpforRegistration(mobile, URL_VERIFY_REGISTRATION,"c");
                }
            }
        }
        else if(v.getId ()==R.id.btn_resend){

            otp=module.getRandomKey(4);
            if (type.equalsIgnoreCase("f")) {
                sendOtpforPass(mobile, otp, URL_GENERATE_OTP);
            } else {
                sendOtpforRegistration(mobile, URL_VERIFY_REGISTRATION,"r");
            }

        }
       else if(v.getId ()==R.id.btn_otp){

           String etOtp = et_otp.getText().toString ();
            if(etOtp.isEmpty ()){
                et_otp.setError ("Otp Required");
                et_otp.requestFocus ();

            }
            else if(etOtp.length()<4)
            {
                module.errorToast ("OTP is too short");
                et_otp.requestFocus();
            }
            else {
                Log.e("fghjkl",   myOtp+"----"+etOtp);
                if (etOtp.equals(myOtp)) {

                    if (type.equalsIgnoreCase("r")){
                        sendOtpforRegistration(et_number.getText().toString(),URL_VERIFY_REGISTRATION,"v");
                    } else {
                        Intent intent = new Intent(ForgetActivity.this, PasschangeActivity.class);
                        intent.putExtra("mobile", sessionMangement.getUserDetails().get(KEY_MOBILE));
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        finish();
                    }
                }
                else
                {
                    module.errorToast("Wrong Otp Entered");
                }

            }
        }
    }

    private void sendOtpforPass(String mobile,  String otp,String url) {
        loadingBar.show();
        HashMap<String, String> params=new HashMap<>();
        if (type.equalsIgnoreCase("r")){
            params.put("mobile", et_number.getText().toString());
        }else {
            params.put("mobile", sessionMangement.getUserDetails().get(KEY_MOBILE));
            params.put("otp", otp);
        }
        if(!otp.isEmpty ()){
            et_otp.getText ().clear ();
        }
        Log.e ( "sendOtpforPass: ",params.toString () );
         module.postRequest (url, params, new Response.Listener<String> ( ) {
             @Override
             public void onResponse(String response) {
                 loadingBar.dismiss();
                 Log.e (TAG, "forrgte"+response );
                 loadingBar.dismiss();
                 JSONObject jsonObject= null;
                 try
                 {
                     jsonObject = new JSONObject (response.toString());
                     String res= jsonObject.getString ("status");

                     if(res.equalsIgnoreCase("success"))
                     {
                         try {
                             myOtp = jsonObject.getString("otp");
                         }catch (Exception ex){
                             ex.printStackTrace();
                         }
                         if (type.equalsIgnoreCase("r")){

                             Intent intent = new Intent(ForgetActivity.this,RegisterActivity.class);
                             startActivity(intent);
                         }

                         lin_otp.setVisibility(View.VISIBLE);
                         btn_otp.setVisibility (View.VISIBLE);
                         Log.e ("check_otp", "onResponse: "+msg_status+" :: "+myOtp+" :: "+mobile+" :: "+type );
//                         myOtp=otp;
                         if(msg_status.equals("0"))
                         {
//                             myOtp = otp;
                             if(countDownTimer!=null){
                                 countDownTimer.cancel ();
                             }
                             countDownTimer=new CountDownTimer (5000,1000) {
                                 @Override
                                 public void onTick(long l) {

                                 }

                                 @Override
                                 public void onFinish() {
//                                       et_otp.setOtp (otp);
                                     Log.e("sdfrgt",otp);
//                                     myOtp=otp;
                                     et_otp.setText (myOtp);
//                                     myOtp=otp;
                                 }
                             }.start ();

                         }
                         else
                         {
                             getSmsOtp();
                         }
                         lin_mobile.setVisibility(View.GONE);
                         setCounterTimer(120000,tv_timer);
                         module.successToast(jsonObject.getString("message"));
                         loadingBar.dismiss();
                     }
                     else
                     {
                         if (type.equalsIgnoreCase("r")){
                             Intent intent = new Intent(ForgetActivity.this,LoginActivity.class);
                             startActivity(intent);
                         }

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
    private void sendOtpforRegistration(String mobile,String url,String redirectType) {
        loadingBar.show();
        HashMap<String, String> params=new HashMap<>();
        params.put("mobile", et_number.getText().toString());
        if (redirectType.equals("v")){
            params.put("otp",et_otp.getText().toString());
            params.put("imei",sessionMangement.getDeviceId());
            params.put("token", sessionMangement.getToken());
        }
        if(!otp.isEmpty ()){
            et_otp.getText ().clear ();
        }
        Log.e ( "sendOtpforPass: ",params.toString () );
        module.postRequest (url, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
          Log.e("asdefghaszb",url+"-----"+params.toString());
                loadingBar.dismiss();
                Log.e (TAG, "forrgte"+response );
                loadingBar.dismiss();
                JSONObject jsonObject= null;
                try
                {
                    jsonObject = new JSONObject (response.toString());
                    boolean res= jsonObject.getBoolean ("response");

                    if(res)
                    {
                        try {
                            myOtp = jsonObject.getString("otp");
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        if (redirectType.equals("v")&&redirectPage.equalsIgnoreCase("main")){
                            module.successToast(jsonObject.getString("message"));
                            JSONObject object = jsonObject.getJSONObject("data");
                            String id=module.checkNull(object.getString("id").toString());
                            String name=module.checkNull(object.getString("name").toString());
                            String username=module.checkNull(object.getString("username").toString());
                            String mobile=module.checkNull(object.getString("mobileno").toString());
                            String email=module.checkNull(object.getString("email").toString());
                            String address=module.checkNull(object.getString("address").toString());
                            String city=module.checkNull(object.getString("city").toString());
                            String pincode=module.checkNull(object.getString("pincode").toString());
                            String accno=module.checkNull(object.getString("accountno").toString());
                            String bank=module.checkNull(object.getString("bank_name").toString());
                            String ifsc=module.checkNull(object.getString("ifsc_code").toString());
                            String holder=module.checkNull(object.getString("account_holder_name").toString());
                            String paytm=module.checkNull(object.getString("paytm_no").toString());
                            String tez=module.checkNull(object.getString("tez_no").toString());
                            String phonepay=module.checkNull(object.getString("phonepay_no").toString());
//                            String wallet=module.checkNull(object.getString("wallet").toString());
                            String dob=module.checkNull(object.getString("dob").toString());
                            //String gender=module.checkNull(jsonObject.getString("gender").toString());
                            String p = object.getString("password");
                            sessionMangement.setIsLoginSuccess();
                                sessionMangement.createLoginSession (id,name,username,mobile,email,dob,address
                                        ,city,pincode,accno,bank,ifsc,holder,paytm,tez,phonepay,"0","");

                                module.getConfigData(new GetAppSettingData() {
                                    @Override
                                    public void getAppSettingData(IndexResponse model) {

                                    }
                                });

                                Intent intent = new Intent(ForgetActivity.this, MainActivity.class);
                                intent.putExtra("username", object.getString("username").toString());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                        }
                        else if (redirectType.equals("v")&&redirectPage.equalsIgnoreCase("register")){
                            Intent intent = new Intent(ForgetActivity.this,RegisterActivity.class);
                            intent.putExtra("mobile",et_number.getText().toString());
                            startActivity(intent);
                        }

                        redirectPage="register";

                        lin_otp.setVisibility(View.VISIBLE);
                        btn_otp.setVisibility (View.VISIBLE);
                        Log.e ("check_otp", "onResponse: "+msg_status+" :: "+otp+" :: "+mobile+" :: "+type );
                        if(msg_status.equals("0"))
                        {
                            if(countDownTimer!=null){
                                countDownTimer.cancel ();
                            }
                            countDownTimer=new CountDownTimer (5000,1000) {
                                @Override
                                public void onTick(long l) {

                                }
                                @Override
                                public void onFinish() {
                                    Log.e("sdfrgt",otp);
//                                    myOtp=otp;
                                    et_otp.setText (myOtp);
                                }
                            }.start ();
                        }
                        else
                        {
                            getSmsOtp();
                        }
                        lin_mobile.setVisibility(View.GONE);
                        setCounterTimer(120000,tv_timer);
                        if (redirectType.equals("v")&&redirectPage.equalsIgnoreCase("main")){

                        }else {
                            module.successToast(jsonObject.getString("message"));
                        }
                        loadingBar.dismiss();
                    }
                    else
                    {
                        redirectPage="main";
                        try {
                            myOtp = jsonObject.getString("otp");
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        lin_otp.setVisibility(View.VISIBLE);
                        btn_otp.setVisibility (View.VISIBLE);
                        Log.e ("check_otp", "onResponse: "+msg_status+" :: "+myOtp+" :: "+mobile+" :: "+type );
                        if(msg_status.equals("0"))
                        {
                            if(countDownTimer!=null){
                                countDownTimer.cancel ();
                            }
                            countDownTimer=new CountDownTimer (5000,1000) {
                                @Override
                                public void onTick(long l) {

                                }
                                @Override
                                public void onFinish() {
                                    Log.e("sdfrgt",otp);
                                    et_otp.setText (myOtp);
                                }
                            }.start ();

                        }
                        else
                        {
                            getSmsOtp();
                        }
                        lin_mobile.setVisibility(View.GONE);
                        setCounterTimer(120000,tv_timer);
                        module.successToast(jsonObject.getString("message"));
                        loadingBar.dismiss();

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
            SmsReceiver.bindListener(new SmsListener () {
                @Override
                public void messageReceived(String messageText) {

                    //From the received text string you may do string operations to get the required OTP
                    //It depends on your SMS format
                    Log.e("Message",messageText);
                    // Toast.makeText(SmsVerificationActivity.this,"Message: "+messageText,Toast.LENGTH_LONG).show();

                    // If your OTP is six digits number, you may use the below code

                    Pattern pattern = Pattern.compile(OTP_REGEX);
                    Matcher matcher = pattern.matcher(messageText);
                    String otp="";
                    while (matcher.find())
                    {
                        otp = matcher.group();
                    }

                    if(!(otp.isEmpty() || otp.equals(""))) {
                        et_otp.setText (otp);
                    }
                }
            });
        }
        catch (Exception ex)
        {
            // Toast.makeText(SmsVerificationActivity.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
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
                {   // btn_otp.setVisibility (View.GONE);
                    btn_otp.setVisibility (View.VISIBLE);
                    btn_resend.setVisibility(View.VISIBLE);
                }

            }
        }.start();
    }
}