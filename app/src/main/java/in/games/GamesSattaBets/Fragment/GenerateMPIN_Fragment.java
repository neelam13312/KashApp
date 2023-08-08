package in.games.GamesSattaBets.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.games.GamesSattaBets.Activity.ForgotMpinActivity;
import in.games.GamesSattaBets.Activity.MainActivity;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Config.SmsReceiver;
import in.games.GamesSattaBets.Interfaces.GetAppSettingData;
import in.games.GamesSattaBets.Interfaces.SmsListener;
import in.games.GamesSattaBets.Model.IndexResponse;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.SessionMangement;

import static in.games.GamesSattaBets.Activity.ForgetActivity.OTP_REGEX;
import static in.games.GamesSattaBets.Config.BaseUrls.CHANGE_MPIN;
import static in.games.GamesSattaBets.Config.BaseUrls.CREATE_MPIN;
import static in.games.GamesSattaBets.Config.BaseUrls.FORGOT_MPIN;
import static in.games.GamesSattaBets.Config.Constants.KEY_ID;
import static in.games.GamesSattaBets.Config.Constants.KEY_MOBILE;


public class GenerateMPIN_Fragment extends Fragment implements View.OnClickListener {
    RelativeLayout forgot_mpin,change_mpin;
LoadingBar loadingBar;
Module module;
String msg_status="";
CountDownTimer countDownTimer,cTimer ;
SessionMangement sessionMangement;
String myOtp="";
EditText otp_view;

    public GenerateMPIN_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_generate_m_p_i_n_, container, false);
        initview(view);
        ((MainActivity)getActivity ()).setTitles ("Genearte MPIN");
        module.getConfigData(new GetAppSettingData() {
            @Override
            public void getAppSettingData(IndexResponse model) {
                msg_status = model.getMsg_status();
            }
        });
        return view;
    }

    private void initview(View view) {
        sessionMangement = new SessionMangement(getActivity());
        loadingBar = new LoadingBar(getActivity());
        module = new Module(getActivity());
        forgot_mpin=view.findViewById (R.id.forgot_mpin);
        change_mpin=view.findViewById (R.id.change_mpin);
        forgot_mpin.setOnClickListener (this);
        change_mpin.setOnClickListener (this);

    }

    @Override
    public void onClick(View v) {
         if(v.getId ()==R.id.forgot_mpin){
            Intent intent = new Intent(getActivity(),ForgotMpinActivity.class);
            startActivity(intent);
        }

        if(v.getId ()==R.id.change_mpin){
            Dialog dialog ;
            dialog=new Dialog(getContext ());
            dialog.getWindow ().setBackgroundDrawableResource (android.R.color.transparent);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialogue_change_mpin);
            dialog.show();


            Button btn_gen = (Button) dialog.findViewById (R.id.btn_gen);
            TextView tv_request=dialog.findViewById (R.id.tv_request);

             otp_view=(EditText)dialog.findViewById (R.id.et_otp);
            EditText et_ompin=(EditText)dialog.findViewById (R.id.et_ompin);
            EditText et_nmpin=(EditText)dialog.findViewById (R.id.et_nmpin);
            EditText et_cnmpin=(EditText)dialog.findViewById (R.id.et_cnmpin);

            tv_request.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View view) {
                    sendOtpChangePass(sessionMangement.getUserDetails().get(KEY_MOBILE),"",otp_view);
//                    module.successToast("OTP Send to your register phone number");
                }
            });
            btn_gen.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    String otp = otp_view.getText ().toString ();
                    String o_mpin = et_ompin.getText ().toString ();
                    String n_mpin = et_nmpin.getText ().toString ();
                    String  cn_mpin= et_cnmpin.getText ().toString ();

                    if(otp_view.getText ().toString ().isEmpty ()){
                        otp_view.setError ("OTP Required");
                        otp_view.requestFocus ();
                    }
                    else if(et_ompin.getText ().toString ().isEmpty ()){
                        et_ompin.setError ("Old MPIN Required");
                        et_ompin.requestFocus ();
                    }
                    else if(et_nmpin.getText ().toString ().isEmpty ()){
                        et_nmpin.setError ("New MPIN Required");
                        et_nmpin.requestFocus ();
                    }
                    else if(et_nmpin.getText ().toString ().length()!=4){
                        et_nmpin.setError ("Please enter 4 digits mpin");
                        et_nmpin.requestFocus ();
                    }

                    else if(et_cnmpin.getText ().toString ().isEmpty ()){
                        et_cnmpin.setError ("Confirm New MPIN Required");
                        et_cnmpin.requestFocus ();
                    } else if(et_cnmpin.getText ().toString ().length()!=4){
                        et_cnmpin.setError ("Please enter 4 digits mpin");
                        et_cnmpin.requestFocus ();
                    }
                    else if(!n_mpin.equals (cn_mpin)){
                        et_cnmpin.setError ("MPIN Not Matched");
                        et_cnmpin.requestFocus ();
                    }

                    else {
//Just for dumy chnage api curently use foget mpin api
                        getChageMPIN(sessionMangement.getUserDetails ().get (KEY_MOBILE),otp,o_mpin,n_mpin,cn_mpin);
                        dialog.dismiss();
//                        getMPINData(mpin,et_mpin);
//                        dialog.dismiss();
                    }

                }
            });

            dialog.setCanceledOnTouchOutside (true);

        }

    }
    private void sendOtpChangePass(String mobile, String type,EditText et_otp ) {
        loadingBar.show();
        HashMap<String, String> params=new HashMap<>();
        params.put("mobile",sessionMangement.getUserDetails().get(KEY_MOBILE));
        if (type.equals("v")){
            params.put("otp",otp_view.getText().toString());
        }
        Log.e ( "sendOtpChangePass: ",params.toString () );
        module.postRequest (CHANGE_MPIN, params, new Response.Listener<String> ( ) {
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

                            String otp = jsonObject.getString("otp");

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
                                        Log.e("sdfrgt", otp);
                                        et_otp.setText(otp);
                                        myOtp = otp;
                                    }
                                }.start();

                            } else {
                                getSmsOtp();
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
    private void getChageMPIN(String mobile, String otp, String o_mpin, String n_mpin, String cn_mpin)
    {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("mpin",cn_mpin);
        params.put("old_mpin",o_mpin);
//        params.put("mpin",n_mpin);
//        params.put("mpin",cn_mpin)
        params.put("mobile",mobile);
        params.put("otp",myOtp);

        module.postRequest(CHANGE_MPIN, params, new Response.Listener<String>() {
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

    private void getForgteMPIN(String mob, String mpin) {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("mpin",mpin);
        params.put("mobile",mob);

        module.postRequest(FORGOT_MPIN, params, new Response.Listener<String>() {
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
}