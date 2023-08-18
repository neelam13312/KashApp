package in.games.GamesSattaBets.Fragment.AllTable;

import static in.games.GamesSattaBets.Activity.MainActivity.starline_id;
import static in.games.GamesSattaBets.Activity.SplashActivity.min_bet_amount;
import static in.games.GamesSattaBets.Adapter.NewSinglePointsAdapter.is_error;
import static in.games.GamesSattaBets.Config.Constants.KEY_ID;
import static in.games.GamesSattaBets.Config.list_input_data.single_digit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import in.games.GamesSattaBets.Adapter.FinalBidAdapter;
import in.games.GamesSattaBets.Adapter.NewSinglePointsAdapter;
import in.games.GamesSattaBets.Adapter.StarlineGameAdapter;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Fragment.SelectGameActivity;
import in.games.GamesSattaBets.Model.StarlineGameModel;
import in.games.GamesSattaBets.Model.TableModel;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.SessionMangement;

public class SingleDigirtFragment extends Fragment implements View.OnClickListener {
    List<TableModel> list;
    List<String> digit_list ;
    private int val_p=0;
    public static Button btnSubmit;
    public  static TextView btnReset;
    ImageView iv_back,img_notification;
    public static TextView tv_game;

    String matName="";
    String game;
    String gamedate;

    LoadingBar progressDialog;
    private TextView tv_wallet;
    public  static TextView tv_type;
    private String game_id,game_name;
    private String m_id ,end_time,start_time ,bet_type,title;
    String name,betdate,bettype,w_amount="", matka_name, matka_id ,s_time ,e_time,no,is_market_open_nextday="",is_market_open_nextday2="";


    RecyclerView rv_digits ;
    NewSinglePointsAdapter pointsAdapter ;
    String dashName,user_id="";
    Dialog dialog;
    SessionMangement session_management;
    public static ArrayList<TableModel>bet_list,tempList;
    String type="",market_status="";
    TextView tv_title,tv_market,txt_date;

    TextView tv_mid;
    TextView tv_date,tv_open,tv_close,tv_single,tv_jodi,tv_date1,tv_date2,tv_date3,txtDate_id;

    ArrayList<StarlineGameModel> sList;
    RecyclerView rec_market;
    Module common;
    StarlineGameAdapter starlineMarketAdapter;
    public static String th="";

