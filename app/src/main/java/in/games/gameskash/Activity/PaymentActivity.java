package in.games.gameskash.Activity;

import static in.games.gameskash.Config.BaseUrls.URL_RAZORPAY_PAYMENT;
import static in.games.gameskash.Config.BaseUrls.URL_REQUEST;
import static in.games.gameskash.Config.BaseUrls.URL_UPI_GATEWAY_COMPLETE_DETAILS;
import static in.games.gameskash.Config.BaseUrls.URL_UPI_GATEWAY_DETAILS;
import static in.games.gameskash.Config.BaseUrls.URL_UPI_ID;
import static in.games.gameskash.Config.Constants.KEY_ID;
import static in.games.gameskash.Config.Constants.KEY_MOBILE;
import static in.games.gameskash.Fragment.AddFundFragment.desc;
import static in.games.gameskash.Fragment.AddFundFragment.gatewayStatus;

import static in.games.gameskash.Fragment.AddFundFragment.razor_pay;
import static in.games.gameskash.Fragment.AddFundFragment.razorpay_email;
import static in.games.gameskash.Fragment.AddFundFragment.razorpay_mobile;
import static in.games.gameskash.Fragment.AddFundFragment.s_amount;
import static in.games.gameskash.Fragment.AddFundFragment.s_transaction_id;
import static in.games.gameskash.Fragment.AddFundFragment.u_name;
import static in.games.gameskash.Fragment.AddFundFragment.upi_merchant_code;
import static in.games.gameskash.Fragment.AddFundFragment.upi_name;
import static in.games.gameskash.Fragment.FundFragment.my_error;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.razorpay.PaymentMethodsCallback;
import com.razorpay.PaymentResultListener;
import com.razorpay.Razorpay;

import com.razorpay.ValidationListener;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.model.PaymentApp;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;
import com.wangsun.upi.payment.UpiPayment;
import com.wangsun.upi.payment.model.PaymentDetail;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.games.gameskash.Config.Module;
import in.games.gameskash.Fragment.AddFundFragment;
import in.games.gameskash.R;
import in.games.gameskash.Util.ConnectivityReceiver;
import in.games.gameskash.Util.LoadingBar;
import in.games.gameskash.Util.SessionMangement;


public class PaymentActivity extends AppCompatActivity implements View.OnClickListener , PaymentResultListener, com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener {

    ImageView img_logo,img_close,img_edit,iv_gpay;
    TextView tv_name,tv_amount,tv_mob,tv_email;
    LinearLayout lin_Gpay,lin_PhnPe,lin_paytm,lin_other;
    LinearLayout lin_bg_Gpay,lin_bg_phnpe,lin_bg_paytm,lin_bg_other;
    CheckBox check_Gpay,check_PhnPe,check_paytm,check_other;
    String paymentMode="";
    Button btn_pay;
    private JSONObject payload;
    private Razorpay razorpay;
    boolean upi_flag=false;
    private EasyUpiPayment mEasyUpiPayment;
    Module module;
    LoadingBar loadingBar;
    SessionMangement sessionMangement;
    WebView payment_webview;
    LinearLayout lin_main;
    String order_id_value="";
    String tran_ref_id;
    String pa = "",pn = "", tr ="", txn = "", am = "", cu = "", mc = "";
    public static int UPI_REQUEST_CODE=123;
    ImageView img_phonePe;
    TextView tv_phonePe;
    ArrayList<String> newList = new ArrayList<String>();
    public static String trans_id="",amount="",u_email="",u_phone="",u_transaction_status="",company_logo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initView();

