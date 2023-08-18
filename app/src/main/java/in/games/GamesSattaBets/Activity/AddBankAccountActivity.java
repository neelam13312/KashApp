package in.games.GamesSattaBets.Activity;

import static in.games.GamesSattaBets.Activity.SplashActivity.bank_change_charge;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_GET_BANK_NAME;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_GET_STATUS;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_REGISTER;
import static in.games.GamesSattaBets.Config.Constants.KEY_ADDRESS;
import static in.games.GamesSattaBets.Config.Constants.KEY_CITY;
import static in.games.GamesSattaBets.Config.Constants.KEY_DOB;
import static in.games.GamesSattaBets.Config.Constants.KEY_EMAIL;
import static in.games.GamesSattaBets.Config.Constants.KEY_ID;
import static in.games.GamesSattaBets.Config.Constants.KEY_IFSC;
import static in.games.GamesSattaBets.Config.Constants.KEY_MOBILE;
import static in.games.GamesSattaBets.Config.Constants.KEY_PAYTM;
import static in.games.GamesSattaBets.Config.Constants.KEY_PHONEPAY;
import static in.games.GamesSattaBets.Config.Constants.KEY_PINCODE;
import static in.games.GamesSattaBets.Config.Constants.KEY_TEZ;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Pattern;

import in.games.GamesSattaBets.Config.CommonMethods;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.SessionMangement;

public class AddBankAccountActivity extends AppCompatActivity {
    Button btn_bank;
    RelativeLayout  rel_edit,rel_data;
    EditText et_accno;
    EditText et_bankname,et_branchname;
    EditText et_ifsc;
    EditText et_hname;
    TextView tv_title;
    ImageView img_back;
    Button btn_change;
    SessionMangement sessionMangement;
    LoadingBar loadingBar;
    Module module;
    String mobile, email, dob, name;
    String  paytm_number, phonePay_number, googlePay_number, address, city, pincode;
    int year, month, day;
    String regex_ifsc = "^[A-Z]{4}0[A-Z0-9a-z]{6}$";
    CommonMethods common;
    // Compile the ReGex
    Pattern p_ifsc = Pattern.compile( regex_ifsc );
    TextView tv_hod,tv_bank,tv_acc,tv_ifsc;
    Integer w_amount;

