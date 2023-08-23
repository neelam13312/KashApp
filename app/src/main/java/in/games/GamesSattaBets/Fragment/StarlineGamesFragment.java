package in.games.GamesSattaBets.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import in.games.GamesSattaBets.Activity.MainActivity;
import in.games.GamesSattaBets.Adapter.StarlineGameAdapter;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Model.RateModel;
import in.games.GamesSattaBets.Model.StarlineGameModel;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.ConnectivityReceiver;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.RecyclerTouchListener;
import in.games.GamesSattaBets.Util.SessionMangement;

import static in.games.GamesSattaBets.Config.BaseUrls.URL_GET_STATUS;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_NOTICE;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_SET_STATUS;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_STARLINE;
import static in.games.GamesSattaBets.Config.Constants.KEY_ID;
import static in.games.GamesSattaBets.Config.Constants.KEY_WALLET;


public class StarlineGamesFragment extends AppCompatActivity implements View.OnClickListener {
    private final String TAG= StarlineGamesFragment.class.getSimpleName();
    RecyclerView rec_starline;
    SwipeRefreshLayout swipe;
    ArrayList<StarlineGameModel> sList;
    ArrayList<RateModel> list;
    ArrayList<RateModel> slist;
    Module module;
    Switch swt_notification;
    String starline_notification="1";
    LoadingBar loadingBar;
    SessionMangement sessionMangement;
    StarlineGameAdapter starlineGameAdapter;
    TextView tv_singleDigit,tv_singlePanna,tv_doublePanna,tv_triplePanna;
    RelativeLayout rel_bidHistory,rel_winHistory;

    TextView tv_title,tv_wallet;
    ImageView img_back;
    LinearLayout lin_notification;

    public StarlineGamesFragment() {
        // Required empty public constructor
        //rec_starline
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.fragment_starline_games);

