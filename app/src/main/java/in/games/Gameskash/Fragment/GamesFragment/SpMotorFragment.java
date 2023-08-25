package in.games.Gameskash.Fragment.GamesFragment;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import in.games.Gameskash.Adapter.FinalBidAdapter;
import in.games.Gameskash.Adapter.TableAdapter;
import in.games.Gameskash.Config.Module;
import in.games.Gameskash.Fragment.SelectGameActivity;
import in.games.Gameskash.Model.TableModel;
import in.games.Gameskash.R;
import in.games.Gameskash.Util.LoadingBar;

import static in.games.Gameskash.Activity.MainActivity.starline_id;
import static in.games.Gameskash.Activity.SplashActivity.max_bet_amount;
import static in.games.Gameskash.Activity.SplashActivity.min_bet_amount;
import static in.games.Gameskash.Config.BaseUrls.URL_DpMotor;
import static in.games.Gameskash.Config.BaseUrls.URL_SpMotor;

public class SpMotorFragment extends Fragment implements View.OnClickListener {
    LinearLayout lin_selectDate,lin_type,lin_spMotor,lin_dpMotor;
    TextView tv_type,tv_date,tv_motor;
    EditText et_spMotor,et_points,et_dpMotor;
    Button btn_add,btn_submit;
    Module module;
    String name,w_amount="",market_status="";
    String gamedate;
    ListView list_table;
    TableAdapter tableAdaper;
    List<TableModel> list;
    LoadingBar loadingBar;
    Dialog dialog;
    TextView tv_date1,tv_date2,tv_date3,txtDate_id,tv_open,tv_close;
    private String matka_id="",e_time="",s_time="" ,matka_name="" , game_id="" , game_name="" ,type = "open" ,game_date="",title="",is_market_open_nextday,is_market_open_nextday2;

