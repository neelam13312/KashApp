package in.games.GamesSattaBets.Fragment.GamesFragment;

import static in.games.GamesSattaBets.Activity.MainActivity.starline_id;
import static in.games.GamesSattaBets.Activity.SplashActivity.max_bet_amount;
import static in.games.GamesSattaBets.Activity.SplashActivity.min_bet_amount;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.games.GamesSattaBets.Adapter.FinalBidAdapter;
import in.games.GamesSattaBets.Adapter.TableAdapter;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Fragment.SelectGameActivity;
import in.games.GamesSattaBets.Model.TableModel;
import in.games.GamesSattaBets.R;


public class DigitBasedJodiFragment extends Fragment implements View.OnClickListener {
LinearLayout lin_selectDate;
TextView tv_date;
EditText et_lDigit,et_rDigit,et_points;
Button btn_add,btn_submit;
Module module;
String w_amount="",market_status;
TextView tv;
ListView list_table;
TableAdapter tableAdaper;
List<TableModel> list;
Dialog dialog;

    private final String[] single_digits={"0","1","2","3","4","5","6","7","8","9"};

TextView tv_date1,tv_date2,tv_date3,txtDate_id;
private String matka_id,e_time,s_time ,matka_name , game_id , game_name ,type = "open" ,game_date="",title="",is_market_open_nextday,is_market_open_nextday2;
TextView tv_subBid,tv_subAmount;
LinearLayout lin_submit;

