package in.games.Gameskash.Fragment.GamesFragment;

import android.app.Dialog;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import in.games.Gameskash.Adapter.AddDuplicatesCommonAdpater;
import in.games.Gameskash.Adapter.FinalBidAdapter;
import in.games.Gameskash.Config.Module;
import in.games.Gameskash.Fragment.SelectGameActivity;
import in.games.Gameskash.Model.TableModel;
import in.games.Gameskash.R;

import static in.games.Gameskash.Activity.MainActivity.starline_id;
import static in.games.Gameskash.Activity.SplashActivity.max_bet_amount;
import static in.games.Gameskash.Activity.SplashActivity.min_bet_amount;
import static in.games.Gameskash.Config.list_input_data.group_jodi_digits;

public class JodiDigitBulkFragment extends Fragment implements View.OnClickListener {
    AutoCompleteTextView et_digit;
    EditText et_point;
    Button btn_add, btn_submit;
    ;
    ListView list_table;
    //    TableAdapter tableAdaper;
    AddDuplicatesCommonAdpater tableAdaper;
    Module module;
    Dialog dialog;
    List<String> digitlist;
    List<TableModel> list;
    TextView tv;
    String market_status = "", betdate, w_amount = "", bettype, s_time, e_time, matka_name, game_id, matka_id, game_name, title,is_market_open_nextday,is_market_open_nextday2;
    TextView tv_date, tv_open, tv_close, tv_single, tv_jodi, tv_date1, tv_date2, tv_date3, txtDate_id, tv_type;
    TextView tv_subBid, tv_subAmount;
    LinearLayout lin_submit;

    public JodiDigitBulkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate (R.layout.fragment_jodi_digit_bulk, container, false);
        market_status = getArguments ( ).getString ("market_status");
        is_market_open_nextday = getArguments ( ).getString ("is_market_open_nextday");
        is_market_open_nextday2 = getArguments ( ).getString ("is_market_open_nextday2");
        matka_name = getArguments ( ).getString ("matka_name");
        game_name = getArguments ( ).getString ("game_name");
        matka_id = getArguments ( ).getString ("m_id");
        game_id = getArguments ( ).getString ("game_id");
        s_time = getArguments ( ).getString ("start_time");
        e_time = getArguments ( ).getString ("end_time");
        title = getArguments ( ).getString ("title");

        initview (view);

        int m_id = Integer.parseInt (matka_id);

        w_amount = ((SelectGameActivity) getActivity ( )).getGameWallet ( );
        Log.e("jodiw_amount", "onCreateView: "+w_amount );

        Log.e ("jodidigit_startline", "onCreateView: " + m_id + "empty" + starline_id);

