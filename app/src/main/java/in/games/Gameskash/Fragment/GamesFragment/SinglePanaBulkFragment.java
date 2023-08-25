package in.games.Gameskash.Fragment.GamesFragment;

import static in.games.Gameskash.Activity.MainActivity.starline_id;
import static in.games.Gameskash.Activity.SplashActivity.max_bet_amount;
import static in.games.Gameskash.Activity.SplashActivity.min_bet_amount;
import static in.games.Gameskash.Config.list_input_data.doublePannaBulk;
import static in.games.Gameskash.Config.list_input_data.singlePaana;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import in.games.Gameskash.Adapter.BulkAdapter;
import in.games.Gameskash.Adapter.FinalBidAdapter;
import in.games.Gameskash.Adapter.NumberAdpter;
import in.games.Gameskash.Config.Module;
import in.games.Gameskash.Fragment.SelectGameActivity;
import in.games.Gameskash.Model.NumberModel;
import in.games.Gameskash.Model.TableModel;
import in.games.Gameskash.R;
import in.games.Gameskash.Util.RecyclerTouchListener;

public class SinglePanaBulkFragment extends Fragment implements View.OnClickListener {
    private final String TAG=PanaFragment.class.getSimpleName();
    Module module;
    Dialog dialog;
    String gamedate;
    RecyclerView rec_number;
    AutoCompleteTextView et_panna;
    RelativeLayout rel_single;
    EditText et_point;
    RecyclerView list_table;
    ArrayList<NumberModel>numlist;
    NumberAdpter adpter;
    BulkAdapter tableAdaper;
    Button btn_add,btn_submit;
    List<TableModel> list;
    List<String> digitlist;

    List<String> single_list;
    List<String> finalList,dummylist;
    ArrayList<String> removeList;
    String addOrRemove="",pageType="";


    String total_amount="",market_status="";
    String name,betdate,bettype,w_amount="",s_time ,e_time,matka_name ,game_id,matka_id,game_name,title,is_market_open_nextday,is_market_open_nextday2;
    TextView tv_date,tv_type,tv_open,tv_close,tv_single,tv_double,tv_triple,tv_date1,tv_date2,tv_date3,txtDate_id;

    TextView tv_subBid,tv_subAmount;
    LinearLayout lin_submit;
    String[] listt= {"137","128","146","236","245","290","380","470","489","560"};
    String point0="0",point1="0",point2="0",point3="0",point4="0",point5="0",point6="0",
            point7="0",point8="0",point9="0";
    String digit0,digit1,digit2,digit3,digit4,digit5,digit6,digit7,digit8,digit9;

    List<String> zeroList,oneList,twoList,threeList,fourList,fiveList,sixList,sevenList,eigthList,nineList;

    public SinglePanaBulkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate (R.layout.fragment_single_pana_bulk, container, false);
        pageType = getArguments().getString("type");
        initview(view);

