package in.games.GamesSattaBets.Fragment;

import static in.games.GamesSattaBets.Config.BaseUrls.URL_NOTICEBOARD;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.GamesSattaBets.Activity.MainActivity;
import in.games.GamesSattaBets.Adapter.NoticeBordAdapter;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Interfaces.GetAppSettingData;
import in.games.GamesSattaBets.Model.IndexResponse;
import in.games.GamesSattaBets.Model.NoticeBordModel;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.SessionMangement;

//import static in.games.gdmatkalive.Activity.SplashActivity.home_text;
//import static in.games.gdmatkalive.Activity.SplashActivity.indexMinAmount;
//import static in.games.gdmatkalive.Activity.SplashActivity.index_min_wallet_msg;
//import static in.games.gdmatkalive.Activity.SplashActivity.index_notice;
//import static in.games.gdmatkalive.Activity.SplashActivity.index_w_amount;
//import static in.games.gdmatkalive.Activity.SplashActivity.min_add_amount;
//import static in.games.gdmatkalive.Activity.SplashActivity.whatsapp_msg;
//import static in.games.gdmatkalive.Activity.SplashActivity.whatsapp_no;
//import static in.games.gdmatkalive.Activity.SplashActivity.withdraw_no;
//import static in.games.gdmatkalive.Activity.SplashActivity.withdraw_text;


public class NoticeboardFragment extends Fragment {
LoadingBar loadingBar;
Module module;
LinearLayout lin_whatsapp;
TextView tv_withdraw,tv_whatsapp,tv_sample,tv_minDeposit,tv_minwithdraw,tv_notice,tv_wdescription,tv_starline_des,tv_stitle;
SessionMangement sessionMangement;
String title="",description="";
SwipeRefreshLayout swipe;
String indexMinAmount,index_min_wallet_msg,index_notice,index_w_amount,min_add_amount,
        whatsapp_msg="Hi,Admin!",whatsapp_no,withdraw_no,withdraw_text;

RecyclerView rec_notice;
NoticeBordAdapter noticeBordAdapter;
ArrayList<NoticeBordModel> modellist;
    NoticeBordModel model;

    public NoticeboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_noticeboard, container, false);
        ((MainActivity)getActivity()).setTitles("NoticeBoard");
        initview(view);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing())
                {
                    tv_withdraw.setText (withdraw_text);
                    tv_whatsapp.setText (withdraw_no);
                    tv_sample.setText(index_min_wallet_msg);
                    tv_minDeposit.setText(min_add_amount);
                    tv_minwithdraw.setText(index_w_amount);
                    tv_notice.setText(index_notice);

                    initNoticerecview();
                    swipe.setRefreshing(false);
                }
            }
        });

        noticeBoard();
        return view;
    }



    private void initview(View view) {

        sessionMangement = new SessionMangement(getActivity());
        loadingBar = new LoadingBar(getActivity());
        module = new Module(getActivity());
        tv_wdescription=view.findViewById (R.id.tv_wdescription);
        rec_notice=view.findViewById (R.id.rec_notice);
        rec_notice.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_notice.setLayoutManager(layoutManager);

        modellist= new ArrayList<>();

        swipe = view.findViewById(R.id.swipe);
        tv_withdraw=view.findViewById (R.id.tv_withdraw);
        tv_starline_des=view.findViewById (R.id.tv_starline_des) ;
        tv_stitle=view.findViewById (R.id.tv_stitle) ;
        tv_whatsapp =view.findViewById (R.id.tv_whatsapp);
        lin_whatsapp=view.findViewById (R.id.lin_whatsapp);
        tv_sample = view.findViewById(R.id.tv_sample);
        tv_minDeposit = view.findViewById(R.id.tv_minDeposit);
        tv_minwithdraw = view.findViewById(R.id.tv_minwithdraw);
        tv_notice = view.findViewById(R.id.tv_notice);

        module.getConfigData(new GetAppSettingData() {
            @Override
            public void getAppSettingData(IndexResponse model) {
                tv_wdescription.setText (Html.fromHtml (model.getWithdraw_description ()));
//                tv_stitle.setText (Html.fromHtml (model.getWithdraw_description ()));
//                tv_starline_des.setText (Html.fromHtml (model.getWithdraw_description ()));
                tv_stitle.setText ("Starline Terms And Condition");
                tv_starline_des.setText (Html.fromHtml (model.getWithdraw_description ()));
                indexMinAmount =  model.getMin_amount();
                index_min_wallet_msg = model.getMin_wallet_msg();
                index_notice =model.getNotice();
                index_w_amount= model.getW_amount();
                min_add_amount= model.getMin_wallet();
                whatsapp_no = model.getMobile();
                withdraw_no =model.getWithdraw_no();
                withdraw_text = model.getWithdraw_text();

                tv_withdraw.setText (withdraw_text);
                tv_whatsapp.setText (withdraw_no);
                tv_sample.setText(index_min_wallet_msg);
                tv_minDeposit.setText(indexMinAmount);
                tv_minwithdraw.setText(index_w_amount);
                tv_notice.setText(index_notice);

            }
        });
        lin_whatsapp.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                module.whatsapp(whatsapp_no,whatsapp_msg);
            }
        });

    }


    private void initNoticerecview() {



    }


    public void noticeBoard() {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
//        params.put ("title",title);
//        params.put ("detail",detail);
        module.postRequest(URL_NOTICEBOARD, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("noticeboard", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    boolean status = object.getBoolean("responce");
                    Log.e("noticb_respone", "onResponse: "+response );
                    if (status) {
                        JSONArray data_arr = object.getJSONArray("data");
                        for (int i = 0; i <= data_arr.length() - 1; i++) {

                            JSONObject jsonObject = data_arr.getJSONObject(i);
                            model= new NoticeBordModel();
                            model.setTitle(jsonObject.getString("title"));
                       model.setDiscription(jsonObject.getString("description"));

                       modellist.add(model);
                        }
                        noticeBordAdapter = new NoticeBordAdapter(getActivity(),modellist);
                        rec_notice.setAdapter(noticeBordAdapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                   module.showToast("Something went wrong");
                }
                loadingBar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loadingBar.dismiss();
            }
        });
    }
}