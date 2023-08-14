package in.games.GamesSattaBets.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
//import com.razorpay.Checkout;
//import com.razorpay.Order;
//
//
// import com.razorpay.Order;
//import com.razorpay.RazorpayClient;
//import com.razorpay.RazorpayException;
//import com.razorpay.RazorpayClient;
//import com.razorpay.RazorpayException;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;
import com.wangsun.upi.payment.UpiPayment;
import com.wangsun.upi.payment.model.PaymentDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.GamesSattaBets.Activity.MainActivity;
import in.games.GamesSattaBets.Activity.PaymentActivity;
import in.games.GamesSattaBets.Activity.PaymentWebViewActivity;
import in.games.GamesSattaBets.Adapter.AddFundPointAdpter;
import in.games.GamesSattaBets.Config.BaseUrls;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Interfaces.GetAppSettingData;
import in.games.GamesSattaBets.Model.IndexResponse;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.ConnectivityReceiver;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.RecyclerTouchListener;
import in.games.GamesSattaBets.Util.SessionMangement;


import static in.games.GamesSattaBets.Config.BaseUrls.URL_CNC_PAY;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_GET_GATEWAY;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_INDEX;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_KASTAKAR_PAY;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_OrderId;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_PAYMENT_GATEWAY_LINK;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_REQUEST;
import static in.games.GamesSattaBets.Config.Constants.KEY_EMAIL;
import static in.games.GamesSattaBets.Config.Constants.KEY_ID;
import static in.games.GamesSattaBets.Config.Constants.KEY_NAME;
import static in.games.GamesSattaBets.Fragment.FundFragment.my_error;

//
public class AddFundFragment extends AppCompatActivity implements View.OnClickListener, PaymentStatusListener {
SwipeRefreshLayout swipe;
ImageView civ_logo,img_back;
TextView tv_title,tv_message,tv_whatsapp,tv_wallet,tv_walletAmount;
EditText et_points;
LinearLayout lin_whatsapp,lin_whats;
Button btn_add;
SessionMangement sessionMangement;
RecyclerView rec_point;
private EasyUpiPayment mEasyUpiPayment;
AddFundPointAdpter adapter;
boolean upi_flag=false;
//ArrayList<AddFundPointModel> pointlist;
ArrayList<String> pointlist;
Module module;
String pnts,wallet_amt="",withdr_no="";
public  static  String razorpay_email="",minAmount="",razorpay_mobile="";
LoadingBar loadingBar;
String order_val="" ;
String orderid="";
ArrayList<String> newList = new ArrayList<String>();
public static String themeColor,desc,imageUrl,requestStatus,gatewayStatus;
public  static  String upi="",upi_name="",upi_desc="",upi_type="",transactionId="",webview_link="",upi_merchant_code="";
public static String razor_pay_order_id="",razor_pay="",secrete_key="",s_amount="",s_transaction_id="",u_name="",company_upi_id="";
public static int UPI_REQUEST_CODE=101;
String radio_upipay="";
String phonepay_val="false";
String tran_ref_id="";
ProgressDialog progressDialog;
int count = 0;
String your_user_id = "";


    public AddFundFragment() {
        // Required empty public constructor
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.fragment_add_fund);
        initView ( );

