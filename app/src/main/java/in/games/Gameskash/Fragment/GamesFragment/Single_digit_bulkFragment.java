package in.games.Gameskash.Fragment.GamesFragment;

import static in.games.Gameskash.Activity.MainActivity.starline_id;
import static in.games.Gameskash.Config.list_input_data.single_digit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import in.games.Gameskash.Adapter.FinalBidAdapter;
import in.games.Gameskash.Adapter.SingleDigitNumberAdpter;
import in.games.Gameskash.Adapter.TableAdapter;
import in.games.Gameskash.Config.Module;
import in.games.Gameskash.Fragment.SelectGameActivity;
import in.games.Gameskash.Model.SingleDigitNumberModel;
import in.games.Gameskash.Model.TableModel;
import in.games.Gameskash.R;
import in.games.Gameskash.Util.RecyclerTouchListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Single_digit_bulkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Single_digit_bulkFragment extends Fragment implements View.OnClickListener {
    AutoCompleteTextView et_digit;
    RecyclerView rec_number;
    int points=0;
    EditText et_point;

    Dialog dialog;
    ListView list_table;
    TableAdapter tableAdaper;
    Button btn_add,btn_submit;
    List<TableModel> list;
    String name="SIngle Ghar Bulk",betdate,bettype,w_amount="",game_name, matka_name, matka_id ,game_id,s_time ,e_time,no,title,is_market_open_nextday,is_market_open_nextday2;
    Module module;
    RelativeLayout rel_single,rel_jodi;
    List<String> digitlist;
    String game,market_status;
    String gamedate;
    ArrayList<SingleDigitNumberModel>numlist;
    SingleDigitNumberAdpter adpter;
    TextView tv_date,tv_type,tv_open,tv_close,tv_single,tv_jodi,tv_date1,tv_date2,tv_date3,txtDate_id;

    TextView tv_subBid,tv_subAmount;
    LinearLayout lin_submit;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Single_digit_bulkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ingle_digit_bulkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Single_digit_bulkFragment newInstance(String param1, String param2) {
        Single_digit_bulkFragment fragment = new Single_digit_bulkFragment ( );
        Bundle args = new Bundle ( );
        args.putString (ARG_PARAM1, param1);
        args.putString (ARG_PARAM2, param2);
        fragment.setArguments (args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        if (getArguments ( ) != null) {
            mParam1 = getArguments ( ).getString (ARG_PARAM1);
            mParam2 = getArguments ( ).getString (ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_ingle_digit_bulk, container, false);
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
        int  m_id = Integer.parseInt (matka_id);
//int min_amt= Integer.parseInt (min_add_amount);

        Log.e("single_digit_startline", "onCreateView: "+m_id  + "empty"+ starline_id );

        if (m_id>starline_id)
        {

            ((SelectGameActivity) getActivity ( )).setGameTitle (title);

            tv_date.setVisibility(View.GONE);
            tv_type.setVisibility(View.GONE);
            try {
                if(market_status.equals ("open")) {
                    module.getCurrentDate(tv_date);
                }
                else
                {
                    tv_date.setText ("Select Date");
                }
              //  module.getCurrentDate(tv_date);
                if (module.getTimeDifference(s_time) > 0) {

                    tv_type.setText("Open");
                } else {
                  //  tv_type.setText("Close");
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

            tv_type.setVisibility(View.VISIBLE);
            matka_name = getArguments().getString("matka_name");
            //module.getCurrentDate(tv_date);

            if(market_status.equals ("open")) {

                module.getCurrentDate(tv_date);
            }
            else
            {
                tv_date.setText ("Select Date");
            }
        }



        w_amount = ((SelectGameActivity) getActivity()).getGameWallet();
        final  Bundle bundle = getArguments ();
       // name= bundle.getString ("type");
        game=bundle.getString ("matka_name");
//        if(name.equalsIgnoreCase ("SINGLE")){

            et_digit.setFilters (new InputFilter[]{new InputFilter.LengthFilter (1)});
            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,single_digit);
            et_digit.setAdapter(adapter1);
            digitlist= Arrays.asList (single_digit);
            tv_single.setVisibility (View.VISIBLE);
            tv_jodi.setVisibility (View.GONE);

//        }

setRec_number();


        rec_number.addOnItemTouchListener(new RecyclerTouchListener (getActivity (), rec_number, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.e("sdfgthy","Sdfgh");


               TextView tv_point=(TextView)view.findViewById(R.id.tv_point);

                String point=et_point.getText ().toString ();
                Log.e("sadfg",numlist.get(position).getNumber ());


                {
                    betdate = tv_date.getText().toString();
                    bettype = tv_type.getText().toString();



                    if(betdate.equalsIgnoreCase ("SELECT DATE"))
                    {
                        module.fieldRequired ("Please Select Date");

                    }

                    else if(bettype.equalsIgnoreCase ("SELECT GAME TYPE"))
                    {
                        module.fieldRequired ("Please Select Game Type");

                    }

                    else  if(et_point.getText ().toString ().isEmpty ()){
                        et_point.setError ("Point Required");
                        et_point.requestFocus ();
                    }
                    else  if(!digitlist.contains (numlist.get (position).getNumber ())){
                        et_digit.setError ("Invalid");
                        et_digit.setText ("");
                        et_digit.requestFocus ();
                    }
                    else {
                        int points = Integer.parseInt(et_point.getText().toString().trim());
                        if (points < 10) {
                            et_point.setError("Minimum Biding amount is 10");
                            et_point.requestFocus();
                            return;


                        }
                        else if (points > Integer.parseInt(w_amount)) {
                            module.fieldRequired ("Insufficient Amount");
                        }
                        else {
                            points=(Integer.parseInt (tv_point.getText ().toString ()))+(Integer.parseInt (et_point.getText ().toString ()));

                            tv_point.setText(String.valueOf(points));
                            int num=1;
                            for (int n = 0; n < list.size(); n++) {
//                      num = num +Integer.parseInt (String.valueOf (list.get (n)));
                                num=num+1;
                            }

                            String number=String.valueOf(num);

                            module.addData(number,name,numlist.get (position).getNumber (),point,bettype,list,tableAdaper,list_table,btn_submit,tv_subBid,tv_subAmount,lin_submit);
                            et_digit.requestFocus();
                            clearData ();


                        }
                    }

                }
//                et_points.setText(pointlist.get(position).getPoint());
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        return view;

    }

    private void setRec_number() {
        numlist.add (new SingleDigitNumberModel ( "1" ));
        numlist.add (new SingleDigitNumberModel ( "2" ));
        numlist.add (new SingleDigitNumberModel ( "3" ));
        numlist.add (new SingleDigitNumberModel ( "4" ));
        numlist.add (new SingleDigitNumberModel ( "5" ));
        numlist.add (new SingleDigitNumberModel ( "6" ));
        numlist.add (new SingleDigitNumberModel ( "7" ));
        numlist.add (new SingleDigitNumberModel ( "8" ));
        numlist.add (new SingleDigitNumberModel ( "9" ));
        numlist.add (new SingleDigitNumberModel ( "0" ));
        adpter=new SingleDigitNumberAdpter (getActivity (),numlist);
        rec_number.setAdapter (adpter);




    }


    private void initview(View view) {
        rec_number=view.findViewById (R.id.rec_number);
        rec_number.setLayoutManager(new GridLayoutManager (getActivity (),3));

        module=new Module (getActivity ());
        numlist=new ArrayList<SingleDigitNumberModel> (  );
        rel_single=view.findViewById (R.id.rel_single);
        et_digit=view.findViewById (R.id.et_digit);
        et_point=view.findViewById (R.id.et_point);
        btn_add=view.findViewById (R.id.btn_add);
        btn_add.setOnClickListener (this);
        btn_submit=view.findViewById (R.id.btn_submit);
        btn_submit.setOnClickListener (this);
        tv_date=view.findViewById (R.id.tv_date);
        tv_type=view.findViewById (R.id.tv_type);
        tv_single=view.findViewById (R.id.tv_single);
        tv_jodi=view.findViewById (R.id.tv_jodi);  tv_subBid = view.findViewById(R.id.tv_subBid);
        tv_subAmount = view.findViewById(R.id.tv_subAmount);
        lin_submit = view.findViewById(R.id.lin_submit);

        tv_type.setOnClickListener (this);
        tv_date.setOnClickListener (this);
        list=new ArrayList<> ();
        list_table=view.findViewById(R.id.list_table);
        digitlist=new ArrayList<> ();

        gamedate=tv_date.getText ().toString ();



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {


        String digit=et_digit.getText ().toString ();
        String point=et_point.getText ().toString ();
        if(v.getId ()==R.id.btn_add){
            betdate = tv_date.getText().toString();
            bettype = tv_type.getText().toString();
//            if(name.equalsIgnoreCase ("Jodi")){
//                tv_type.setText ("close");
//                int betType;
////
//                betType=getBetType(getASandC(s_time,e_time));
//                if (betType==1)
//                {
//                    module.errorToast (getContext(),"Bidding is closed for today !");
//                }
//                else{
//                    if(betdate.equalsIgnoreCase ("SELECT DATE"))
//                    {
//                        module.fieldRequired ("Please Select Date");
//
//                    }
//
//                    else if(bettype.equalsIgnoreCase ("SELECT GAME TYPE"))
//                    {
//                        module.fieldRequired ("Please Select Game Type");
//
//                    }
//                    else if(et_digit.getText ().toString ().isEmpty ()){
//                        et_digit.setError ("Digit Required");
//                        et_digit.requestFocus ();
//
//                    }
//                    else  if(et_point.getText ().toString ().isEmpty ()){
//                        et_point.setError ("Point Required");
//                        et_point.requestFocus ();
//                    }
//                    else  if(!digitlist.contains (digit)){
//                        et_digit.setError ("Invalid");
//                        et_digit.setText ("");
//                        et_digit.requestFocus ();
//                    }
//                    else {
//                        int points = Integer.parseInt(et_point.getText().toString().trim());
//                        if (points < 10) {
//                            et_point.setError("Minimum Biding amount is 10");
//                            et_point.requestFocus();
//                            return;
//
//
//                        }
//                        else if (points > Integer.parseInt(w_amount)) {
//                            module.fieldRequired ("Insufficient Amount");
//                        }
//                        else {
//
//                            int num=1;
//                            for (int n = 0; n < list.size(); n++) {
////                      num = num +Integer.parseInt (String.valueOf (list.get (n)));
//                                num=num+1;
//                            }
//                            String number=String.valueOf(num);
//
//                            module.addData(number,name,digit,point,bettype,list,tableAdaper,list_table,btn_submit);
//                            et_digit.requestFocus();
//                            clearData ();
//
//
//                        }
//                    }
//                }
//
//
//            }


            if(betdate.equalsIgnoreCase ("SELECT DATE"))
            {
                module.fieldRequired ("Please Select Date");

            }

            else if(bettype.equalsIgnoreCase ("SELECT GAME TYPE"))
            {
                module.fieldRequired ("Please Select Game Type");

            }
            else if(et_digit.getText ().toString ().isEmpty ()){
                et_digit.setError ("Digit Required");
                et_digit.requestFocus ();

            }
            else  if(et_point.getText ().toString ().isEmpty ()){
                et_point.setError ("Point Required");
                et_point.requestFocus ();
            }
            else  if(!digitlist.contains (digit)){
                et_digit.setError ("Invalid");
                et_digit.setText ("");
                et_digit.requestFocus ();
            }
            else {
                int points = Integer.parseInt(et_point.getText().toString().trim());
                if (points < 10) {
                    et_point.setError("Minimum Biding amount is 10");
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

                    module.addData(number,name,digit,point,bettype,list,tableAdaper,list_table,btn_submit,tv_subBid,tv_subAmount,lin_submit);
                    et_digit.requestFocus();
                    clearData ();


                }
            }

        }
        else if(v.getId ()==R.id.tv_date){
            module.setDateDialog (is_market_open_nextday,is_market_open_nextday2,market_status,dialog,matka_id,tv_date1,tv_date2,tv_date3,txtDate_id,tv_date);
        }
        else if(v.getId ()==R.id.tv_type){
            module.setBetTypeDialog (dialog,gamedate,tv_open,tv_close,tv_type,s_time,e_time,game_id);
        }
        else if(v.getId ()==R.id.btn_submit){
            placedBid("show",dialog);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void placedBid(String dialogType,Dialog dialog)
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
                            Log.e("my", "onClick: " + Integer.parseInt(w_amount) + "" + list + matka_id + betdate + game_id + w_amount + matka_name + s_time + e_time);
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
        FinalBidAdapter finalBidAdapter = new FinalBidAdapter(list,getActivity());
        Log.e("FinalBidAdapter", "showPlacingBidData: "+list.size() );
        if (list.size()<4){
            ViewGroup.LayoutParams params=rec_dialogBid.getLayoutParams();
            Log.e("list", "showPlacingBidData: "+params.height );
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