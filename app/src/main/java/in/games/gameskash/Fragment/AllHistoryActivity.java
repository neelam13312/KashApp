package in.games.gameskash.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import in.games.gameskash.Adapter.AllHistoryAdapter;
import in.games.gameskash.Adapter.FilterAdapter;
import in.games.gameskash.Config.Module;
import in.games.gameskash.Model.AllHistoryModel;
import in.games.gameskash.Model.FilterModel;
import in.games.gameskash.Model.MatkaModel;
import in.games.gameskash.Model.StarlineGameModel;
import in.games.gameskash.R;
import in.games.gameskash.Util.ConnectivityReceiver;
import in.games.gameskash.Util.LoadingBar;
import in.games.gameskash.Util.SessionMangement;

import static in.games.gameskash.Config.BaseUrls.JackpotWIN_HISTORY;
import static in.games.gameskash.Config.BaseUrls.STARLINE_WIN_HISTORY;
import static in.games.gameskash.Config.BaseUrls.URL_GET_HISTORY;
import static in.games.gameskash.Config.BaseUrls.URL_Jackpot_HISTORY;
import static in.games.gameskash.Config.BaseUrls.URL_Matka;
import static in.games.gameskash.Config.BaseUrls.URL_STARLINE;
import static in.games.gameskash.Config.BaseUrls.URL_STARLINE_HISTORY;
import static in.games.gameskash.Config.BaseUrls.WIN_HISTORY;
import static in.games.gameskash.Config.Constants.KEY_ID;
import static in.games.gameskash.Util.AppController.TAG;


public class AllHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rec_history;
    AllHistoryAdapter allHistoryAdapter;
    TextView tv_date, tv_title;
    Button btn_search;
    String cb_dat="";
    DatePickerDialog.OnDateSetListener setListener;
    ArrayList<AllHistoryModel> hList, wList;
    LoadingBar loadingBar;
    Module module;
    SessionMangement sessionMangement;
    String name = "";
    SwipeRefreshLayout swipe;
    ImageView  img_back;
    String type = "", matka_id = "";
    RecyclerView rec_gametype;
    FilterAdapter filterAdapter;
    ArrayList<FilterModel> filterlist;

    ArrayList<MatkaModel> mList = new ArrayList<>();
    ArrayList<StarlineGameModel> sList=new ArrayList<>();
    HashMap<String,Boolean>selectedOptionList=new HashMap<>();
    ArrayList<String>selectedGameType=new ArrayList<>();
    ArrayList<AllHistoryModel> hFilterList=new ArrayList<>();

    //ArrayList<StarlineGameModel> sFilteredList=new ArrayList<>();


    public AllHistoryActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_all_history);
        initview();
       // allMatkaGames();
        // ((MainActivity)getActivity()).setTitles("My History");

        rec_history.setLayoutManager(new LinearLayoutManager(AllHistoryActivity.this));
        if (ConnectivityReceiver.isConnected()) {
            if (type.equalsIgnoreCase("matka")) {
                getHistry(sessionMangement.getUserDetails().get(KEY_ID));
            } else if (type.equalsIgnoreCase("starline")) {
                getStarLineHistory(sessionMangement.getUserDetails().get(KEY_ID));
            } else if (type.equalsIgnoreCase("matka_win")) {
                getWinHistory(sessionMangement.getUserDetails().get(KEY_ID));
            } else if (type.equalsIgnoreCase("starline_win")) {
                getStarlineWinHistory(sessionMangement.getUserDetails().get(KEY_ID));
            } else if (type.equalsIgnoreCase("jackpot")) {
                getJackpotHistry(sessionMangement.getUserDetails().get(KEY_ID), matka_id);
            } else if (type.equalsIgnoreCase("jackpot_win")) {
                getjackpotWinHistory(sessionMangement.getUserDetails().get(KEY_ID));
            }

//            else {
//                getJackpotHistry(sessionMangement.getUserDetails().get(KEY_ID));
//            }
        } else {
            module.noInternet();

        }


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing()) {
                    if (ConnectivityReceiver.isConnected()) {

                        if (type.equalsIgnoreCase("matka")) {
                            getHistry(sessionMangement.getUserDetails().get(KEY_ID));

                        } else if (type.equalsIgnoreCase("starline")) {
                            getStarLineHistory(sessionMangement.getUserDetails().get(KEY_ID));
                        } else if (type.equalsIgnoreCase("matka_win") || type.equalsIgnoreCase("starline_win")) {
                            getWinHistory(sessionMangement.getUserDetails().get(KEY_ID));
                        } else if (type.equalsIgnoreCase("jackpot")) {
                            getJackpotHistry(sessionMangement.getUserDetails().get(KEY_ID), matka_id);
                        } else if (type.equalsIgnoreCase("jackpot_win")) {
                            getjackpotWinHistory(sessionMangement.getUserDetails().get(KEY_ID));
                        }

                    } else {
                        module.noInternet();
                    }

                    swipe.setRefreshing(false);
                }
            }
        });

        getGameTypeFilterList();

    }

    private void refreshFilterData(){
        if (type.equalsIgnoreCase("matka")|| type.equalsIgnoreCase("starline")) {
           // if(selectedOptionList.size()>0 || selectedGameType.size()>0){
                new FilterAsyncTask().execute();
            //}
        }
    }

    private void getGameTypeFilterList() {
        if (ConnectivityReceiver.isConnected()) {

            if (type.equalsIgnoreCase("matka")) {
                allMatkaGames();
            } else if (type.equalsIgnoreCase("starline")) {
                starlineGames();
            }
        }else{
            module.noInternet();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_history, container, false);

        return view;

    }


    private void initview() {
        type = getIntent().getStringExtra("type");
        name = getIntent().getStringExtra("name");
        matka_id = getIntent().getStringExtra("matka_id");
        Log.e("type", type);
        tv_title = findViewById(R.id.tv_title);
        img_back = findViewById(R.id.img_back);
        tv_title.setText("History");

        img_back.setOnClickListener(this);
        sessionMangement = new SessionMangement(AllHistoryActivity.this);
        module = new Module(AllHistoryActivity.this);
        loadingBar = new LoadingBar(AllHistoryActivity.this);
        swipe = findViewById(R.id.swipe);
        rec_history = findViewById(R.id.rec_history);
        hList = new ArrayList<>();
        wList = new ArrayList<>();
        tv_date = findViewById(R.id.tv_date);
        tv_date.setOnClickListener(this);
        btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);


        Date date = new Date();
        SimpleDateFormat simpleDateFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
        }
        String currentdate = simpleDateFormat.format(date);
        tv_date.setText(currentdate);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_date) {
            datePicker();
        } else if (v.getId() == R.id.btn_search) {
            if (allHistoryAdapter != null) {
                allHistoryAdapter.getFilter().filter(tv_date.getText().toString());
            }
        }
