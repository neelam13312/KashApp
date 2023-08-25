package in.games.gameskash.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import in.games.gameskash.Config.Module;
import in.games.gameskash.R;
import in.games.gameskash.Util.ConnectivityReceiver;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
Module module;
    String name ,email;
    EditText  et_number,et_username,et_pass,et_confpass;
    TextView tvPrivacy;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        module= new Module(RegisterActivity.this);
        et_number=findViewById(R.id.et_number);
        et_username=findViewById(R.id.et_username);
        et_pass=findViewById(R.id.et_pass);
        et_confpass=findViewById(R.id.et_confpass);
        btnSignUp=findViewById(R.id.btnSignUp);
        tvPrivacy=findViewById(R.id.tvPrivacy);
        tvPrivacy.setText(Html.fromHtml("By click on SignUp you are agree with our <u>Privacy Policy</u>"));
        btnSignUp.setOnClickListener(this);
        if (ConnectivityReceiver.isConnected ( )) {

        } else {
            module.noInternet ( );
        }
    }


    private void onValidation() {
         name =et_username.getText ( ).toString ( );

         if (Module.validateUserName  (et_username) &&
                (Module.validateWhatsappNumber (et_number)
                && (Module.validatePassword(et_pass)&&
                        (Module.validateEmail(et_confpass))))){

             callApi();


        }

    }

    private void callApi() {
        Intent  i_reg = new Intent(RegisterActivity.this, OTPActivity.class);
        startActivity(i_reg);
        finish();    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignUp:
                onValidation();
                break;

        }

    }
}