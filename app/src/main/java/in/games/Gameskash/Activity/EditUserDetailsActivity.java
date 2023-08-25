package in.games.Gameskash.Activity;

import static in.games.Gameskash.Fragment.AddFundFragment.u_name;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

import in.games.Gameskash.Config.Module;
import in.games.Gameskash.R;
import in.games.Gameskash.Util.ConnectivityReceiver;


public class EditUserDetailsActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView img_logo,img_close;
    TextView tv_name,tv_amount;
    EditText et_mob,et_email;
    String countryCodeAndroid = "";
    CountryCodePicker ccp;
    Button btn_submit;
    Module module;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_details);
        module = new Module(EditUserDetailsActivity.this);
        img_logo = findViewById(R.id.img_logo);
        img_close = findViewById(R.id.img_close);
        tv_name = findViewById(R.id.tv_name);
        tv_amount = findViewById(R.id.tv_amount);
        et_mob = findViewById(R.id.et_mob);
        btn_submit = findViewById(R.id.btn_submit);
        et_email = findViewById(R.id.et_email);
        String phoneNumber =  PaymentActivity.u_phone;
        String strLastFourDi = phoneNumber.length() >= 10 ? phoneNumber.substring(phoneNumber.length() - 10): "";
        et_email.setText(PaymentActivity.u_email);
        tv_amount.setText("Rs."+ PaymentActivity.amount);
        tv_name.setText(u_name);

        et_mob.setText(strLastFourDi);
        ccp = findViewById(R.id.ccp);
        btn_submit.setOnClickListener(this);
        img_close.setOnClickListener(this);

        Log.e ("hhhjchsc", "onClick: "+ccp.getSelectedCountryCode () );

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                String mob = et_mob.getText().toString();
                if (mob.isEmpty()||mob.equalsIgnoreCase("null")||mob==null){
                    et_mob.setError("Mobile number required");
                    et_mob.setFocusable(true);
                }else if (Integer.parseInt(mob.substring(0,1))<6){
                    et_mob.setError("Mobile number must star with 6 or >6");
                    et_mob.setFocusable(true);
                } else if(mob.length()!=10)
                {
                    et_mob.setError ("Mobile number length must be 10");
                    et_mob.requestFocus ();
                } else if (et_email.getText ( ).toString ( ).isEmpty ( )) {
                    et_email.setError ("Email address required");
                    et_email.requestFocus ();
                }else {

                    if(ConnectivityReceiver.isConnected()){
                            PaymentActivity.u_email=et_email.getText().toString();
                            PaymentActivity.u_phone=mob;
                            Intent intent = new Intent(EditUserDetailsActivity.this, PaymentActivity.class);
                            intent.putExtra("mob", et_mob.getText().toString());
                            intent.putExtra("code", ccp.getSelectedCountryCode());
                            intent.putExtra("email", et_email.getText().toString());
                            startActivity(intent);
                            finish();
                    }else {
                           module.noInternet ();
                    }
                }
                break;
            case R.id.img_close:
                showExistDialog();
//                finishAffinity();
                break;
        }
    }
    public void showExistDialog(){
        final Dialog dialog;
        dialog = new Dialog(EditUserDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_exit);
        TextView txtMessage=(TextView)dialog.findViewById(R.id.tv_msg);
        RelativeLayout btnOk=(RelativeLayout)dialog.findViewById(R.id.rel_ok);
        RelativeLayout btnNo=(RelativeLayout)dialog.findViewById(R.id.rel_close);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        txtMessage.setText("Do you really want to exit? ");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
                finish();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}