        tv_title.setText ("Add Fund");
        if (ConnectivityReceiver.isConnected ( )) {
            tv_wallet.setText (module.getAndSetWalletAmount ( ));
            tv_walletAmount.setText ("Rs. " + module.getAndSetWalletAmount ( ));
            Log.e ("sdfrgthy", module.getAndSetWalletAmount ( ));
            init_RecyclerView ( );
            getGatewaySetting ( );
        } else {
            module.showToast ("No Internet Connection");
        }
        rec_point.addOnItemTouchListener (new RecyclerTouchListener (AddFundFragment.this, rec_point, new RecyclerTouchListener.OnItemClickListener ( ) {
            @Override
            public void onItemClick(View view, int position) {
                Log.e ("sadfg", pointlist.get (position));
                if (Integer.parseInt(pointlist.get(position))>=Integer.parseInt(minAmount)) {
                    et_points.setText(pointlist.get(position));
                }
            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

    private void initView() {

        loadingBar = new LoadingBar(AddFundFragment.this);
        module = new Module(AddFundFragment.this);
        sessionMangement = new SessionMangement(AddFundFragment.this);
        your_user_id = sessionMangement.getUserDetails ().get (KEY_ID);
        progressDialog = new ProgressDialog(AddFundFragment.this);
        progressDialog.setMessage("Transaction in progress...");

        swipe = findViewById(R.id.swipe);
        civ_logo =findViewById(R.id.civ_logo);
        tv_message =findViewById(R.id.tv_message);
        tv_whatsapp = findViewById(R.id.tv_whatsapp);
        tv_wallet = findViewById(R.id.tv_wallet);
        tv_walletAmount = findViewById(R.id.tv_walletAmount);
        et_points = findViewById(R.id.et_points);
        tv_title = findViewById(R.id.tv_title);
        img_back = findViewById(R.id.img_back);
        lin_whatsapp = findViewById(R.id.lin_whatsapp);
        btn_add = findViewById(R.id.btn_add);
        rec_point = findViewById(R.id.rec_point);
        lin_whats = findViewById(R.id.lin_whats);

        lin_whats.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        lin_whatsapp.setOnClickListener(this);
        img_back.setOnClickListener(this);

        module.getConfigData(new GetAppSettingData() {
            @Override
            public void getAppSettingData(IndexResponse model) {
                tv_whatsapp.setText(model.getWithdraw_no());
                withdr_no =model.getWithdraw_no();
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing()){
                    tv_walletAmount.setText("Rs. "+module.getAndSetWalletAmount());
                }
            }
        });
    }

    private void init_RecyclerView()
    {
        rec_point.setHasFixedSize(true);
        rec_point.setLayoutManager(new GridLayoutManager(AddFundFragment.this,4));

        pointlist= new ArrayList<>();
        HashMap<String, String> params = new HashMap<> ( );
        module.postRequest (URL_INDEX, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String resp) {
                Log.e ("money_list", "onResponse: " + resp.toString ( ));
                try {
                    JSONObject jsonObject = new JSONObject (resp);
                    Boolean result = Boolean.valueOf (jsonObject.getString ("responce"));
                    if (result) {
                        JSONArray arr = jsonObject.getJSONArray ("data");
                        JSONObject dataObj=arr.getJSONObject(0);
                        minAmount=dataObj.getString ("minimum_amount");

                        JSONArray data=new JSONArray (dataObj.getString ("money_list"));
                        for (int i = 0; i < data.length(); i++) {
                            pointlist.add(data.getString(i));
                        }

                        adapter = new AddFundPointAdpter(AddFundFragment.this,pointlist);
                        rec_point.setAdapter(adapter);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  showVolleyError(error);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
//            minAmount=0;
            case R.id.btn_add:
                if(TextUtils.isEmpty(et_points.getText().toString())) {
                    et_points.setError("Enter Some Points");
                } else if((Integer.parseInt(et_points.getText().toString().trim()))<(Integer.parseInt(minAmount))) {
                    module.fieldRequired("Minimum Range for points is "+ minAmount);
                } else {
                    count=0;
                    getOrderId (et_points.getText().toString() );
                }

                break;
            case R.id.lin_whatsapp:
                module.whatsapp(withdr_no,"Hello! Admin ");
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

    private void onValidatingData( String orderid){
        int points=0;

            if(TextUtils.isEmpty(et_points.getText().toString())) {
                et_points.setError("Enter Some Points");
                return;
            } else {
                points=Integer.parseInt(et_points.getText().toString().trim());
                if(points<Integer.parseInt(minAmount))
                {
                    module.fieldRequired("Minimum Range for points is "+ minAmount);

                } else {
                    pnts=String.valueOf(points);
                    String p=String.valueOf(points);
                    String st="pending";
                    transactionId = "TXN" + System.currentTimeMillis();
                    String transactionRefId = transactionId;
                    u_name=sessionMangement.getUserDetails().get(KEY_NAME);
                    s_amount=p;
                    s_transaction_id=transactionRefId;

                    if (ConnectivityReceiver.isConnected()) {
                        if(gatewayStatus.equals("0")){
                            saveInfoIntoDatabase(your_user_id, pnts, requestStatus, "Add","");
                        }else if(gatewayStatus.equals("1")){
                            //razorpay
//                            on status 1
//                            1- hit paymentuniq_id api(get order id).
//                            2- on click on pay hit payment_order api.
//                            3- no API hit on payment success.

//                            loadingBar.show ();
//                            ExampleAsync calBestObj = new ExampleAsync();
//                            calBestObj.execute();
                            if(module.checkNull (orderid).equalsIgnoreCase (""))
                            {
                               module.errorToast ("Something went Wrong");
                            }
                            else {
                                Intent intents = new Intent (AddFundFragment.this, PaymentActivity.class);
                                intents.putExtra ("email", sessionMangement.getUserDetails ( ).get (KEY_EMAIL));
                                intents.putExtra ("mob", razorpay_mobile);
                                intents.putExtra ("code", "");
                                intents.putExtra ("order_id_value", orderid);
                                startActivity (intents);
                            }
                           }else if (gatewayStatus.equals("2")){
                            // upi gateway.
                            String payeeVpa = upi;
                            String payeeName = upi_name;
                            try {
                                newList.clear();
                                newList.addAll(UpiPayment.getExistingUpiApps(AddFundFragment.this));
                                newList.remove(newList.indexOf("phonepe"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            startUpiPayment(transactionId, payeeVpa, payeeName, transactionRefId, desc, pnts,"","");

                        }else if (gatewayStatus.equals("3")){
//                            on status 3
//                            1- hit paymentuniq_id api(get order id).
//                            2- hit get_payment to get payment detials
//                            3- on click on pay call easy upi gateway.
//                            4- on payment success call add_request api.

//                            q63720472@ybl
//                            String url = "upi://pay?pa=q63720472@ybl&pn=rahulnamdev&tr=EZY2022121215300389110320&txn=EZY2022121215300389110320&am=1.00&cu=INR&mc=5691";
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.setAction(Intent.ACTION_VIEW);
//                            intent.setData(Uri.parse(url));
//                            Intent chooser = Intent.createChooser(intent, "Pay with...");
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                startActivityForResult(chooser, UPI_REQUEST_CODE, null);
//                            }

                            //razor pay like design with upi gateway.
                            Intent intents = new Intent (AddFundFragment.this, PaymentActivity.class);
                            startActivity (intents);
                        }else if(gatewayStatus.equals("4")){
                            //web view
                            Intent intents = new Intent (AddFundFragment.this, PaymentWebViewActivity.class);
                            intents.putExtra("url",orderid);
                            startActivity (intents);
                        }else if(gatewayStatus.equals("5")){
//                            on status 5
//                            custom razor pay.
//                            1- hit getorderid_pg api(get order id from txn_id field).
//                            2- on click on pay hit payment_order api.
//                            3- no API hit on payment success.

                            if(module.checkNull (orderid).equalsIgnoreCase (""))
                            {
                                module.errorToast ("Something went Wrong");
                            }
                            else {

                                Intent intents = new Intent (AddFundFragment.this, PaymentActivity.class);
                                intents.putExtra ("email", sessionMangement.getUserDetails ( ).get (KEY_EMAIL));
                                intents.putExtra ("mob", razorpay_mobile);
                                intents.putExtra ("code", "");
                                intents.putExtra ("order_id_value", orderid);
                                startActivity (intents);
                            }
                        }else if(gatewayStatus.equals("6")){
                            Log.e("zsxdftgyhuji",webview_link);
                            Intent intents = new Intent (AddFundFragment.this, PaymentWebViewActivity.class);
                            intents.putExtra("url",webview_link+your_user_id+"/"+
                                    s_amount);
                            startActivity(intents);
                        }
                        else if(gatewayStatus.equals("7")){
                            Log.e("zsxdftgyhuji",webview_link+your_user_id+"/"+
                                    s_amount);

                            String url = webview_link+"/"+your_user_id+"/"+s_amount;
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            Intent chooser = Intent.createChooser(intent, "Pay with...");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                startActivityForResult(chooser, 348, null);
                            }

                        }else if (gatewayStatus.equals("8")){
                            //razor pay like design with upi gateway. same as status
                            // 3 gateway, only the difference is, post pending status on payment success.
                            Intent intents = new Intent (AddFundFragment.this, PaymentActivity.class);
                            intents.putExtra ("order_id_value", orderid);
                            startActivity (intents);

                        }else if (gatewayStatus.equals("9")){
                            //razor pay like design with upi gateway. same as status
                            // 8 gateway, only the difference is, post pending status on payment success.
                            Intent intents = new Intent (AddFundFragment.this, PaymentActivity.class);
                            intents.putExtra ("order_id_value", "");
                            startActivity (intents);

                        }else if (gatewayStatus.equals("10")){
                            //razor pay like design with upi gateway. same as status
                            // 9 gateway, only the difference is, hit new api to get gatway details.
                            Intent intents = new Intent (AddFundFragment.this, PaymentActivity.class);
                            intents.putExtra ("order_id_value", "");
                            startActivity (intents);

                        }else if(gatewayStatus.equals("11")){
                            Log.e("zsxdftgyhuji",webview_link);
                            Intent intents = new Intent (AddFundFragment.this, PaymentWebViewActivity.class);
                            intents.putExtra("url",webview_link+your_user_id+"/"+
                                    s_amount);
                            startActivity(intents);

                        } else if (gatewayStatus.equals("12")) {
//                            String url = "upi://pay?pa=q63720472@ybl&pn=rahulnamdev&tr=EZY2022121215300389110320&txn=EZY2022121215300389110320&am=1.00&cu=INR&mc=5691";
//                            String url = orderid.replace("am=100.0","am="+pnts);

                            Log.e("dftgyhuj",orderid);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(orderid));
                            Intent chooser = Intent.createChooser(intent, "Pay with...");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                startActivityForResult(chooser, UPI_REQUEST_CODE, null);
                            }
                        }
                    }
                    else
                    {
                       module.noInternet();
                    }
                }
            }
        }

    public void getOrderId(String final_points) {
        String url="";
        if (gatewayStatus.equals("4")||gatewayStatus.equals("5")) {
             url = URL_PAYMENT_GATEWAY_LINK;
            if (count==0) {
                loadingBar.show();
            }
        }else {
            if (gatewayStatus.equals("12")){
                url = URL_CNC_PAY+"/"+your_user_id+"/"+final_points;
//                url = URL_KASTAKAR_PAY+"/"+your_user_id+"/"+final_points;

            }else {
                url = URL_OrderId;
            }
            loadingBar.show();
        }

        HashMap<String,String> params=new HashMap<>();
        String amt=String.valueOf (et_points.getText().toString());
        params.put("user_id",your_user_id);
        params.put("amount",amt);
        Log.e("sendRequest", "saveInfoIntoDatabase: "+params.toString() );
        module.postRequest(url, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (gatewayStatus.equals("4")) {
                    if (count == 0) {
                        loadingBar.dismiss();
                    }
                }else {
                    loadingBar.dismiss();
                }
                Log.e("oder_id_valu",response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    boolean resp;
                    if (gatewayStatus.equals("4")||gatewayStatus.equals("5")){
                        resp = obj.getBoolean("response");
                    }else {
                        resp = obj.getBoolean("responce");
                    }
                    if(resp)
                    {
                        if (gatewayStatus.equals("4")){
                            JSONObject object =obj.getJSONObject ("message");
                            String url = object.getString("dyn_txn_url");
                            String time = object.getString("delayTime");
                            long timer = Long.parseLong(time);
                            if (gatewayStatus.equals("4")) {
                                if (count == 0) {
                                    progressDialog.show();
                                }
                            }
                            if (object.getString("status").equalsIgnoreCase("INITIATE")){
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        onValidatingData(url);
                                    }
                                },timer);

                            }else {
                                if (count<10) {
                                    count++;
                                    getOrderId(final_points);
                                }else {
                                    dialogPaymentError(final_points);
                                }
                            }
                        }
                        else if (gatewayStatus.equals("5")){
                            JSONObject object = obj.getJSONObject ("message");
                            order_val = object.getString ("txn_id");
                            razor_pay = object.getString ("appkey");
                            onValidatingData(order_val);

                        } else if (gatewayStatus.equals("12")) {
                            order_val = obj.getString("pay_url");
                            onValidatingData(order_val);

//                            JSONObject object = obj.getJSONObject ("message");
//                            order_val = object.getString ("upi_intent_link");
//                            onValidatingData(order_val);


//                            {"responce":true,"message":{"upi_intent_link":"upi:\/\/pay?pa=Sharukh@indianbk&pn=Sharukh@indianbk&mc=5816&tr=2012316016420326057&tn=Pay&am=1.00&cu=INR&refUrl=",
//                                    "transaction_id":"inpc1d2de04ff1686309151"}}


                        } else {
                            order_val = obj.getString ("message");
                            onValidatingData(order_val);

                        }
                    } else {
                        module.errorToast(""+obj.getString("message"));

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (gatewayStatus.equals("4")) {
                    if (count == 0) {
                        loadingBar.dismiss();
                    }
                }else {
                    loadingBar.dismiss();
                }
                module.showVolleyError(error);
            }
        });
        // return order_val;
    }


    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails)
    {
        Log.e("transactionDetails",""+transactionDetails);
        if(transactionDetails.getStatus().equalsIgnoreCase("success"))
        {
            saveInfoIntoDatabase(your_user_id, pnts, "approved", "Add", transactionDetails.getTransactionId().toString());
        } else {
             module.showToast("Payment Failed.");
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
    }

    @Override
    public void onTransactionCancelled() {
        module.showToast("Cancelled");
    }

    @Override
    public void onAppNotFound() {
        module.showToast("App Not Found");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(upi_flag){
            try {
                mEasyUpiPayment.detachListener();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
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
                        module.successToast(""+obj.getString("message"));
                        loadingBar.dismiss();
                        tv_walletAmount.setText("Rs. "+module.getAndSetWalletAmount());
                        SuccessBidDailoge();
                    }
                    else
                    {
                        module.errorToast(""+obj.getString("error"));
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

    public void getGatewaySetting(){
        HashMap<String,String> params=new HashMap<>();

        module.postRequest(URL_GET_GATEWAY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("gateway",response.toString());
                try {
                    JSONArray arr = new JSONArray(response);
                    JSONObject object = arr.getJSONObject(0);
                    imageUrl=object.getString("icon");
                    themeColor=object.getString("theme_color");
                    desc=object.getString("description");
                    requestStatus=object.getString("request_status");
                    gatewayStatus=object.getString("gateway_status");

//                    gatewayStatus="12";

                    if (!gatewayStatus.equals("5")) {
                        razor_pay = object.getString("razorpay_id");
                    }

                    secrete_key = object.getString("secret_key");
                    razorpay_mobile = object.getString("mobile");
                    razorpay_email = object.getString("email_id");
                    upi=object.getString ("upi_id");
                    company_upi_id=upi;
                    upi_name=object.getString ("upi_name");
                    upi_type=object.getString ("upi_type");
                    webview_link=object.getString ("webview_link");
                    upi_merchant_code=object.getString ("upi_merchant_code");
//                  upi_merchant_code="1234";
                    upi_desc=desc;

                    if (gatewayStatus.equals("0")){
                        lin_whats.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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


//    public void startPayment(int amt) {
//
//        if(loadingBar.isShowing ())
//        {
//            loadingBar.dismiss ();
//        }
//        /**
//         * Instantiate Checkout
//         */
//        //Checkout checkout = new Checkout();
////        KBRbIOyoVHe2rR
////      checkout.setKeyID("rzp_live_aJPMRDFbURlvG2");//
//        // checkout.setKeyID(razor_pay);//
////       need razorpay id than uncomment this line.
//
//
//
//        /**
//         * Set your logo here
//         */
//       // checkout.setImage(R.drawable.app_logo);
//
//        /**
//         * Reference to current activity
//         */
//        Activity activity=AddFundFragment.this;
//        /**
//         * Pass your payment options to the Razorpay Checkout as a JSONObject
//         */
//        try {
//            Log.e("dcfvgbhnjm",razorpay_email);
//            JSONObject options = new JSONObject();
//            options.put("name", sessionMangement.getUserDetails().get(KEY_NAME));
//            options.put("description", desc);
//            options.put("image", imageUrl);
//            options.put("order_id", razor_pay_order_id);//from response of step 3.
//            options.put("theme.color", themeColor);
//            options.put("currency", "INR");
//            options.put("amount", ((Double.parseDouble (String.valueOf (amt)))*100));//pass amount in currency subunits
////            options.put("amount", (500));//pass amount in currency subunits
//            options.put("prefill.email", razorpay_email);
////            options.put("prefill.email", "Sattabets@gmail.com");
//            options.put("prefill.contact",razorpay_mobile);
////            options.put("prefill.contact",sessionMangement.getUserDetails().get(KEY_MOBILE));
//          //  checkout.open(activity, options);
//        } catch(Exception e) {
//            Log.e("TAG", "Error in starting Razorpay Checkout"+ e);
//        }
//    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e("TAG", "onPaymentSuccess: "+s.toString() );
       saveInfoIntoDatabase(your_user_id, pnts,"approved","Add","");
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("TAG", "onPaymentError: "+s );
        module.errorToast("Payment Failed. Try again later");
        finish();


    }
//    private class ExampleAsync extends AsyncTask<Integer, Integer, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            // ...
//        }
//
//        @Override
//        protected String doInBackground(Integer... integers) {
//            // ...
//            try {
//
//
//                String amt=et_points.getText().toString();
//                Log.e("cfvgbhnjm",razor_pay+"-----"+secrete_key);
//               // RazorpayClient razorpay = new RazorpayClient(razor_pay, secrete_key);
//             // RazorpayClient razorpay = new RazorpayClient("<api_key>", "<api_secret>");
//                JSONObject orderRequest = new JSONObject();
//               orderRequest.put("amount", ((Double.parseDouble (String.valueOf (Integer.parseInt(et_points.getText().toString()))))*100)); // amount in the smallest currency unit
//                orderRequest.put("amount", ((Double.parseDouble (amt))*100)); // amount in the smallest currency unit
//                orderRequest.put("currency", "INR");
//                orderRequest.put("receipt", "order_rcptid_11");
//                Log.e("orderRequest",orderRequest.toString());
//                try {
//                   // Order order = razorpay.Orders.create(orderRequest);
//                 // order_KEyzvo76WeLWD4
////                    Log.e("order_data", String.valueOf(order));
////                    Log.e("order_id", order.get("id"));
////                    razor_pay_order_id = order.get("id");
//                    startPayment(Integer.parseInt(amt));
//
//                }catch (Exception ex){
//                    ex.printStackTrace();
//                }
//            }
//            catch (JSONException e) {
//                // Handle Exception
//                System.out.println(e.getMessage());
//            }
//            return "Finished!";
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//            // ...
//        }
//
//        @Override
//        protected void onPostExecute(String string) {
//            super.onPostExecute(string);
//            // ...
//        }
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d ("update_rstart", "onResume: "+"_rstart");
       // tv_wallet.setText(module.getAndSetWalletAmount());
        tv_walletAmount.setText("Rs. "+module.getAndSetWalletAmount());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed ( );
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume ( );
        Log.d ("update_resume", "onResume: "+"iiiii::::"+my_error);

        if(my_error.equalsIgnoreCase ("error"))
        {
            finish ();

        }
        else {
            loadingBar.show ( );
            HashMap<String, String> params = new HashMap<> ( );
            params.put ("user_id", your_user_id);
            module.postRequest (BaseUrls.URL_GET_WALLET_AMOUNT, params, new Response.Listener<String> ( ) {
                @Override
                public void onResponse(String response) {
                    Log.e ("wallet_resume", response.toString ( ));
                    loadingBar.dismiss ( );
                    try {
                        JSONObject object = new JSONObject (response);
                        if (object.getBoolean ("responce")) {
                            JSONArray arr = object.getJSONArray ("data");
                            JSONObject obj = arr.getJSONObject (0);
                            String wamt = obj.getString ("wallet_points");
                            tv_walletAmount.setText("Rs. "+wamt);

                        } else {
                            module.showToast ("Something went wrong");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace ( );
                    }

                }
            }, new Response.ErrorListener ( ) {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loadingBar.dismiss ( );
                    module.showVolleyError (error);
                }
            });
        }
    }

    public  void  SuccessBidDailoge(){
        Dialog dialog;
        dialog = new Dialog (AddFundFragment.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        dialog.setContentView (R.layout.dailoge_custom_bitsuccess );
        dialog.show ( );
        ImageView iv_like;
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
                Intent i = new Intent(AddFundFragment.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });


        dialog.setCanceledOnTouchOutside (false);
        dialog.setCancelable(false);
    }

    private void startUpiPayment(String transactionId, String payeeVpa, String payeeName,
                                 String transactionRefId, String description, String amount,
                                 String merchant_code,String txnRefId) {
        //and pass this to: setUpiApps(newList): or setUpiApps(appList):
        PaymentDetail payment = new PaymentDetail(payeeVpa,payeeName,merchant_code,txnRefId,
                description,String.valueOf(Double.parseDouble(amount)));

//        PaymentDetail payment = new PaymentDetail("8530366082rainet@icici","rahul namdev",
//                "5691","EZY2022121215300389110320", description,String.valueOf(Double.parseDouble("1")));
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

                            saveInfoIntoDatabase(your_user_id, amount, "approved", "Add", trans);
                        } else{
                            module.errorToast("Payment Failed. Try again later");
                            finish();
                        }
                    }

                    @Override
                    public void onSubmitted(@NonNull com.wangsun.upi.payment.model.TransactionDetails transactionDetails) {
                        Log.e("onSubmitted", String.valueOf(transactionDetails));
                        Toast.makeText(AddFundFragment.this, "transaction pending: " + transactionDetails, Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onError( String message) {
                        Log.e("error", String.valueOf(message));
                        module.errorToast("Payment Failed. Try again later");
                        finish();
                    }
                }).pay();
    }


    public  void dialogPaymentError(String final_points){
        count=0;
        progressDialog.dismiss();
        Dialog dialog;
        dialog = new Dialog (AddFundFragment.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        dialog.setCancelable(false);
        dialog.setContentView (R.layout.dailoge_nohistory);
        dialog.show ( );
        Button btn_ok;
        TextView tv_bid,tv_luck;

        tv_bid = dialog.findViewById (R.id.tv_bid);
        tv_luck = dialog.findViewById (R.id.tv_luck);
        btn_ok = dialog.findViewById (R.id.btn_ok);
        tv_bid.setText("Something went wrong, Please try again!");
        btn_ok.setText("Try Again");

        btn_ok.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                getOrderId(final_points);
                dialog.dismiss();

            }
        });
        dialog.setCanceledOnTouchOutside (true);
//        dialog.setCancelable(false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        try {
                if (requestCode == UPI_REQUEST_CODE) {
                    Log.d("result", data.toString());
                    if (data.getStringExtra("Status").equalsIgnoreCase("SUCCESS")) {
                        saveInfoIntoDatabase(your_user_id, pnts, "pending", "Add", data.getStringExtra("txnId"));
//                        SuccessBidDailoge();

                    } else {
                        module.showToast("Payment Failed.");
                        finish();
                    }
                    Log.e("UPI_REQUEST_CODE", String.valueOf(UPI_REQUEST_CODE));
                } else {
                    Log.e("UPI_REQUEST_CODE", "400 Failed");
                }

            } catch (Exception e) {
                Log.e("Error in UPI", e.getMessage());
            }
    }

}