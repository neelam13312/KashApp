package in.games.GamesSattaBets.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import in.games.GamesSattaBets.Activity.AddBankAccountActivity;
import in.games.GamesSattaBets.Activity.MainActivity;
import in.games.GamesSattaBets.Config.BaseUrls;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Interfaces.GetAppSettingData;
import in.games.GamesSattaBets.Model.IndexResponse;
import in.games.GamesSattaBets.Model.TimeSlots;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.SessionMangement;

import static in.games.GamesSattaBets.Config.BaseUrls.URL_REQUEST;
import static in.games.GamesSattaBets.Config.Constants.KEY_ACCOUNNO;
import static in.games.GamesSattaBets.Config.Constants.KEY_ID;
import static in.games.GamesSattaBets.Config.Constants.KEY_IFSC;
import static in.games.GamesSattaBets.Config.Constants.KEY_PAYTM;
import static in.games.GamesSattaBets.Config.Constants.KEY_PHONEPAY;
import static in.games.GamesSattaBets.Config.Constants.KEY_TEZ;
import static in.games.GamesSattaBets.Config.Constants.KEY_WALLET;

public class WithdrawalFundFragment extends AppCompatActivity implements View.OnClickListener {
    SwipeRefreshLayout swipe;
    ImageView civ_logo,img_back;
    TextView tv_reeedm_msg,tv_title,tv_message,tv_whatsapp,tv_wallet,tv_desciption,tv_walletAmount;
    EditText et_points;
    LinearLayout lin_whatsapp;
    Button btn_add,btn_withdraw;
    String wallet_amt="",bank_type="",withdraw_no="",max_withdraw_amount="0";
    Module module;
    SessionMangement sessionMangement;
    LoadingBar loadingBar;
    int wMinAmt=0;
    int wSaturday=0,wSunday=0;
    ArrayList<TimeSlots> timeList;
    String w_status="",status="0",withdraw_limit="";