    public SingleDigirtFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_single_digirt, container, false);
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
        Log.e ("get_sta", ": "+market_status );
        Log.e ("get_sta", ": "+is_market_open_nextday );
        Log.e ("get_sta", ": "+is_market_open_nextday2 );


        gamedate=tv_date.getText ().toString ();

        int  m_id = Integer.parseInt (matka_id);
        //int min_amt= Integer.parseInt (min_add_amount);
        if (common.getTimeDifference(s_time) > 0) {

            tv_type.setText("Open");

        } else {
           // tv_type.setText("Close");
            if (common.getTimeDifference(e_time) > 0)
            {
                tv_type.setText("Close");
            }
            else{
                tv_type.setText("Open");
            }
        }

        if (m_id>starline_id)
        {

            ((SelectGameActivity) getActivity ( )).setGameTitle (title);

            tv_date.setVisibility(View.GONE);
            tv_type.setVisibility(View.GONE);
            try {
               // common.getCurrentDate(tv_date);
                if(market_status.equals ("open")) {
                    common.getCurrentDate(tv_date);
                }
                else
                {
                    tv_date.setText ("Select Date");
                }

                if (common.getTimeDifference(s_time) > 0) {

                    tv_type.setText("Open");

                } else {
                   // tv_type.setText("Close");
                    if (common.getTimeDifference(e_time) > 0)
                    {
                        tv_type.setText("Open");

                    }
                    else{
                        tv_type.setText("Close");

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
            ((SelectGameActivity) getActivity ( )).setGameTitle (title+" ("+matka_name+")");
            tv_type.setVisibility(View.VISIBLE);
            matka_name = getArguments().getString("matka_name");
            if(market_status.equals ("open")) {
                common.getCurrentDate(tv_date);
            }
            else
            {
                tv_date.setText ("Select Date");
            }
//            common.getCurrentDate(tv_date);
        }
        w_amount = ((SelectGameActivity) getActivity()).getGameWallet();
        return view;

    }

    private void initview(View view) {
        common=new Module (getActivity ());

        sList = new ArrayList<>();

        session_management = new SessionMangement (getActivity ());
        user_id = session_management.getUserDetails().get(KEY_ID);
        list=new ArrayList<>();
        tv_game=view.findViewById(R.id.tv_game);
        rv_digits =view.findViewById(R.id.rec_jodi);

        progressDialog=new LoadingBar(getActivity ());
        tv_wallet=view.findViewById(R.id.tv_wallet);
        tv_type=view.findViewById (R.id.tv_type);
        tv_type.setOnClickListener (this);
        btnSubmit =view.findViewById(R.id.btn_add);
        btnReset = view.findViewById(R.id.btnreset);
        tv_title=view.findViewById(R.id.tv_title);
        tv_date =view.findViewById(R.id.tv_date);
        tv_date.setOnClickListener(this);
        common.getCurrentDate(tv_date);
        list = new ArrayList<>();
        bet_list=new ArrayList<>();
        tempList=new ArrayList<>();
        btnSubmit.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        digit_list =  Arrays.asList(single_digit);
        pointsAdapter =new NewSinglePointsAdapter (digit_list,getActivity ());
        rv_digits.setNestedScrollingEnabled(false);
        rv_digits.setLayoutManager(new GridLayoutManager (getActivity (),2));
        rv_digits.setAdapter(pointsAdapter);



    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        bettype=tv_type.getText ().toString () ;
        int id = v.getId();
        if (v.getId()==R.id.tv_date){
            common.setDateDialog (is_market_open_nextday,is_market_open_nextday2,market_status,dialog,matka_id,tv_date1,tv_date2,tv_date3,txtDate_id,tv_date);

        }else if(v.getId ()==R.id.tv_type){
            common.setBetTypeDialog (dialog,gamedate,tv_open,tv_close,tv_type,s_time,e_time,game_id);
        }
        else if (id == R.id.btn_add) {
            if (bettype.equalsIgnoreCase ("SELECT GAME TYPE"))
            {
                common.fieldRequired ("Please Select Game Type");

            }else {
//                for (int i=0;i<bet_list.size();i++) {
//                    if (Integer.parseInt(bet_list.get(i).getPoints())>max_)
//                }
                    placedBid("show",dialog);

            }
        }


    }
    public void placedBid(String dialogType ,Dialog dialog)
        {
            tempList.clear();

            Log.e ("wdefrgthy", String.valueOf(bet_list.size ()));
            for(int k=0; k<bet_list.size();k++)
            {
                Log.e ("dfgh", "onClick: "+bet_list.get(k).getPoints().toString() );
                if(bet_list.get(k).getPoints().toString().equals("0") || bet_list.get(k).getPoints().toString().equals(""))
                {
                    //Toast.makeText (getActivity (), "fghjk", Toast.LENGTH_SHORT).show ( );
                }
                else
                {
                    Log.e ("wdefrgthy", "axsdfghyj");
                    tempList.add(bet_list.get(k));
                    Log.e ("list_data", "onClick: "+bet_list.toString ());

                }
            }
            for(TableModel model:tempList){
                Log.e("temp_data",""+model.getDigits()+" - "+model.getPoints());
            }
            if (tempList.size()<=0) {
                common.fieldRequired ("Please enter some points");

            }
            else if( tv_date.getText().toString().equalsIgnoreCase ("Select Date"))
            {
                common.fieldRequired ("Please Select Game Date");
            }
            else if(bettype.equalsIgnoreCase ("SELECT GAME TYPE"))
            {
                common.fieldRequired ("Please Select Game Type");


            }
            else {
                if (is_error) {
                    common.fieldRequired ("Minimum bid amount is "+min_bet_amount);

                } else {


//                    String dt = btnGameType.getText().toString().trim();
                    //String c = txt_date.getText().toString().substring(0, 10);
//                    String d[] = dt.split(" ");

//                    String c = d[0].toString();
                    // String w = tv_wallet.getText().toString().trim();
                    Date date = new Date();
//                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

                    String cur_time = format.format(date);

                    try {
                        Date s_date = format.parse(s_time);
                        Date e_date = format.parse(e_time);
                        Date c_date = format.parse(cur_time);
                        long difference = c_date.getTime() - s_date.getTime();
                        long as = (difference / 1000) / 60;

                        long diff_close = c_date.getTime() - e_date.getTime();
                        long curr = (diff_close / 1000) / 60;
                        long current_time = c_date.getTime();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long bidTime = common.getTimeDifference(e_time);

//                    if(bidTime>0)
//                    {
//                        common.setBidsDialog(tempList, m_id, c, game_id, w, dashName, progressDialog, btnSubmit, start_time, end_time);

                        if (dialogType.equals("placed")) {

                                    common.setBidsDialog (Integer.parseInt (w_amount), tempList, matka_id, tv_date.getText ( ).toString ( ).substring (0, 10), game_id, w_amount, matka_name, dialog.findViewById (R.id.btn_dialogSubmit), s_time, e_time, dialog);



                        } else {
                            String vv="";
                            for (int i = 0; i < tempList.size ( ); i++) {
                                if (Integer.parseInt (tempList.get (i).getPoints ( )) < (min_bet_amount)) {

                                    vv="true";
                                  //  common.errorToast (getContext(),getActivity ( ).getResources ( ).getString (R.string.min_bet_value) + " " + min_bet_amount);
////
                                }
                                else {
                                    vv="false";

                                }
                            }
                            if(vv.equalsIgnoreCase ("true"))
                            {
                                common.errorToast (getContext(),getActivity ( ).getResources ( ).getString (R.string.min_bet_value) + " " + min_bet_amount);
////
                            }else
                            {
                                String total = common.getSumOfPoints (tempList);
                                showPlacingBidData (matka_name, String.valueOf (tempList.size ( )), total, w_amount);

                            }




                        }
                        //Log.e("my", "onClick: "+Integer.parseInt(w_amount)+""+list+ matka_id+betdate+game_id+w_amount+matka_name+s_time+e_time );
//                    }
//                    else
//                    {
//                        common.fieldRequired ("Biding is Closed Now");
//                    }


//                    list.clear();
                }
            }

        }
    public long[] getASandC(String startTime,String endTime){
        long[] tArr=new long[2];
        Date date=new Date();
        SimpleDateFormat sim=new SimpleDateFormat ("HH:mm:ss");
        String time1 = startTime.toString();
        String time2 = endTime.toString();

        Date cdate=new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat ("HH:mm:ss");
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
        Log.e("showPlacingBidData",matkaName+"--"+bid+"---"+totalAmount+"---"+wallet);
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
        Button btn_dialogSubmit = dialog.findViewById (R.id.btn_dialogSubmit);
        Button btn_dialogCancel = dialog.findViewById (R.id.btn_dialogCancel);
        rec_dialogBid.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager( getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_dialogBid.setLayoutManager(layoutManager);
        list = new ArrayList<>();
//        rec_dialogBid.setLayoutManager(new LinearLayoutManager(getActivity()));
        FinalBidAdapter finalBidAdapter = new FinalBidAdapter (tempList, getActivity ( ));
        Log.e("singledigit_BidAdapter", "showPlacingBidData: "+tempList.size() );
        if (tempList.size()<4){
            ViewGroup.LayoutParams params=rec_dialogBid.getLayoutParams();

            params.height=220;
            Log.e("list_bidadapter", "showPlacingBidData: "+params.height );
            rec_dialogBid.setLayoutParams(params);
        }
        else if (tempList.size()>4){
            ViewGroup.LayoutParams params=rec_dialogBid.getLayoutParams();

            params.height=350;
            Log.e("list_4", "showPlacingBidData: "+params.height );

            rec_dialogBid.setLayoutParams(params);
        }

        rec_dialogBid.setAdapter(finalBidAdapter);



        tv_dialogTitle.setText(matkaName);
        tv_dialogBid.setText(bid);
        tv_dialogWallet.setText(wallet);
        tv_dialogAmount.setText(totalAmount);
        double tot = Double.parseDouble(wallet)-Double.parseDouble(totalAmount);
        tv_dialogAfterWallet.setText(String.valueOf(tot));

        dialog.setCanceledOnTouchOutside (true);
        try {
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }

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