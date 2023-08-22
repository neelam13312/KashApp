package in.games.GamesSattaBets.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import in.games.GamesSattaBets.Activity.MainActivity;
import in.games.GamesSattaBets.Adapter.SelectGameAdapter;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Fragment.GamesFragment.ChoicePannaFragment;
import in.games.GamesSattaBets.Fragment.GamesFragment.DigitBasedJodiFragment;
import in.games.GamesSattaBets.Fragment.GamesFragment.DigitFragment;
import in.games.GamesSattaBets.Fragment.GamesFragment.FullSangamFragment;
import in.games.GamesSattaBets.Fragment.GamesFragment.HalfSangamFragment;
import in.games.GamesSattaBets.Fragment.GamesFragment.JodiDigitBulkFragment;
import in.games.GamesSattaBets.Fragment.GamesFragment.LeftRightDigitFragment;
import in.games.GamesSattaBets.Fragment.AllTable.SingleDigirtFragment;
import in.games.GamesSattaBets.Fragment.GamesFragment.PanelGroupFragment;
import in.games.GamesSattaBets.Fragment.GamesFragment.SinglePanaBulkFragment;
import in.games.GamesSattaBets.Fragment.GamesFragment.SpDpTpFragment;
import in.games.GamesSattaBets.Fragment.GamesFragment.SpMotorFragment;
import in.games.GamesSattaBets.Fragment.GamesFragment.TwoDigitPannaFragment;
import in.games.GamesSattaBets.Fragment.GamesFragment.JodiFamilyFragment;
import in.games.GamesSattaBets.Fragment.GamesFragment.OddEvenFragment;
import in.games.GamesSattaBets.Fragment.GamesFragment.PanaFragment;
import in.games.GamesSattaBets.Fragment.GamesFragment.PannaFamilyFragment;
import in.games.GamesSattaBets.Fragment.GamesFragment.RedBracketFragment;
import in.games.GamesSattaBets.Model.SelectGameModel;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.ConnectivityReceiver;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.RecyclerTouchListener;

import static in.games.GamesSattaBets.Config.BaseUrls.URL_MATKAGAMES;


public class SelectGameFragment extends Fragment {
    RecyclerView rec_selectGame;
    ArrayList<SelectGameModel> sList, starline_list;
    SelectGameAdapter selectGameAdapter;
    SwipeRefreshLayout swipe;
    Module module;
    LoadingBar loadingBar;
    String market_status="", matka_name = "", game_id, game_name, matka_id = "", start_time = "",
            end_time = "", s_num = "", num = "", e_num = "", type = "",is_market_open_nextday="",is_market_open_nextday2="";

    public SelectGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate (R.layout.fragment_select_game, container, false);
        backPress(view);
        matka_name = getArguments ( ).getString ("matka_name");
        matka_id = getArguments ( ).getString ("m_id");
        start_time = getArguments ( ).getString ("start_time");
        end_time = getArguments ( ).getString ("end_time");
        s_num = getArguments ( ).getString ("s_num");
        num = getArguments ( ).getString ("num");
        e_num = getArguments ( ).getString ("e_num");
        type = getArguments ( ).getString ("type");
        market_status=getArguments ().getString ("market_status");
        is_market_open_nextday=getArguments ().getString ("is_market_open_nextday");
        is_market_open_nextday2=getArguments ().getString ("is_market_open_nextday2");
        Log.e("xdcfvgbh",is_market_open_nextday);
        Log.e("xdcfvgbh",is_market_open_nextday2);
        if (Integer.parseInt(matka_id)<20) {
            Log.e("matka_id", "onCreateView:  "+matka_id );
            ((SelectGameActivity) getActivity ( )).setGameTitle (matka_name);
        } else if (Integer.parseInt(matka_id)>20 && Integer.parseInt(matka_id)<100){
            ((SelectGameActivity) getActivity ( )).setGameTitle ("Starline Games"+"("+matka_name+")");
        }else {
            ((SelectGameActivity) getActivity ( )).setGameTitle(matka_name);
        }
        module = new Module (getActivity ( ));
        loadingBar = new LoadingBar (getActivity ( ));
        sList = new ArrayList<> ( );
        rec_selectGame = view.findViewById (R.id.rec_selectGame);
//        rec_selectGame.setLayoutManager(new GridLayoutManager (getActivity (),2));