    TextView tv_subBid,tv_subAmount;
    LinearLayout lin_submit;
    public SpMotorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sp_motor, container, false);
        initView(view);

        int  m_id = Integer.parseInt (matka_id);

        if (module.getTimeDifference(s_time) > 0) {

            tv_type.setText("Open");

        } else {
           // tv_type.setText("Close");
//            if (module.getValidTime(s_time,e_time))
//            {
//                tv_type.setText("Open");
//
//            }
//            else{
//                tv_type.setText("Close");
//
//            }
            if (module.getTimeDifference(e_time) > 0)
            {
                tv_type.setText("Close");

            }
            else{
                tv_type.setText("Open");

            }

        }
        Log.e("spmotor_startline", "onCreateView: "+m_id  + "empty"+ starline_id );

        if (m_id>starline_id)
        {
            ((SelectGameActivity) getActivity ( )).setGameTitle (title.toUpperCase());

            tv_date.setVisibility(View.GONE);
            tv_type.setVisibility(View.GONE);
            try {
                Date date = new Date();
              SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy");

                String cur_date = sdf.format(date);
                if(market_status.equals ("open")) {
                    tv_date.setText(cur_date);
                }
                else
                {
                    tv_date.setText ("Select Date");
                }
//                tv_date.setText(cur_date);
                if (module.getTimeDifference(s_time) > 0)
                {

                    tv_type.setText("Open");
                }
                else {
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
            ((SelectGameActivity)getActivity()).setGameTitle(title.toUpperCase()+" ("+matka_name+")");
//            module.getCurrentDate(tv_date);

            if(market_status.equals ("open")) {
                module.getCurrentDate(tv_date);
            }
            else
            {
                tv_date.setText ("Select Date");
            }
            tv_type.setVisibility(View.VISIBLE);
            matka_name = getArguments().getString("matka_name");
        }
        manageVisibility();
        return view;
    }

    private void initView(View view) {

        name = getArguments().getString("name");
        w_amount = ((SelectGameActivity) getActivity()).getGameWallet();
        module = new Module(getActivity());
        loadingBar = new LoadingBar(getActivity());
        list=new ArrayList<>();
        list_table=view.findViewById(R.id.list_table);

        matka_name = module.checkNull(getArguments().getString("matka_name"));
        game_name = module.checkNull(getArguments().getString("game_name"));
        matka_id = module.checkNull(getArguments().getString("m_id"));
        game_id = module.checkNull(getArguments().getString("game_id"));
        s_time = module.checkNull(getArguments().getString("start_time"));
        e_time = module.checkNull(getArguments().getString("end_time"));
        title = module.checkNull(getArguments().getString("title"));
        market_status=module.checkNull (getArguments ().getString ("market_status"));
        is_market_open_nextday=module.checkNull (getArguments ().getString ("is_market_open_nextday"));
        is_market_open_nextday2=module.checkNull (getArguments ().getString ("is_market_open_nextday2"));

        tv_motor = view.findViewById(R.id.tv_motor);
        btn_submit = view.findViewById(R.id.btn_submit);
        lin_spMotor = view.findViewById(R.id.lin_spMotor);
        lin_dpMotor = view.findViewById(R.id.lin_dpMotor);
//        lin_selectDate = view.findViewById(R.id.lin_selectDate);
//        lin_type = view.findViewById(R.id.lin_type);
        tv_type = view.findViewById(R.id.tv_type);
        tv_date = view.findViewById(R.id.tv_date);
        et_dpMotor = view.findViewById(R.id.et_dpMotor);
        et_spMotor = view.findViewById(R.id.et_spMotor);
        et_points = view.findViewById(R.id.et_points);
        btn_add = view.findViewById(R.id.btn_add);

        tv_subBid = view.findViewById(R.id.tv_subBid);
        tv_subAmount = view.findViewById(R.id.tv_subAmount);
        lin_submit = view.findViewById(R.id.lin_submit);

        btn_add.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        tv_type.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        gamedate=tv_date.getText ().toString ();
        game_date = gamedate.substring(0, 10);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId())
        {
            case R.id.btn_add:
                
               onValidation();
                break;
            case R.id.tv_date:
                module.setDateDialog (is_market_open_nextday,is_market_open_nextday2,market_status,dialog,matka_id,tv_date1,tv_date2,tv_date3,txtDate_id,tv_date);
                break;
            case R.id.tv_type:

                module.setBetTypeDialog (dialog,gamedate,tv_open,tv_close,tv_type,s_time,e_time,game_id);
                break;
            case R.id.btn_submit:
                
                placedBid("show",dialog);
                break;
        }
        if (fragment!=null)
        {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();
        }
    }

    private void placedBid(String dialogType,Dialog dialog) {
            int amt=0;
            for(int j=0;j<list.size();j++)
            {
                amt=amt+Integer.parseInt(list.get(j).getPoints());
            }
            if (amt>Integer.parseInt(w_amount))
            {
               module.errorToast(getContext(),"Insufficient Amount");
                clearData();
            }
            else {
                try {
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    String cur_time = format.format(date);
                    String cur_date = sdf.format(date);
                    String g_d = game_date.substring(0, 10);

                    Log.e("date ", String.valueOf(g_d) + "\n" + String.valueOf(cur_date));

                    if (cur_date.equals(g_d)) {
                        Log.e("true", "today");
                        Date s_date = format.parse(s_time);
                        Date e_date = format.parse(e_time);
                        Date c_date = format.parse(cur_time);
                        long difference = c_date.getTime() - s_date.getTime();
                        long as = (difference / 1000) / 60;

                        long diff_close = c_date.getTime() - e_date.getTime();
                        long curr = (diff_close / 1000) / 60;
                        long current_time = c_date.getTime();

                        if (as < 0) {
                            if (dialogType.equals("placed")) {
                                module.setBidsDialog(Integer.parseInt(w_amount), list, matka_id, game_date.substring(0, 10), game_id, w_amount, matka_name, dialog.findViewById (R.id.btn_dialogSubmit), s_time, e_time,dialog);
                            }else {
                                showPlacingBidData(matka_name,tv_subBid.getText().toString(),tv_subAmount.getText().toString(),w_amount);

                            }
                        } else if (curr < 0) {
                            if (dialogType.equals("placed")) {
                                module.setBidsDialog(Integer.parseInt(w_amount), list, matka_id, game_date.substring(0, 10), game_id, w_amount, matka_name, dialog.findViewById (R.id.btn_dialogSubmit), s_time, e_time,dialog);
                            }else {
                                showPlacingBidData(matka_name,tv_subBid.getText().toString(),tv_subAmount.getText().toString(),w_amount);

                            }
                        } else {
                            clearData();
                            module.marketClosed("Biding is Closed Now");
                        }
                    } else {
//                        game_date.substring(0, 10)
                        if (dialogType.equals("placed")) {
                            module.setBidsDialog(Integer.parseInt(w_amount), list, matka_id, game_date.substring(0, 10), game_id, w_amount, matka_name, dialog.findViewById (R.id.btn_dialogSubmit), s_time, e_time,dialog);
                            clearData();
                        }else {
                            showPlacingBidData(matka_name,tv_subBid.getText().toString(),tv_subAmount.getText().toString(),w_amount);

                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

    private void onValidation() {
        String spMotor = et_spMotor.getText().toString();
        int len=spMotor.length();
//        String dpMotor = et_dpMotor.getText().toString();
        String points = et_points.getText().toString();
        String type = tv_type.getText().toString();
            game_date =tv_date.getText().toString();

            String bet = type;
            if(game_date.equalsIgnoreCase ("Select Date"))
            {
                module.fieldRequired ("Date Required");
            }else
            if (type.equalsIgnoreCase("Select Game Type"))
            {
                module.fieldRequired("Select game type");

            }
            else if (TextUtils.isEmpty(spMotor)) {
                et_spMotor.setError("Please enter digit");
                et_spMotor.requestFocus();
                return;
            }
            else if (len<4) {
                et_spMotor.setError("Please enter 4 digit");
                et_spMotor.requestFocus();
                return;
            } else if (TextUtils.isEmpty(points)) {
                et_points.setError("Please enter point");
                et_points.requestFocus();
                return;

            }  else {
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
                else if (pints>Integer.parseInt(w_amount))
                {
                   module.errorToast(getContext(),"Insufficient Amount");

                }
                else {

//                    list.clear();
                    String d = et_spMotor.getText().toString();
                    String p = et_points.getText().toString();
                    String th = type;
                    int stat = 0;
                    if(stat==1)
                    {
                        th="open";
                    }
                    else if(stat==2)
                    {
                        if(type.equals("open"))
                        {
                            th="open";
                        }
                        else  if(type.equals("close"))
                        {
                            th="close";
                        }
                    }

                    String inputData =et_spMotor.getText().toString();
                    if (inputData.equals("false")) {
                        module.showToast( "Wrong input");
                    }
                    else {
                        if (game_name.equalsIgnoreCase("sp_motor")) {
//                            module.customToast("sp_motor");
                            getDataSet(inputData, p, type, URL_SpMotor);
                        }
                        else
                        {
//                            module.customToast("dp_motor");
                            getDataSet(inputData, p, type,URL_DpMotor);

                        }
                }

                }

            }
    }

    public  void manageVisibility()
    {

        if (name.equalsIgnoreCase("SP MOTOR"))
        {
            tv_motor.setText("SP MOTOR");
        }else {
            tv_motor.setText("DP MOTOR");
        }

//        if (name.equalsIgnoreCase("sp motor"))
//        {
//            lin_spMotor.setVisibility(View.VISIBLE);
//            lin_dpMotor.setVisibility(View.GONE);
//
//        }else if(name.equalsIgnoreCase("dp motor"))
//        {
//              lin_dpMotor.setVisibility(View.VISIBLE);
//              lin_spMotor.setVisibility(View.GONE);
//        }
    }

    private void clearData() {
        et_spMotor.setText ("");
        et_dpMotor.setText ("");
        et_points.setText("");
    }


    private void getDataSet(final String inputData, final String point, final String typ , final String url) {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("arr", inputData);
               module.postRequest(url, params, new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       Log.e("sp_dp_motor_response",response.toString());
                       try {

                           JSONObject jsonObject = new JSONObject(response);
                           String status = jsonObject.getString("status");
                           if (status.equals("success")) {
                               JSONArray as = jsonObject.getJSONArray("data");
                               final String data= String.valueOf(as);
                               if (data.equals("[]"))
                               {
                                   et_spMotor.setError("Enter valid digits!");
                               }else {
                                   for (int i = 0; i <= as.length() - 1; i++) {
                                       String p = as.getString(i);

                                       int num = 1;
                                       for (int n = 0; n < list.size(); n++) {
                                           num=num+1;
                                       }
                                       String number=String.valueOf(num);
                                       module.addData(number,name,p,point,typ,list,tableAdaper,list_table,btn_submit,tv_subBid,tv_subAmount,lin_submit);
                                       clearData();
                                   }
                               }
                               loadingBar.dismiss();
                           } else {
                               loadingBar.dismiss();
                              module.errorToast(getContext(), "Something went wrong");
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


}