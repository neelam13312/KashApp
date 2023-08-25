package in.games.gameskash.Activity;

import static in.games.gameskash.Config.BaseUrls.URL_PAYMENT_GATEWAY_STATUS;
import static in.games.gameskash.Config.Constants.KEY_ID;
import static in.games.gameskash.Fragment.AddFundFragment.gatewayStatus;
import static in.games.gameskash.Fragment.AddFundFragment.s_amount;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import in.games.gameskash.Config.Module;
import in.games.gameskash.R;
import in.games.gameskash.Util.ConnectivityReceiver;
import in.games.gameskash.Util.LoadingBar;
import in.games.gameskash.Util.SessionMangement;

public class PaymentWebViewActivity extends AppCompatActivity {
WebView webview;
Module module;
LoadingBar loadingBar;
Timer timerstatus;
int timertime = 2000;
SessionMangement sessionMangement;
String txn_id="";
boolean hideApi= true;
String finalURL="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web_view);

        Log.e("url",getIntent().getStringExtra("url"));
        String link = getIntent().getStringExtra("url");
//        String link = "https://securegw.paytm.in/theia/api/v2/showPaymentPage?mid=AADHAR65589224286281&orderId=U162212291705011032175342&txnToken=bf0bc518554148998bc8f18baf16cb581672313704539&amount=1000&sourceName=googlechrome&sourceUrl=https://securegw.paytm.in/theia/api/v2/showPaymentPage&isAppLink=true&appInvokeFrom=REDIRECTION_APPINVOKE";

        sessionMangement = new SessionMangement(PaymentWebViewActivity.this);
        loadingBar = new LoadingBar(PaymentWebViewActivity.this);
        module = new Module(PaymentWebViewActivity.this);
        webview = findViewById(R.id.webview);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        finalURL = link;
        if (!gatewayStatus.equals("6") && !gatewayStatus.equals("11")) {
            String str[] = link.split("txnid=");
            txn_id = str[1];
            Log.e("xdcfvgbh", txn_id);
            InitCoutDown();
            webview.loadUrl(finalURL);
        }
        else {
            if (gatewayStatus.equals("6")){
                webview.loadUrl(finalURL);
            }else {
                getPaymentGateWayUrl(finalURL);
            }

            loadView();
        }
    }
    
    private void InitCoutDown() {

        timerstatus = new Timer();
        timerstatus.scheduleAtFixedRate(new TimerTask() {
                                            @Override
                                            public void run() {
                                                if (hideApi) {
                                                    getPaymentGateWay();
                                                }

                                            }
                                        },
                //Set how long before to start calling the TimerTask (in milliseconds)
                timertime,
                //Set the amount of time between each execution (in milliseconds)
                timertime);
    }

    @Override
    protected void onDestroy() {
        try {
            if ((!gatewayStatus.equals ("6"))&& (!gatewayStatus.equals("11"))) {
                timerstatus.cancel ( );
            }
            super.onDestroy ( );
        }catch (Exception e)
        {e.printStackTrace ();}
    }

    public void loadView(){
        webview.setWebViewClient(new WebViewClient()
        {
            @RequiresApi(api = Build.VERSION_CODES.O_MR1)
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                try {
                    if (ConnectivityReceiver.isConnected()) {
                        webview.setSafeBrowsingWhitelist(Collections.singletonList(url), null);
                    }
                    else
                    {
                        module.noInternet();
                        Toast.makeText (PaymentWebViewActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show ( );
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                Log.e("WebView", "your current url when webpage loading.." + url);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                try {
                    if(loadingBar.isShowing ()) {
                        loadingBar.dismiss ( );}
                }catch (Exception e)
                {e.printStackTrace ();}
                if (url.contains("done")){
                    hideApi= false;
                    SuccessBidDailoge();
                }
                Log.e("WebView", "your current url when webpage loading.. finish" + url);
                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onLoadResource(view, url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("upi:")||url.startsWith("gpay:")||url.startsWith("phonepe:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }
    
    public void getPaymentGateWay() {

        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",sessionMangement.getUserDetails ().get (KEY_ID));
        params.put("amount",s_amount);
        params.put("txn_id",txn_id);
        Log.e("params",params.toString() );
        module.postRequest(URL_PAYMENT_GATEWAY_STATUS, params, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                Log.e("PAYMENT_GATEWAY_LINK",response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    boolean   resp = obj.getBoolean("response");
                    if(resp) {
                        hideApi= false;
                        SuccessBidDailoge();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                module.showVolleyError(error);
            }
        });
    }

    public void getPaymentGateWayUrl(String url) {
        loadingBar.show ();
        HashMap<String,String> params=new HashMap<>();
        Log.e("params",params.toString() );
        module.postRequest(url, params, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("PAYMENT_GATEWAY_URL",response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    String resp = obj.getString("status");
                    if(resp.equalsIgnoreCase("ok")) {
                        finalURL = obj.getString("pay_url");
                        webview.loadUrl(finalURL);
                        loadView();

                    }else {
                        module.errorToast(PaymentWebViewActivity.this,obj.getString("message"));
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                module.showVolleyError(error);
            }
        });
    }


    public  void  SuccessBidDailoge(){
        Log.e("SuccessBidDailoge","yes");
        Dialog dialog;
        dialog = new Dialog (PaymentWebViewActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        dialog.setContentView (R.layout.dailoge_custom_bitsuccess );
        dialog.show ( );
        TextView tv_luck,tv_bid;
        Button btn_ok;

        tv_luck = dialog.findViewById (R.id.tv_luck);
        btn_ok = dialog.findViewById (R.id.btn_ok);
        tv_bid= dialog.findViewById (R.id.tv_bid);
        tv_luck.setText("Payment Successful !");
        tv_bid.setText("Transaction Success. Points Added To Your Wallet");


        btn_ok.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                dialog.dismiss ( );
                Intent i = new Intent(PaymentWebViewActivity.this,MainActivity.class);
                startActivity(i);
                finish();

            }
        });
        dialog.setCanceledOnTouchOutside (false);
        dialog.setCancelable(false);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
            if (hideApi) {
                module.showToast("Payment Failed.");
                finish();
            }
    }

}