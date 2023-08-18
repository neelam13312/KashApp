package in.games.GamesSattaBets.Fragment.GamesFragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import in.games.GamesSattaBets.Adapter.FinalBidAdapter;
import in.games.GamesSattaBets.Adapter.TableAdapter;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Fragment.SelectGameActivity;
import in.games.GamesSattaBets.Model.TableModel;
import in.games.GamesSattaBets.R;

import static in.games.GamesSattaBets.Activity.MainActivity.starline_id;
import static in.games.GamesSattaBets.Activity.SplashActivity.max_bet_amount;
import static in.games.GamesSattaBets.Activity.SplashActivity.min_bet_amount;
import static in.games.GamesSattaBets.Config.list_input_data.red_bracket;


public class RedBracketFragment extends Fragment implements View.OnClickListener {
    AutoCompleteTextView et_digit;
    EditText et_point;
    Dialog dialog;
    String gamedate,market_status="";
    Button btn_add,btn_submit;
    CheckBox check_bid;
    RelativeLayout rel_bracket;
    String title,betdate,bettype,w_amount="",s_time ,e_time,matka_name ,game_id,matka_id,game_name,is_market_open_nextday,is_market_open_nextday2;
    TextView tv_date,tv_type,tv_open,tv_close,tv_date1,tv_date2,tv_date3,txtDate_id;
    ListView list_table;
    TableAdapter tableAdaper;
    Module module;
    List<String> digitlist;
    List<TableModel> list;
    TextView tv_subBid,tv_subAmount;
    LinearLayout lin_submit;