        w_amount = ((SelectGameActivity) getActivity()).getGameWallet();
        Bundle bundle = getArguments ();
        name= bundle.getString ("panna");
        setRec_number() ;
        return view;
    }

    private void setRec_number() {
        numlist.add (new NumberModel ( "1" ));
        numlist.add (new NumberModel ( "2" ));
        numlist.add (new NumberModel ( "3" ));
        numlist.add (new NumberModel ( "4" ));
        numlist.add (new NumberModel ( "5" ));
        numlist.add (new NumberModel ( "6" ));
        numlist.add (new NumberModel ( "7" ));
        numlist.add (new NumberModel ( "8" ));
        numlist.add (new NumberModel ( "9" ));
        numlist.add (new NumberModel ( "0" ));
        adpter=new NumberAdpter (getActivity (),numlist);
        rec_number.setAdapter (adpter);


//        rec_number.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rec_number, new RecyclerTouchListener.OnItemClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onItemClick(View view, int position) {
//                
//                String panaDigit = numlist.get(position).getNumber();
//
//                betdate = tv_date.getText ( ).toString ( );
//                bettype = tv_type.getText ( ).toString ( );
//                if(betdate.equalsIgnoreCase ("SELECT DATE"))
//                {
//                    module.fieldRequired ("Date Required");
//                }
//
//                else if(bettype.equalsIgnoreCase ("SELECT GAME TYPE"))
//                {
//                    module.fieldRequired ("Please Select Game Type");
//                }
//                else if (et_point.getText ( ).toString ( ).isEmpty ( )) {
//                    et_point.setError ("Point Required");
//                    et_point.requestFocus ( );
//                }
//
//                else {
//                    int points = Integer.parseInt (et_point.getText ( ).toString ( ).trim ( ));
//
//                if (points < min_bet_amount) {
//                    et_point.setError(getResources().getString(R.string.min_bet_value)+" "+min_bet_amount);
//                    et_point.requestFocus();
//                    return;
//
//
//                }else if (points>max_bet_amount){
//                    et_point.setError(getResources().getString(R.string.max_bet_value)+" "+max_bet_amount);
//                    et_point.requestFocus();
//                    return;
//                }
//                else if (points > Integer.parseInt (w_amount)) {
//                        module.fieldRequired ("Insufficient Amount");
//                    }
//                    else {
//            //check work
//
//                        if (panaDigit.equals("1")){
//                            String panaDigitKey = "1";
//                            Integer signlebase = 9;
//                            Log.e("aswdefr", String.valueOf(finalList)+"----------"+String.valueOf(single_list.subList(0,12)));
//                            if (pageType.equals("single")) {
//                               finalList = single_list.subList(0,12);
//                                //finalList= Arrays.asList (listt);
//                            }else {
//                                finalList = single_list.subList(0,9);
//                            }
//                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
//                                removeList.remove(panaDigitKey);
//                                oneList.clear();
//                            }else {
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
//                                removeList.add(panaDigitKey);
//                                oneList.addAll(finalList);
//                            }
//                        }
//                        else if (panaDigit.equals("2")){
//
//                            if (pageType.equals("single")) {
//                                finalList = single_list.subList(12,24);
//                            }else {
//                                finalList = single_list.subList(9,18);
//                            }
//                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
//                                removeList.remove("2");
//                                twoList.clear();
//
//                            }else {
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
//                                removeList.add("2");
//                                twoList.addAll(finalList);
//                            }
//
//                        }
//                        else if (panaDigit.equals("3")){
//
//                            if (pageType.equals("single")) {
//                                finalList = single_list.subList(24,36);
//                            }else {
//                                finalList = single_list.subList(18,27);
//                            }
//                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
//                                removeList.remove("3");
//                                threeList.clear();
//                            }else {
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
//                                removeList.add("3");
//                                threeList.addAll(finalList);
//                            }
//
//                        }else if (panaDigit.equals("4")){
//
//                            if (pageType.equals("single")) {
//                                finalList = single_list.subList(36,48);
//                            }else {
//                                finalList = single_list.subList(27,36);
//                            }
//                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
//                                removeList.remove("4");
//                                fourList.clear();
//                            }else {
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
//                                removeList.add("4");
//                                fourList.addAll(finalList);
//                            }
//
//                        }else if (panaDigit.equals("5")){
//
//
//                            if (pageType.equals("single")) {
//                                finalList = single_list.subList(48,60);
//                            }else {
//                                finalList = single_list.subList(36,45);
//                            }
//                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
//                                removeList.remove("5");
//                                fiveList.clear();
//                            }else {
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
//                                removeList.add("5");
//                                fiveList.addAll(finalList);
//                            }
//
//                        }else if (panaDigit.equals("6")){
//
//
//
//                            if (pageType.equals("single")) {
//                                finalList = single_list.subList(60,72);
//                            }else {
//                                finalList = single_list.subList(45,54);
//                            }
//                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
//                                removeList.remove("6");
//                                sixList.clear();
//                            }else {
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
//                                removeList.add("6");
//                                sixList.addAll(finalList);
//                            }
//
//                        }else if (panaDigit.equals("7")){
//
//                            if (pageType.equals("single")) {
//                                finalList = single_list.subList(72,84);
//                            }else {
//                                finalList = single_list.subList(54,63);
//                            }
//                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
//                                removeList.remove("7");
//                                sevenList.clear();
//                            }else {
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
//                                removeList.add("7");
//                                sevenList.addAll(finalList);
//                            }
//
//                        }else if (panaDigit.equals("8")){
//
//
//                            if (pageType.equals("single")) {
//                                finalList = single_list.subList(84,96);
//                            }else {
//                                finalList = single_list.subList(63,72);
//                            }
//                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
//                                removeList.remove("8");
//                                eigthList.clear();
//                            }else {
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
//                                removeList.add("8");
//                                eigthList.addAll(finalList);
//                            }
//
//                        }else if (panaDigit.equals("9")){
//
//                            if (pageType.equals("single")) {
//                                Log.e("xsdfgb", String.valueOf(single_list.size()));
//                                finalList = single_list.subList(96,108);
//                            }else {
//                                finalList = single_list.subList(72,81);
//                            }
//                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
//                                removeList.remove("9");
//                                nineList.clear();
//                            }else {
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
//                                removeList.add("9");
//                                nineList.addAll(finalList);
//                            }
//                        }else if (panaDigit.equals("0")){
//
//                            if (pageType.equals("single")) {
//                                finalList = single_list.subList(108,single_list.size());
//                            }else {
//                                finalList = single_list.subList(81,single_list.size());
//                            }
//                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
//                                removeList.remove("0");
//                                zeroList.clear();
//                            }else {
//                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
//                                removeList.add("0");
//                                zeroList.addAll(finalList);
//                            }
//                        }
////check work
//                    dummylist.addAll(zeroList);
//                    dummylist.addAll(oneList);
//                    dummylist.addAll(twoList);
//                    dummylist.addAll(threeList);
//                    dummylist.addAll(fourList);
//                    dummylist.addAll(fiveList);
//                    dummylist.addAll(sixList);
//                    dummylist.addAll(sevenList);
//                    dummylist.addAll(eigthList);
//                    dummylist.addAll(nineList);
//                        for (int p=0;p<dummylist.size();p++) {
//                            Log.d ("lastcheck", "onItemClick: "+dummylist.size ());
//                            int num = 1;
//                            for (int n = 0; n < list.size(); n++) {
//                                num=num+1;
//                            }
//                            String number=String.valueOf(num);
//                            module.dummy(number, name, dummylist.get(p), et_point.getText().toString(), bettype, list, tableAdaper, list_table, btn_submit,addOrRemove,removeList,numlist.get(position).getNumber(),tv_subBid,tv_subAmount,lin_submit);
//                        }
//
//                    }
//                }
//            }
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));




        rec_number.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rec_number, new RecyclerTouchListener.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(View view, int position) {
                
                String panaDigit = numlist.get(position).getNumber();

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
                else if (et_point.getText ( ).toString ( ).isEmpty ( )) {
                    et_point.setError ("Point Required");
                    et_point.requestFocus ( );
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
                    }
                    else {
//check work

                        if (panaDigit.equals("1")){
                            String panaDigitKey = "1";
                            Integer signlebase = 9;
                            Log.e("aswdefr", String.valueOf(finalList)+"----------"+String.valueOf(single_list.subList(0,12)));

                            if (pageType.equals("single")) {
                               finalList = single_list.subList(0,12);
                                //finalList= Arrays.asList (listt);
                            }else {
                                finalList = single_list.subList(0,9);
                            }
                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
                                removeList.remove(panaDigitKey);
                            }else {
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                                removeList.add(panaDigitKey);
                            }
                        }
                        else if (panaDigit.equals("2")){


                            if (pageType.equals("single")) {
                                finalList = single_list.subList(12,24);
                            }else {
                                finalList = single_list.subList(9,18);
                            }
                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
                                removeList.remove("2");
                            }else {
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                                removeList.add("2");
                            }

                        }
                        else if (panaDigit.equals("3")){

                            if (pageType.equals("single")) {
                                finalList = single_list.subList(24,36);
                            }else {
                                finalList = single_list.subList(18,27);
                            }
                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
                                removeList.remove("3");
                            }else {
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                                removeList.add("3");
                            }

                        }else if (panaDigit.equals("4")){

                            if (pageType.equals("single")) {
                                finalList = single_list.subList(36,48);
                            }else {
                                finalList = single_list.subList(27,36);
                            }
                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
                                removeList.remove("4");
                            }else {
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                                removeList.add("4");
                            }

                        }else if (panaDigit.equals("5")){


                            if (pageType.equals("single")) {
                                finalList = single_list.subList(48,60);
                            }else {
                                finalList = single_list.subList(36,45);
                            }
                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
                                removeList.remove("5");
                            }else {
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                                removeList.add("5");
                            }

                        }else if (panaDigit.equals("6")){



                            if (pageType.equals("single")) {
                                finalList = single_list.subList(60,72);
                            }else {
                                finalList = single_list.subList(45,54);
                            }
                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
                                removeList.remove("6");
                            }else {
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                                removeList.add("6");
                            }

                        }else if (panaDigit.equals("7")){

                            if (pageType.equals("single")) {
                                finalList = single_list.subList(72,84);
                            }else {
                                finalList = single_list.subList(54,63);
                            }
                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
                                removeList.remove("7");
                            }else {
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                                removeList.add("7");
                            }

                        }else if (panaDigit.equals("8")){


                            if (pageType.equals("single")) {
                                finalList = single_list.subList(84,96);
                            }else {
                                finalList = single_list.subList(63,72);
                            }
                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
                                removeList.remove("8");
                            }else {
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                                removeList.add("8");
                            }

                        }else if (panaDigit.equals("9")){

                            if (pageType.equals("single")) {
                                Log.e("xsdfgb", String.valueOf(single_list.size()));
                                finalList = single_list.subList(96,108);
                            }else {
                                finalList = single_list.subList(72,81);
                            }
                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
                                removeList.remove("9");
                            }else {
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                                removeList.add("9");
                            }
                        }else if (panaDigit.equals("0")){

                            if (pageType.equals("single")) {
                                finalList = single_list.subList(108,single_list.size());
                            }else {
                                finalList = single_list.subList(81,single_list.size());
                            }
                            if(view.findViewById(R.id.lin_point).getBackgroundTintList()==getResources().getColorStateList(R.color.colorPrimary)){
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.white));
                                removeList.remove("0");
                            }else {
                                view.findViewById(R.id.lin_point).setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                                removeList.add("0");
                            }
                        }

