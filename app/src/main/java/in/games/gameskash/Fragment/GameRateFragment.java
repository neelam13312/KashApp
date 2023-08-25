package in.games.gameskash.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.gameskash.Activity.MainActivity;
import in.games.gameskash.Adapter.RateAdapter;
import in.games.gameskash.Config.Module;
import in.games.gameskash.Model.RateModel;
import in.games.gameskash.R;
import in.games.gameskash.Util.ConnectivityReceiver;
import in.games.gameskash.Util.LoadingBar;
import in.games.gameskash.Util.SessionMangement;

import static in.games.gameskash.Config.BaseUrls.URL_NOTICE;


public class GameRateFragment extends Fragment {
SwipeRefreshLayout swipe;
RecyclerView rec_rate,rec_srate;
RateAdapter rateAdapter;
ArrayList<RateModel> rList,slist;
LoadingBar loadingBar;
Module module;
SessionMangement sessionMangement;

    public GameRateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_game_rate, container, false);
        initview(view);
        ((MainActivity)getActivity()).setTitles("Game Rate");
        rec_rate.setLayoutManager (new LinearLayoutManager (getContext ()));
        rec_srate.setLayoutManager (new LinearLayoutManager (getContext ()));
//        rateData ( );

        if (ConnectivityReceiver.isConnected()) {
            getNotice();
        } else
        {
           module.noInternet();
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing())
                {
                    if (ConnectivityReceiver.isConnected())
                    {
                        getNotice();
                    }else {
                        module.noInternet();
                    }
                    swipe.setRefreshing(false);
                }

            }
        });

        return view;
    }


    private void initview(View view) {

        module = new Module(getActivity());
        sessionMangement = new SessionMangement(getActivity());
        loadingBar = new LoadingBar(getActivity());
        rec_rate=view.findViewById (R.id.rec_rate);
        rec_srate=view.findViewById (R.id.rec_srate);
        swipe = view.findViewById(R.id.swipe);
        rList=new ArrayList<> (  );
        slist = new ArrayList<>();
    }
    private void rateData() {
        rList.add(new RateModel ());
        rList.add(new RateModel ());

        rateAdapter = new RateAdapter (getActivity (), rList);
        rateAdapter.notifyDataSetChanged();
        rec_rate.setAdapter(rateAdapter);
    }
    private void getNotice() {

        loadingBar.show();
        rList = new ArrayList<>();
        slist = new ArrayList<>();

        HashMap<String, String> params = new HashMap<String, String>();

        module.postRequest(URL_NOTICE, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("game_rate",response.toString());
                try {
                     JSONObject obj = new JSONObject(response);
                     String status = obj.getString("status");
                    if (status.equals("success")) {
                        JSONArray array = obj.getJSONArray("data");

                        for (int i = 0; i < array.length(); i++) {
                            RateModel gameRateModel = new RateModel();
                            JSONObject object = array.getJSONObject(i);
                            gameRateModel.setId(object.getString("id"));
                            gameRateModel.setName(object.getString("name"));
                           // gameRateModel.setRate_range(object.getString("rate_range"));
                            gameRateModel.setRate(object.getString("rate"));
                            String type = object.getString("type").toString();
                            gameRateModel.setType(type);
                            if (type.equals("0")) {
                                rList.add(gameRateModel);

                            } else {
                                slist.add(gameRateModel);
                            }
                        }

                        rateAdapter = new RateAdapter (getActivity (), rList);
                        rec_rate.setAdapter(rateAdapter);
                        rateAdapter.notifyDataSetChanged();

                        rateAdapter = new RateAdapter (getActivity (), slist);
                        rec_srate.setAdapter(rateAdapter);
                        rateAdapter.notifyDataSetChanged();


                    } else {
                        Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                    }

                    loadingBar.dismiss();
                } catch (Exception ex) {
                    loadingBar.dismiss();
                    module.showToast(ex.getMessage());
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
}