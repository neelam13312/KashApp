package in.games.GamesSattaBets.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.ConnectivityReceiver;

public class RegisterActivity extends AppCompatActivity {
Module module;
    String name ,email;
    EditText  et_number,et_username,et_pass,et_email;

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
        et_email=findViewById(R.id.et_email);

        if (ConnectivityReceiver.isConnected ( )) {
            onValidation ( );
        } else {
            module.noInternet ( );
        }
    }

    private void onValidation() {
         name =et_username.getText ( ).toString ( );
         email = et_email.getText ( ).toString ( );

         if (Module.validateUserName  (et_username)
                &&(Module.validateEmail(et_email) &&
                (Module.validateWhatsappNumber (et_number)
                && (Module.validatePassword(et_pass))))){

             callApi();


        }

    }

    private void callApi() {
        Intent  i_reg = new Intent(RegisterActivity.this, OTPActivity.class);
        startActivity(i_reg);
        finish();    }

}