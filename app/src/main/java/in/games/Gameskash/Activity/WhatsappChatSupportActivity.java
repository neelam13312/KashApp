package in.games.Gameskash.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import in.games.Gameskash.Config.Module;
import in.games.Gameskash.Interfaces.GetAppSettingData;
import in.games.Gameskash.Model.IndexResponse;
import in.games.Gameskash.R;

public class WhatsappChatSupportActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_back;
    TextView tv_title,tv_msg;
    Button btn_submit;
    Module module;
    String  chat_mobile,chat_msg,chat_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_chat_support);
        module = new Module(WhatsappChatSupportActivity.this);
        tv_title = findViewById(R.id.tv_title);
        img_back = findViewById(R.id.img_back);
        tv_msg = findViewById(R.id.tv_msg);
        btn_submit = findViewById(R.id.btn_submit);
        tv_title.setText("Support");


        img_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        module.getConfigData(new GetAppSettingData() {
            @Override
            public void getAppSettingData(IndexResponse model) {
                chat_msg=model.getChat_msg ();
                chat_text = model.getChat_text();
                chat_mobile = model.getChat_mobile();
                tv_msg.setText(chat_text);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_submit:
                module.whatsapp(chat_mobile,chat_msg);
                break;
        }
    }
}