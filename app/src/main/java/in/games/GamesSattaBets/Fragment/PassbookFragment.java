package in.games.GamesSattaBets.Fragment;

import static in.games.GamesSattaBets.Config.BaseUrls.URL_Passbook;
import static in.games.GamesSattaBets.Config.Constants.KEY_ID;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.GamesSattaBets.Activity.MainActivity;
import in.games.GamesSattaBets.Adapter.PassbookAdapter;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Model.PassbookHistoryModel;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.SessionMangement;


public class PassbookFragment extends AppCompatActivity {
ImageView img_back,    img_passbook;
TextView tv_title;
RecyclerView rec_passbook;
ArrayList<PassbookHistoryModel>list ;
PassbookAdapter adapter;
Module module;
LinearLayout lin_passbook;
LoadingBar loadingBar;
SessionMangement sessionMangement;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PassbookFragment() {
        // Required empty public constructor
    }





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        setContentView (R.layout.fragment_passbook);
        initview();

        getPassbokdata();
    }

    private void getPassbokdata() {

            loadingBar.show ( );

            list.clear();
            final HashMap<String, String> params = new HashMap<String, String>( );
            params.put ("user_id",sessionMangement.getUserDetails ().get (KEY_ID));

            module.postRequest(URL_Passbook, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("URL_Passbook",response.toString());
                    loadingBar.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        boolean res = object.getBoolean ("response");
                        if (res) {
                            JSONArray data = object.getJSONArray ("data");

                            for (int i = 0; i < data.length ( ); i++) {
                                PassbookHistoryModel model = new PassbookHistoryModel ( );
                                JSONObject obj = data.getJSONObject (i);
                                model.setBid_played (obj.getString ("bid_played"));
                                model.setGame_name (obj.getString ("game_name"));
                                //model.setMatka_id (obj.getString ("matka_id"));
                                model.setMatka_name (obj.getString ("matka_name"));
                                model.setBet_type (obj.getString ("type"));
                                model.setRequest_id (obj.getString ("request_id"));
                                model.setTrans_id (obj.getString ("txn_id"));
                                model.setDate (obj.getString ("time"));
                                model.setParticuler_text (obj.getString ("remark"));
                                model.setMode (obj.getString ("mode"));
                                model.setTrans_damt (obj.getString ("request_points"));
                                model.setDate_and_time (obj.getString ("time"));
                                model.setStatus (obj.getString ("request_status"));
                                model.setPrev_amount (obj.getString ("previous_amount"));
                                model.setTrnasaction_amt (obj.getString ("request_points"));
                                model.setCurrent_amt (obj.getString ("wallet_points"));
                                model.setAmount_type (obj.getString ("type"));
                                model.setAdded_by (module.checkNullNumber(obj.getString ("added_by")));
                                model.setTransaction_id (obj.getString("dyn_txn_id"));
                                list.add (model);
                            }
                            Log.d ("list", String.valueOf (list.size ( )));

                            if (list.size ( ) > 0) {
                                adapter = new PassbookAdapter (PassbookFragment.this, list );
                                rec_passbook.setAdapter(adapter);

                            } else {
                                module.errorToast ("No History Available");
                            }
                        } else {
                            module.errorToast(object.getString("Something Went Wrong"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace ( );
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




    private void initview() {
       list=new ArrayList<> (  );
        loadingBar=new LoadingBar (PassbookFragment.this);
        sessionMangement=new SessionMangement (PassbookFragment.this);
        module=new Module (PassbookFragment.this);
        rec_passbook=findViewById (R.id.rec_passbook);
        //rec_passbook.setLayoutManager (new LinearLayoutManager (PassbookFragment.this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
      /////// reverse method startt////
        //linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
        //////reverse method end////
        rec_passbook.setLayoutManager(linearLayoutManager);

        img_back=findViewById (R.id.img_back);
        img_passbook=findViewById (R.id.img_passbook);
        lin_passbook=findViewById (R.id.lin_passbook);
        lin_passbook.setVisibility( View.VISIBLE );
        tv_title=findViewById (R.id.tv_title);
        tv_title.setText ("Passbook");
        img_passbook.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                {
                    PassbookFragment.this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );

                }
                else {
                    PassbookFragment.this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
                }
            }
        } );
        img_back.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent ( PassbookFragment.this, MainActivity.class);
                startActivity (intent);
            }
        });
    }


}