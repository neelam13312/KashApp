package in.games.Gameskash.Fragment.GamesFragment;

import static in.games.Gameskash.Activity.SplashActivity.max_bet_amount;
import static in.games.Gameskash.Activity.SplashActivity.min_bet_amount;

import android.app.Dialog;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.games.Gameskash.Adapter.FinalBidAdapter;
import in.games.Gameskash.Adapter.TableAdapter;
import in.games.Gameskash.Config.Module;
import in.games.Gameskash.Fragment.SelectGameActivity;
import in.games.Gameskash.Model.TableModel;
import in.games.Gameskash.R;

public class OddEvenFragment extends Fragment implements View.OnClickListener {
  CheckBox check_odd,check_even;
    EditText et_point;
    Dialog dialog;
    String gamedate,title,market_status="";
    String betdate,bettype,w_amount="",check_value,game_name, matka_name, matka_id ,game_id,s_time ,e_time,no,is_market_open_nextday,is_market_open_nextday2;
    Button btn_add,btn_submit;
    TextView tv_date,tv_type,tv_open,tv_close,tv_date1,tv_date2,tv_date3,txtDate_id;
    ListView list_table;
    TableAdapter tableAdaper;
    Module module;
    List<TableModel> list;
    TextView tv_subBid,tv_subAmount;
    LinearLayout lin_submit;
    public OddEvenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_odd_even, container, false);
        initview(view);
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
        if (module.getTimeDifference(s_time) > 0) {

            tv_type.setText("Open");
        } else {
            if (module.getTimeDifference(e_time) > 0)
            {
                tv_type.setText("Close");

            }
            else{
                tv_type.setText("Open");

            }

//            tv_type.setText("Close");
        }

        if (Integer.parseInt(matka_id)>20){
            ((SelectGameActivity) getActivity ( )).setGameTitle (title);

//            ((SelectGameActivity) getActivity()).setGameTitle(game_name);
        }else {
            ((SelectGameActivity) getActivity()).setGameTitle(title + " (" + matka_name + ")");
        }

        check_even.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(list.size()>0)
                {
                    list.clear();
                    tableAdaper = new TableAdapter(list, getActivity(),tv_subBid,tv_subAmount,lin_submit);
                    list_table.setAdapter(tableAdaper);
                    tableAdaper.notifyDataSetChanged();

                }
                if(check_odd.isChecked())
                {
                    check_odd.setChecked(false);
                    check_even.setChecked(true);
                }
                else
                {
                    check_even.setChecked(true);
                }

            }
        });

        check_odd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(list.size()>0)
                {
                    list.clear();
                    tableAdaper = new TableAdapter(list, getActivity(),tv_subBid,tv_subAmount,lin_submit);
                    list_table.setAdapter(tableAdaper);
                    tableAdaper.notifyDataSetChanged();

                }
                if(check_even.isChecked())
                {
                    check_odd.setChecked(true);
                    check_even.setChecked(false);
                }
                else
                {
                    check_odd.setChecked(true);
                }

            }
        });



        w_amount = ((SelectGameActivity) getActivity()).getGameWallet();;


        return view;
    }

    private void initview(View view) {

        et_point=view.findViewById (R.id.et_point);
        tv_date=view.findViewById(R.id.tv_date);
        tv_type=view.findViewById (R.id.tv_type);
        tv_type.setOnClickListener (this);
        tv_date.setOnClickListener (this);
        check_even=view.findViewById (R.id.check_even);
        check_odd=view.findViewById (R.id.check_odd);
        btn_add=view.findViewById (R.id.btn_add);
        btn_add.setOnClickListener (this);
        btn_submit=view.findViewById (R.id.btn_submit);
        btn_submit.setOnClickListener (this);
        tv_subBid = view.findViewById(R.id.tv_subBid);
        tv_subAmount = view.findViewById(R.id.tv_subAmount);
        lin_submit = view.findViewById(R.id.lin_submit);

        list=new ArrayList<> ();
        list_table=view.findViewById(R.id.list_table);
        module=new Module (getContext ());
       /// module.getCurrentDate(tv_date);
        if(market_status.equalsIgnoreCase ("open")) {
            module.getCurrentDate(tv_date);
        }
        else
        {
            tv_date.setText ("Select Date");
            module.getCurrentDate(tv_date);
        }
        gamedate=tv_date.getText ().toString ();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        String point=et_point.getText ().toString ();

        if(v.getId ()==R.id.btn_add)
        {
            
            betdate = tv_date.getText().toString();
            bettype = tv_type.getText().toString();
            bettype = tv_type.getText().toString();

            if(betdate.equalsIgnoreCase ("SELECT DATE"))
            {
                module.fieldRequired ("Please Select Date");

            }
            else if(bettype.equalsIgnoreCase ("SELECT GAME TYPE"))
            {
                module.fieldRequired ("Please Select Game Type");
            }
            else if(!check_odd.isChecked ()&&!check_even.isChecked ()){
                module.fieldRequired ("Please Select Odd & Even");
            }



            else  if(et_point.getText ().toString ().isEmpty ()){
                et_point.setError ("Point Required");
                et_point.requestFocus ();
            }
            else {
                int points = Integer.parseInt(et_point.getText().toString().trim());

                if (points < min_bet_amount) {
                    et_point.setError(getResources().getString(R.string.min_bet_value)+" "+min_bet_amount);
                    et_point.requestFocus();
                    return;


                }else if (points>max_bet_amount){
                    et_point.setError(getResources().getString(R.string.max_bet_value)+" "+max_bet_amount);
                    et_point.requestFocus();
                    return;
                }



                else if (points > Integer.parseInt(w_amount)) {
                    module.fieldRequired ("Insufficient Amount");
                }
                else {

                    int num=1;
                    for (int n = 0; n < list.size(); n++) {

//                      num = num +Integer.parseInt (String.valueOf (list.get (n)));
                      num=num+1;
                    }
                    String number=String.valueOf(num);


                    String p=et_point.getText().toString().trim();
                    if(check_odd.isChecked())
                    {

                        String[] odd={"1","3","5","7","9"};

                        for(int i=0; i<=odd.length-1; i++)
                        {

                            module.addData(number,"Odd",odd[i],p,bettype,list,tableAdaper,list_table,btn_submit,tv_subBid,tv_subAmount,lin_submit);

                        }
                    }
                    else if(check_even.isChecked())
                    {

                        String[] even={"0","2","4","6","8"};


                        for(int i=0; i<=even.length-1; i++)
                        {

                            module.addData(number,"Even",even[i],p,bettype,list,tableAdaper,list_table,btn_submit,tv_subBid,tv_subAmount,lin_submit);


                        }
                    }
                    else
                    {
                       module.errorToast(getContext(),"Please select any digit type");
                        return;
                    }
                    //module.addData(number,"Odd Even",check_value,point,bettype,list,tableAdaper,list_table,btn_submit);
                    et_point.requestFocus();
                    clearData ();


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
    public  void placedBid(String dialogType,Dialog dialog){
        {
            int er = list.size();
            if (er <= 0) {
                String message = "Please Add Some Bids";
                module.fieldRequired (message);
                return;

            } else {

                try {
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    String cur_time = format.format(date);
                    String cur_date = sdf.format(date);

                    Log.e("true", "today");
                    Date s_date = format.parse(s_time);
                    Date e_date = format.parse(e_time);
                    Date c_date = format.parse(cur_time);
                    long difference = c_date.getTime() - s_date.getTime();
                    long as = (difference / 1000) / 60;

                    long diff_close = c_date.getTime() - e_date.getTime();
                    long curr = (diff_close / 1000) / 60;
                    long current_time = c_date.getTime();



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
    }
    private void clearData() {

        et_point.setText ("");

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