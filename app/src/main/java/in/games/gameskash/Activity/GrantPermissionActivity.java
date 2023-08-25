package in.games.gameskash.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import in.games.gameskash.Config.Module;
import in.games.gameskash.R;

public class GrantPermissionActivity extends AppCompatActivity {

    Button btn_permission;
    RadioButton rb_record, rb_call, rb_storage;
    Module module;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 10,REQUEST_PHONE_CALL=11,REQUEST_STORAGE_PERMISSIONS=12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grant_permission);
        module = new Module(GrantPermissionActivity.this);
        btn_permission = findViewById(R.id.btn_permission);
        rb_record = findViewById(R.id.rb_record);
        rb_call = findViewById(R.id.rb_call);
        rb_storage = findViewById(R.id.rb_storage);

        rb_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioRecord();
            }
        });

        rb_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calling();
            }
        });

        rb_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage();
            }
        });


        btn_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_record.isChecked() && rb_call.isChecked() && rb_storage.isChecked()) {
                    Intent intent = new Intent(GrantPermissionActivity.this, SplashActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    module.showToast("Allow some access to proceed");
                }
            }
        });
    }


    void AudioRecord() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO);
        }else {
            rb_record.setChecked(true);
        }
    }
    void calling(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
        }else {
            rb_call.setChecked(true);
        }
    }
    void storage(){
        try {
            startActivity(new Intent(android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:in.games.GamesSattaBets")));
        }catch (Exception e){
            e.printStackTrace();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GrantPermissionActivity.this, new String[]
                    { android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    }, REQUEST_STORAGE_PERMISSIONS);
        }else {
            rb_storage.setChecked(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    rb_record.setChecked(true);
                } else {
                    rb_record.setChecked(false);
                    Toast.makeText(this, "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
            }
            break;
            case REQUEST_PHONE_CALL:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    rb_call.setChecked(true);
                } else {
                    rb_call.setChecked(false);
                    Toast.makeText(this, "Permissions Denied to make call", Toast.LENGTH_LONG).show();
                }
            }
            break;
            case REQUEST_STORAGE_PERMISSIONS:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    rb_storage.setChecked(true);
                } else {
                    rb_storage.setChecked(false);
                    Toast.makeText(this, "Permissions Denied to access storage", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }
}