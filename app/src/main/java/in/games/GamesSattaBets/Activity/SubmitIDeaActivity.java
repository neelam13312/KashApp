package in.games.GamesSattaBets.Activity;

import static in.games.GamesSattaBets.Config.Constants.KEY_ID;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.HashMap;

import in.games.GamesSattaBets.Config.BaseUrls;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.SessionMangement;

public class SubmitIDeaActivity extends AppCompatActivity {
TextInputEditText tv_msg;
Button btn_submit;
TextView tv_title;
ImageView img_back;
Module module;
LoadingBar loadingBar;
SessionMangement sessionMangement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_idea);
        initview();
    }

    private void initview() {
        tv_title=findViewById (R.id.tv_title);
        tv_title.setText ("Submit Idea");
        img_back=findViewById (R.id.img_back);
        img_back.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
              Intent intent=new Intent (  SubmitIDeaActivity.this,MainActivity.class );
              startActivity (intent);
            }
        });
        sessionMangement=new SessionMangement (SubmitIDeaActivity.this);
        module=new Module (SubmitIDeaActivity.this);
        loadingBar=new LoadingBar (SubmitIDeaActivity.this);
        tv_msg=findViewById (R.id.tv_msg);
        btn_submit=findViewById (R.id.btn_submit);
        btn_submit.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                if(tv_msg.getText ().toString ().isEmpty ())
                {
                   module.errorToast (SubmitIDeaActivity.this,"Message field required");
                }
                else
                {
                   postMessage(tv_msg.getText ().toString ());
                }
            }


        });

    }
    private void postMessage(String message)  {

        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id", sessionMangement.getUserDetails().get(KEY_ID));
        params.put("message",message);
        module.postRequest (BaseUrls.URL_Submit_idea, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e ("URL_Submit_idea", "onResponse: "+response);
                    JSONObject object = new JSONObject(response);
                    boolean resp = object.getBoolean("response");

                    if (resp) {
                        loadingBar.dismiss();
                        module.successToast (SubmitIDeaActivity.this,object.getString("success").toString());
                        Intent intent = new Intent (SubmitIDeaActivity.this, MainActivity.class);
                        startActivity (intent);
//                        finish ( );

                    } else {
                        loadingBar.dismiss ();
                        module.errorToast (SubmitIDeaActivity.this,object.getString("error").toString());
                    }
                } catch (Exception ex) {
                    loadingBar.dismiss ();
                    ex.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                module.showVolleyError(error);
                loadingBar.dismiss ();
            }
        });


    }
}