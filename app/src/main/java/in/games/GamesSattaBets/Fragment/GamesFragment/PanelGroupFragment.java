package in.games.GamesSattaBets.Fragment.GamesFragment;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import static in.games.GamesSattaBets.Adapter.TableAdapter.list_t;
import static in.games.GamesSattaBets.Config.list_input_data.panelGroup;

public class PanelGroupFragment extends Fragment implements View.OnClickListener {
    private final String TAG=PanaFragment.class.getSimpleName();
    Module module;
    Dialog dialog;
    String gamedate;
    AutoCompleteTextView et_panna;
    RelativeLayout rel_single;
    EditText et_point;
    ListView list_table;
    TableAdapter tableAdaper;
    Button btn_add,btn_submit;
    List<TableModel> list;
    List<String> digitlist;
    String market_status="",betdate,bettype,w_amount="",s_time ,e_time,matka_name ,game_id,matka_id,game_name,title,is_market_open_nextday,is_market_open_nextday2;
    TextView tv_date,tv_type,tv_open,tv_close,tv_single,tv_double,tv_triple,tv_date1,tv_date2,tv_date3,txtDate_id;
    TextView tv_subBid,tv_subAmount;
    LinearLayout lin_submit;


    public PanelGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_panel_group, container, false);
        initview(view);
        w_amount = ((SelectGameActivity) getActivity()).getGameWallet();

        Bundle bundle = getArguments ();
            tv_single.setVisibility (View.VISIBLE);
            tv_double.setVisibility (View.GONE);
            tv_triple.setVisibility (View.GONE);
            et_panna.setFilters (new InputFilter[]{new InputFilter.LengthFilter (3)});
            digitlist= Arrays.asList (panelGroup);
            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,panelGroup);
            et_panna.setAdapter(adapter1);


        return view;
    }

    private void initview(View view) {
        et_panna=view.findViewById (R.id.et_panna);
        et_point=view.findViewById (R.id.et_point);
        btn_add=view.findViewById (R.id.btn_add);
        btn_add.setOnClickListener (this);
        btn_submit=view.findViewById (R.id.btn_submit);
        btn_submit.setOnClickListener (this);
        tv_date=view.findViewById (R.id.tv_date);
        tv_type=view.findViewById (R.id.tv_type);
        tv_single=view.findViewById (R.id.tv_single);
        tv_double=view.findViewById (R.id.tv_double);
        tv_triple=view.findViewById (R.id.tv_triple);

        tv_subBid = view.findViewById(R.id.tv_subBid);
        tv_subAmount = view.findViewById(R.id.tv_subAmount);
        lin_submit = view.findViewById(R.id.lin_submit);

        tv_type.setOnClickListener (this);
        tv_date.setOnClickListener (this);
        list=new ArrayList<>();
        digitlist=new ArrayList<> ();
        module=new Module (getContext ());
        list_table=view.findViewById(R.id.list_table);

        gamedate=tv_date.getText ().toString ();
        matka_name = getArguments().getString("matka_name");
        game_name = getArguments().getString("game_name");
        matka_id = getArguments().getString("m_id");
        game_id = getArguments().getString("game_id");
        s_time = getArguments().getString("start_time");
        e_time = getArguments().getString("end_time");
        title = getArguments().getString("title");
        int  m_id = Integer.parseInt (matka_id);
        market_status=getArguments ().getString ("market_status");
        is_market_open_nextday=getArguments ().getString ("is_market_open_nextday");
        is_market_open_nextday2=getArguments ().getString ("is_market_open_nextday2");
        //int m_id= Integer.parseInt(matka_id);
        if (module.getTimeDifference(s_time) > 0) {
            tv_type.setText("Open");
        } else {
           // tv_type.setText("Close");
            if (module.getTimeDifference(e_time) > 0) {
                tv_type.setText("Close");
            } else{
                tv_type.setText("Open");
            }

        }

        Log.e("panelgroup_startline", "onCreateView: "+m_id  + "empty"+ starline_id );

        if (m_id>starline_id){
            ((SelectGameActivity) getActivity ( )).setGameTitle (title);

            tv_date.setVisibility(View.GONE);
            tv_type.setVisibility(View.GONE);
//            tv_type.setText("Open");
            try {
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy EEEE");
                String cur_date = dateFormat.format(date);
               // tv_date.setText(cur_date);

                if(market_status.equals ("open")) {
                    tv_date.setText(cur_date);
                }
                else
                {
                    tv_date.setText ("Select Date");
                }

                if (module.getTimeDifference(s_time) > 0) {

                    tv_type.setText("Open");

                } else {
                  //  tv_type.setText("Close");
                    if (module.getTimeDifference(e_time) > 0) {
                        tv_type.setText("Close");
                    } else{
                        tv_type.setText("Open");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }else {
            ((SelectGameActivity) getActivity ( )).setGameTitle (title+"("+matka_name+")");
            if(market_status.equals ("open")) {
                module.getCurrentDate(tv_date);
            } else {
                tv_date.setText ("Select Date");
            }

//            module.getCurrentDate(tv_date);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        String panna=et_panna.getText ().toString ();
        String point=et_point.getText ().toString ();
        if (v.getId ( ) == R.id.btn_add) {
            module.sessionOut();
            betdate = tv_date.getText ( ).toString ( );
            bettype = tv_type.getText ( ).toString ( );
            if(betdate.equalsIgnoreCase ("SELECT DATE"))
            {
                module.fieldRequired ("Date Required");
            }

            else if(bettype.equalsIgnoreCase ("SELECT GAME TYPE"))
            {
                module.fieldRequired ("Please Select Game Type");
            }
            else if (et_panna.getText ( ).toString ( ).isEmpty ( )) {
                et_panna.setError ("Panna Required");
                et_panna.requestFocus ( );

            } else if (et_point.getText ( ).toString ( ).isEmpty ( )) {
                et_point.setError ("Point Required");
                et_point.requestFocus ( );
            }
            else  if(!digitlist.contains (panna)){
                et_panna.setError ("Invalid");
                et_panna.setText ("");
                et_panna.requestFocus ();
            }
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
                    int num = 1;
                    for (int n = 0; n < list.size(); n++) {
//                      num = num +Integer.parseInt (String.valueOf (list.get (n)));
                        num=num+1;
                    }
                    String number=String.valueOf(num);


                    module.addPanelGroupData(number,"Panel Group",panna,point,bettype,list,tableAdaper,list_table,btn_submit,tv_subBid,tv_subAmount,lin_submit);
//                    module.addData(number,name,panna,point,bettype,list,tableAdaper,list_table,btn_submit);

                    et_panna.requestFocus();
                    clearData ();

                }
            }

        }


        else if(v.getId ()==R.id.btn_submit){
            module.sessionOut();

          placedBid("show",dialog);
        }

        else if(v.getId ()==R.id.tv_date){
            module.setDateDialog (is_market_open_nextday,is_market_open_nextday2,market_status,dialog,matka_id,tv_date1,tv_date2,tv_date3,txtDate_id,tv_date);
        }
        else if(v.getId ()==R.id.tv_type){
            module.setBetTypeDialog (dialog,gamedate,tv_open,tv_close,tv_type,s_time,e_time,game_id);
        }
    }
public void placedBid(String dialogtype,Dialog dialog)
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
                if (dialogtype.equals("placed")) {
                    module.setBidsDialog(Integer.parseInt(w_amount), list_t, matka_id, betdate, game_id, w_amount, matka_name, dialog.findViewById (R.id.btn_dialogSubmit), s_time, e_time,dialog);
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
        et_panna.setText ("");
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
        FinalBidAdapter finalBidAdapter = new FinalBidAdapter(list_t,getActivity());
        Log.e("FinalBidAdapter", "showPlacingBidData: "+list_t.size() );
        if (list_t.size()<4){
            ViewGroup.LayoutParams params=rec_dialogBid.getLayoutParams();
            Log.e("list_bidadapter", "showPlacingBidData: "+params.height );
            params.height=90;
            rec_dialogBid.setLayoutParams(params);
        }
        else if (list_t.size()>4){
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