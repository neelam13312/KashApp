package in.games.GamesSattaBets.Fragment.GamesFragment;

import static in.games.GamesSattaBets.Activity.SplashActivity.max_bet_amount;
import static in.games.GamesSattaBets.Activity.SplashActivity.min_bet_amount;
import static in.games.GamesSattaBets.Config.list_input_data.two_digit;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.games.GamesSattaBets.Adapter.FinalBidAdapter;
import in.games.GamesSattaBets.Adapter.TableAdapter;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Fragment.SelectGameActivity;
import in.games.GamesSattaBets.Model.TableModel;
import in.games.GamesSattaBets.R;


public class TwoDigitPannaFragment extends Fragment implements View.OnClickListener {

    LinearLayout lin_selectDate,lin_type;
    TextView tv_type,tv_date;
    EditText et_twoDigit,et_points;
    Button btn_add,btn_submit;
    Module module;
    String gamedate;
    String w_amount="";
    ListView list_table;
    TableAdapter tableAdaper;
    List<TableModel> list;
    Dialog dialog;
    List<String> digitlist;
    int count=0;
    String ne="",market_status="";
    ArrayList<String> digitList;
    private final String[] digits={"0","1","2","3","4","5","6","7","8","9"};
    TextView tv_date1,tv_date2,tv_date3,txtDate_id,tv_open,tv_close;
    private String matka_id,e_time,s_time ,matka_name , game_id , game_name ,type = "open" ,game_date="",title="",is_market_open_nextday,is_market_open_nextday2;
    TextView tv_subBid,tv_subAmount;
    LinearLayout lin_submit;

    public TwoDigitPannaFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two_digit_panna, container, false);
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
        gamedate=tv_date.getText ().toString ();
