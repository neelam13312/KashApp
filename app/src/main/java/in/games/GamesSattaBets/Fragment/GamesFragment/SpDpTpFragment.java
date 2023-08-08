package in.games.GamesSattaBets.Fragment.GamesFragment;

import static in.games.GamesSattaBets.Activity.MainActivity.starline_id;
import static in.games.GamesSattaBets.Activity.SplashActivity.max_bet_amount;
import static in.games.GamesSattaBets.Activity.SplashActivity.min_bet_amount;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_SPDPTP;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.games.GamesSattaBets.Adapter.FinalBidAdapter;
import in.games.GamesSattaBets.Adapter.TableAdapter;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Fragment.SelectGameActivity;
import in.games.GamesSattaBets.Model.TableModel;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.LoadingBar;

public class SpDpTpFragment extends Fragment implements View.OnClickListener {
LinearLayout lin_selectDate,lin_type;
TextView tv_type,tv_date;
EditText et_digit,et_points;
Button btn_add,btn_submit;
CheckBox cb_sp,cb_dp,cb_tp;
Module module;
int num=0;
LoadingBar loadingBar;
String gamedate;
String gameType="";
String w_amount="";
Dialog dialog;
TextView tv_date1,tv_date2,tv_date3,txtDate_id,tv_open,tv_close;
private String matka_id,e_time,s_time ,matka_name , game_id , game_name ,type = "open" ,game_date="",title="",is_market_open_nextday,is_market_open_nextday2;
ListView list_table;
TableAdapter tableAdaper;
List<TableModel> list;
    String game,sp,dp,tp,market_status="";
    TextView tv_subBid,tv_subAmount;
    LinearLayout lin_submit;

    public SpDpTpFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sp_dp_moter, container, false);
        initView(view);

        game_name = getArguments().getString("game_name");
        matka_id = getArguments().getString("m_id");
        game_id = getArguments().getString("game_id");
        s_time = getArguments().getString("start_time");
        e_time = getArguments().getString("end_time");
        matka_name = getArguments().getString("matka_name");
        title = getArguments().getString("title");
        market_status=getArguments ().getString ("market_status");
        is_market_open_nextday=getArguments ().getString ("is_market_open_nextday");
        is_market_open_nextday2=getArguments ().getString ("is_market_open_nextday2");

        int  m_id = Integer.parseInt (matka_id);

/////////// add by nee
//        if (Integer.parseInt(matka_id)>20){
//            ((SelectGameActivity) getActivity ( )).setGameTitle (title);
//
////            ((SelectGameActivity) getActivity()).setGameTitle(game_name);
//        }else {
//            ((SelectGameActivity) getActivity()).setGameTitle(title + " (" + matka_name + ")");
//        }