        swipe = view.findViewById (R.id.swipe);

        swipe.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener ( ) {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing ( )) {
                    if (ConnectivityReceiver.isConnected ( )) {
                        if (type.equalsIgnoreCase ("starline")) {
                            getAllSatrlineGames ( );
                        } else if (type.equalsIgnoreCase ("matka")) {
                            getAllGames ( );
                        } else {

//                            allGames ( );
                            getJackpotGmaes();
                        }
                    } else {
                        module.noInternet ( );
                    }
                    module.getWalletAmount("game");
                    swipe.setRefreshing (false);
                }
            }
        });

        Log.e ("type", type);
        if (ConnectivityReceiver.isConnected ( )) {
            if (type.equalsIgnoreCase ("starline")) {
                getAllSatrlineGames ( );
            } else if (type.equalsIgnoreCase ("matka")) {
                getAllGames ( );
            } else {
//                allGames ( );
                getJackpotGmaes();
            }
        } else {
            module.noInternet ( );
        }

        rec_selectGame.addOnItemTouchListener (new RecyclerTouchListener (getActivity ( ), rec_selectGame, new RecyclerTouchListener.OnItemClickListener ( ) {
            @Override
            public void onItemClick(View view, int position) {
                try {
                SelectGameModel model = sList.get (position);
                String gameNAme = sList.get (position).getName ( );
                String name = "";
                // Toast.makeText (getContext (), "gh"+gameNAme, Toast.LENGTH_SHORT).show ( );
                Bundle bundle = new Bundle ( );
                Fragment fragment = null;
                if (model.getIs_close ( ).equals ("1")) {
                    module.marketClosed ("Biding is closed for today");
                } else if (type.equals ("jackpot")) {
                    if (gameNAme.equals ("LEFT DIGIT")) {
                        fragment = new LeftRightDigitFragment ( );
                        name = "LEFT";
                        bundle.putString ("jackpot", "LEFT");
                    }
                    else if (gameNAme.equals ("JODI DIGIT")) {
                        fragment = new LeftRightDigitFragment ( );
                        name = "JODI";
                        bundle.putString ("jackpot", "JODI");
                    }else  if (gameNAme.equals ("RIGHT DIGIT")) {
                        fragment = new LeftRightDigitFragment ( );
                        name = "RIGHT";
                        bundle.putString ("jackpot", "RIGHT");
                    }

                }
                else {

                    switch (gameNAme.toLowerCase()) {
                        case "single and bulk digit":
                            fragment=new SingleDigirtFragment();
//                            fragment = new Single_digit_bulkFragment ( );
                            break;

                        case "jodi digit bulk":
                            fragment = new JodiDigitBulkFragment( );
                            break;
                        case "group jodi":
                            fragment = new JodiFamilyFragment ( );
                            break;
                        case "panel group":
                            fragment = new PanelGroupFragment( );
                            break;
                        case "single pana bulk":
                            fragment = new SinglePanaBulkFragment ( );
                            bundle.putString ("type", "single");
                            break;
                        case "double pana bulk":
                            fragment = new SinglePanaBulkFragment ( );
                            bundle.putString ("type", "double");
                            break;
                        case "single digit":
                            fragment=new SingleDigirtFragment ();
//                            fragment = new DigitFragment( );
//                            bundle.putString ("type", "SINGLE");
//
                            break;

                        case "jodi digit":
//                            fragment=new JOdiFragment ();
                            fragment = new DigitFragment ( );
                            bundle.putString ("type", "JODI");
                            break;

                        case "odd even":
                            fragment = new OddEvenFragment ( );//
                            break;

                        case "single pana":
                            fragment = new PanaFragment ( );
                            bundle.putString ("panna", "SINGLE PANNA");
                            break;

                        case "double pana":
                            fragment = new PanaFragment ( );
                            bundle.putString ("panna", "DOUBLE PANNA");

                            break;

                        case "triple pana":
                            fragment = new PanaFragment ( );
                            bundle.putString ("panna", "TRIPLE PANNA");

                            break;

                        case "jodi family":
                            fragment = new JodiFamilyFragment ( );//
                            break;

                        case "pana family":
                            fragment = new PannaFamilyFragment ( );
                            break;

                        case "red bracket":
                            fragment = new RedBracketFragment ( );
                            break;
                        case "half sangam":
                           fragment = new HalfSangamFragment ( );
                            break;
                        case "full sangam":
                            //comment by client
                            fragment = new FullSangamFragment ( );
                            break;
                        case "sp dp tp":
                            fragment = new SpDpTpFragment ( );
                            break;
                        case "choice pana":
                            fragment = new ChoicePannaFragment ( );
                            break;
                        case "sp motor":
                            fragment = new SpMotorFragment ( );
                            name = "SP MOTOR";
                            break;
                        case "dp motor":
                            fragment = new SpMotorFragment ( );
                            name = "DP MOTOR";
                            break;
                        case "two digit pana(cp,sr)":
                            fragment = new TwoDigitPannaFragment ( );
                            break;
                        case "digit based jodi":
                            fragment = new DigitBasedJodiFragment ( );
                            break;

                        case "left digits":
                            fragment = new LeftRightDigitFragment ( );
                            name = "left";
                            bundle.putString ("jackpot", "LEFT");
                            break;
                        case "jackpot jodi digits":
                            fragment = new LeftRightDigitFragment ( );
                            name = " jJodi";
                            bundle.putString ("jackpot", "JODI");
                            break;
                        case "right digits":
                            fragment = new LeftRightDigitFragment ( );
                            name = "right";
                            bundle.putString ("jackpot", "RIGHT");
//                            fragment.setArguments (bundle);
                            break;

                    }
                }


                if (fragment != null) {
                    Log.e ("mar_sel", "onItemClick: "+market_status );

                    //((SelectGameActivity) getActivity ( )).setGameTitle (gameNAme + " (" + matka_name + ")");
                    if (type.equalsIgnoreCase ("matka")) {
                        bundle.putString ("matka_name", matka_name);
                        bundle.putString ("game_name", model.getGame_name ( ));
                        bundle.putString ("m_id", matka_id);
                        bundle.putString ("game_id", model.getGame_id ( ));
                        bundle.putString ("start_time", start_time);
                        bundle.putString ("end_time", end_time);
                        bundle.putString ("title", gameNAme);
                        bundle.putString ("market_status",market_status);
                        bundle.putString ("is_market_open_nextday",is_market_open_nextday);
                        bundle.putString ("is_market_open_nextday2",is_market_open_nextday2);

                    } else if (type.equalsIgnoreCase ("starline")) {
                        bundle.putString ("matka_name", matka_name);
                        bundle.putString ("game_name", model.getGame_name ( ));
                        bundle.putString ("m_id", matka_id);
                        bundle.putString ("game_id", model.getGame_id ( ));
                        bundle.putString ("start_time", start_time);
                        bundle.putString ("end_time", end_time);
                        bundle.putString ("title", gameNAme);
                        bundle.putString ("market_status",market_status);
                        bundle.putString ("is_market_open_nextday",is_market_open_nextday);
                        bundle.putString ("is_market_open_nextday2",is_market_open_nextday2);
                    } else {
                        bundle.putString ("matka_name", matka_name);
                        bundle.putString ("game_name",name);
                        bundle.putString ("m_id", matka_id);
                        bundle.putString ("game_id", model.getGame_id ( ));
                        bundle.putString ("start_time", start_time);
                        bundle.putString ("end_time", end_time);
                        bundle.putString ("title", gameNAme);
                        bundle.putString ("market_status",market_status);
                        bundle.putString ("is_market_open_nextday",is_market_open_nextday);
                        bundle.putString ("is_market_open_nextday2",is_market_open_nextday2);
                    }


                    bundle.putString ("name", name);

                    Log.e ("selectGame_bundle", bundle.toString ( ));
                    fragment.setArguments (bundle);
                    FragmentManager fragmentManager = getActivity ( ).getSupportFragmentManager ( );
                    fragmentManager.beginTransaction ( ).replace (R.id.game_frame, fragment).addToBackStack (null).commit ( );
                }
            }catch (Exception exception){
                    exception.printStackTrace();
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        return view;
    }

    private void getJackpotGmaes()
    {
        sList.clear();
        sList.add(new SelectGameModel("2","single_digit","LEFT DIGIT","9.5","10","0","0","0","0","0"));
        sList.add(new SelectGameModel("3","jodi_digits","JODI DIGIT","95","10","0","0","1","0","0"));
        sList.add(new SelectGameModel("2","single_digit","RIGHT DIGIT","9.5","10","0","0","0","0","0"));
        if (sList.size()>0)

        {

            selectGameAdapter = new SelectGameAdapter(getActivity(),sList);
            selectGameAdapter.notifyDataSetChanged();
            rec_selectGame.setAdapter(selectGameAdapter);

        }

    }
    private void allGames() {
        sList.clear();
        loadingBar.show();
        HashMap<String,String> params = new HashMap<>();
        module.postRequest(URL_MATKAGAMES, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("jackpot_games",response.toString());
                loadingBar.dismiss ();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i=0;i<array.length();i++)
                    {
                        JSONObject object = array.getJSONObject(i);
                        SelectGameModel selectGameModel = new SelectGameModel();
                        selectGameModel.setGame_id(object.getString("game_id"));
                        selectGameModel.setGame_name(object.getString("game_name"));
                        selectGameModel.setName(object.getString("name"));
                        selectGameModel.setPoints(object.getString("points"));
                        selectGameModel.setStarline_points(object.getString("starline_points"));
                        selectGameModel.setIs_close(object.getString("is_close"));
                        selectGameModel.setIs_disabled(object.getString("is_disabled"));
                        selectGameModel.setIs_starline_disable(object.getString("is_starline_disable"));
                        selectGameModel.setIs_deleted(object.getString("is_deleted"));
                        selectGameModel.setIs_jackpot_disabled(object.getString("is_jackpot_disabled"));

                        if (object.getString("is_jackpot_disabled").equals("1"))
                        {

                        }else {
                            if(type.equals ("jackpot")) {
                                sList.add(selectGameModel);
                            }
                        }
                    }
                    if (sList.size()>0)
                    {
                        selectGameAdapter = new SelectGameAdapter(getActivity(),sList);
                        selectGameAdapter.notifyDataSetChanged();
                        rec_selectGame.setAdapter(selectGameAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                loadingBar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                module.showVolleyError(error);
                loadingBar.dismiss();
            }
        });




    }

    public void getAllGames()
    {
        sList.clear();
        loadingBar.show();
        HashMap<String,String> params = new HashMap<>();
        module.postRequest(URL_MATKAGAMES, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("matka_games",response.toString());

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i=0;i<array.length();i++)
                    {
                        int betType;

                        JSONObject object = array.getJSONObject(i);
                        SelectGameModel selectGameModel = new SelectGameModel();
                        selectGameModel.setGame_id(object.getString("game_id"));
                        selectGameModel.setGame_name(object.getString("game_name"));
                        selectGameModel.setName(object.getString("name"));
                        selectGameModel.setPoints(object.getString("points"));
                        selectGameModel.setStarline_points(object.getString("starline_points"));
                        selectGameModel.setIs_close(object.getString("is_close"));
                        selectGameModel.setIs_disabled(object.getString("is_disabled"));
                        selectGameModel.setIs_starline_disable(object.getString("is_starline_disable"));
                        selectGameModel.setIs_deleted(object.getString("is_deleted"));
                        selectGameModel.setIs_main (object.getString("is_main"));

                        if(object.getString("is_main").equals ("0"))
                        {
                            selectGameModel.setGame_id(object.getString("game_id"));
                        }
                        else {
                            selectGameModel.setGame_id(object.getString("is_main"));
                        }

                        if (object.getString("is_disabled").equals("1"))
                        {

                        }
                        else {
                            if (object.getString("name").equalsIgnoreCase("JODI DIGIT")
                                    ||object.getString("name").equalsIgnoreCase ("JODI DIGIT BULK")
                                    ||object.getString("name").equalsIgnoreCase ("RED BRACKET")
                                    ||object.getString("name").equalsIgnoreCase ("DIGIT BASED JODI")
                                    ||object.getString("name").equalsIgnoreCase ("GROUP JODI")
                            ||object.getString("name").equalsIgnoreCase ("HALF SANGAM")
                            ||object.getString("name").equalsIgnoreCase ("FULL SANGAM")){
                                betType = getBetType(getASandC(start_time, end_time));

                                if (betType == 1) {

                                } else {
                                    sList.add(selectGameModel);
                                }
                            }
                            else if(object.getString("name").equals("Single Digit Bulk"))
                            {

                            }
                            else {
                                if (object.getString("is_deleted").equals("1")){

                                }else {
                                    sList.add(selectGameModel);
                                }
                            }
                        }

                        }
                    if (sList.size()>0)
                    {
                        int sssize=sList.size ();
                        selectGameAdapter = new SelectGameAdapter(getActivity(),sList);
                       // selectGameAdapter.notifyDataSetChanged();
                        rec_selectGame.setAdapter(selectGameAdapter);
                        GridLayoutManager glm;
                        Log.e ("nsgs", "onResponse: "+sList.size () );
                        if(sList.size ()==18)
                        {
                            glm = new GridLayoutManager (getActivity ( ), 2);
                            glm.setSpanSizeLookup (new GridLayoutManager.SpanSizeLookup ( ) {
                                @Override
                                public int getSpanSize(int position) {



                                    Log.d ("xhh", "getSpanSize: " + position);
                                    switch (position) {
                                        case 0:
                                        case 17:



                                            return 2;
                                        case 1:
                                        case 2:
                                        case 3:
                                        case 4:
//                    case 2:
                                            return 1;
                                            default:
                                            //never gonna happen
                                            return 1;
                                    }
                                }

                            });
                        }
                        else if(sList.size ()==20)
                        {

                            glm = new GridLayoutManager (getActivity ( ), 2);
                            glm.setSpanSizeLookup (new GridLayoutManager.SpanSizeLookup ( ) {
                                @Override
                                public int getSpanSize(int position) {

                                    switch (position) {
                                        case 0:
                                        case 19:



                                            return 2;
                                        case 1:
                                        case 2:
                                        case 3:
                                        case 4:
//                    case 2:
                                            return 1;
                                        default:
                                            //never gonna happen
                                            return 1;
                                    }
                                }

                            });
                        }
                        else {

                             glm = new GridLayoutManager (getActivity ( ), 2);
                            glm.setSpanSizeLookup (new GridLayoutManager.SpanSizeLookup ( ) {
                                @Override
                                public int getSpanSize(int position) {
//                if (position % 4 == 2) {
//                    return 2;

//                }


                                    Log.d ("xhh", "getSpanSize: " + position);
                                    switch (position) {
                                        case 0:


                                            return 2;
                                        case 1:
                                        case 2:
                                        case 3:
                                        case 4:
//                    case 2:
                                            return 1;

                                        default:
                                            //never gonna happen
                                            return 1;
                                    }
                                }

                            });
                        }

                        rec_selectGame.setLayoutManager (glm);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                loadingBar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                module.showVolleyError(error);
                loadingBar.dismiss();
            }
        });

    }

    public void getAllSatrlineGames()
    {
        loadingBar.show();
        sList.clear();
        HashMap<String,String> params = new HashMap<>();
        module.postRequest(URL_MATKAGAMES, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("starlinGame",response.toString());

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i=0;i<array.length();i++)
                    {
                        JSONObject object = array.getJSONObject(i);
                        SelectGameModel selectGameModel = new SelectGameModel();
                        selectGameModel.setGame_id(object.getString("game_id"));
                        selectGameModel.setGame_name(object.getString("game_name"));
                       // selectGameModel.setName(object.getString("name"));
                        selectGameModel.setPoints(object.getString("points"));
                        selectGameModel.setStarline_points(object.getString("starline_points"));
                        selectGameModel.setIs_close(object.getString("is_close"));
                        selectGameModel.setIs_disabled(object.getString("is_disabled"));
                        selectGameModel.setIs_starline_disable(object.getString("is_starline_disable"));
                        selectGameModel.setIs_deleted(object.getString("is_deleted"));
                        if(object.getString("name").equalsIgnoreCase ("SINGLE and BULK DIGIT")) {
                            selectGameModel.setName("SINGLE DIGIT");
                        }
                        else {
                            selectGameModel.setName(object.getString("name"));
                        }

                        if (object.getString("is_starline_disable").equals("0")) {
                            if (!object.getString("is_deleted").equals("1")){
                                sList.add(selectGameModel);
                            }
                        }

                    }
                    if (sList.size()>0) {
                        selectGameAdapter = new SelectGameAdapter(getActivity(),sList);
                        selectGameAdapter.notifyDataSetChanged();
                        rec_selectGame.setAdapter(selectGameAdapter);
                        GridLayoutManager glm;
                        Log.e ("nsgs", "onResponse: "+sList.size () );
                        if(sList.size ()==8)
                        {

                            glm = new GridLayoutManager (getActivity ( ), 2);
                            glm.setSpanSizeLookup (new GridLayoutManager.SpanSizeLookup ( ) {
                                @Override
                                public int getSpanSize(int position) {
//                if (position % 4 == 2) {
//                    return 2;

//                }


                                    Log.d ("xhh", "getSpanSize: " + position);
                                    switch (position) {
                                        case 0:
                                        case 7:


                                            return 2;
                                        case 1:
                                        case 2:
                                        case 3:
                                        case 4:
//                    case 2:
                                            return 1;

                                        default:
                                            //never gonna happen
                                            return 1;
                                    }
                                }

                            });
                        }
                        else {

                            glm = new GridLayoutManager (getActivity ( ), 2);
                            glm.setSpanSizeLookup (new GridLayoutManager.SpanSizeLookup ( ) {
                                @Override
                                public int getSpanSize(int position) {
//                if (position % 4 == 2) {
//                    return 2;

//                }


                                    Log.d ("xhh", "getSpanSize: " + position);
                                    switch (position) {
                                        case 0:


                                            return 2;
                                        case 1:
                                        case 2:
                                        case 3:
                                        case 4:
//                    case 2:
                                            return 1;

                                        default:
                                            //never gonna happen
                                            return 1;
                                    }
                                }

                            });
                        }

                        rec_selectGame.setLayoutManager (glm);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                loadingBar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                module.showVolleyError(error);
                loadingBar.dismiss();
            }
        });
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

    public void backPress(View view)
    {
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if(type.equalsIgnoreCase ("starline"))
                    {
                        Intent intent = new Intent(getContext(), StarlineGamesFragment.class);
                        startActivity(intent);
                    }
                       else {
                        Intent intent = new Intent (getContext ( ), MainActivity.class);
                        startActivity (intent);
                    }
                    return true;
                }
                return false;
            }
        });

    }
}