//        else if (v.getId() == R.id.img_notification) {
//            Intent intent = new Intent(AllHistoryActivity.this, NotificationFragment.class);
//            startActivity(intent);
////            Fragment fm = null;
////            fm=new NotificationFragment ();
////            FragmentManager fragmentManager = getSupportFragmentManager();
////            fragmentManager.beginTransaction().replace(R.id.frame,fm).addToBackStack(null).commit();
//
//        }
        else if (v.getId() == R.id.img_back) {
            finish();
        } else if (v.getId() == R.id.lin_filter) {
            showBottomSheetDialog();
        }
    }

    private void showBottomSheetDialog() {
       // HashMap<String,Boolean>hashMap=new HashMap<>();

        //selectedGameType.clear();
        if (selectedOptionList.size()==0) {
            selectedOptionList.clear();
            selectedOptionList.put("Open", false);
            selectedOptionList.put("Close", false);
            selectedOptionList.put("Win", false);
            selectedOptionList.put("Loss", false);
            selectedOptionList.put("Pending", false);
        }

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
        CheckBox cb_open = bottomSheetDialog.findViewById(R.id.cb_open);
        CheckBox cb_close = bottomSheetDialog.findViewById(R.id.cb_close);
        CheckBox cb_win = bottomSheetDialog.findViewById(R.id.cb_win);
        CheckBox cb_loss = bottomSheetDialog.findViewById(R.id.cb_loss);
        CheckBox cb_pendind = bottomSheetDialog.findViewById(R.id.cb_pendind);
        TextView tv_dates=bottomSheetDialog.findViewById (R.id.tv_date);
        Button btn_date=bottomSheetDialog.findViewById (R.id.btn_date);
        CheckBox cb_date=bottomSheetDialog.findViewById (R.id.cb_date);
        rec_gametype = bottomSheetDialog.findViewById(R.id.rec_gametype);
        Button btn_cancel=bottomSheetDialog.findViewById(R.id.btn_cancel);
        Button btn_submit=bottomSheetDialog.findViewById(R.id.btn_submit);
        rec_gametype.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AllHistoryActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_gametype.setLayoutManager(layoutManager);
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        tv_dates.setText (date);
        btn_date.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v)
            {

                Calendar calendar = Calendar.getInstance ( );
                final int year = calendar.get (Calendar.YEAR);
                final int month = calendar.get (Calendar.MONTH);
                final int day = calendar.get (Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog (AllHistoryActivity.this, android.R.style.Theme_Material_Light_DarkActionBar, setListener, year, month, day);
                datePickerDialog.getWindow ( ).setLayout (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                datePickerDialog.getDatePicker ( ).setMaxDate (System.currentTimeMillis ( ) - 1000);
                datePickerDialog.show ( );
                datePickerDialog.getButton (DatePickerDialog.BUTTON_NEGATIVE).setTextColor (Color.BLACK);
                datePickerDialog.getButton (DatePickerDialog.BUTTON_POSITIVE).setTextColor (Color.BLACK);


                setListener = new DatePickerDialog.OnDateSetListener ( ) {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        String outputPattern = "dd/MM/yyyy";
                        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
                        Date date1 = null;
                        try {
                            date1 = outputFormat.parse(date);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String d = outputFormat.format(date1);
                        tv_dates.setText (d);
                    }
                };


            }
        });
        tv_dates.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                    Calendar calendar = Calendar.getInstance ( );
                    final int year = calendar.get (Calendar.YEAR);
                    final int month = calendar.get (Calendar.MONTH);
                    final int day = calendar.get (Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog (AllHistoryActivity.this, android.R.style.Theme_Material_Light_DarkActionBar, setListener, year, month, day);
                    datePickerDialog.getWindow ( ).setLayout (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    datePickerDialog.getDatePicker ( ).setMaxDate (System.currentTimeMillis ( ) - 1000);
                    datePickerDialog.show ( );
                    datePickerDialog.getButton (DatePickerDialog.BUTTON_NEGATIVE).setTextColor (Color.BLACK);
                    datePickerDialog.getButton (DatePickerDialog.BUTTON_POSITIVE).setTextColor (Color.BLACK);


                    setListener = new DatePickerDialog.OnDateSetListener ( ) {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            month = month + 1;
                            String date = dayOfMonth + "/" + month + "/" + year;
                            String outputPattern = "dd/MM/yyyy";
                            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
                            Date date1 = null;
                            try {
                                date1 = outputFormat.parse(date);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String d = outputFormat.format(date1);
                            tv_dates.setText (d);
                            cb_dat= tv_dates.getText ().toString ();
                        }
                    };


            }
        });
        filterlist = new ArrayList<>();
        Log.e( "rec_gametype", "showBottomSheetDialog: "+filterlist.size()  );
        if (selectedOptionList.size()>0){
            if (!cb_dat.equalsIgnoreCase ("")){
                cb_date.setChecked(true);
            }else{
                cb_date.setChecked(false);
            }
            if (selectedOptionList.get("Open")){
                cb_open.setChecked(true);
            }else{
                cb_open.setChecked(false);
            }
            if (selectedOptionList.get("Close")){
                cb_close.setChecked(true);
            }else{
                cb_close.setChecked(false);
            }
            if (selectedOptionList.get("Win")){
                cb_win.setChecked(true);
            }else{
                cb_win.setChecked(false);
            }
            if (selectedOptionList.get("Loss")){
                cb_loss.setChecked(true);
            }else{
                cb_loss.setChecked(false);
            }
            if (selectedOptionList.get("Pending")){
                cb_pendind.setChecked(true);
            }else{
                cb_pendind.setChecked(false);
            }
        }

        initFilter_RecView();
        bottomSheetDialog.show();

        cb_date.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked){
                    cb_dat=tv_dates.getText ().toString ();
                    selectedOptionList.put(tv_dates.getText ().toString (),true);

                }else{
                    cb_dat="";
                    selectedOptionList.put(tv_dates.getText ().toString (),false);
                }
                Log.d ("darar", ": "+cb_dat);
            }
        });
        cb_close.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedOptionList.put("Close",true);
                }else{
                    selectedOptionList.put("Close",false);
                }
            }
        });

        cb_open.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedOptionList.put("Open",true);
                }else{
                    selectedOptionList.put("Open",false);
                }
            }
        });
        cb_win.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedOptionList.put("Win",true);
                }else{
                    selectedOptionList.put("Win",false);
                }
            }
        });
        cb_pendind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedOptionList.put("Pending",true);
                }else{
                    selectedOptionList.put("Pending",false);
                }
            }
        });
        cb_loss.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedOptionList.put("Loss",true);
                }else{
                    selectedOptionList.put("Loss",false);
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectedOptionList.add(hashMap);
                selectedGameType.clear();
                if(!cb_date.isChecked ())
                {
                  cb_dat="";
                }
                Log.e("selectedList",""+filterAdapter.getSelectedGameType());
                selectedGameType.addAll(filterAdapter.getSelectedGameType());
                new FilterAsyncTask().execute();
                bottomSheetDialog.dismiss();
            }
        });

    }



    @SuppressLint("NotifyDataSetChanged")
    private void initFilter_RecView() {
//        filterlist.add(new FilterModel("CheckBox"));
//        filterlist.add(new FilterModel("CheckBox"));
//        filterlist.add(new FilterModel("CheckBox"));
//        filterlist.add(new FilterModel("CheckBox"));
//        filterlist.add(new FilterModel("CheckBox"));
//        filterlist.add(new FilterModel("CheckBox"));

       // allMatkaGames();
        //filterAdapter = new FilterAdapter(AllHistoryActivity.this,filterlist);

        filterAdapter = new FilterAdapter(this, setGameType(), new FilterAdapter.OnChoiceListener() {
            @Override
            public void onSelection(int position, String name) {

            }

            @Override
            public void onRemove(int position, String name) {

            }
        });
        rec_gametype.setAdapter(filterAdapter);
        filterAdapter.notifyDataSetChanged();
    }

    private ArrayList<FilterModel> setGameType() {
        ArrayList<FilterModel>gameList=new ArrayList<>();
        Log.e( "WQertyu",type+"++"+String.valueOf(  mList.size()) );
        if (type.equalsIgnoreCase("matka")) {
            if (mList.size()>0){
                for (int i=0;i<mList.size();i++){
                    MatkaModel model=mList.get(i);
                    gameList.add(new FilterModel(model.getName()));
                     if (selectedGameType.size()>0){
                         for (int s=0;s<selectedGameType.size();s++){
                             if (model.getName().equalsIgnoreCase(selectedGameType.get(s))){
                                 gameList.get(i).setSelected(true);
                                 break;
                             }
                         }
                     }
                }
            }

        } else if (type.equalsIgnoreCase("starline")) {
            if (sList.size()>0){
                for (int i=0;i<sList.size();i++){
                    StarlineGameModel model=sList.get(i);
                    gameList.add(new FilterModel(model.getS_game_time()));
                    if (selectedGameType.size()>0){
                        for (int s=0;s<selectedGameType.size();s++){
                            if (model.getS_game_time().equalsIgnoreCase(selectedGameType.get(s))){
                                gameList.get(i).setSelected(true);
                                break;
                            }
                        }
                    }

                }
            }
        }
        Log.e("filter_size",""+sList.size());
        return gameList;
    }

    private void datePicker() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(AllHistoryActivity.this, android.R.style.Theme_Material_Light_DarkActionBar, setListener, year, month, day);
        datePickerDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);


        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                String outputPattern = "dd/MM/yyyy";
                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
                Date date1 = null;
                try {
                    date1 = outputFormat.parse(date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String d = outputFormat.format(date1);
                tv_date.setText(d);
            }
        };
    }

    private void getHistry(String user_id) {
        loadingBar.show();
        hList.clear();
        wList.clear();
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        //params.put ("matak_id",matka_id);
        module.postRequest(URL_GET_HISTORY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("AllHistory", response.toString());
                loadingBar.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    boolean res = object.getBoolean("responce");
                    if (res) {
                        JSONArray data = object.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            AllHistoryModel model = new AllHistoryModel();
                            JSONObject obj = data.getJSONObject(i);
                            model.setId(obj.getString("id"));
                            model.setMatka_id(obj.getString("matka_id"));
                            model.setGame_id(obj.getString("game_id"));
                            model.setUser_id(obj.getString("user_id"));
                            model.setPoints(obj.getString("points"));
                            model.setDigits(obj.getString("digits"));
                            model.setDate(obj.getString("date"));
                            model.setTime(obj.getString("time"));
                            model.setBet_type(obj.getString("bet_type"));
                            model.setStatus(obj.getString("status"));
                            model.setName(obj.getString("name"));

//                            if (name.equalsIgnoreCase(obj.getString ("name")))
//                            {
                            Log.e("namess", "onResponse: " + name);
                            hList.add(model);
//                            }
                        }
                        Log.d("bid_list", String.valueOf(hList.size()));

                        if (hList.size() > 0) {
                            refreshFilterData();
//                            allHistoryAdapter = new AllHistoryAdapter(AllHistoryActivity.this, hList);
//                            allHistoryAdapter.notifyDataSetChanged();
//                            rec_history.setAdapter(allHistoryAdapter);

                        } else {
                             module.errorToast(AllHistoryActivity.this,"No Matka History Available");
                        }
                    } else {
                        module.No_historyDailoge();

                         module.errorToast(AllHistoryActivity.this,object.getString("Something Went Wrong"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                module.showVolleyError(error);
                loadingBar.dismiss();
            }
        });
    }

    private void getJackpotHistry(String user_id, String matka_id) {
        loadingBar.show();
        hList.clear();
        wList.clear();
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("us_id", user_id);
        params.put("matka_id", matka_id);
        Log.e("jakparams", "getJackpotHistry: " + matka_id + user_id);
        module.postRequest(URL_Jackpot_HISTORY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("jackHistory", response.toString());
                loadingBar.dismiss();
                try {

                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);


                        AllHistoryModel model = new AllHistoryModel();

                        model.setId(obj.getString("id"));
                        model.setMatka_id(obj.getString("matka_id"));
                        model.setGame_id(obj.getString("game_id"));
                        model.setUser_id(obj.getString("user_id"));
                        model.setPoints(obj.getString("points"));
                        model.setDigits(obj.getString("digits"));
                        model.setDate(obj.getString("date"));
                        model.setTime(obj.getString("time"));
                        model.setBet_type(obj.getString("bet_type"));
                        model.setStatus(obj.getString("status"));
                        model.setName("Jackpot");

//                            if (obj.getString ("matka_id")>20))
//                            {
                        if (tv_date.getText().toString().equals(obj.getString("date"))) {
                            hList.add(model);
                        }
                        Log.e("namess", "onResponse: " + name);
                        //hList.add (model);
                        //}
                    }
                    Log.d("bid_list", String.valueOf(hList.size()));

                    if (hList.size() > 0) {
                        allHistoryAdapter = new AllHistoryAdapter(AllHistoryActivity.this, hList);
                        allHistoryAdapter.notifyDataSetChanged();
                        rec_history.setAdapter(allHistoryAdapter);

                    } else {
                        module.No_historyDailoge();
                         module.errorToast(AllHistoryActivity.this,"No Matka History Available");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                module.showVolleyError(error);
                loadingBar.dismiss();
            }
        });
    }

    private void getStarLineHistory(String user_id) {
        hList.clear();
        loadingBar.show();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        module.postRequest(URL_STARLINE_HISTORY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("starline_history", response.toString());
                loadingBar.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    boolean res = object.getBoolean("responce");
                    if (res) {
                        JSONArray data = object.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            AllHistoryModel model = new AllHistoryModel();
                            JSONObject obj = data.getJSONObject(i);
                            model.setId(obj.getString("id"));
                            model.setMatka_id(obj.getString("matka_id"));
                            model.setGame_id(obj.getString("game_id"));
                            model.setUser_id(obj.getString("user_id"));
                            model.setPoints(obj.getString("points"));
                            model.setDigits(obj.getString("digits"));
                            model.setDate(obj.getString("date"));
                            model.setTime(obj.getString("time"));
                            model.setName(obj.getString("s_game_time"));
                            model.setStatus(obj.getString("status"));
                            model.setBet_type("");

                            //if (name.equalsIgnoreCase(obj.getString("s_game_time"))) {
                            hList.add(model);
//                           } else {
////                                module.showToast("error");
//                            }
                        }
                        Log.d("star_bid_list", String.valueOf(hList.size()));

                        if (hList.size() > 0) {
                            refreshFilterData();
//                            allHistoryAdapter = new AllHistoryAdapter(AllHistoryActivity.this, hList);
//                            allHistoryAdapter.notifyDataSetChanged();
//                            rec_history.setAdapter(allHistoryAdapter);
                        } else {
                            module.No_historyDailoge();
                             module.errorToast(AllHistoryActivity.this,"No Starline History Available");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                module.VolleyErrorMessage(error);
            }
        });

    }

    public void getWinHistory(String user_id) {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        module.postRequest(WIN_HISTORY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("WIN_HISTORY", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("responce")) {
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            AllHistoryModel model = new AllHistoryModel();
                            model.setId(obj.getString("id"));
                            model.setMatka_id(obj.getString("matka_id"));
                            model.setUser_id(obj.getString("user_id"));
                            model.setGame_id(obj.getString("game_id"));
                            model.setDigits(obj.getString("digits"));
                            model.setPoints(obj.getString("amt"));
                            model.setBid_id(obj.getString("bid_id"));
                            model.setTime(obj.getString("time"));
                            model.setBet_type(obj.getString("type"));
                            model.setDate(obj.getString("date"));
                            model.setStatus("win");
                            model.setName(obj.getString("name"));

                            loadingBar.dismiss();
//
                            Log.e("frty6ui", matka_id + "        " + obj.getString("matka_id"));
                            if (matka_id.equals(obj.getString("matka_id"))) {
                                hList.add(model);
                            }
                        }

                        Log.d("star_bid_list", String.valueOf(hList.size()));

                        if (hList.size() > 0) {

                            allHistoryAdapter = new AllHistoryAdapter(AllHistoryActivity.this, hList);
                            allHistoryAdapter.notifyDataSetChanged();
                            rec_history.setAdapter(allHistoryAdapter);
                        } else {
                            module.No_historyDailoge();
                             module.errorToast(AllHistoryActivity.this,"No Win History Available");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    loadingBar.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                module.VolleyErrorMessage(error);
            }
        });
    }

    private void getStarlineWinHistory(String user_id) {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        module.postRequest(STARLINE_WIN_HISTORY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("STARLINE_WIN_HISTORY", response);
                loadingBar.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("responce")) {
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            AllHistoryModel model = new AllHistoryModel();
                            model.setId(obj.getString("id"));
                            model.setMatka_id(obj.getString("matka_id"));
                            model.setUser_id(obj.getString("user_id"));
                            model.setGame_id(obj.getString("game_id"));
                            model.setDigits(obj.getString("digits"));
                            model.setPoints(obj.getString("amt"));
                            model.setBid_id(obj.getString("bid_id"));
                            model.setTime(obj.getString("time"));
                            model.setBet_type(obj.getString("type"));
                            model.setDate(obj.getString("date"));
                            model.setStatus("win");
                            model.setName(obj.getString("s_game_time"));
                            loadingBar.dismiss();
                            Log.e("frty6ui", matka_id + "        " + obj.getString("matka_id"));
                            if (matka_id.equals(obj.getString("matka_id"))) {
                                hList.add(model);
                            }
                        }

                        Log.d("star_bid_list", String.valueOf(hList.size()));

                        if (hList.size() > 0) {

                            allHistoryAdapter = new AllHistoryAdapter(AllHistoryActivity.this, hList);
                            allHistoryAdapter.notifyDataSetChanged();
                            rec_history.setAdapter(allHistoryAdapter);
                        } else {

                             module.errorToast(AllHistoryActivity.this,"No Win History Available");
                        }
                    } else {
                        module.No_historyDailoge();
                         module.errorToast(AllHistoryActivity.this,"No History Available");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    loadingBar.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                module.VolleyErrorMessage(error);
            }
        });

    }

    public void getjackpotWinHistory(String user_id) {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        module.postRequest(JackpotWIN_HISTORY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("WIN_HISTORY", response);
                loadingBar.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("responce")) {
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            AllHistoryModel model = new AllHistoryModel();
                            model.setId(obj.getString("id"));
                            model.setMatka_id(obj.getString("matka_id"));
                            model.setUser_id(obj.getString("user_id"));
                            model.setGame_id(obj.getString("game_id"));
                            model.setDigits(obj.getString("digits"));
                            model.setPoints(obj.getString("amt"));
                            model.setBid_id(obj.getString("bid_id"));
                            model.setTime(obj.getString("time"));
                            model.setBet_type(obj.getString("type"));
                            model.setDate(obj.getString("date"));
                            model.setStatus("win");
                            model.setName(obj.getString("name"));

                            loadingBar.dismiss();
                            Log.e("frty6ui", matka_id + "        " + obj.getString("matka_id"));
                            if (matka_id.equals(obj.getString("matka_id"))) {
                                hList.add(model);
                            }
                        }

                        Log.d("star_bid_list", String.valueOf(hList.size()));

                        if (hList.size() > 0) {

                            allHistoryAdapter = new AllHistoryAdapter(AllHistoryActivity.this, hList);
                            allHistoryAdapter.notifyDataSetChanged();
                            rec_history.setAdapter(allHistoryAdapter);
                        } else {
                            module.No_historyDailoge();
                             module.errorToast(AllHistoryActivity.this,"No Win History Available");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    loadingBar.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                module.VolleyErrorMessage(error);
            }
        });
    }


    private void allMatkaGames() {
        mList.clear();
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        module.postRequest(URL_Matka, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "mstks: " + response+"++++"+type);
                loadingBar.dismiss();

                try {
                    JSONArray datay = new JSONArray(response);
                    for (int i = 0; i < datay.length(); i++) {

                        JSONObject jsonObject = datay.getJSONObject(i);
                        MatkaModel matkasObjects = new MatkaModel();
                        matkasObjects.setId(jsonObject.getString("id"));
                        matkasObjects.setName(jsonObject.getString("name"));
                        matkasObjects.setStart_time(jsonObject.getString("start_time"));
                        matkasObjects.setEnd_time(jsonObject.getString("end_time"));
                        matkasObjects.setStarting_num(jsonObject.getString("starting_num"));
                        matkasObjects.setNumber(jsonObject.getString("number"));
                        matkasObjects.setEnd_num(jsonObject.getString("end_num"));
                        matkasObjects.setBid_start_time(jsonObject.getString("start_time"));
                        matkasObjects.setBid_end_time(jsonObject.getString("end_time"));
//                        matkasObjects.setCreated_at(jsonObject.getString("created_at"));
//                        matkasObjects.setUpdated_at(jsonObject.getString("updated_at"));
                        matkasObjects.setSat_start_time(jsonObject.getString("start_time"));
                        matkasObjects.setSat_end_time(jsonObject.getString("end_time"));
                        matkasObjects.setStatus(jsonObject.getString("status"));
                        // matkasObjects.setLoader(jsonObject.getString("loader"));
                        // matkasObjects.setText(jsonObject.getString("text"));
                        //matkasObjects.setText_status(jsonObject.getString("text_status"));
                        mList.add(matkasObjects);
                    }
                    Log.e( "matkasObjects", "onResponse: "+mList.size() );
                    filterAdapter.notifyDataSetChanged();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    loadingBar.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                module.showVolleyError(error);
                loadingBar.dismiss();
            }
        });


    }


    private  void starlineGames()
    {
        sList.clear ();
        loadingBar.show();

        final JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL_STARLINE, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("starline_response",response.toString());

                for(int i=0; i<response.length();i++)
                {
                    try
                    {
                        JSONObject jsonObject=response.getJSONObject(i);
                        StarlineGameModel matkasObjects=new StarlineGameModel ();
                        matkasObjects.setId(jsonObject.getString("id"));
                        matkasObjects.setS_game_time(jsonObject.getString("s_game_time"));
                        matkasObjects.setS_game_number(jsonObject.getString("s_game_number"));
                        matkasObjects.setS_game_end_time(jsonObject.getString("s_game_end_time"));
                        sList.add(matkasObjects);

                    }
                    catch (Exception ex)
                    {
                        loadingBar.dismiss();

                        return;
                    }
                }
               // filterAdapter.notifyDataSetChanged();
                loadingBar.dismiss();
//                rec_starline.setLayoutManager(new LinearLayoutManager(getActivity()));
//                starlineGameAdapter = new StarlineGameAdapter(getActivity(),sList);
//                starlineGameAdapter.notifyDataSetChanged();
//                rec_starline.setAdapter(starlineGameAdapter);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingBar.dismiss();
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(AllHistoryActivity.this);
        requestQueue.add(jsonArrayRequest);
    }

    private class FilterAsyncTask extends AsyncTask<String ,Void,Void>{


        @Override
        protected Void doInBackground(String... strings) {
            if (type.equalsIgnoreCase("matka")||type.equalsIgnoreCase("starline")) {

                if (hList.size()>0){
                    hFilterList.clear();
                    if(!checkFilterStatus()){
                        hFilterList.addAll(hList);

                    }else {
                        for (int i = 0; i < hList.size(); i++) {
                            AllHistoryModel model = hList.get(i);
                            if (selectedOptionList != null) {
                                if (selectedOptionList.get("Open")) {
                                    if (model.getBet_type().equalsIgnoreCase("open")) {
                                        hFilterList.add(model);
                                        continue;
                                    }
                                }
                                if (selectedOptionList.get("Close")) {
                                    if (model.getBet_type().equalsIgnoreCase("close")) {
                                        hFilterList.add(model);
                                        continue;
                                    }
                                }
                                if (selectedOptionList.get("Win")) {
                                    if (model.getStatus().equalsIgnoreCase("win")) {
                                        hFilterList.add(model);
                                        continue;
                                    }
                                }
                                if (selectedOptionList.get("Loss")) {
                                    if (model.getStatus().equalsIgnoreCase("loss")) {
                                        hFilterList.add(model);
                                        continue;
                                    }
                                }
                                if (selectedOptionList.get("Pending")) {
                                    if (model.getStatus().equalsIgnoreCase("pending")) {
                                        hFilterList.add(model);
                                        continue;
                                    }
                                }
                                Log.d ("kdk", "doInBackground: "+cb_dat+"njk");
                                if (!cb_dat.equalsIgnoreCase ("")) {
                                    String bid_time = model.getTime();
                                    String d = bid_time.substring(0,10);
                                    String dd[] =d.split("-");
                                   String val=(dd[2]+"/"+dd[1]+"/"+dd[0]);
                                    Log.d ("check_date", "do"+val+"-"+cb_dat);
                                    if (val.equalsIgnoreCase(cb_dat)) {
                                        hFilterList.add(model);
                                        continue;
                                    }
                                }

                                if (selectedGameType.size() > 0) {
                                    //  if (type.equalsIgnoreCase("matka")){
                                    for (int g = 0; g < selectedGameType.size(); g++) {
                                        if (type.equalsIgnoreCase("matka")) {
                                            if (model.getName().equalsIgnoreCase(selectedGameType.get(g))) {
                                                hFilterList.add(model);
                                                break;
                                            }
                                        } else if (type.equalsIgnoreCase("starline")) {
                                            if (model.getName().equalsIgnoreCase(selectedGameType.get(g))) {
                                                hFilterList.add(model);
                                            }
                                        }
                                    }
                                    // }
                                }
                            }
                        }
                    }
                }


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Log.e("filterSize ",""+hFilterList.size());
           allHistoryAdapter=new AllHistoryAdapter(AllHistoryActivity.this,hFilterList);
           allHistoryAdapter.notifyDataSetChanged();
           rec_history.setAdapter(allHistoryAdapter);
        }
    }
    private boolean checkFilterStatus(){
        if (!checkStaticOpton()&& selectedGameType.size()==0)
            return false ;
       else{

        }
        return true;
    }

    private boolean checkStaticOpton() {
        boolean status=false;
        if (selectedOptionList.size()==0){
            status= false;
        }else if (selectedOptionList.size()>0){
            if (!selectedOptionList.get("Open")
                  && !selectedOptionList.get("Close")
                    && !selectedOptionList.get("Win")
                    && !selectedOptionList.get("Loss")
                    && !selectedOptionList.get("Pending")
                     &&cb_dat.equals ("")){
                return false;
            }else{
                status= true;
            }
        }else{
            status=false;
        }

        return status;
    }

}