    //            Matcher m = p.matcher(str);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_bank_account );
        // ((MainActivity)getActivity()).setTitles("Fund");
        img_back = findViewById( R.id.img_back );
        tv_title = findViewById( R.id.tv_title );
        rel_data=findViewById (R.id.rel_data);
        rel_edit=findViewById (R.id.rel_edit);
        tv_hod=findViewById (R.id.tv_hod);
        tv_bank=findViewById (R.id.tv_bank);
        tv_acc=findViewById (R.id.tv_acc);
        tv_ifsc=findViewById (R.id.tv_ifsc);
        btn_change=findViewById (R.id.btn_change);
        //name=getIntent ().getStringExtra ("Add Bank Account");
        tv_title.setText ("Bank Account");
        btn_bank = findViewById( R.id.btn_bank );
        et_accno = findViewById( R.id.et_accno );
        et_bankname = findViewById( R.id.et_bankname );
        et_branchname = findViewById( R.id.et_branchname );
        et_ifsc = findViewById( R.id.et_ifsc );
        if(et_ifsc.getText ().toString ().isEmpty ())
        {
            et_ifsc.setInputType (InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        }
        et_hname = findViewById( R.id.et_hname );
        sessionMangement = new SessionMangement( AddBankAccountActivity.this );
        loadingBar = new LoadingBar( AddBankAccountActivity.this );
        module = new Module( AddBankAccountActivity.this );
        module.makeEditTextAcceptCharatacter(et_hname);
        module.makeEditTextAcceptCharatacter(et_bankname);
//        //                module.checkDeviceLogin();

        w_amount = Integer.parseInt (module.getAndSetWalletAmount ( ));

        img_back.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent ( AddBankAccountActivity.this, MainActivity.class );
                startActivity (intent);
            }
        });
        getStatus();
        mobile = sessionMangement.getUserDetails().get(KEY_MOBILE);
        googlePay_number = sessionMangement.getUserDetails().get(KEY_TEZ);
        phonePay_number = sessionMangement.getUserDetails().get(KEY_PHONEPAY);
        paytm_number = sessionMangement.getUserDetails().get(KEY_PAYTM);
        address = sessionMangement.getUserDetails().get(KEY_ADDRESS);
        city = sessionMangement.getUserDetails().get(KEY_CITY);
        pincode = sessionMangement.getUserDetails().get(KEY_PINCODE);
        dob = sessionMangement.getUserDetails().get(KEY_DOB);
        email = sessionMangement.getUserDetails().get(KEY_EMAIL);



        btn_change.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                rel_data.setVisibility (View.GONE);
                rel_edit.setVisibility (View.VISIBLE);
            }
        });

        btn_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mob = sessionMangement.getUserDetails().get(KEY_ID);
                String accno = et_accno.getText().toString();
                String bankname = et_bankname.getText().toString();
                String ifsc = et_ifsc.getText().toString();
                String hod_name = et_hname.getText().toString();
                String branch_name=et_branchname.getText ().toString ();
                 if (et_hname.getText().toString().isEmpty()) {
                    et_hname.setError("Required holder name");
                    et_hname.requestFocus();

                }else if (et_accno.getText().toString().isEmpty()) {
                    et_accno.setError("Required bank account number");
                    et_accno.requestFocus();

                }  else if (et_ifsc.getText().toString().isEmpty()) {
                    et_ifsc.setError("Please fill IFSC code");
                    et_ifsc.requestFocus();

                }else if (!p_ifsc.matcher(ifsc).matches()){
                    et_ifsc.setError("Please fill valid IFSC code");
                    et_ifsc.requestFocus();

               } else if (et_bankname.getText().toString().isEmpty()) {
                    et_bankname.setError("Required bank name");
                    et_bankname.requestFocus();

                }else if (et_branchname.getText().toString().isEmpty()){
                    et_branchname.setError("Required branch name");
                    et_branchname.requestFocus();
                }else {
                     if(module.checkNull (sessionMangement.getUserDetails ().get (KEY_IFSC)).isEmpty ())
                     {
                         storeBankDetails(accno, bankname, ifsc, hod_name, mob,branch_name,"0");
                     }
                     else{
                        openServiceDialog(accno, bankname, ifsc, hod_name, mob,branch_name,"1");
                     }
                }
            }
        });
        et_ifsc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ifsc = s.toString();
                if (ifsc.isEmpty()) {
                    et_ifsc.setError("Please fill IFSC code");
                    et_ifsc.requestFocus();

                }

                else if(ifsc.length ()<3)
                {
                  et_ifsc.setInputType (InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                }
                else
                {
                    if (!p_ifsc.matcher(ifsc).matches()){
                        et_ifsc.setError("Please fill valid IFSC code");
                        et_ifsc.requestFocus();

                    }
                    else {
                        Log.d ("xvgshasad", "onTextChanged: "+ifsc);
                        et_bankname.setText("");
                        et_branchname.setText("");
                        getBankNameUsingIFSCCode(ifsc);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void openServiceDialog(final String accno, final String bankname,
                                   final String ifsc, final String hod_name,
                                   final String mailid,final String branch,String status) {
        w_amount = Integer.parseInt (module.getAndSetWalletAmount ( ));

        Dialog dialog;
        dialog = new Dialog (AddBankAccountActivity.this);
        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable (0));

        dialog.setContentView (R.layout.dialog_service);
        dialog.show ( );
        Button btn_ok=dialog.findViewById (R.id.btn_ok);
        Button btn_cancel=dialog.findViewById (R.id.btn_cancel);
        btn_cancel.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                dialog.dismiss ();
            }
        });

        TextView tv_service=dialog.findViewById (R.id.tv_service);
        TextView tv_wallet=dialog.findViewById (R.id.tv_wallet);
        String service=String.valueOf (bank_change_charge);
        tv_service.setText ("Service Charge : Rs. "+service);
        String mob = sessionMangement.getUserDetails().get(KEY_ID);
        btn_ok.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