 public DigitBasedJodiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_digit_based_jodi, container, false);

        initView(view);
        int  m_id = Integer.parseInt (matka_id);
        Log.e("digitbased_startline", "onCreateView: "+m_id+"empty"+starline_id );

        if (m_id>starline_id)

        {

            ((SelectGameActivity) getActivity ( )).setGameTitle (title);
           // ((SelectGameActivity) getActivity ( )).setGameTitle ("STARLINE");

            tv_date.setVisibility(View.GONE);

            try {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy");

                String cur_date = sdf.format(date);
                if(market_status.equals ("open")) {
                    tv_date.setText (cur_date);
                }
                else
                {
                    tv_date.setText ("Select Date");
                }
                if (module.getTimeDifference(s_time) > 0) {


                } else {

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
//            module.getCurrentDate(tv_date);
            ((SelectGameActivity) getActivity()).setGameTitle(title + " (" + matka_name + ")");
          //  matka_name = getArguments().getString("matka_name");
        }

        return view;
    }

    private void initView(View view) {
        w_amount = ((SelectGameActivity) getActivity()).getGameWallet();
        module = new Module(getActivity());
        list=new ArrayList<>();

        matka_name = getArguments().getString("matka_name");
        game_name = getArguments().getString("game_name");
        matka_id = getArguments().getString("m_id");
        game_id = getArguments().getString("game_id");
        s_time = getArguments().getString("start_time");
        e_time = getArguments().getString("end_time");
        title = getArguments().getString("title");
        market_status=getArguments ().getString ("market_status");
        is_market_open_nextday=getArguments ().getString ("is_market_open_nextday");
        is_market_open_nextday2=getArguments ().getString ("is_market_open_nextday2");

        list_table=view.findViewById(R.id.list_table);
      //  lin_selectDate = view.findViewById(R.id.lin_selectDate);
        tv_date = view.findViewById(R.id.tv_date);
        et_lDigit = view.findViewById(R.id.et_lDigit);
        et_rDigit = view.findViewById(R.id.et_rDigit);
        et_lDigit.setOnClickListener(this);
        et_rDigit.setOnClickListener(this);
        et_points = view.findViewById(R.id.et_points);
        btn_add = view.findViewById(R.id.btn_add);
        btn_submit = view.findViewById(R.id.btn_submit);
        tv_subBid = view.findViewById(R.id.tv_subBid);
        tv_subAmount = view.findViewById(R.id.tv_subAmount);
        lin_submit = view.findViewById(R.id.lin_submit);


        btn_add.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        tv_date.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                module.setDateDialog (is_market_open_nextday,is_market_open_nextday2,market_status,dialog,matka_id,tv_date1,tv_date2,tv_date3,txtDate_id,tv_date);
                break;
            case R.id.btn_submit:
                module.sessionOut();
                placedBid("show",dialog);
                break;

            case R.id.et_lDigit:
                //leftValidation();
                et_rDigit.setText ("");
                et_rDigit.getText ().clear ();
                break;
            case R.id.et_rDigit:
//               et_lDigit.setText ("");
                et_lDigit.setText ("");
                et_lDigit.getText ().clear ();
                break;
        }
        if (fragment!=null)
        {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();
        }
    }


    private void onValidation() {
        String lDigit = et_lDigit.getText().toString();
        String rDigit = et_rDigit.getText().toString();
        String points = et_points.getText().toString();
        game_date = tv_date.getText().toString();

            if(game_date.equalsIgnoreCase ("Select Date"))
            {
                module.fieldRequired ("Date Required");
            }
//            else
//         if (TextUtils.isEmpty(lDigit)) {
//            et_lDigit.setError("Please enter digit");
//            et_lDigit.requestFocus();
//            return;
//        }  else if (TextUtils.isEmpty(rDigit)) {
//            et_rDigit.setError("Please enter digit");
//            et_rDigit.requestFocus();
//            return;
//        }

        else if (TextUtils.isEmpty (lDigit) && TextUtils.isEmpty (rDigit)) {
            module.fieldRequired ("Please enter digit");
//            et_lDigit.setError("Please enter digit");
//            et_lDigit.requestFocus();
            return;
        }

//            else if(!Arrays.asList(single_digits).contains(lDigit))
//            {
//                module.showToast("This is invalid House");
//                et_pana.setText("");
//                et_pana.requestFocus();
//                return;
//            }
        else if (!(lDigit.isEmpty ( ) || (rDigit.isEmpty ( )))) {
                module.fieldRequired ("Please select only one digit");
                et_rDigit.setText (null);
                et_lDigit.setText (null);

                return;
            }
       else if (TextUtils.isEmpty(points)) {
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
                }else if (pints > Integer.parseInt(w_amount)) {
                module.errorToast("Insufficient Amount");

            }else {

                int num = 1;
                for (int n = 0; n < list.size(); n++) {
                    num=num+1;
                }
                String number=String.valueOf(num);
                String p;
                ArrayList<String> digitList = new ArrayList<> ( );
                if (!lDigit.isEmpty ( )) {
                    for (int i = 0; i < single_digits.length; i++) {
                        p = et_lDigit.getText ( ).toString ( ).trim ( );
                        digitList.add (p + single_digits[i]);

                    }
                    Log.e ("leftdigitList", digitList.toString ( ));

                }
                else {
                    for (int i = 0; i < single_digits.length; i++) {
                        p = et_rDigit.getText ( ).toString ( ).trim ( );
                        digitList.add (single_digits[i] + p);

                    }
                    Log.e ("rightdigitList", digitList.toString ( ));

                }
                setArray (digitList, points, "Close");
                //module.addData(number, game_name, lDigit+"-"+rDigit, points, "close", list, tableAdaper, list_table, btn_submit,tv_subBid,tv_subAmount,lin_submit);
               // clearData();
               // et_lDigit.requestFocus();
            }
        }
    }

    public void setArray(ArrayList<String>array ,String p, String th) {
        for (int i = 0; i < array.size ( ); i++) {
            // module.addData(number, "DIGIT BASED JODI", lDigit+"-"+rDigit, points, "close", list, tableAdaper, list_table, btn_submit);


            int num = 1;
            for (int n = 0; n < list.size ( ); n++) {
                num = num + 1;
            }
            String number = String.valueOf (num);
            module.addData (number, game_name, array.get (i), p, "Close", list, tableAdaper, list_table, btn_submit, tv_subBid, tv_subAmount, lin_submit);

            // module.addData(number,"JODI",array.get(i), p,th,list, tableAdaper, list_table,btn_submit);
            et_lDigit.setText ("");
            et_points.setText ("");
            et_lDigit.requestFocus ( );
        }
    }

    private void clearData() {
        et_rDigit.setText ("");
        et_lDigit.setText("");
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
                ArrayList list_digits = new ArrayList();
                ArrayList list_type = new ArrayList();
                ArrayList list_points = new ArrayList();
                int rows = list.size();

                for (int i = 0; i < rows; i++) {


                    TableModel tableModel = list.get(i);
                    String asd = tableModel.getDigits();
//                    String d_all[] = asd.split("-");
//                    String d0 = d_all[0].toString();
//                    String d1 = d_all[1].toString();

                    String asd1 = tableModel.getPoints().toString();

                    amt = amt + Integer.parseInt(asd1);

                }

                int wallet_amount = Integer.parseInt(w_amount);
                if (wallet_amount < amt) {
                    module.errorToast("Insufficient Amount");
                } else {
//                    module.setBidsDialog(Integer.parseInt(w_amount),list,matka_id,"10","10",w_amount,"matka_name",btn_submit, "s_time", "e_time");
                        if (dialogType.equals("placed")) {
                            module.setBidsDialog(Integer.parseInt(w_amount), list, matka_id, game_date.substring(0, 10), game_id, w_amount, matka_name, dialog.findViewById (R.id.btn_dialogSubmit), s_time, e_time,dialog);
                        }else{
                         showPlacingBidData(matka_name,tv_subBid.getText().toString(),tv_subAmount.getText().toString(),w_amount);
                    }

                }} catch (Exception err) {
                err.printStackTrace();
                module.errorToast("Err" + err.getMessage());
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