//check work

                        for (int p=0;p<finalList.size();p++) {
                            Log.d ("lastcheck", "onItemClick: "+finalList.size ());
                            int num = 1;
                            for (int n = 0; n < list.size(); n++) {
                                num=num+1;
                            }
                            String number=String.valueOf(num);
                            module.dummy(number, name, finalList.get(p), et_point.getText().toString(), bettype, list, tableAdaper, list_table, btn_submit,addOrRemove,removeList,numlist.get(position).getNumber(),tv_subBid,tv_subAmount,lin_submit);
                        }

                    }
                }
            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }


    private void initview(View view) {


        zeroList= new ArrayList<>();
        oneList= new ArrayList<>();
        twoList= new ArrayList<>();
        threeList= new ArrayList<>();
        fourList= new ArrayList<>();
        fiveList= new ArrayList<>();
        sixList= new ArrayList<>();
        sevenList= new ArrayList<>();
        eigthList= new ArrayList<>();
        nineList= new ArrayList<>();


        removeList = new ArrayList<>();
        numlist=new ArrayList<> (  );

        single_list = new ArrayList<>();
        if (pageType.equals("single")) {
            single_list = Arrays.asList(singlePaana);
        }else {
            single_list = Arrays.asList(doublePannaBulk);
        }
        finalList=new ArrayList<>();
        dummylist=new ArrayList<>();

        rec_number=view.findViewById (R.id.rec_number);
        rec_number.setLayoutManager(new GridLayoutManager (getActivity (),5));
        list_table=view.findViewById (R.id.list_table);
        list_table.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        list=new ArrayList<> ();
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
        market_status=getArguments ().getString ("market_status");
        is_market_open_nextday=getArguments ().getString ("is_market_open_nextday");
        is_market_open_nextday2=getArguments ().getString ("is_market_open_nextday2");

        et_point.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                total_amount=s.toString();
                Log.e("beforeTextChanged",total_amount);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("onTextChanged",s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("afterTextChanged",s.toString());

                boolean backSpace = false;
                if (total_amount.length() != s.toString().length()) {
                    backSpace = true;
                }
                if (backSpace) {
                    total_amount = s.toString();
                    String pnts = s.toString();

                }
            }

        });

        int  m_id = Integer.parseInt (matka_id);
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
        }

        Log.e("sglpnnabulk_startline", "onCreateView: "+m_id  + "empty"+ starline_id );

        if (m_id>starline_id)

        {
            ((SelectGameActivity) getActivity ( )).setGameTitle (title);

            tv_date.setVisibility(View.GONE);
            tv_type.setVisibility(View.GONE);
            try {
                Date date = new Date();
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat ("dd/MM/yyyy EEEE");
                String cur_date = dateFormat.format(date);
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
        else {
            ((SelectGameActivity) getActivity ( )).setGameTitle (title+"("+matka_name+")");
            if(market_status.equals ("open")) {
                module.getCurrentDate(tv_date);
            }
            else
            {
                tv_date.setText ("Select Date");
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        String panna=et_panna.getText ().toString ();
        String point=et_point.getText ().toString ();
        if (v.getId ( ) == R.id.btn_add) {

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

            }
            else if (et_point.getText ( ).toString ( ).isEmpty ( )) {
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
                if (points < 10) {
                    et_point.setError ("Minimum Biding amount is 10");
                    et_point.requestFocus ( );
                    return;


                } else if (points > Integer.parseInt (w_amount)) {
                    module.fieldRequired ("Insufficient Amount");
                } else {
                    int num = 1;
                    for (int n = 0; n < list.size(); n++) {
//                      num = num +Integer.parseInt (String.valueOf (list.get (n)));
                        num=num+1;
                    }
                    String number=String.valueOf(num);


                    module.dummy(number,name,panna,point,bettype,list,tableAdaper,list_table,btn_submit,"",removeList,"",tv_subBid,tv_subAmount,lin_submit);

                    et_panna.requestFocus();
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
public void placedBid(String dialogType,Dialog dialog){
    int er = list.size();
    if (er <= 0) {
        String message = "Please Add Some Bids";
        module.fieldRequired (message);
        return;

    } else {



        try {
            Date date = new Date();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat ("dd/MM/yyyy");
            java.text.SimpleDateFormat format = new SimpleDateFormat ("HH:mm:ss");
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
                Log.e("dfrg",betdate);
                if (dialogType.equals("placed")) {
                    module.setBidsDialog(Integer.parseInt(w_amount), list, matka_id, betdate, game_id, w_amount, matka_name, dialog.findViewById (R.id.btn_dialogSubmit), s_time, e_time,dialog);
                }else
                {    showPlacingBidData(matka_name,tv_subBid.getText().toString(),tv_subAmount.getText().toString(),w_amount);

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