package in.games.Gameskash.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.Gameskash.Adapter.FundHistoryAdapter;
import in.games.Gameskash.Config.Module;
import in.games.Gameskash.Model.FundHistoryModel;
import in.games.Gameskash.R;
import in.games.Gameskash.Util.LoadingBar;
import in.games.Gameskash.Util.SessionMangement;

import static in.games.Gameskash.Config.BaseUrls.URL_FUND_HISTORY;
import static in.games.Gameskash.Config.Constants.KEY_ID;

public class WithdrawFundHistoryFragment extends AppCompatActivity {
RecyclerView rec_wHistory;
SwipeRefreshLayout swipe;
ArrayList<FundHistoryModel> fList,aList;
FundHistoryAdapter fundHistoryAdapter;
LoadingBar loadingBar;
ImageView img_back;
Module module;
SessionMangement sessionMangement;
String name,status="";
TextView tv_title;
String new_transId="";
Context context = WithdrawFundHistoryFragment.this;
    public WithdrawFundHistoryFragment() {
        // Required empty public constructor
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_withdraw_fund_history);

        name = getIntent().getStringExtra("name");
        status=getIntent ().getStringExtra ("status");

        sessionMangement = new SessionMangement(context);
        module = new Module(context);
        loadingBar = new LoadingBar(context);
        fList = new ArrayList<>();
        aList = new ArrayList<>();
        img_back = findViewById(R.id.img_back);
        rec_wHistory = findViewById(R.id.rec_wHistory);
        swipe = findViewById(R.id.swipe);
        tv_title = findViewById(R.id.tv_title);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (name.equalsIgnoreCase("w"))
        {
//            tv_title.setText("Withdrawal Fund History");
            tv_title.setText(" History");
//            commit by neelam show same name in other fragment
        }else if (name.equalsIgnoreCase("a"))
        {
            tv_title.setText(" History");
            //            commit by neelam show same name in other fragment

            //tv_title.setText("Fund Request History");
        }

//        fundHistory();
        if(status.equals ("approved"))
        {
            getApprovedFundHistry(sessionMangement.getUserDetails().get(KEY_ID));
        }
        else
        {
            getFundHistry(sessionMangement.getUserDetails().get(KEY_ID));
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {
                 if (swipe.isRefreshing())
                 {
//                     fundHistory();
                     getFundHistry(sessionMangement.getUserDetails().get(KEY_ID));
                     swipe.setRefreshing(false);
                 }
             }
         });

    }

    private void getApprovedFundHistry(String user_id) {
        fList.clear ( );
        aList.clear ();
        loadingBar.show ( );

        HashMap<String, String> params = new HashMap<String, String>( );
        params.put ("user_id", user_id);
        module.postRequest(URL_FUND_HISTORY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e ("fund_history", response.toString ( ));
                loadingBar.dismiss ( );
                try {
                    JSONObject  object = new JSONObject(response);
                    String res = object.getString ("status");
                    if (res.equals ("success")) {
                        JSONArray data = object.getJSONArray ("data");

                        if (data.equals("[]"))
                        {
                            module.errorToast(getApplicationContext(),"No History Available");
                        }

                        for (int i = 0; i < data.length ( ); i++) {
                            FundHistoryModel model = new FundHistoryModel ( );
                            JSONObject obj = data.getJSONObject (i);
                            //model.setTransaction_id (obj.getString ("trans_id"));
                            model.setTransaction_id (obj.getString("dyn_txn_id"));
                            model.setRequest_id (obj.getString ("request_id"));
                            model.setRequest_points (obj.getString ("request_points"));
                            model.setRequest_status (obj.getString ("request_status"));
                            model.setUser_id (obj.getString ("user_id"));
                            model.setType (obj.getString ("type"));
                            model.setTime (obj.getString ("time"));
                            model.setMode (obj.getString ("mode"));
                            //  model.setWithdraw_type(obj.getString("withdraw_type"));
                            if (obj.getString ("type").equalsIgnoreCase("withdrawal"))
                            {
                                if(!obj.getString ("request_status").equalsIgnoreCase ("pending")) {
                                    fList.add (model);
                                }
                            }


                            else if (obj.getString ("type").equalsIgnoreCase("Add"))
                            {
                                if(!obj.getString ("request_status").equalsIgnoreCase ("pending")) {
                                    aList.add(model);
                                }

                            }else {
                                // module.showToast("error");
                            }

                        }
                        Log.d ("req_list", String.valueOf (fList.size ( )));

                        if (name.equalsIgnoreCase("w"))
                        {
                            if (fList.size ( ) > 0) {

                                rec_wHistory.setLayoutManager(new LinearLayoutManager(context));
                                fundHistoryAdapter = new FundHistoryAdapter(context,fList);
                                fundHistoryAdapter.notifyDataSetChanged();
                                rec_wHistory.setAdapter(fundHistoryAdapter);

                            } else {
                                module.errorToast(getApplicationContext(),"No History Available");
                            }
                        }else if (name.equalsIgnoreCase("a"))
                        {
                            if (aList.size ( ) > 0) {

                                rec_wHistory.setLayoutManager(new LinearLayoutManager(context));
                                fundHistoryAdapter = new FundHistoryAdapter(context,aList);
                                fundHistoryAdapter.notifyDataSetChanged();
                                rec_wHistory.setAdapter(fundHistoryAdapter);

                            } else {

                                module.errorToast(getApplicationContext(),"No History Available");
                            }
                        }

                    } else {
                        module.errorToast(getApplicationContext(),"something went wrong");
                    }
                } catch (JSONException e) {
                    e.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void fundHistory() {
        fList.add(new FundHistoryModel());
        fList.add(new FundHistoryModel());
        fList.add(new FundHistoryModel());
        fList.add(new FundHistoryModel());
        rec_wHistory.setLayoutManager(new LinearLayoutManager(context));
        fundHistoryAdapter = new FundHistoryAdapter(context,fList);
        fundHistoryAdapter.notifyDataSetChanged();
        rec_wHistory.setAdapter(fundHistoryAdapter);

    }


    private void getFundHistry(String user_id) {
        fList.clear ( );
        aList.clear ();
        loadingBar.show ( );

        HashMap<String, String> params = new HashMap<String, String>( );
        params.put ("user_id", user_id);
        module.postRequest(URL_FUND_HISTORY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e ("fund_history", response.toString ( ));
                loadingBar.dismiss ( );
                try {
                    JSONObject  object = new JSONObject(response);
                    String res = object.getString ("status");
                    if (res.equals ("success")) {
                        JSONArray data = object.getJSONArray ("data");

                        if (data.equals("[]"))
                        {
                            module.errorToast(getApplicationContext(),"No History Available");
                        }

                        for (int i = 0; i < data.length ( ); i++) {
                            FundHistoryModel model = new FundHistoryModel ( );
                            JSONObject obj = data.getJSONObject (i);
                            model.setTransaction_id (obj.getString ("dyn_txn_id"));
                            model.setRequest_id (obj.getString ("request_id"));
                            model.setRequest_points (obj.getString ("request_points"));
                            model.setRequest_status (obj.getString ("request_status"));
                            model.setUser_id (obj.getString ("user_id"));
                            model.setType (obj.getString ("type"));
                            model.setTime (obj.getString ("time"));
                            model.setMode(obj.getString("mode"));
                          //  model.setWithdraw_type(obj.getString("withdraw_type"));
                            if (obj.getString ("type").equalsIgnoreCase("withdrawal"))
                            {
                                fList.add (model);
                            }else if (obj.getString ("type").equalsIgnoreCase("Add"))
                            {
                                aList.add(model);
                            }else {
                               // module.showToast("error");
                            }

                        }
                        Log.d ("req_list", String.valueOf (fList.size ( )));

                        if (name.equalsIgnoreCase("w"))
                        {
                            if (fList.size ( ) > 0) {

                                rec_wHistory.setLayoutManager(new LinearLayoutManager(context));
                                fundHistoryAdapter = new FundHistoryAdapter(context,fList);
                                fundHistoryAdapter.notifyDataSetChanged();
                                rec_wHistory.setAdapter(fundHistoryAdapter);

                            } else {

                                module.errorToast(getApplicationContext(),"No History Available");
                            }
                        }else if (name.equalsIgnoreCase("a"))
                        {
                            if (aList.size ( ) > 0) {

                                rec_wHistory.setLayoutManager(new LinearLayoutManager(context));
                                fundHistoryAdapter = new FundHistoryAdapter(context,aList);
                                fundHistoryAdapter.notifyDataSetChanged();
                                rec_wHistory.setAdapter(fundHistoryAdapter);

                            } else {

                                module.errorToast(getApplicationContext(),"No History Available");
                            }
                        }

                    } else {
                        module.errorToast(getApplicationContext(),"something went wrong");
                    }
                } catch (JSONException e) {
                    e.printStackTrace ( );
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }


}