        if (m_id > starline_id) {
            market_status = getArguments ( ).getString ("market_status");
                ((SelectGameActivity) getActivity ( )).setGameTitle (title);
            } else {
                ((SelectGameActivity) getActivity ( )).setGameTitle (title + " (" + matka_name + ")");


//        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,group_jodi_digits);
//        et_digit.setAdapter(adapter2);
//            digitlist = Arrays.asList (group_jodi_digits);
            return view;
        }
        return view;
    }



        private void initview (View view){
            et_digit = view.findViewById (R.id.et_digit);
            et_point = view.findViewById (R.id.et_point);
            btn_add = view.findViewById (R.id.btn_add);
            btn_add.setOnClickListener (this);
            btn_submit = view.findViewById (R.id.btn_submit);
            btn_submit.setOnClickListener (this);
            tv_date = view.findViewById (R.id.tv_date);
            tv_subBid = view.findViewById (R.id.tv_subBid);
            tv_subAmount = view.findViewById (R.id.tv_subAmount);
            lin_submit = view.findViewById (R.id.lin_submit);
            module = new Module (getContext ( ));
            tv_date.setOnClickListener (this);
            //module.getCurrentDate(tv_date);

            if (market_status.equals ("open")) {
                module.getCurrentDate (tv_date);
            } else {
                tv_date.setText ("Select Date");
            }
            //tv_type=view.findViewById (R.id.tv_type);
            digitlist = new ArrayList<> ( );
            digitlist = Arrays.asList (group_jodi_digits);

            list = new ArrayList<> ( );
            list_table = view.findViewById (R.id.list_table);
            et_digit.addTextChangedListener (new TextWatcher ( ) {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    Log.e ("beforeTextChanged", s.toString ( ));
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.e ("onTextChanged", s.toString ( ));
                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.e ("afterTextChanged", s.toString ( ));
                    int points = Integer.parseInt(et_point.getText().toString());
                    if (points < min_bet_amount) {
                        et_point.setError(getResources().getString(R.string.min_bet_value)+" "+min_bet_amount);
                        et_point.requestFocus();
                        return;


                    }else if (points>max_bet_amount){
                        et_point.setError(getResources().getString(R.string.max_bet_value)+" "+max_bet_amount);
                        et_point.requestFocus();
                        return;
                    }else {
                        if (s.toString ( ).length ( ) == 2) {
                            addDigits (s.toString ( ));

                        }
                    }

                }

            });
        }


        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick (View v){

            String digit = et_digit.getText ( ).toString ( );
            String point = et_point.getText ( ).toString ( );
            if (v.getId ( ) == R.id.btn_submit) {
                placedBid ("show",dialog);
            } else if (v.getId ( ) == R.id.tv_date) {
                module.setDateDialog (is_market_open_nextday,is_market_open_nextday2,market_status, dialog, matka_id, tv_date1, tv_date2, tv_date3, txtDate_id, tv_date);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void placedBid (String dialogType,Dialog dialog){
            int er = list.size ( );
            if (er <= 0) {
                String message = "Please Add Some Bids";
                module.fieldRequired (message);
                return;

            } else {

                try {
                    Date date = new Date ( );
                    SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy");
                    SimpleDateFormat format = new SimpleDateFormat ("HH:mm:ss");
                    String cur_time = format.format (date);
                    String cur_date = sdf.format (date);

                    Log.e ("true", "today");
                    Date s_date = format.parse (s_time);
                    Date e_date = format.parse (e_time);
                    Date c_date = format.parse (cur_time);
                    long difference = c_date.getTime ( ) - s_date.getTime ( );
                    long as = (difference / 1000) / 60;

                    long diff_close = c_date.getTime ( ) - e_date.getTime ( );
                    long curr = (diff_close / 1000) / 60;
                    long current_time = c_date.getTime ( );

                    if (curr < 0) {
                        if (dialogType.equals ("placed")) {
                            module.setBidsDialog (Integer.parseInt (w_amount), list, matka_id, betdate, game_id, w_amount, matka_name, dialog.findViewById (R.id.btn_dialogSubmit), s_time, e_time,dialog);
                        } else {
                            showPlacingBidData (matka_name, tv_subBid.getText ( ).toString ( ), tv_subAmount.getText ( ).toString ( ), w_amount);

                        }
                    } else {
                        module.fieldRequired ("Biding is Closed Now");
                    }


                } catch (ParseException e) {
                    e.printStackTrace ( );
                }
            }

        }
        private void clearData () {
            et_digit.setText ("");
        }
        public void addDigits (String digits)
        {
            Log.e("jodiet_digit", "addDigits: "+digits );
            Log.e("jodidigitlist", "addDigits: "+digitlist );
            String digit = digits;
            String point = et_point.getText ( ).toString ( );
            betdate = tv_date.getText ( ).toString ( );
            if (betdate.equalsIgnoreCase ("SELECT DATE")) {
                module.fieldRequired ("Date Required");
                et_digit.setText ("");
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
                    module.showToast ("Insufficient Amount");
                } else {

                    int num = 1;
                    for (int n = 0; n < list.size ( ); n++) {
//                      num = num +Integer.parseInt (String.valueOf (list.get (n)));
                        num = num + 1;
                    }
                    String number = String.valueOf (num);

//                    module.addOrRemoveData(number, "jodi", digit, point, "close", list, tableAdaper, list_table, btn_submit,"jodibulk",removeList,numlist.get(position).getNumber());


//                    module.addData(number,"JODI FAMILY",digit,point,"close",list,tableAdaper,list_table,btn_submit,tv_subBid,tv_subAmount,lin_submit);

                    module.addDataDuplicates (number, "jodi", digit, point, "close", list, tableAdaper, list_table, btn_submit, tv_subBid, tv_subAmount, lin_submit);
                    et_digit.requestFocus ( );
                    clearData ( );


                }
            }
        }
        public void showPlacingBidData (String matkaName, String bid, String totalAmount, String wallet){
            Dialog dialog;
            dialog = new Dialog (getActivity ( ));
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
            rec_dialogBid.setLayoutManager(new LinearLayoutManager(getActivity()));

//        rec_dialogBid.setLayoutManager(new LinearLayoutManager(getActivity()));
            FinalBidAdapter finalBidAdapter = new FinalBidAdapter (list, getActivity ( ));
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


            tv_dialogTitle.setText (matkaName);
            tv_dialogBid.setText (bid);
            tv_dialogWallet.setText (wallet);
            tv_dialogAmount.setText (totalAmount);
            int tot = Integer.parseInt (wallet) - Integer.parseInt (totalAmount);
            tv_dialogAfterWallet.setText (String.valueOf (tot));

            dialog.setCanceledOnTouchOutside (true);
            dialog.show ( );

            btn_dialogSubmit.setOnClickListener (new View.OnClickListener ( ) {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    placedBid ("placed",dialog);
                }
            });

            btn_dialogCancel.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    dialog.dismiss ( );
                }
            });

        }


}