        initView();
        //tv_singleDigit.setText (getRate());
        //allStarlineGames();
        //StarlineGames();
        if (ConnectivityReceiver.isConnected())
        {
            StarlineGames ();
            getStatus();
            getmatkaRate ();
        }else {module.noInternet();}



        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing())
                {
                    if (ConnectivityReceiver.isConnected())
                    {
                        StarlineGames ();
                        getStatus();
                        getmatkaRate ();
                    }else {module.noInternet();}
                    swipe.setRefreshing(false);
                }
            }
        });

        rec_starline.addOnItemTouchListener(new RecyclerTouchListener(StarlineGamesFragment.this, rec_starline, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                StarlineGameModel model = sList.get(position);
                String e_t = get24Hours(model.getS_game_end_time());
                String s_t = get24Hours(model.getS_game_time());
                int sTime=module.getTimeFormatStatus(model.getS_game_time());
                Date date=new Date();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
                String ddt=simpleDateFormat.format(date);
                int c_tm=Integer.parseInt(ddt);
                String st=module.get24Hours(model.getS_game_end_time().toString());
                long tmLong=module.getTimeDifference(st);
                if(tmLong<=0)
                {module.marketClosed("Betting is closed for today");
                    return;
                }else {

                    Intent intent  = new Intent (StarlineGamesFragment.this, SelectGameActivity.class);

                    intent.putExtra("m_id",model.getId());
                    intent.putExtra("end_time",e_t);
                    intent.putExtra("start_time",s_t);
                    intent.putExtra("matka_name",model.getS_game_end_time());
                    intent.putExtra("type","starline");
                    intent.putExtra ("market_status","open");
                    intent.putExtra ("is_market_open_nextday","1");
                    intent.putExtra ("is_market_open_nextday2","1");

                    sessionMangement.setnumValue("",
                            "",
                            "","starline","open","1","1");
//                    AppCompatActivity activity = (AppCompatActivity) context;
                    startActivity(intent);
//                    Bundle bundle = new Bundle();
//                    Fragment fragment = new SelectGameFragment();
//
//                    bundle.putString("m_id",model.getId());
//                    bundle.putString("end_time",e_t);
//                    bundle.putString("start_time",s_t);
//                    bundle.putString("matka_name",model.getS_game_end_time());
//                    bundle.putString("type","starline");
//                    fragment.setArguments(bundle);
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();
                }

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_starline_games, container, false);
//      try {
//          ((MainActivity) getActivity ( )).setTitles ("Starline Games");
//      }catch(Exception e){
//            ((SelectGameActivity)getActivity ()).setGameTitle ("Starline Games");
//        }
        initView();
        //tv_singleDigit.setText (getRate());
        //allStarlineGames();
        //StarlineGames();
        if (ConnectivityReceiver.isConnected())
        {
            StarlineGames ();
            getStatus();
            getmatkaRate ();
        }else {module.noInternet();}

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing())
                {
                    if (ConnectivityReceiver.isConnected())
                    {
                        StarlineGames ();
                        getStatus();
                        getmatkaRate ();
                    }else {
                        module.noInternet();
                    }
                    swipe.setRefreshing(false);
                }
            }
        });

        rec_starline.addOnItemTouchListener(new RecyclerTouchListener(StarlineGamesFragment.this, rec_starline, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                StarlineGameModel model = sList.get(position);
                String e_t = get24Hours(model.getS_game_end_time());
                String s_t = get24Hours(model.getS_game_time());
                int sTime=module.getTimeFormatStatus(model.getS_game_time());
                Date date=new Date();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
                String ddt=simpleDateFormat.format(date);
                int c_tm=Integer.parseInt(ddt);
                String st=module.get24Hours(model.getS_game_end_time().toString());
                long tmLong=module.getTimeDifference(st);
                if(tmLong<=0)
                {
                    module.marketClosed("Betting is closed for today");
                    return;
                }else {

                    Intent intent  = new Intent (StarlineGamesFragment.this, SelectGameActivity.class);

                    intent.putExtra("m_id",model.getId());
                    intent.putExtra("end_time",e_t);
                    intent.putExtra("start_time",s_t);
                    intent.putExtra("matka_name",model.getS_game_end_time());
                    intent.putExtra("type","starline");
                    intent.putExtra ("market_status","open");
                    intent.putExtra ("is_market_open_nextday","1");
                    intent.putExtra ("is_market_open_nextday2","1");

                    sessionMangement.setnumValue("",
                            "",
                            "","starline","open","0","0");
//                    AppCompatActivity activity = (AppCompatActivity) context;
                    startActivity(intent);




//                    Bundle bundle = new Bundle();
//                    Fragment fragment = new SelectGameFragment();
//
//                    bundle.putString("m_id",model.getId());
//                    bundle.putString("end_time",e_t);
//                    bundle.putString("start_time",s_t);
//                    bundle.putString("matka_name",model.getS_game_end_time());
//                    bundle.putString("type","starline");
//                    fragment.setArguments(bundle);
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();
                }

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        return view;
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent ( StarlineGamesFragment.this,MainActivity.class );
        startActivity (intent);
    }

    private void getmatkaRate() {
        loadingBar.show();
        sList.clear ();
        list.clear ();
        list=new ArrayList<>();
        slist=new ArrayList<>();
        String tag_json_obj = "json_notice_req";
        HashMap<String, String> params = new HashMap<String, String> ();
        module.postRequest (URL_NOTICE, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                Log.e (TAG, "onResponse: "+response);
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject (String.valueOf (response));
                    jsonObject.getString ("status");
                    if(jsonObject.getString ("status").equals ("success")) {
                        JSONArray array=jsonObject.getJSONArray("data");

                        for (int i=0; i<array.length();i++)
                        {
                            RateModel gameRateModel=new RateModel();
                            JSONObject object=array.getJSONObject(i);
                            gameRateModel.setId(object.getString("id"));
                            gameRateModel.setName(object.getString("name"));
                            gameRateModel.setRate_range(object.getString("rate_range"));
                            gameRateModel.setRate(object.getString("rate"));
                            String type=object.getString("type").toString();
                            gameRateModel.setType(type);
//                            if(type.equals("0"))
//                            {
//                                list.add(gameRateModel);
//                            }
//                            else
//                            {
//                                slist.add(gameRateModel);
//                            }
                            if (object.getString("name").equalsIgnoreCase("SIngle Ghart"))
                            {
                                tv_singleDigit.setText(object.getString("rate_range")+"-"+object.getString("rate"));
                            }
                            if (object.getString("name").equalsIgnoreCase("single pana"))
                            {
                                tv_singlePanna.setText(object.getString("rate_range")+"-"+object.getString("rate"));
                            }
                            if (object.getString("name").equalsIgnoreCase("triple pana"))
                            {
                                tv_triplePanna.setText(object.getString("rate_range")+"-"+object.getString("rate"));
                            }
                            if (object.getString("name").equalsIgnoreCase("Double Panu"))
                            {
                                tv_doublePanna.setText(object.getString("rate_range")+"-"+object.getString("rate"));
                            }
                        }
//                        tv_singleDigit.setText(getRate ());
                    }
                    else
                    {
                        module.errorToast (getApplicationContext(),"Something Went Wrong");
                    }
                    loadingBar.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                module.showVolleyError(error);
            }
        });
    }
    private String getRate() {
        StringBuffer stringBuffer=new StringBuffer();
        for(RateModel m:slist){
            // if (slist.size()%4==2||slist.size()%4==0) {
            if (slist.size()%4==2) {
                stringBuffer.append(m.getName() + " - " + m.getRate_range() + " : " + m.getRate() + "\n");
            }else {
                stringBuffer.append(m.getName() + " - " + m.getRate_range() + " : " + m.getRate()+"  |  ");
            }
        }
        Log.e(TAG, "setRates: "+stringBuffer.toString() );
        return stringBuffer.toString();
    }

    private void initView() {
        tv_title=findViewById(R.id.tv_title);
        tv_title.setText ("Starline Games");
        tv_wallet=findViewById(R.id.tv_wallet);;


        img_back=findViewById(R.id.img_back);;
        lin_notification=findViewById(R.id.lin_notification);;

        lin_notification.setOnClickListener (this);
        img_back.setOnClickListener (this);
        sList = new ArrayList<>();
        rec_starline = findViewById(R.id.rec_starline);
        swipe = findViewById(R.id.swipe);
        swt_notification=findViewById(R.id.swt_notification);
        tv_singleDigit = findViewById(R.id.tv_singleDigit);
        tv_singlePanna = findViewById(R.id.tv_singlePana);
        tv_doublePanna = findViewById(R.id.tv_doublePana);
        tv_triplePanna = findViewById(R.id.tv_triplePana);
        rel_bidHistory =findViewById(R.id.rel_bidHistory);
        rel_winHistory = findViewById(R.id.rel_winHistory);

        rel_bidHistory.setOnClickListener(this);
        rel_winHistory.setOnClickListener(this);
        list = new ArrayList<>();
        slist = new ArrayList<>();
        sessionMangement=new SessionMangement (StarlineGamesFragment.this);
        tv_wallet.setText (sessionMangement.getUserDetails ().get (KEY_WALLET));
        loadingBar=new LoadingBar (StarlineGamesFragment.this);
        module=new Module (StarlineGamesFragment.this);
        swt_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    starline_notification="1";
                    setStatus(starline_notification);

                    // The toggle is enabled
                } else {
                    starline_notification="0";
                    setStatus(starline_notification);

                    // The toggle is disabled
                }
            }
        });
    }

    private  void StarlineGames()
    {
        sList.clear ();
        loadingBar.show();

        final JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL_STARLINE, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.e("starline_response",response.toString());
                    sList.clear ();
                    for(int i=0; i<response.length();i++)
                    {
                        JSONObject jsonObject=response.getJSONObject(i);
                        StarlineGameModel matkasObjects=new StarlineGameModel ();
                        matkasObjects.setId(jsonObject.getString("id"));
                        matkasObjects.setS_game_time(jsonObject.getString("s_game_time"));
                        matkasObjects.setS_game_number(jsonObject.getString("s_game_number"));
                        matkasObjects.setS_game_end_time(jsonObject.getString("s_game_end_time"));
                        sList.add(matkasObjects);
                    }
                    loadingBar.dismiss();
                    Log.e("swdcfg", String.valueOf(sList.size()));
                    if (sList.size()>0) {
                        rec_starline.setLayoutManager(new LinearLayoutManager(StarlineGamesFragment.this));
                        starlineGameAdapter = new StarlineGameAdapter(StarlineGamesFragment.this, sList);
                        starlineGameAdapter.notifyDataSetChanged();
                        Animation leftTORight = AnimationUtils.loadAnimation (StarlineGamesFragment.this, R.anim.item_slidright);
                        rec_starline.setAdapter(starlineGameAdapter);
                        rec_starline.startAnimation (leftTORight);

                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    loadingBar.dismiss();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        loadingBar.dismiss();
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(StarlineGamesFragment.this);
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (v.getId())
        {

            case R.id.lin_notification:
                Intent intent=new Intent (StarlineGamesFragment.this,NotificationFragment.class);
                startActivity (intent);
                break;
            case R.id.img_back:
                Intent intentss=new Intent (StarlineGamesFragment.this,MainActivity.class);
                startActivity (intentss);
                break;
            case R.id.rel_bidHistory:
                fragment = new MatkaNameHistoryFragment();
                bundle.putString("type","starline");
                break;
            case R.id.rel_winHistory:
                fragment = new MatkaNameHistoryFragment();
                bundle.putString("type","starline_win");
                break;

        }
        if (fragment!=null)
        {
            fragment.setArguments(bundle);
            FragmentManager fragmentManager =getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();
        }
    }

    String  get24Hours(String time)
    {
        String t[]=time.split(" ");
        String time_type=t[1].toString();
        String t1[]=t[0].split(":");

        int tm=Integer.parseInt(t1[0].toString());

        if(time_type.equalsIgnoreCase("PM") || time_type.equalsIgnoreCase("p.m"))
        {
            if(tm==12)
            {

            }
            else
            {
                tm=12+tm;
            }
        }

//       String s ="";
//       String h = time.substring(0,1);
//       if (time.contains("PM")|| time.contains("p.m"))
//       {
//       int hours = Integer.parseInt(h);
//       if (hours<12)
//       {
//          hours =hours+12;
//       }
        String s = String.valueOf(tm)+":"+t1[1]+":00";

        return s;
    }
    private void getStatus()
    {
        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        loadingBar.show();
        HashMap<String,String> params = new HashMap<>();
        params.put("device_id",android_id);

        module.postRequest(URL_GET_STATUS, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("URL_GET_STATUS",response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("response")){
                        JSONArray array = object.getJSONArray("data");

                        JSONObject obj = array.getJSONObject(0);
                        if(obj.getString ("starline_notification").equalsIgnoreCase ("1"))
                        {
                            swt_notification.setChecked (true);
                        }
                        else
                        {
                            swt_notification.setChecked (false);
                        }
                    }
                    else {
                        swt_notification.setChecked (false);
//                        module.errorToast("Something Went Wrong");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

                loadingBar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ();
                module.VolleyErrorMessage (error);
            }
        });
    }
    private void setStatus(String starline)

    {

        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        loadingBar.show();
        HashMap<String,String> params = new HashMap<>();
        params.put("type","");
        params.put("is_password","1");
        params.put("is_mpin","0");
        params.put("game_notifaction","0");
        params.put("main_notification","0");
        params.put("starline_notification",starline);
        params.put("user_id",sessionMangement.getUserDetails ().get (KEY_ID));
        params.put("device_id",android_id);
        module.postRequest(URL_SET_STATUS, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("URL_SET_STATUS",response);

                loadingBar.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("response")){
                        module.customToast (object.getString ("success"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    loadingBar.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ();
                module.VolleyErrorMessage (error);
            }
        });
    }

}