//int min_amt= Integer.parseInt (min_add_amount);
        gamedate=tv_date.getText ().toString ();

        if (module.getTimeDifference(s_time) > 0) {

            tv_type.setText("Open");
        } else {
            //tv_type.setText("Close");
            if (module.getTimeDifference(e_time) > 0)
            {
                tv_type.setText("Close");

            }
            else{
                tv_type.setText("Open");

            }

        }
        Log.e("spdp_startline", "onCreateView: "+m_id  + "empty"+ starline_id );

        if (m_id>starline_id)
        {


            ((SelectGameActivity) getActivity ( )).setGameTitle (title);

            tv_date.setVisibility(View.GONE);
            tv_type.setVisibility(View.GONE);
            try {

//                tv_date.setText(cur_date);

                if(market_status.equals ("open")) {
                    module.getCurrentDate(tv_date);
                }
                else
                {
                    tv_date.setText ("Select Date");
                }
                if (module.getTimeDifference(s_time) > 0) {

                    tv_type.setText("Open");
                } else {
                   // tv_type.setText("Close");
                    if (module.getTimeDifference(e_time) > 0)
                    {
                        tv_type.setText("Close");

                    }
                    else{
                        tv_type.setText("Open");

                    }

                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {

            if(market_status.equals ("open")) {
                module.getCurrentDate(tv_date);
            }
            else
            {
                tv_date.setText ("Select Date");
            }
            tv_type.setVisibility(View.VISIBLE);
            ((SelectGameActivity) getActivity()).setGameTitle(title + " (" + matka_name + ")");

            //  matka_name = getArguments().getString("matka_name");
        }
        return view;
    }
    private void initView(View view) {
        w_amount = ((SelectGameActivity) getActivity()).getGameWallet();

        loadingBar=new LoadingBar (getActivity ());
//        matka_name = getArguments().getString("matka_name");
//        game_name = getArguments().getString("game_name");
//        matka_id = getArguments().getString("m_id");
//        game_id = getArguments().getString("game_id");
//        s_time = getArguments().getString("start_time");
//        e_time = getArguments().getString("end_time");
//        title = getArguments().getString("title");

        module = new Module(getActivity());
        list=new ArrayList<>();
        list_table=view.findViewById(R.id.list_table);
//        lin_selectDate = view.findViewById(R.id.lin_selectDate);
//        lin_type = view.findViewById(R.id.lin_type);
        tv_type = view.findViewById(R.id.tv_type);
        tv_date = view.findViewById(R.id.tv_date);
        et_digit = view.findViewById(R.id.et_digit);
        et_points = view.findViewById(R.id.et_points);
        btn_add = view.findViewById(R.id.btn_add);
        cb_tp = view.findViewById(R.id.cb_tp);
        cb_sp = view.findViewById(R.id.cb_sp);
        cb_dp = view.findViewById(R.id.cb_dp);
        btn_submit = view.findViewById(R.id.btn_submit);
        tv_subBid = view.findViewById(R.id.tv_subBid);
        tv_subAmount = view.findViewById(R.id.tv_subAmount);
        lin_submit = view.findViewById(R.id.lin_submit);


        btn_add.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        tv_type.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        cb_tp.setOnClickListener(this);
        cb_sp.setOnClickListener(this);
        cb_dp.setOnClickListener(this);
//        module.getCurrentDate(tv_date);

        if(market_status.equals ("open")) {
            module.getCurrentDate(tv_date);
        }
        else
        {
            tv_date.setText ("Select Date");
        }

    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId())
        {
            case R.id.btn_add:
                module.sessionOut();
                onValidation();
                break;
            case R.id.tv_date:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    module.setDateDialog (is_market_open_nextday,is_market_open_nextday2,market_status,dialog,matka_id,tv_date1,tv_date2,tv_date3,txtDate_id,tv_date);
                }
                break;
            case R.id.tv_type:
                module.setBetTypeDialog (dialog,game_date,tv_open,tv_close,tv_type,s_time,e_time,game_id);
                break;
            case R.id.btn_submit:
                module.sessionOut();
                placedBid("show",dialog);
                break;
            case R.id.cb_sp:
                num =1;
                sp="SP";
                break;
            case R.id.cb_dp:
                num =2;
                dp="DP";
                break;
            case R.id.cb_tp:
                num =3;
                tp="TP";
                break;
        }
        if (fragment!=null)
        {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();
        }
    }

    private void onValidation() {
        String digit = et_digit.getText().toString();;
        String points = et_points.getText().toString();
        String type = tv_type.getText().toString();
         game_date = tv_date.getText().toString();

         if(game_date.equalsIgnoreCase ("Select Date"))
          {
              module.fieldRequired ("Date Required");
          }else if (type.equalsIgnoreCase("Select Game Type"))
          {
            module.fieldRequired("Select game type");
        } else if (num==0)
        {
            module.customToast("Please select alteast one check box");
        }else if (TextUtils.isEmpty(digit)) {
            et_digit.setError("Please enter digit");
            et_digit.requestFocus();
            return;
        } else if (TextUtils.isEmpty(points)) {
            et_points.setError("Please enter point");
            et_points.requestFocus();
            return;

        } else {
            int pints = Integer.parseInt(points);
             if (pints < min_bet_amount) {
                 et_points.setError(getResources().getString(R.string.min_bet_value)+" "+min_bet_amount);
                 et_points.requestFocus();
                 return;


             }else if (pints>max_bet_amount){
                 et_points.setError(getResources().getString(R.string.max_bet_value)+" "+max_bet_amount);
                 et_points.requestFocus();
                 return;
             }

            else if (pints > Integer.parseInt(w_amount)) {
                module.errorToast("Insufficient Amount");

            }else {
//                if (num==1)
//                {
//                    gameType = "SP";
//                }else if (num==2)
//                {
//                    gameType = "DP";
//                }else if (num ==3)
//                {
//                    gameType = "TP";
//                }
//                int num = 1;
//                for (int n = 0; n < list.size(); n++) {
//                    num=num+1;
//                }
//                String number=String.valueOf(num);
//                module.addData(number,gameType, digit, points, type, list, tableAdaper, list_table, btn_submit,tv_subBid,tv_subAmount,lin_submit);
//                clearData();
//                et_digit.requestFocus();

                String d = et_digit.getText ( ).toString ( );
                String p = et_points.getText ( ).toString ( );
                String th = tv_type.getText ( ).toString ( );
                int stat = 0;
                if (stat == 1) {
                    th = "open";
                } else if (stat == 2) {
                    if (type.equals ("open")) {
                        th = "open";
                    } else if (type.equals ("close")) {
                        th = "close";
                    }
                }

                String inputData = et_digit.getText ( ).toString ( );
                if (inputData.equals ("false")) {
                    module.showToast ("Wrong input");
                } else {

                    String spdata="",dpdata="",tpdata="";
                    JSONArray jsonArray = new JSONArray ( );




                    if(cb_sp.isChecked ()){
                         spdata = module.checkNull (sp);
                    }
                    else {
                         spdata ="";
                    }
                    if(cb_dp.isChecked ()){
                        dpdata = module.checkNull (dp);
                    }
                    else {
                        dpdata ="";
                    }
                    if(cb_tp.isChecked ()){
                         tpdata = module.checkNull (tp);
                    }
                    else {
                        tpdata ="";
                    }


                    if (!spdata.isEmpty ( )) {
                        jsonArray.put (spdata);
                    }
                    if (!dpdata.isEmpty ( )) {
                        jsonArray.put (dpdata);
                    }
                    if (!tpdata.isEmpty ( )) {
                        jsonArray.put (tpdata);
                    }


                    String alltype = String.valueOf (jsonArray);


                    getDataSet (alltype, inputData, p, type, URL_SPDPTP);

                }

            }

        }
    }
    private void clearData() {

        et_digit.setText("");
        et_points.setText("");
    }
    private void placedBid(String dialogType,Dialog dialog) {

        int er = list.size();
        if (er <= 0) {
            String message = "Please Add Some Bids";
            module.fieldRequired(message);
            return;
        } else {
            try {
                int amt = 0;
                for (int j = 0; j < list.size(); j++) {
                    amt = amt + Integer.parseInt(list.get(j).getPoints());
                }
                if (amt > Integer.parseInt(w_amount)) {
                    module.errorToast("Insufficient Amount");
                    clearData();
                } else {
                    if (dialogType.equals("placed")) {
                        module.setBidsDialog(Integer.parseInt(w_amount), list, matka_id, game_date.substring(0, 10), game_id, w_amount, matka_name, dialog.findViewById (R.id.btn_dialogSubmit), s_time, e_time,dialog);
                    }else {
                        showPlacingBidData(matka_name,tv_subBid.getText().toString(),tv_subAmount.getText().toString(),w_amount);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public void showPlacingBidData(String matkaName, String bid, String totalAmount,String wallet) {
        Dialog dialog;
        dialog = new Dialog (getActivity());
        dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
        dialog.setContentView (R.layout.dailoge_submitbit);
        TextView tv_dialogTitle = dialog.findViewById (R.id.tv_dialogTitle);
        TextView tv_dialogBid = dialog.findViewById (R.id.tv_dialogBid);
        TextView tv_dialogAmount = dialog.findViewById (R.id.tv_dialogAmount);
        TextView tv_dialogWallet = dialog.findViewById (R.id.tv_dialogWallet);
        TextView tv_dialogAfterWallet = dialog.findViewById (R.id.tv_dialogAfterWallet);
        RecyclerView rec_dialogBid = dialog.findViewById (R.id.rec_dialogBid);
        Button btn_dialogSubmit = dialog.findViewById(R.id.btn_dialogSubmit);
        Button btn_dialogCancel = dialog.findViewById(R.id.btn_dialogCancel);
        rec_dialogBid.setLayoutManager(new LinearLayoutManager(getActivity()));

//        rec_dialogBid.setLayoutManager(new LinearLayoutManager(getActivity()));
        FinalBidAdapter finalBidAdapter = new FinalBidAdapter(list,getActivity());
        Log.e("FinalBidAdapter", "showPlacingBidData: "+list.size() );
        if (list.size()<4){
            ViewGroup.LayoutParams params=rec_dialogBid.getLayoutParams();
            Log.e("list_bidadapter", "showPlacingBidData: "+params.height );
            params.height=90;
            rec_dialogBid.setLayoutParams(params);
        }
        else if (list.size()>4){
            ViewGroup.LayoutParams params=rec_dialogBid.getLayoutParams();
            Log.e("list_4", "showPlacingBidData: "+params.height );

            params.height=350;
            rec_dialogBid.setLayoutParams(params);
        }

        rec_dialogBid.setAdapter(finalBidAdapter);


        tv_dialogTitle.setText(matkaName);
        tv_dialogBid.setText(bid);
        tv_dialogWallet.setText(wallet);
        tv_dialogAmount.setText(totalAmount);
        int tot = Integer.parseInt(wallet)-Integer.parseInt(totalAmount);
        tv_dialogAfterWallet.setText(String.valueOf(tot));

        dialog.setCanceledOnTouchOutside (true);
        dialog.show ( );

        btn_dialogSubmit.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                placedBid("placed",dialog);
            }
        });

        btn_dialogCancel.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void getDataSet(String type ,final String inputData, final String point, final String typ , final String url) {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<> ();
//        final String data = String.valueOf (jsonArray);

        params.put("arr",inputData);
        params.put ("type",String.valueOf (type));
        Log.e ("spdptp",""+params );
        module.postRequest(url, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("sp_dp_motor_response",response.toString());
                try {

                    JSONObject jsonObject = new JSONObject (response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONArray as = jsonObject.getJSONArray("data");
                        final String data= String.valueOf(as);
                        if (data.equals("[]"))
                        {
                            et_digit.setError("Enter valid digits!");
                        }else {
                            for (int i = 0; i <= as.length() - 1; i++) {
                                String p = as.getString(i);

                                int num = 1;
                                for (int n = 0; n < list.size(); n++) {
                                    num=num+1;
                                }
                                String number=String.valueOf(num);
                                if (num==1)
                                {
                                    gameType = "SP";
                                }else if (num==2)
                                {
                                    gameType = "DP";
                                }else if (num ==3)
                                {
                                    gameType = "TP";
                                }

                                module.addData(number,"SPDPTP",p,point,typ,list,tableAdaper,list_table,btn_submit,tv_subBid,tv_subAmount,lin_submit);
                                clearData();
                            }
                        }
                        loadingBar.dismiss();
                    } else {
                        loadingBar.dismiss();
                        module.errorToast( "Something went wrong");
                    }

                } catch (Exception ex) {
                    module.showToast("Error :" + ex.getMessage());
                    loadingBar.dismiss();
                    return;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                module.showVolleyError(error);
                loadingBar.dismiss();
            }
        });
//               loadingBar.dismiss();

    }

}