        initRazorpay();
        createWebView();
        if (gatewayStatus.equals("3")) {
            getGatewayDetails();
        }
        else if (gatewayStatus.equals("8")){
            // just to not call else condition
            getGatewayDetails();
            img_phonePe.setImageResource(R.drawable.phonepe);
            tv_phonePe.setText("PhonePe");
        }else if (gatewayStatus.equals("9")){
            // hit api to get upi id only
            tr = s_transaction_id;
            pn = upi_name;
            mc = upi_merchant_code;
            getUpiId();
            img_phonePe.setImageResource(R.drawable.phonepe);
            tv_phonePe.setText("PhonePe");
        }else if (gatewayStatus.equals("10")){
            getCompleteGatewayDetails();
            img_phonePe.setImageResource(R.drawable.phonepe);
            tv_phonePe.setText("PhonePe");
        }
        else {
            img_phonePe.setImageResource(R.drawable.phonepe);
            tv_phonePe.setText("PhonePe");
        }
//        newList.addAll(UpiPayment.getExistingUpiApps(PaymentActivity.this));
//        if(!newList.contains ("phonepe"))
//        {
//            lin_PhnPe.setEnabled(false);
//            lin_PhnPe.setAlpha(0.4f);
//        }
//        if(!newList.contains ("gpay"))
//        {
//            lin_Gpay.setEnabled(false);
//            lin_Gpay.setAlpha(0.4f);
//        }
//        if(!newList.contains ("paytm"))
//        {
//            lin_paytm.setEnabled(false);
//            lin_paytm.setAlpha(0.4f);
//        }
        installedApps();
        Log.e("xdcfgh",razor_pay);
    }


    public void installedApps()
    {
        List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
        for (int i=0; i < packList.size(); i++) {
            PackageInfo packInfo = packList.get(i);
            if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                Log.e("Appname", appName);
//                if(appName.equalsIgnoreCase ("PhonePe"))
//                {
//                    phonepay_val="true";
//                }
//                Log.e ("allval", "installedApps: "+phonepay_val);
//            }
//        }
//
//        if(phonepay_val.equalsIgnoreCase ("true"))
//        {
//            rb_phonepe.setVisibility (View.VISIBLE);
//        }
//        else
//        {
//            rb_phonepe.setVisibility (View.GONE);
//        }
            }
        }
        }

    private void initRazorpay() {
        razorpay = new Razorpay (this);

        razorpay.getPaymentMethods(new PaymentMethodsCallback () {
            @Override
            public void onPaymentMethodsReceived(String result) {

                /**
                 * This returns JSON data
                 * The structure of this data can be seen at the following link:
                 * https://api.razorpay.com/v1/methods?key_id=rzp_test_1DP5mmOlF5G5ag
                 *
                 */
                Log.d("Result", "" + result);
                // inflateLists(result);
            }

            @Override
            public void onError(String error) {
                Log.d("Get Payment error",error);
            }
        });

//
    }

    public void initView() {
        payment_webview=(WebView) findViewById (R.id.payment_webview);
        lin_main=findViewById (R.id.lin_main);

        sessionMangement = new SessionMangement(PaymentActivity.this);
        module = new Module(PaymentActivity.this);
        loadingBar = new LoadingBar(PaymentActivity.this);
        img_logo = findViewById(R.id.img_logo);
        img_close = findViewById(R.id.img_close);
        img_edit = findViewById(R.id.img_edit);
        tv_name = findViewById(R.id.tv_name);
        tv_name = findViewById(R.id.tv_name);
        iv_gpay=findViewById (R.id.iv_gpay);
        tv_phonePe=findViewById (R.id.tv_phonePe);
        img_phonePe=findViewById (R.id.img_phonePe);

        tv_amount = findViewById(R.id.tv_amount);
        tv_mob = findViewById(R.id.tv_mob);
        tv_email = findViewById(R.id.tv_email);
        lin_Gpay = findViewById(R.id.lin_Gpay);
        lin_PhnPe = findViewById(R.id.lin_PhnPe);
        lin_paytm = findViewById(R.id.lin_paytm);
        lin_other = findViewById(R.id.lin_other);

        lin_bg_Gpay = findViewById(R.id.lin_bg_Gpay);
        lin_bg_phnpe = findViewById(R.id.lin_bg_phnpe);
        lin_bg_paytm = findViewById(R.id.lin_bg_paytm);
        lin_bg_other = findViewById(R.id.lin_bg_other);

        check_Gpay = findViewById(R.id.check_Gpay);
        check_PhnPe = findViewById(R.id.check_PhnPe);
        check_paytm = findViewById(R.id.check_paytm);
        check_other = findViewById(R.id.check_other);
        btn_pay = findViewById(R.id.btn_pay);
        iv_gpay.setOnClickListener (this);
        lin_Gpay.setOnClickListener(this);
        lin_bg_Gpay.setOnClickListener(this);
        lin_bg_phnpe.setOnClickListener(this);
        lin_PhnPe.setOnClickListener(this);
        lin_paytm.setOnClickListener(this);
        lin_other.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
        img_close.setOnClickListener(this);
        img_edit.setOnClickListener(this);
        order_id_value=getIntent ().getStringExtra ("order_id_value");
        amount=s_amount;
//        s_amount = "1";
        tv_amount.setText("Rs."+s_amount);

        if(ConnectivityReceiver.isConnected()){
            if (getIntent().getStringExtra("mob")!=null){
                Log.e("mob",getIntent().getStringExtra("mob"));
                Log.e("code",getIntent().getStringExtra("code"));
                String phoneNumber =  getIntent().getStringExtra("mob");
                u_phone=phoneNumber;
                String strLastFourDi = phoneNumber.length() >= 10 ? phoneNumber.substring(phoneNumber.length() - 10): "0000000000";
                tv_mob.setText("+"+getIntent().getStringExtra("code")+strLastFourDi);
                tv_email.setText(getIntent().getStringExtra("email"));
                tv_name.setText(u_name);
            }
        }else {
            module.noInternet ();
        }


        check_Gpay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    paymentMode= "GOOGLE_PAY";
                    findCheckMode("GOOGLE_PAY");
                }

            }
        });
        check_PhnPe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    paymentMode= "PHONE_PE";
                    findCheckMode("PHONE_PE");
                }

            }
        });
        check_paytm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    paymentMode= "PAYTM";
                    findCheckMode("PAYTM");
                }

            }
        });
        check_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    paymentMode= "OTHER";
                    findCheckMode("OTHER");
                }

            }
        });


    }
    private void createWebView() {
        /**
         * You need to pass the webview to Razorpay
         */
        razorpay.setWebView(payment_webview);

        /**
         * Override the RazorpayWebViewClient for your custom hooks
         */
//        razorpay.setWebviewClient(new RazorpayWebViewClient (razorpay) {
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                Log.d("mn_first", "Custom client onPageStarted");
//                Toast.makeText (PaymentActivity.this, "first", Toast.LENGTH_SHORT).show ( );
//
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                Log.d("jjj_second", "Custom client onPageFinished");
//
//            }
//        });
    }

    private void sendRequest() {
        razorpay.validateFields(payload, new ValidationListener () {
            @Override
            public void onValidationSuccess() {
                try {
                    //my_error="";
                    payment_webview.setVisibility(View.VISIBLE);
                    lin_main.setVisibility(View.GONE);
                    btn_pay.setVisibility (View.GONE);
                    razorpay.submit (payload, PaymentActivity.this);

                } catch (Exception e) {
                    Log.e("com.example", "Exception: ", e);
                }
            }


            public void onValidationError(Map<String, String> error) {
                my_error="error";
                Log.d("com.example", "Validation failed: " + error.get("field") + " " + error.get("description"));
                 module.errorToast (PaymentActivity.this,"Validation: " + error.get("field") + " " + error.get("description"));
              //  Toast.makeText(PaymentActivity.this, "Validation: " + error.get("field") + " " + error.get("description"), Toast.LENGTH_SHORT).show();
            }
        });
        razorpay.submit (payload, new PaymentResultListener ( ) {
            @Override
            public void onPaymentSuccess(String s) {
                Log.d ("successpay_msg", "onPaymentError: "+s);
                payment_webview.setVisibility(View.GONE);
                module.successToast (PaymentActivity.this,"Added Successfully");
                my_error="";
                //Toast.makeText (PaymentActivity.this, "Successfully", Toast.LENGTH_SHORT).show ( );
                Intent intent=new Intent(PaymentActivity.this,MainActivity.class);
                startActivity (intent);
                finish ();
            }

            @Override
            public void onPaymentError(int i, String s) {
                Log.d ("errorpay", "onPaymentError: "+s);
//                module.showToast(razor_pay+"------"+order_id_value);
//                module.showToast(s);
                payment_webview.setVisibility(View.GONE);
                 module.errorToast (PaymentActivity.this,"Something went wrong");
                my_error="error";
                Intent intent=new Intent(PaymentActivity.this,AddFundFragment.class);
                startActivity (intent);
                finish ();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_close:
                showExistDialog();
//                finishAffinity();
                break;
            case R.id.img_edit:
                Intent intent = new Intent(PaymentActivity.this,EditUserDetailsActivity.class);
                intent.putExtra("mob",u_phone);
                intent.putExtra("email",u_email);
                intent.putExtra("amount",amount);
                intent.putExtra("name",u_name);
                startActivity(intent);
                break;
            case R.id.lin_Gpay:
            case R.id.lin_bg_Gpay:
            case R.id.iv_gpay:
                managePaymentMethod(lin_bg_Gpay,check_Gpay,"GOOGLE_PAY");
                break;
            case R.id.lin_PhnPe:
            case R.id. lin_bg_phnpe:
                managePaymentMethod(lin_bg_phnpe,check_PhnPe,"PHONE_PE");
                break;
            case R.id.lin_paytm:
            case R.id.lin_bg_paytm:
                managePaymentMethod(lin_bg_paytm,check_paytm,"PAYTM");
                break;
            case R.id.lin_other:
            case R.id.lin_bg_other:
                managePaymentMethod(lin_bg_other,check_other,"OTHER");
                break;
            case R.id.btn_pay:
                try {

                    if (!check_Gpay.isChecked() && !check_PhnPe.isChecked() && !check_paytm.isChecked() && !check_other.isChecked()) {
                        Toast.makeText(PaymentActivity.this, "Please select atleast one payment mode", Toast.LENGTH_SHORT).show();
                    } else {
                        //GenrateOderId
                        if (gatewayStatus.equals("3")) {
                            Log.e("all_params", pa + "---" + pn + "---" + tr + "---" + txn + "---" + am + "---" + cu + "---" + mc);
                            payViaUpi(tr, pa, pn, tr, desc, s_amount + ".00", mc);
                        } else if (gatewayStatus.equals("8") || gatewayStatus.equals("9") || gatewayStatus.equals("10")) {
                            if (paymentMode.equalsIgnoreCase("OTHER")) {
                                startUpiPayment(tr, pa, pn, tr, desc, s_amount + ".00", mc, tr);
                            } else {
                                payViaUpi(tr, pa, pn, tr, desc, s_amount + ".00", mc);
                            }
                        } else {
                            getOrderId(order_id_value);
                        }

                        Log.d("odrdkj", "onClick: " + order_id_value);

                    }
                }catch (Exception ex){
                  ex.printStackTrace();
                }

                break;
        }
    }

    private void getOrderId(String order_id)
    {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",sessionMangement.getUserDetails ().get (KEY_ID));
        params.put("order_id",order_id);
        params.put("amount",s_amount);
        params.put("upi_name",paymentMode);

        Log.e("post_datata", "saveInfoIntoDatabase: "+params.toString() );
        module.postRequest(URL_RAZORPAY_PAYMENT, params, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                loadingBar.dismiss ();
                Log.e("add_fund",response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    boolean resp=obj.getBoolean("responce");
                    if(resp)
                    {
                        Log.e ("swdergty", paymentMode);
//                        if (gatewayStatus.equals("8")) {
//                            payViaUpi(tr,pa,pn,tr,desc,s_amount+".00",mc);
//                        }else {

                            String amout = String.valueOf((Integer.parseInt(String.valueOf(s_amount))) * 100);
                            String mobiles = sessionMangement.getUserDetails().get(KEY_MOBILE);
                            Log.d("fgrsgrg", "onClick: " + amout + "--" + order_id + "--" + razorpay_email + "--" + mobiles);

                            try {
                                payload = new JSONObject("{currency: 'INR'}");
                                payload.put("amount", amout);
                                payload.put("contact", razorpay_mobile);
                                payload.put("email", razorpay_email);
                                payload.put("order_id", order_id);
                                payload.put("display_logo", false);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                //   JSONArray jArray = new JSONArray ( );
//                jArray.put("in.org.npci.upiapp");
//                jArray.put("com.snapwork.hdfc");
                                //jArray.put ("com.google.android.apps.nbu.paisa.user");
                                payload.put("display_logo", false);
                                payload.put("description", desc);
                                payload.put("key_id", razor_pay);
                                payload.put("method", "upi");
                                //   payload.put("vpa", upi);
                                payload.put("_[flow]", "intent");
                                if (paymentMode.equals("GOOGLE_PAY")) {
                                    payload.put("upi_app_package_name", "com.google.android.apps.nbu.paisa.user");
                                } else if (paymentMode.equals("PHONE_PE")) {
                                    payload.put("upi_app_package_name", "com.phonepe.app");
                                } else if (paymentMode.equals("PAYTM")) {
                                    payload.put("upi_app_package_name", "net.one97.paytm");
                                }

                                // payload.put("preferred_apps_order", jArray);
                                // payload.put("other_apps_order", jArray);
                                sendRequest();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                        }

                    }
                    else
                    {
                       module.errorToast(PaymentActivity.this,""+obj.getString("message"));
                        loadingBar.dismiss();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loadingBar.dismiss();
                module.showVolleyError(error);
            }
        });
    }



    public  void managePaymentMethod(LinearLayout lin,CheckBox checkBox,String mode){
        btn_pay.setText("PAY");
        if (checkBox.isChecked()){
            checkBox.setChecked(false);
            lin.setBackgroundDrawable(ContextCompat.getDrawable(PaymentActivity.this, R.drawable.outline_rounded_15dp) );

        }else {
            paymentMode = mode;
            checkBox.setChecked(true);
            lin.setBackgroundDrawable(ContextCompat.getDrawable(PaymentActivity.this, R.drawable.green_outline_rounded_15dp) );

        }
        findCheckMode(mode);

    }
    public void findCheckMode(String mode){
        Log.e("dcfvgbh",mode);
        if (mode.equals("GOOGLE_PAY")){
            unchecked(check_PhnPe,check_paytm,check_other,lin_bg_phnpe,lin_bg_paytm,lin_bg_other);
        }else if (mode.equals("PHONE_PE")){
            unchecked(check_Gpay,check_paytm,check_other,lin_bg_Gpay,lin_bg_paytm,lin_bg_other);
        }else if (mode.equals("PAYTM")){
            unchecked(check_PhnPe,check_Gpay,check_other,lin_bg_phnpe,lin_bg_Gpay,lin_bg_other);
        }else if (mode.equals("OTHER")){
            unchecked(check_PhnPe,check_Gpay,check_paytm,lin_bg_phnpe,lin_bg_paytm,lin_bg_Gpay);
        }

        managePayButton();

    }
    public void unchecked(CheckBox cb1,CheckBox cb2,CheckBox cb3,LinearLayout lin1,LinearLayout lin2,LinearLayout lin3){
        cb1.setChecked(false);
        cb2.setChecked(false);
        cb3.setChecked(false);
        lin1.setBackgroundDrawable(ContextCompat.getDrawable(PaymentActivity.this, R.drawable.outline_rounded_15dp) );
        lin2.setBackgroundDrawable(ContextCompat.getDrawable(PaymentActivity.this, R.drawable.outline_rounded_15dp) );
        lin3.setBackgroundDrawable(ContextCompat.getDrawable(PaymentActivity.this, R.drawable.outline_rounded_15dp) );

    }
    public void managePayButton(){
        if (!check_Gpay.isChecked()&&!check_PhnPe.isChecked()&&!check_paytm.isChecked()&&!check_other.isChecked()){
//            btn_pay.setVisibility(View.GONE);
            btn_pay.setEnabled(false);
            btn_pay.setBackgroundDrawable(ContextCompat.getDrawable(PaymentActivity.this, R.color.light_gray) );

            paymentMode="";
        }else {
            btn_pay.setEnabled(true);
            btn_pay.setBackgroundDrawable(ContextCompat.getDrawable(PaymentActivity.this, R.color.razor_blue) );
        }
    }


    public void showExistDialog(){
        final Dialog dialog=new Dialog(PaymentActivity.this);
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
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExistDialog();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPaymentSuccess(String s) {
        module.successToast (PaymentActivity.this,"Success");
       // Toast.makeText (this, "Success", Toast.LENGTH_SHORT).show ( );
        payment_webview.setVisibility(View.GONE);
        my_error="";
        Intent intent=new Intent(PaymentActivity.this,MainActivity.class);
        startActivity (intent);
        finish ();

    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.d ("error_msg", "onPaymentError: "+s);
        payment_webview.setVisibility(View.GONE);
         module.errorToast (PaymentActivity.this,"Error");
        my_error="error";
        Intent intent=new Intent(PaymentActivity.this,AddFundFragment.class);
        startActivity (intent);
        finish ();
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult (requestCode, resultCode, data);
//        razorpay.onActivityResult (requestCode, resultCode, data);
//    }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed ( );

        finish ();
    }

    private void payViaUpi(String transactionId, String payeeVpa, String payeeName, String transactionRefId, String description, String amount,String m_code) {
        // START PAYMENT INITIALIZATION
        Log.e("fcvgbh",amount+"----"+payeeVpa+"----"+transactionId+"---"+description+"-----"+payeeVpa);

        upi_flag=true;
        mEasyUpiPayment = new EasyUpiPayment.Builder()
                .with(this)
                .setPayeeVpa(payeeVpa)
                .setPayeeName(payeeName)
                .setTransactionId(transactionId)
                .setTransactionRefId(transactionRefId)
                .setDescription(description)
                .setPayeeMerchantCode(m_code)
                .setAmount(amount)
                .build();

        // Register Listener for Events
        mEasyUpiPayment.setPaymentStatusListener(this);


        switch (paymentMode) {
            //  switch (upi_type) {
            case "None":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.NONE);
                break;
            case "OTHER":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.NONE);
                break;
            case "GOOGLE_PAY":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.GOOGLE_PAY);
                break;
            case "PHONE_PE":
//                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.PHONE_PE);
                if (gatewayStatus.equals("8")||gatewayStatus.equals("9")||gatewayStatus.equals("10")){
                    mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.PHONE_PE);
                }else {
                    mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.AMAZON_PAY);
                }

                break;
            case "PAYTM":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.PAYTM);
                break;

        }