    public RedBracketFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_red_bracket, container, false);
        initview(view);

        w_amount = ((SelectGameActivity) getActivity()).getGameWallet();
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,red_bracket);
        et_digit.setFilters (new InputFilter[]{new InputFilter.LengthFilter (2)});
        et_digit.setAdapter(adapter2);
        digitlist= Arrays.asList (red_bracket);

        check_bid.setOnClickListener (new View.OnClickListener ( ) {
    @Override
    public void onClick(View v) {
        if(check_bid.isChecked ()){
            rel_bracket.setVisibility (View.GONE);
        }else {
            rel_bracket.setVisibility (View.VISIBLE);
        }

    }
});


        return view;
    }

    private void initview(View view) {
        module = new Module (getContext ( ));
        rel_bracket = view.findViewById (R.id.rel_bracket);
        et_digit = view.findViewById (R.id.et_digit);
        et_point = view.findViewById (R.id.et_point);
        btn_add = view.findViewById (R.id.btn_add);
        btn_add.setOnClickListener (this);
        btn_submit = view.findViewById (R.id.btn_submit);
        btn_submit.setOnClickListener (this);
        check_bid = view.findViewById (R.id.check_bid);
        tv_date = view.findViewById (R.id.tv_date);
        tv_subBid = view.findViewById (R.id.tv_subBid);
        tv_subAmount = view.findViewById (R.id.tv_subAmount);
        lin_submit = view.findViewById (R.id.lin_submit);

        tv_date.setOnClickListener (this);


        list = new ArrayList<> ( );
        digitlist = new ArrayList<> ( );
        list_table = view.findViewById (R.id.list_table);

        bettype = "open";
        matka_name = getArguments ( ).getString ("matka_name");
        game_name = getArguments ( ).getString ("game_name");
        matka_id = getArguments ( ).getString ("m_id");
        game_id = getArguments ( ).getString ("game_id");
        s_time = getArguments ( ).getString ("start_time");
        e_time = getArguments ( ).getString ("end_time");
        title = getArguments ( ).getString ("title");
        is_market_open_nextday = getArguments ( ).getString ("is_market_open_nextday");
        is_market_open_nextday2 = getArguments ( ).getString ("is_market_open_nextday2");
        int m_id = Integer.parseInt (matka_id);

//        if (module.getTimeDifference(s_time) > 0) {
//
//            tv_type.setText("Open");
//        } else {
//            tv_type.setText("Close");
//        }

        Log.e ("redbracket_startline", "onCreateView: " + m_id + "empty" + starline_id);

        if (m_id > starline_id) {
            market_status = getArguments ( ).getString ("market_status");

            if (market_status.equalsIgnoreCase ("open")) {
                module.getCurrentDate (tv_date);
            } else {
                tv_date.setText ("Select Date");
            }
            if (Integer.parseInt (matka_id) > 20) {
                ((SelectGameActivity) getActivity ( )).setGameTitle (title);

//            ((SelectGameActivity) getActivity()).setGameTitle(game_name);
            } else {
                ((SelectGameActivity) getActivity ( )).setGameTitle (title + " (" + matka_name + ")");
            }


        }
        if (market_status.equalsIgnoreCase ("open")) {
            module.getCurrentDate (tv_date);
        } else {
            tv_date.setText ("Select Date");
            module.getCurrentDate(tv_date);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        String digit=et_digit.getText ().toString ();
        String point=et_point.getText ().toString ();
        if(v.getId ()==R.id.btn_add)
        {
            
            betdate = tv_date.getText().toString();

            int betType;
            betType=getBetType(getASandC(s_time,e_time));
            if (betType==1)
            {
                module.errorToast (getContext(),"Bidding is closed for today !");
            }
            else {

                if (betdate.equalsIgnoreCase ("SELECT DATE")) {
                    module.fieldRequired ("Date Required");
                }
                else if (rel_bracket.getVisibility ( ) == View.GONE) {

                    if (et_point.getText ( ).toString ( ).isEmpty ( )) {
                        et_point.setError ("Point Required");
                        et_point.requestFocus ( );
                    }
//            else  if(!digitlist.contains (digit)){
//                et_digit.setError ("Invalid");
//                et_digit.setText ("");
//                et_digit.requestFocus ();
//            }
                    else {
                        int points = Integer.parseInt (et_point.getText ( ).toString ( ).trim ( ));
                        if (points < min_bet_amount) {
                            et_point.setError(getResources().getString(R.string.min_bet_value)+" "+min_bet_amount);
                            et_point.requestFocus();
                            return;


                        }else if (points>max_bet_amount){
                            et_point.setError(getResources().getString(R.string.max_bet_value)+" "+max_bet_amount);
                            et_point.requestFocus();
                            return;
                        }

                        else if (points > Integer.parseInt (w_amount)) {
                            module.fieldRequired ("Insufficient Amount");
                        } else {
                            for (int i = 0; i <= red_bracket.length - 1; i++) {
                                int num = 1;
                                for (int n = 0; n < list.size ( ); n++) {
                                    num = num + 1;
                                }
                                String number = String.valueOf (num);
                                module.addData (number, "Red Bracket", red_bracket[i], point, "Close", list, tableAdaper, list_table, btn_submit,tv_subBid,tv_subAmount,lin_submit);

                            }
                        }
                    }
                }
                else if (rel_bracket.getVisibility ( ) == View.VISIBLE) {
                    if (et_digit.getText ( ).toString ( ).isEmpty ( )) {
                        et_digit.setError ("Digit Required");
                        et_digit.requestFocus ( );

                    } else if (et_point.getText ( ).toString ( ).isEmpty ( )) {
                        et_point.setError ("Point Required");
                        et_point.requestFocus ( );
                    } else if (!digitlist.contains (digit)) {
                        et_digit.setError ("Invalid");
                        et_digit.setText ("");
                        et_digit.requestFocus ( );
                    } else {
                        int points = Integer.parseInt (et_point.getText ( ).toString ( ).trim ( ));
                        if (points < 10) {
                            et_point.setError ("Minimum Biding amount is 10");
                            et_point.requestFocus ( );
                            return;

                        } else if (points > Integer.parseInt (w_amount)) {
                            module.fieldRequired ("Insufficient Amount");
                        } else {
                            int num = 1;
                            for (int n = 0; n < list.size ( ); n++) {
//                      num = num +Integer.parseInt (String.valueOf (list.get (n)));
                                num = num + 1;
                            }
                            String number = String.valueOf (num);
                            module.addData (number, "Red Bracket", digit, point, "Close", list, tableAdaper, list_table, btn_submit,tv_subBid,tv_subAmount,lin_submit);
                            et_digit.requestFocus ( );
                            clearData ( );
                        }
                    }

                }
            }
        }
        else if(v.getId ()==R.id.btn_submit){
            
            placedBid("show",dialog);
                   }
        else if(v.getId ()==R.id.tv_date){
            module.setDateDialog (is_market_open_nextday,is_market_open_nextday2,market_status,dialog,matka_id,tv_date1,tv_date2,tv_date3,txtDate_id,tv_date);
        }
        else if(v.getId ()==R.id.tv_type){

            module.setBetTypeDialog (dialog,gamedate,tv_open,tv_close,tv_type,s_time,e_time,game_id);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void placedBid(String dialogType,Dialog dialog){
        int er = list.size();
        if (er <= 0) {
            String message = "Please Add Some Bids";
            module.fieldRequired (message);
            return;

        } else {
            try {
                Date date = new Date();
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                String cur_time = format.format(date);
//                    String cur_date = sdf.format(date);

                Log.e("true", "today");
//                        Date s_date = format.parse(s_time);
                Date e_date = format.parse(e_time);
                Date c_date = format.parse(cur_time);
//                        long difference = c_date.getTime() - s_date.getTime();
//                        long as = (difference / 1000) / 60;

                long diff_close = c_date.getTime() - e_date.getTime();
                long curr = (diff_close / 1000) / 60;
//                        long current_time = c_date.getTime();

                if (curr < 0) {
                    if (dialogType.equals("placed")) {
                        module.setBidsDialog(Integer.parseInt(w_amount), list, matka_id, betdate, game_id, w_amount, matka_name, dialog.findViewById (R.id.btn_dialogSubmit), s_time, e_time,dialog);
                    }else {
                        showPlacingBidData(matka_name,tv_subBid.getText().toString(),tv_subAmount.getText().toString(),w_amount);
                    }
                }else{
                    module.fieldRequired ("Biding is Closed Now");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }
    private void clearData() {
        et_digit.setText ("");
        et_point.setText ("");
    }

    public long[] getASandC(String startTime,String endTime){
        long[] tArr=new long[2];
        Date date=new Date();
        java.text.SimpleDateFormat sim=new java.text.SimpleDateFormat ("HH:mm:ss");
        String time1 = startTime.toString();
        String time2 = endTime.toString();

        Date cdate=new Date();
        @SuppressLint("SimpleDateFormat") java.text.SimpleDateFormat format = new java.text.SimpleDateFormat ("HH:mm:ss");
        String time3=format.format(cdate);
        Date date1 = null;
        Date date2=null;
        Date date3=null;
        try {
            date1 = format.parse(time1);
            date2 = format.parse(time2);
            date3=format.parse(time3);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        long difference = date3.getTime() - date1.getTime();
        long as=(difference/1000)/60;

        long diff_close=date3.getTime()-date2.getTime();
        long c=(diff_close/1000)/60;
        tArr[0]=as;
        tArr[1]=c;
        return tArr;
    }
    public int getBetType(long[] tArr) {
        // as<0 => open,close
        //c>0 =>nothing but biding closed
        //else=>close
        long as = tArr[0];
        long c = tArr[1];
        if (as < 0) {
            return 2;
        } else if (c > 0) {
            return 0;
        } else {
            return 1;
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
            @RequiresApi(api = Build.VERSION_CODES.N)
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