    public WithdrawalFundFragment() {
        // Required empty public constructor
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.fragment_withdrawal_fund);
       // ((MainActivity)getActivity()).setTitles("Redeem Fund");
       // wallet_amt = ((MainActivity)getActivity()).getWallet();
        initView();
        tv_title.setText("Withdraw Fund");
        getTimeSlots();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing()){
                    getTimeSlots();
                    swipe.setRefreshing(false);
                }
            }
        });

    }

    private void initView() {

        loadingBar = new LoadingBar(WithdrawalFundFragment.this);
        sessionMangement = new SessionMangement(WithdrawalFundFragment.this);
        module = new Module(WithdrawalFundFragment.this);
        swipe = findViewById(R.id.swipe);
        civ_logo =findViewById(R.id.civ_logo);
        tv_message = findViewById(R.id.tv_message);
        tv_whatsapp =findViewById(R.id.tv_whatsapp);
        tv_reeedm_msg=findViewById(R.id.tv_reeedm_msg);
        tv_wallet =findViewById(R.id.tv_wallet);
//        et_points = findViewById(R.id.et_points);
        lin_whatsapp =findViewById(R.id.lin_whatsapp);
        btn_add = findViewById(R.id.btn_add);
        tv_desciption = findViewById(R.id.tv_desciption);
        tv_walletAmount = findViewById(R.id.tv_walletAmount);
        tv_title = findViewById(R.id.tv_title);
        img_back = findViewById(R.id.img_back);
        btn_withdraw = findViewById(R.id.btn_withdraw);
        timeList = new ArrayList<>();

        module.getConfigData(new GetAppSettingData() {
            @Override
            public void getAppSettingData(IndexResponse model) {
                tv_message.setText(model.getWithdraw_text());
                withdraw_no = model.getWithdraw_no();
                tv_desciption.setText (Html.fromHtml (model.getWithdraw_description ()));
                tv_whatsapp.setText(withdraw_no);
                String text ="All withdrawal related queries \n";
                String text1 = " whatsapp call ";
                tv_reeedm_msg.setText(Html.fromHtml(text+"\n" +"<br><font color=green >"+ "<big>"+ withdraw_no +"</big>" +"</font><br>"
                       +"\n" + text1));

                max_withdraw_amount = model.getMax_withdraw_amount();
                w_status= model.getWithdraw_status();
            }
        });


        btn_add.setOnClickListener(this);
        tv_reeedm_msg.setOnClickListener(this);
        lin_whatsapp.setOnClickListener(this);
        img_back.setOnClickListener(this);
        btn_withdraw.setOnClickListener(this);
        tv_wallet.setText(module.getAndSetWalletAmount());
        tv_walletAmount.setText("Rs. "+module.getAndSetWalletAmount());


        String tez = sessionMangement.getUserDetails().get(KEY_TEZ);
        String phonePay = sessionMangement.getUserDetails().get(KEY_PHONEPAY);
        String accountno = sessionMangement.getUserDetails().get(KEY_ACCOUNNO);
        String paytm = sessionMangement.getUserDetails().get(KEY_PAYTM);
        String ifsc = sessionMangement.getUserDetails().get(KEY_IFSC);

        Log.e("check_deatail",tez+"\n"+phonePay+"\n"+accountno+"\n"+paytm+"\n"+ifsc);
//        if (!tez.equals("")|| !tez.equalsIgnoreCase("null"))
//        {
//            bank_type="Google Pay";
//
//        }else if (!phonePay.equals("")|| !phonePay.equalsIgnoreCase("null"))
//        {
//            bank_type="PhonePe";
//        }else if (!paytm.equals("")|| !paytm.equalsIgnoreCase("null"))
//        {
//            bank_type = "paytm";
//        }else if (!ifsc.equals("")||!ifsc.equalsIgnoreCase("null") && !accountno.equals("")||!accountno.equalsIgnoreCase("null"))
//        {
//            bank_type = "Bank";
//        }else {
//            module.fieldRequired("Please fill any of your payment details to proceed!");
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
//            case R.id.btn_add:
//               onValidation();
//            break;
            case R.id.lin_whatsapp:
                module.whatsapp(withdraw_no.toString(),"Hello! Admin ");
                break;
                case R.id.tv_reeedm_msg:
                module.whatsapp(withdraw_no.toString(),"Hello! Admin ");
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_withdraw:
                String   ifsc_code = sessionMangement.getUserDetails().get(KEY_IFSC);
                if (!ifsc_code.isEmpty()) {
                    getTimeSlots();
                    showWithdrawalDialog();
                }else {
                     module.errorToast(getApplicationContext(),"Please add bank first");
                    Intent i = new Intent(WithdrawalFundFragment.this, AddBankAccountActivity.class);
                    startActivity(i);
                }

                break;
        }
    }

    public void showWithdrawalDialog()
    {
        Dialog dialog ;
        dialog=new Dialog(WithdrawalFundFragment.this);
        dialog.getWindow ( ).setBackgroundDrawable (new ColorDrawable(0));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_withdraw_fund);
        dialog.show();

        Button btn_add = (Button) dialog.findViewById (R.id.btn_add);
         et_points=(EditText)dialog.findViewById (R.id.et_points);
         Button btn_cancel=dialog.findViewById (R.id.btn_cancel);
        btn_add.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                onValidation();
            }
        });
        btn_cancel.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside (false);

    }

    private void onValidation() {

        String points=et_points.getText().toString().trim();

        if(points.isEmpty()) {
            et_points.setError("Enter Some Points");
        } else {
            String user_id = sessionMangement.getUserDetails().get(KEY_ID);
            String pnts = String.valueOf(points);
            String st = "pending";
            int w_amt = Integer.parseInt(module.getAndSetWalletAmount());
            int t_amt = Integer.parseInt(pnts);

            if (w_amt > 0) {
                if(t_amt<wMinAmt) {
                    module.fieldRequired("Minimum Withdrawal amount "+wMinAmt+".");
                }else if (t_amt>Integer.parseInt(max_withdraw_amount)){
                    module.fieldRequired("Maximum Withdrawal amount "+max_withdraw_amount+".");
                } else {
                    if (t_amt > w_amt) {
                        module.fieldRequired("Your requested amount exceeded");
                        return;
                    } else {

                        int flg=0;
                        if(getCurrentDay().equalsIgnoreCase("Sunday"))
                        {
                            if(wSunday==1){
                                flg=1;
                            }
                            else{
                                flg=2;
                            }
                        }
                        else if(getCurrentDay().equalsIgnoreCase("Saturday"))
                        {
                            if(wSaturday==1){
                                flg=3;
                            }
                            else{
                                flg=4;
                            }
                        }
                        else
                        {
                            flg=5;
                        }
                        if(flg==1 || flg==3 || flg==5){
                            if(w_status.equals("1")) {
                                if (status.equals("1")) {
                                    Log.e ("jjh", "onValidation: "+ getStartTimeOutStatus(timeList)+"----"+getEndTimOutStatus(timeList));
                                    if (getStartTimeOutStatus(timeList)){
                                        if(getEndTimOutStatus(timeList)) {
                                            getwithdrawAmount (user_id, st, "Withdraw", bank_type, String.valueOf (t_amt));
                                        }else{
                                             module.errorToast(getApplicationContext(),"Withdrawal Request is not allowed after "+timeList.get(0).getEnd_time());
                                        }
                                    } else {
                                         module.errorToast(getApplicationContext(),"Withdrawal Request is not allowed before "+timeList.get(0).getStart_time());
                                    }

                                }else {
                                     module.errorToast(getApplicationContext(),"Withdrawal Request is not allowed on "+getCurrentDay());
                                }
                            }else {
                                 module.errorToast(getApplicationContext(),"Withdrawal Request is not allowed on "+getCurrentDay());

                            }
                        }
                        else if(flg==2 || flg==4)
                        {
                             module.errorToast(getApplicationContext(),"Withdrawal Request is not allowed on "+getCurrentDay());
                        }
                    }
                }

            } else {
                module.fieldRequired("You don't have enough points in wallet ");
            }

        }
    }


    public boolean getStartTimeOutStatus(ArrayList<TimeSlots> list){
        int j=0;
        boolean flag=false;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
        SimpleDateFormat spdfMin=new SimpleDateFormat("mm");
        Date c_date=new Date();
        int chours=Integer.parseInt(simpleDateFormat.format(c_date));
        int cMins=Integer.parseInt(spdfMin.format(c_date));
        for(int i=0; i<list.size();i++){
            int shours=Integer.parseInt(list.get(i).getStart_time().split(":")[0].toString());
            int sMins=Integer.parseInt(list.get(i).getStart_time().split(":")[1].toString());
            Log.d ("val", ": "+shours+"--"+sMins+"--"+chours+"--"+cMins);
            if(chours>shours)
            {j=1;
                flag=true;
                break;
            }
            else if(chours == shours)
            {

                //if(cMins<=sMins)
                if(cMins>=sMins)
                {
                    j=2;
                    flag=true;
                    break;
                }
                else{
                    j=3;
                    flag=false;
                    break;
                }
            }
        }

        return flag;

    }
    public boolean getEndTimOutStatus(ArrayList<TimeSlots> list){
        int j=0;
        boolean flag=false;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
        SimpleDateFormat spdfMin=new SimpleDateFormat("mm");
        Date c_date=new Date();
        int chours=Integer.parseInt(simpleDateFormat.format(c_date));
        int cMins=Integer.parseInt(spdfMin.format(c_date));

        for(int i=0; i<list.size();i++){
            int ehours=Integer.parseInt(list.get(i).getEnd_time().split(":")[0].toString());
            int eMins=Integer.parseInt(list.get(i).getEnd_time().split(":")[1].toString());
            Log.d ("enff", ": "+ehours+"--"+eMins+"--"+chours+"--"+cMins);
            if(chours<ehours)
            {j=1;
                flag=true;
                break;
            }
            else if(chours == ehours)
            {
                if(cMins<=eMins)
                {
                    j=4;
                    flag=true;
                    break;
                }
                else{
                    j=5;
                    flag=false;
                    break;
                }
            }
        }
        return flag;
    }
    public String getCurrentDay()
    {
        Date date=new Date();
        SimpleDateFormat smdf=new SimpleDateFormat("EEEE");
        String day=smdf.format(date);
        return day;
    }

    public void getwithdrawAmount(String user_id,String st, String type, String bank_type,String points)
    {
        Date date=new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
        String dt=dateFormat.format(date);
        int wl = Integer.parseInt(sessionMangement.getUserDetails().get(KEY_WALLET));
        final String wal = String.valueOf(wl - Integer.parseInt(points));
        loadingBar.show();
        HashMap<String,String> params=new HashMap<String, String>();

        params.put("user_id", user_id);
        params.put("points", points);
        params.put("request_status", st);
        params.put("type", "Withdraw");
        params.put("wallet", wal);
        params.put("trans_id", "");
        params.put("w_type","w_type");
        params.put("date",dt);
        params.put("bank_type","bank");
        Log.e("asdasd",""+params.toString());

        module.postRequest(URL_REQUEST, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("withdrawal_request", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    boolean resp = object.getBoolean("responce");
                    if (resp) {
                        loadingBar.dismiss();
                        module.successToast(getApplicationContext(),object.getString("message"));
                       tv_walletAmount.setText(module.getAndSetWalletAmount());
                        Intent i = new Intent(WithdrawalFundFragment.this,MainActivity.class);
                        startActivity(i);
                    }else {
                        module.errorToast(getApplicationContext(),object.getString("error"));
                        loadingBar.dismiss();
                    }
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                module.showVolleyError(error);
                loadingBar.dismiss();
            }
        });
    }
    public void getTimeSlots(){
        loadingBar.show();
        timeList.clear();
        HashMap<String,String> params=new HashMap<>();
        module.postRequest(BaseUrls.URL_TIME_SLOTS, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String resp) {
                Log.e("timeslot",resp.toString());

                loadingBar.dismiss();
                try {
                    JSONObject response=new JSONObject(resp);
                    if(response.getBoolean("responce")){
                        Gson gson=new Gson();
                        Type typeList=new TypeToken<List<TimeSlots>>(){}.getType();
                        timeList=gson.fromJson(response.getString("data").toString(),typeList);
                        withdraw_limit= (response.getString ("withdraw_limit"));
                        wMinAmt=Integer.parseInt(response.getString("min_amount"));
                        wSaturday=Integer.parseInt(response.getString("w_saturday").toString());
                        wSunday=Integer.parseInt(response.getString("w_sunday").toString());
                        status =timeList.get (0).getStatus ();

                    }else{
                         module.errorToast(getApplicationContext(),"Something Went Wrong");
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                module.showVolleyError(error);
            }
        });
    }
}