//                int wallet_amount = (w_amount);
                if (w_amount < Integer.parseInt(service)) {
                     module.errorToast (AddBankAccountActivity.this,"Insufficient Amount");
                }
                else {

                    storeBankDetails (accno, bankname, ifsc, hod_name, mob, branch, status);
                    dialog.dismiss ();
                }


            }
        });


        dialog.setCanceledOnTouchOutside (true);
    }

    private void getBankNameUsingIFSCCode(final String ifsc) {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("ifsc_code", ifsc);
        Log.d ("ifsccc", "c"+params);
        module.postRequest(URL_GET_BANK_NAME, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("URL_GET_BANK_NAME",response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean resp = jsonObject.getBoolean("success");
                    if (resp) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        et_bankname.setText(object.getString("bank_name"));
                        et_branchname.setText(object.getString("branch"));
                    } else {
                         module.errorToast (AddBankAccountActivity.this,"" + jsonObject.getString("message"));
                        Log.e("errorToast", "onResponse: "+ jsonObject.getString("message"));
                    }
                    loadingBar.dismiss();
                } catch (Exception ex) {
                    ex.printStackTrace();
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

    private void storeBankDetails(final String accno, final String bankname,
                                  final String ifsc, final String hod_name,
                                  final String mailid,final String branch,String status) {
//staus 0-fresh,1-Edit
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("key", "3");
//        params.put("mobile",mailid);
        params.put("mobile", mobile);
        params.put("user_id", sessionMangement.getUserDetails().get(KEY_ID));
        params.put("accountno", accno);
        params.put("bankname", bankname);
        params.put("ifsc", ifsc);
        params.put("accountholder", hod_name);
        params.put("bank_address", branch);
        params.put ("status",status);
        module.postRequest(URL_REGISTER, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingBar.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean resp = jsonObject.getBoolean("responce");
                    if (resp) {
                        sessionMangement.updateAccSection(accno, bankname, ifsc, hod_name,branch);
                        module.successToast(AddBankAccountActivity.this,"" + jsonObject.getString("message"));
                        rel_data.setVisibility (View.VISIBLE);
                        tv_hod.setText (hod_name);
                        tv_bank.setText (bankname+" "+branch);
                        tv_acc.setText (accno);
                        tv_ifsc.setText (ifsc);
                        rel_edit.setVisibility (View.GONE);
                    } else {
                         module.errorToast (AddBankAccountActivity.this,"" + jsonObject.getString("error"));
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    module.showToast("wrong");
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
    private void getStatus()
    {
        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        HashMap<String,String> params = new HashMap<>();
        params.put("device_id",android_id);

        module.postRequest(URL_GET_STATUS, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("URL_GET_STATUS",response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("response")){
                        JSONArray array = object.getJSONArray("data");

                        JSONObject obj = array.getJSONObject(0);
                        String account_number = obj.getString("accountno");
                        String bank_name = obj.getString("bank_name");
                        String ifsc = obj.getString("ifsc_code");
                        String hod_name = obj.getString("account_holder_name");
                        String branch_name = obj.getString("bank_address");

                        if(rel_edit.getVisibility ()==View.VISIBLE)
                        {
                            if (!module.checkNull(ifsc).equals("")){
                                getBankNameUsingIFSCCode(ifsc);
                            }
                        }

                        if(module.checkNull(ifsc).equals("")) {
                            rel_edit.setVisibility (View.VISIBLE);
                            rel_data.setVisibility (View.GONE);
                        } else {
                            rel_data.setVisibility (View.VISIBLE);
                            rel_edit.setVisibility (View.GONE);
                            tv_hod.setText (hod_name);
                            tv_bank.setText (bank_name+" "+branch_name);
                            tv_acc.setText (account_number);
                            tv_ifsc.setText (ifsc);
                        }
                    }
                    else {
                         module.errorToast (AddBankAccountActivity.this,object.getString("error"));
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
}