//        mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.NONE);

        // Check if app exists or not
        if (mEasyUpiPayment.isDefaultAppExist()) {
            onAppNotFound();
            return;
        }
        // END INITIALIZATION

        // START PAYMENT
        mEasyUpiPayment.startPayment();
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        Log.e("transactionDetails",""+transactionDetails);

        if(transactionDetails.getStatus().equalsIgnoreCase("success")) {
            String user_id= sessionMangement.getUserDetails().get(KEY_ID);
            if (gatewayStatus.equals("8")||gatewayStatus.equals("9")||gatewayStatus.equals("10")){
                SuccessBidDailoge();
//                saveInfoIntoDatabase(user_id, s_amount, "pending", "Add", transactionDetails.getTransactionId().toString());
            }else {
                saveInfoIntoDatabase(user_id, s_amount, "approved", "Add", transactionDetails.getTransactionId().toString());
            }

        } else {
            module.showToast("Payment Failed.");
            finish ();
        }
    }

    @Override
    public void onTransactionSuccess() {

    }

    @Override
    public void onTransactionSubmitted() {

    }

    @Override
    public void onTransactionFailed() {
        module.showToast("Failed");
        finish ();
    }

    @Override
    public void onTransactionCancelled() {
        module.showToast("Cancelled");
        finish ();
    }

    @Override
    public void onAppNotFound() {
        module.showToast("App Not Found");
    }

    void getUpiId(){

        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id", sessionMangement.getUserDetails().get(KEY_ID));
        params.put("amount",s_amount);
        Log.e("upu_id_param",params.toString() );
        module.postRequest(URL_UPI_ID, params, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loadingBar.dismiss ();
                Log.e("URL_UPI_ID",response.toString());
//                pa="cfmer.395967826576@icici";
                try {
                    JSONObject obj = new JSONObject(response);
                    String resp=obj.getString("status");
                    if(resp.equalsIgnoreCase("success"))
                    {
                        JSONObject object = obj.getJSONObject("data");
                        pa = object.getString("UPI");

                    } else {
                        getUpiId();
                        loadingBar.dismiss();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loadingBar.dismiss();
                module.showVolleyError(error);
            }
        });
    }

    void getCompleteGatewayDetails(){

        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        Log.e("getGatewayDetails_param",params.toString() );
        String finalURL= URL_UPI_GATEWAY_COMPLETE_DETAILS+"/"+sessionMangement.getUserDetails().get(KEY_ID)+"/"+s_amount;
        module.postRequest(finalURL, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingBar.dismiss ();
                Log.e("getGatewayDetails",response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    boolean resp=obj.getBoolean("responce");
//                    {"responce":true,"message":{"status":"TXN","message":"Successfully Created Intent","upi_qr":"upi:\/\/pay?pa=swipe.6374140342@icici&pn=A.K.+ENTERPRISES&tr=EZV2023032210253893733967&am=100&cu=INR&mc=5691","refno":"EZV2023032210253893733967","txnid":"KSKT2210253695"}}
                    if(resp)
                    {
                        JSONObject data = obj.getJSONObject("message");
                        String qr_link = data.getString("upi_qr");
                        tran_ref_id = data.getString("refno");
                        String[] items = qr_link.split("&");

                        for (String item : items)
                        {
                            System.out.println("item = " + item);
                            String str[] = item.split("=");

                            switch (str[0]){
                                case "upi://pay?pa":
                                    pa = item.replace("upi://pay?pa=","");
                                    break;
                                case "pn":
                                    pn = item.replace("pn=","");
                                    break;
                                case "tr":
                                    tr = item.replace("tr=","");
                                    break;
                                case "txn":
                                    txn = item.replace("txn=","");
                                    break;
                                case "am":
                                    am = item.replace("am=","");
                                    break;
                                case "cu":
                                    cu = item.replace("cu=","");
                                    break;
                                case "mc":
                                    mc = item.replace("mc=","");
                                    break;
                            }
                        }
                        Log.e("all_params",pa+"---"+pn+"---"+tr+"---"+txn+"---"+am+"---"+cu+"---"+mc);

                        switch (paymentMode) {
                            //  switch (upi_type) {
                            case "GOOGLE_PAY":
                                qr_link = qr_link.replace("upi://pay?pa","gpay://pay?pa");
                                break;
                            case "PHONE_PE":
                                qr_link = qr_link.replace("upi://pay?pa","phonepe://pay?pa");
                                break;
                            case "PAYTM":
                                qr_link = qr_link.replace("upi://pay?pa","paytm://pay?pa");
                                break;
                        }
                    }
                    else
                    {
                        getCompleteGatewayDetails();
                        loadingBar.dismiss();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loadingBar.dismiss();
                module.showVolleyError(error);
            }
        });
    }
    void getGatewayDetails(){

        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        if (gatewayStatus.equals("8")) {
            params.put("user_id", sessionMangement.getUserDetails().get(KEY_ID));
            params.put("amount", s_amount);
        }
        Log.e("getGatewayDetails_param",params.toString() );
        module.postRequest(URL_UPI_GATEWAY_DETAILS, params, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loadingBar.dismiss ();
                Log.e("getGatewayDetails",response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    String resp=obj.getString("status");

                    if(resp.equalsIgnoreCase("success"))
                    {

                        JSONObject data = obj.getJSONObject("data");
                        String qr_link = data.getString("qr_link");
                        qr_link = qr_link+"&am="+s_amount;
//                        qr_link = qr_link+"&am="+"1";
                        Log.e("fcvghbj",qr_link);
                        tran_ref_id = data.getString("refid");
//                        String s="upi://pay?pa=q63720472@ybl&pn=rahulnamdev&tr=EZY2022121215300389110320&txn=EZY2022121215300389110320&am=1.00&cu=INR&mc=5691";
                        String[] items = qr_link.split("&");

                        for (String item : items)
                        {
                            System.out.println("item = " + item);
                            String str[] = item.split("=");

                            switch (str[0]){
                                case "upi://pay?pa":
                                    pa = item.replace("upi://pay?pa=","");
                                    break;
                                case "pn":
                                    pn = item.replace("pn=","");
                                    break;
                                case "tr":
                                    tr = item.replace("tr=","");
                                    break;
                                case "txn":
                                    txn = item.replace("txn=","");
                                    break;
                                case "am":
                                    am = item.replace("am=","");
                                    break;
                                case "cu":
                                    cu = item.replace("cu=","");
                                    break;
                                case "mc":
                                    mc = item.replace("mc=","");
                                    break;
                            }
                        }
                        Log.e("all_params",pa+"---"+pn+"---"+tr+"---"+txn+"---"+am+"---"+cu+"---"+mc);

                        switch (paymentMode) {
                            //  switch (upi_type) {
                            case "GOOGLE_PAY":
                                qr_link = qr_link.replace("upi://pay?pa","gpay://pay?pa");
                                break;
                            case "PHONE_PE":
                                qr_link = qr_link.replace("upi://pay?pa","phonepe://pay?pa");
                                break;
                            case "PAYTM":
                                qr_link = qr_link.replace("upi://pay?pa","paytm://pay?pa");
                                break;
                        }


//                            String url = "upi://pay?pa=q63720472@ybl&pn=rahulnamdev&tr=EZY2022121215300389110320&txn=EZY2022121215300389110320&am=1.00&cu=INR&mc=5691";
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.setAction(Intent.ACTION_VIEW);
//                            intent.setData(Uri.parse(qr_link));
//                            Intent chooser = Intent.createChooser(intent, "Pay with...");
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                startActivityForResult(chooser, UPI_REQUEST_CODE, null);
//                            }
                    }
                    else
                    {
                        getGatewayDetails();
//                       module.errorToast(PaymentActivity.this,"Something went wrong");
                        loadingBar.dismiss();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loadingBar.dismiss();
                module.showVolleyError(error);
            }
        });
    }
    private void saveInfoIntoDatabase(final String user_id, final String points, final String st,String type,String trans_id) {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",user_id);
        params.put("points",points);
        params.put("request_status",st);
        params.put("type",type);
        params.put("trans_id",trans_id);
        params.put("w_type","");
        params.put("ref_id",tran_ref_id);

        Log.e("TAG", "saveInfoIntoDatabase: "+params.toString() );
        module.postRequest(URL_REQUEST, params, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                Log.e("add_fund",response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    boolean resp=obj.getBoolean("responce");
                    if(resp)
                    {
                         module.successToast (PaymentActivity.this,""+obj.getString("message"));
                        loadingBar.dismiss();
                        SuccessBidDailoge();
                    } else {
                       module.errorToast(PaymentActivity.this,""+obj.getString("error"));
                        loadingBar.dismiss();
                        finish();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loadingBar.dismiss();
                module.showVolleyError(error);
            }
        });
    }
    public  void  SuccessBidDailoge(){
        Dialog dialog;
        dialog = new Dialog (PaymentActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        dialog.setContentView (R.layout.dailoge_custom_bitsuccess );
        dialog.show ( );
        TextView tv_luck,tv_bid;
        Button btn_ok;
        RelativeLayout rel_img;

        tv_luck = dialog.findViewById (R.id.tv_luck);
        btn_ok = dialog.findViewById (R.id.btn_ok);
        tv_bid= dialog.findViewById (R.id.tv_bid);
        rel_img= dialog.findViewById (R.id.rel_img);

        if ((gatewayStatus.equals("10")||gatewayStatus.equals("9")||gatewayStatus.equals("8"))&& paymentMode.equals("PHONE_PE")){
            tv_luck.setText("Payment Under Processing !");
            tv_bid.setVisibility(View.GONE);
            rel_img.setBackgroundTintList(getResources().getColorStateList(R.color.dark_orangr2));
            btn_ok.setBackgroundTintList(getResources().getColorStateList(R.color.dark_orangr2));
        }else {
            tv_luck.setText("Payment Successful !");
            tv_bid.setText("Transaction Success. Points Added To Your Wallet");
        }

        btn_ok.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                dialog.dismiss ( );
                my_error="";
                Intent intent=new Intent(PaymentActivity.this,MainActivity.class);
                startActivity (intent);
                finish ();
            }
        });


        dialog.setCanceledOnTouchOutside (false);
        dialog.setCancelable(false);
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (!gatewayStatus.equals("3")&&!gatewayStatus.equals("8")&&!gatewayStatus.equals("9")&&!gatewayStatus.equals("10")) {//confirm
            razorpay.onActivityResult(requestCode, resultCode, data);
        }else {
            try {
                Log.e("UPI RESULT REQUEST CODE", String.valueOf(requestCode));
                Log.e("UPI RESULT RESULT CODE", String.valueOf(resultCode));
                Log.e("UPI_REQUEST_CODE", String.valueOf(UPI_REQUEST_CODE));
                Log.e("UPI RESULT DATA-->", "" + data.getStringExtra("Status"));
                Log.e("UPI RESULT DATA-->", "" + data.getExtras().toString());

                if (requestCode == UPI_REQUEST_CODE) {
                    Log.d("result", data.toString());
                    if (data.getStringExtra("Status").equalsIgnoreCase("SUCCESS")) {
                        String user_id = sessionMangement.getUserDetails().get(KEY_ID);
                        if (gatewayStatus.equals("8")||gatewayStatus.equals("9")||gatewayStatus.equals("10")){
                            SuccessBidDailoge();
//                            saveInfoIntoDatabase(user_id, am, "pending", "Add", data.getStringExtra("txnId"));
                        }else {
                            saveInfoIntoDatabase(user_id, am, "approved", "Add", data.getStringExtra("txnId"));
                        }
                    } else {
                        module.showToast("Payment Failed.");
                        finish();
                    }
//                data?.getStringExtra("Status")?.let { Log.d("result", it) };
//                data?.getStringExtra("Status")?.let { Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show() };
                    Log.e("cfvgbhn", "200 Success");
                } else {
                    Log.e("cfvgbhn", "400 Failed");
                }

            } catch (Exception e) {
                Log.e("Error in UPI", e.getMessage());
            }
        }
    }

    private void startUpiPayment(String transactionId, String payeeVpa, String payeeName,
                                 String transactionRefId, String description, String amount,
                                 String merchant_code,String txnRefId) {
        //and pass this to: setUpiApps(newList): or setUpiApps(appList):
        PaymentDetail payment = new PaymentDetail(payeeVpa,payeeName,merchant_code,txnRefId,
                description,String.valueOf(Double.parseDouble(amount)));

//        PaymentDetail payment = new PaymentDetail("8530366082rainet@icici","rahul namdev",
//                "5691","EZY2022121215300389110320", description,String.valueOf(Double.parseDouble("1")));
        try {
            newList.clear();
            newList.addAll(UpiPayment.getExistingUpiApps(PaymentActivity.this));
            newList.remove(newList.indexOf("phonepe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        new UpiPayment(this)
                .setPaymentDetail(payment)
                .setUpiApps(newList)
                .setCallBackListener(new UpiPayment.OnUpiPaymentListener() {
                    @Override
                    public void onSuccess(@NonNull com.wangsun.upi.payment.model.TransactionDetails transactionDetails) {
                        Log.e("success", String.valueOf(transactionDetails));
                        if(transactionDetails.getStatus().equalsIgnoreCase("success")){
                            String trans="";
                            if(module.checkNullField (transactionDetails.getTransactionId().toString()))
                            {
                                trans=transactionId;
                            }
                            else {
                                trans= transactionDetails.getTransactionId().toString();
                            }
                            SuccessBidDailoge();
//                            saveInfoIntoDatabase(sessionMangement.getUserDetails().get(KEY_ID), amount, "approved", "Add", trans);
                        } else{
                           module.errorToast(PaymentActivity.this,"Payment Failed. Try again later");
                            finish();
                        }
                    }

                    @Override
                    public void onSubmitted(@NonNull com.wangsun.upi.payment.model.TransactionDetails transactionDetails) {
                        Log.e("onSubmitted", String.valueOf(transactionDetails));
                        Toast.makeText(PaymentActivity.this, "transaction pending: " + transactionDetails, Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onError( String message) {
                        Log.e("error", String.valueOf(message));
                       module.errorToast(PaymentActivity.this,"Payment Failed. Try again later");
                        finish();
                    }
                }).pay();
    }

}