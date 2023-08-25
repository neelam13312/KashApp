package in.games.Gameskash.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import in.games.Gameskash.Config.Module;
import in.games.Gameskash.R;

public class PlayVideoActivity extends AppCompatActivity {
VideoView video;
WebView webView;
//String video_url="https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4";
String video_url="";
ImageView img_back;
TextView tv_title;
Module module;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView (R.layout.activity_play_video);
        initview();
        if (SplashActivity.sessionCountDownTimer!=null){
            SplashActivity.sessionCountDownTimer.cancel();
        }
        //                module.checkDeviceLogin();

        try {
            if (!video_url.isEmpty ( )) {

                String svideo_url = video_url;
                String r_youtube = svideo_url.replace ("https://youtu.be/", "https://www.youtube.com/embed/");
                Log.e ("tt", "onBindViewHolder: " + r_youtube);



                String htmls =" <iframe  class=youtube-player style= width:100%!important;border:0;height:50%!important;padding:0px;margin:0px\n" +
                        "id=ytplayer type=text/html src=\n" +
                        "                 frameborder=0;\n" +
                        "      allowfullscreen\n" +
                        "allow=fullscreen\n" +" > </iframe>";

                String valls = htmls.replace ("src=", "src=" + r_youtube);
                Log.e ("videee", "onBindViewHolder: " + valls);

                webView.getSettings ( ).setPluginState (WebSettings.PluginState.ON_DEMAND);
                webView.getSettings ( ).setJavaScriptEnabled (true);
                webView.getSettings ( ).setAllowFileAccess (true);
                webView.setWebChromeClient (new WebChromeClient ( ));
                webView.setWebViewClient (new WebViewClient ( ));

//                webView.setWebViewClient(new Browser());
//                webView.setWebChromeClient(new MyWebClient());

                webView.getSettings ( ).setDomStorageEnabled(true);
                webView.getSettings ( ).setSupportMultipleWindows(false);
                webView.getSettings ( ).setJavaScriptCanOpenWindowsAutomatically(false);

                webView.loadDataWithBaseURL ("SomeStringForBaseURL", valls, "text/html", "UTF-8", "");


            }
        }
        catch(Exception e)
        {
            Toast.makeText (PlayVideoActivity.this, "No video found", Toast.LENGTH_SHORT).show ( );
        }

    }

    private void initview() {
        module = new Module(PlayVideoActivity.this);
        video_url=getIntent ().getStringExtra ("video_url");
        video =(VideoView)findViewById (R.id.video);
        img_back =(ImageView) findViewById (R.id.img_back);
        tv_title =(TextView) findViewById (R.id.tv_title);
        webView=findViewById (R.id.webView);
        tv_title.setText(getIntent ().getStringExtra ("video_name"));
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}


