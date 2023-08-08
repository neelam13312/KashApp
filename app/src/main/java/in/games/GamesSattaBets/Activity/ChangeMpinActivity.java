package in.games.GamesSattaBets.Activity;

import static in.games.GamesSattaBets.Config.BaseUrls.FORGOT_MPIN;
import static in.games.GamesSattaBets.Config.Constants.KEY_MOBILE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.ConnectivityReceiver;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.SessionMangement;

public class ChangeMpinActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_npass,et_cpass;
    Button btn_submit;
    String mobile="";
    LoadingBar loadingBar;
    Module module;
    SessionMangement sessionMangement;
    String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_mpin);
        initView();
    }

    private void initView() {

        module = new Module(ChangeMpinActivity.this);

        sessionMangement = new SessionMangement(ChangeMpinActivity.this);
        et_npass=findViewById (R.id.et_npass);
        et_cpass=findViewById (R.id.et_cpass);
        btn_submit=findViewById (R.id.btn_submit);
        btn_submit.setOnClickListener (this);
        mobile=sessionMangement.getUserDetails().get(KEY_MOBILE);
        module=new Module (ChangeMpinActivity.this);
        loadingBar=new LoadingBar (ChangeMpinActivity.this);
        if (getIntent().getExtras()!=null) {
            type = getIntent().getStringExtra("type");
            btn_submit.setText("GENERATE MPIN");
        }else {
            btn_submit.setText("CHANGE MPIN");
            module.sessionOut();
//            module.checkDeviceLogin();
        }

    }

    @Override
    public void onClick(View v) {
        String npass= et_npass.getText ( ).toString ( );
        String cpass= et_cpass.getText ( ).toString ( );
        if (v.getId ( ) == R.id.btn_submit) {
            if (et_npass.getText ( ).toString ( ).isEmpty ( )) {
                et_npass.setError ("Required");
                et_npass.requestFocus ( );

            } else if (et_npass.getText ( ).toString ( ).length()!=4) {
                et_npass.setError ("Please enter 4 digits mpin");
                et_npass.requestFocus ( );

            } else if (et_cpass.getText ( ).toString ( ).isEmpty ( )) {
                et_cpass.setError ("Required");
                et_cpass.requestFocus ( );
            }else if (et_cpass.getText ( ).toString ( ).length()!=4) {
                et_cpass.setError ("Please enter 4 digits mpin");
                et_cpass.requestFocus ( );

            }
            else if (!(npass.equals (cpass))) {
                module.errorToast ("Password must be matched");
                et_npass.requestFocus ( );
            }
            else {

                if (ConnectivityReceiver.isConnected()) {
                    if (type.equals("r")){
                        Intent intent = new Intent(ChangeMpinActivity.this, OTPActivity.class);
                        intent.putExtra("mobile",mobile);
                        intent.putExtra("mpin",npass);
                        startActivity(intent);
                        finish();
//                        updatePassword(getIntent().getStringExtra("mobile"), npass,CREATE_MPIN);
                    }
                    else{
                        updatePassword(mobile, npass,FORGOT_MPIN);}
                }
                else
                {
                    module.noInternet ();
                }
            }
        }
    }


    private void updatePassword(String mobile, String npass, String url) {
        loadingBar.show();
        HashMap<String, String> params=new HashMap<>();
        params.put("mpin",npass);
        params.put("mobile",mobile);

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
                        if (type.equals("r")){
                            Intent intent = new Intent(ChangeMpinActivity.this, OTPActivity.class);
                            intent.putExtra("mobile",mobile);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(ChangeMpinActivity.this, MpinLoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else
                    {
                        loadingBar.dismiss();
                        module.errorToast (jsonObject.getString("message"));
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
}