//int min_amt= Integer.parseInt (min_add_amount);
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

        if (m_id>20)
        {

            ((SelectGameActivity) getActivity ( )).setGameTitle (title);

            //((SelectGameActivity) getActivity ( )).setGameTitle ("STARLINE");

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
//                tv_date.setText(cur_date);
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
           // matka_name = getArguments().getString("matka_name");
        }
        return view;
    }

    private void initView(View view) {
        digitList = new ArrayList<>();
        w_amount = ((SelectGameActivity) getActivity()).getGameWallet();
        module = new Module(getActivity());
        list=new ArrayList<>();
        digitlist=new ArrayList<> (  );
        digitlist= Arrays.asList (two_digit);
        digitList.clear ();
        list.clear ();
//        matka_name = getArguments().getString("matka_name");
//        game_name = getArguments().getString("game_name");
//        matka_id = getArguments().getString("m_id");
//        game_id = getArguments().getString("game_id");
//        s_time = getArguments().getString("start_time");
//        e_time = getArguments().getString("end_time");
//        title = getArguments().getString("title");
        btn_submit = view.findViewById(R.id.btn_submit);
        list_table=view.findViewById(R.id.list_table);
//        lin_selectDate = view.findViewById(R.id.lin_selectDate);
//        lin_type = view.findViewById(R.id.lin_type);
        tv_type = view.findViewById(R.id.tv_type);
        tv_date = view.findViewById(R.id.tv_date);
        et_twoDigit = view.findViewById(R.id.et_twoDigit);
        et_points = view.findViewById(R.id.et_points);
        btn_add = view.findViewById(R.id.btn_add);
        tv_subBid = view.findViewById(R.id.tv_subBid);
        tv_subAmount = view.findViewById(R.id.tv_subAmount);
        lin_submit = view.findViewById(R.id.lin_submit);


        btn_add.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        tv_type.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        if(market_status.equals ("open")) {
            module.getCurrentDate(tv_date);
        }
        else
        {
            tv_date.setText ("Select Date");
        }
//        module.getCurrentDate(tv_date);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId())
        {
            case R.id.btn_add:
                
//                Log.e ("list_soize", "onClick: ."+list.size () );
//                if(list.isEmpty ()) {
//                    Log.e ("emptyr", "onClick: ." );
                    onValidation ( );
               // }
//                else
//                {


                   // Log.e ("notemptyr", "jjj: ." );
//                    digitList.clear ();
//                    list.clear ();
                    count=0;

//                    tableAdaper = new TableAdapter (list, getActivity (),tv_subBid, tv_subAmount, lin_submit);
//                    list_table.setAdapter (tableAdaper);
//                    tableAdaper.notifyDataSetChanged ();
                   // onValidation ( );
               // }
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

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onValidation() {
        digitList.clear ();

        String twoDigit = et_twoDigit.getText().toString();;
        String points = et_points.getText().toString();
        String type = tv_type.getText().toString();
         game_date = tv_date.getText().toString();

            if(game_date.equalsIgnoreCase ("Select Date"))
            {
                module.fieldRequired ("Date Required");
            }else
        if (type.equalsIgnoreCase("Select Game Type")) {
            module.fieldRequired("Select game type");

        }  else if (TextUtils.isEmpty(twoDigit)) {
            et_twoDigit.setError("Please enter digit");
            et_twoDigit.requestFocus();
            return;
        }
        else if (!(et_twoDigit.getText ().toString ().length ()==2)) {
            et_twoDigit.setError("Minimum 2 digit required");
            et_twoDigit.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(points)) {
            et_points.setError("Please enter point");
            et_points.requestFocus();
            return;

        }
        else if (!digitlist.contains (et_twoDigit.getText ().toString ())) {
            et_twoDigit.setError("Invalid");
            et_twoDigit.setText ("");
            et_twoDigit.requestFocus();
            return;

        }
        else {
            int pints = Integer.parseInt(points);
            if (pints < min_bet_amount) {
                et_points.setError(getResources().getString(R.string.min_bet_value)+" "+min_bet_amount);
                et_points.requestFocus();
                return;


            }else if (pints>max_bet_amount){
                et_points.setError(getResources().getString(R.string.max_bet_value)+" "+max_bet_amount);
                et_points.requestFocus();
                return;
            }else if (pints > Integer.parseInt(w_amount)) {
                module.errorToast("Insufficient Amount");

            }else {
                int num = 1;
                for (int n = 0; n < list.size(); n++) {
                    num=num+1;
                }
                Log.e ("xmx", "ll");
                ArrayList<String> sortList = new ArrayList<>();
                for(int i=0;i<digits.length;i++) {
                    String p = et_twoDigit.getText().toString().trim();
                    digitList.add(module.cpspData(p+digits[i]));
                    Log.e ("digitistt", ""+digitList.get (i));
                }

//                for(int i=0;i<digitList.size ()-1;i++) {
//                    if(digitList.get(i).contains ("0")) {
//
////                            for (int in = 0; in < digitList.size ( ); in++) {
//                        String str = digitList.get (i);
//                        char strd=0;
//
//
//                        Log.e ("strr", ": " + str);
//                        for(int ii=0;ii<str.length ();ii++) {
//                            Log.e ("vg", ": " + str.charAt (ii));
//                            if(str.charAt (ii)=='0')
////                                Pattern pattern=Pattern.compile ("0");
////                                Matcher matcher=pattern.matcher (str);
//                            // if(matcher.find ())
//                            {
//                                Log.e ("0val", ": " + str.charAt (ii));
//                                count=count+1;
//                               digitList.remove (i);
//                            }
//                            ne = str.replaceAll("0", "");
//                            Log.e ("jjjj", "onValidation: " + ne+"=="+count);
//                            //digitList.remove (i);
//                        }
//
//
//
//
//
////                                String dg = digitList.get (i);
////                                String digit = dg.substring (0, dg.length ( ) - 1) + "0";
//                                Log.e ("ew_valys", "onValidation: " + ne);
//                            //}
//                        }
//                    for(int c=0;c<count;c++)
//                    {
//                        ne=ne+"0";
//                        Log.e ("hdhd", "onValidation: " + ne);
//                    }
////                    if(ne.contains ("0000000000"))
////                    {
////                        digitList.remove (i);
////                    }
//                    }
//
//
//
//                sortList.add (ne);
               // digitList.addAll (sortList);
                Log.e ("val_;it", ""+digitList.size () );





                String number=String.valueOf(num);
                for (int i =0 ;i<digitList.size() ; i++) {
                    Log.e ("digiig", "onValidation: "+digitList.get (i) );
                    module.addData (number, "TWO DIGIT", digitList.get (i), points, type, list, tableAdaper, list_table, btn_submit, tv_subBid, tv_subAmount, lin_submit);

                }
                tableAdaper = new TableAdapter (list, getActivity (),tv_subBid, tv_subAmount, lin_submit);
                list_table.setAdapter (tableAdaper);
                tableAdaper.notifyDataSetChanged ();
                clearData();

                et_twoDigit.requestFocus();
            }
        }
    }
    private void clearData() {

        et_twoDigit.setText("");
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
//                try {
//                    Date date = new Date();
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
//                    String cur_time = format.format(date);
//                    String cur_date = sdf.format(date);
//                    String g_d = game_date.substring(0, 10);
////                Toast.makeText(getActivity(),""+g_d,Toast.LENGTH_LONG).show();
//                    Log.e("date ", String.valueOf(g_d) + "\n" + String.valueOf(cur_date));
//
//                    if (cur_date.equals(g_d)) {
//                        Log.e("true", "today");
//                        Date s_date = format.parse(s_time);
//                        Date e_date = format.parse(e_time);
//                        Date c_date = format.parse(cur_time);
//                        long difference = c_date.getTime() - s_date.getTime();
//                        long as = (difference / 1000) / 60;
//
//                        long diff_close = c_date.getTime() - e_date.getTime();
//                        long curr = (diff_close / 1000) / 60;
//                        long current_time = c_date.getTime();
//
//                        if (as < 0) {
//
//                            module.setBidsDialog(Integer.parseInt(w_amount), list, matka_id, game_date, game_id, w_amount, matka_name, btnSave, s_time, e_time);
//                        } else if (curr < 0) {
//                            module.setBidsDialog(Integer.parseInt(w_amount), list, matka_id, game_date, game_id, w_amount, matka_name, progressDialog, btnSave, s_time, e_time);
//                        } else {
//                            clearData();
//                            module.marketClosed("Biding is Closed Now");
//
//                        }
//                    } else {

//                        module.setBidsDialog(Integer.parseInt(w_amount),list,"11","10","10",w_amount,"matka_name",btn_submit, "s_time", "e_time");
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

}