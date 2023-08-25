package in.games.gameskash.Activity;

import static in.games.gameskash.Config.BaseUrls.URL_VIDEO_LIST;
import static in.games.gameskash.Config.Constants.KEY_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.gameskash.Adapter.ShowVideoAdapter;
import in.games.gameskash.Config.Module;
import in.games.gameskash.Model.ShowVideoModel;
import in.games.gameskash.R;
import in.games.gameskash.Util.LoadingBar;
import in.games.gameskash.Util.RecyclerTouchListener;
import in.games.gameskash.Util.SessionMangement;

public class ShowVedioActivity extends AppCompatActivity {
RecyclerView rec_vedios;
ShowVideoAdapter adapter;
String name="",lan_id="";
ArrayList<ShowVideoModel >modellist;
LoadingBar loadingBar;
SessionMangement sessionMangement;
Module module;
TextView tv_title;
ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_vedio);
        initView();
        //                module.checkDeviceLogin();

        initRecyclerview();
    }
    private void initView() {
        img_back=findViewById (R.id.img_back);
        tv_title=findViewById (R.id.tv_title);

        modellist=new ArrayList<> (  );

        module=new Module (ShowVedioActivity.this);
        sessionMangement=new SessionMangement (ShowVedioActivity.this);
        loadingBar=new LoadingBar (ShowVedioActivity.this);

        rec_vedios= findViewById(R.id.rec_vedios);
        rec_vedios.setHasFixedSize(true);
        name=getIntent ().getStringExtra ("title");
        tv_title.setText (name);
        img_back.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent ( ShowVedioActivity.this,MainActivity.class );
                startActivity (intent);
            }
        });
        lan_id=getIntent ().getStringExtra ("lan_id");


        LinearLayoutManager layoutManager = new LinearLayoutManager(ShowVedioActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rec_vedios.setLayoutManager(layoutManager);

        rec_vedios.addOnItemTouchListener (new RecyclerTouchListener (ShowVedioActivity.this, rec_vedios, new RecyclerTouchListener.OnItemClickListener ( ) {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent (ShowVedioActivity.this, PlayVideoActivity.class);
                intent.putExtra ("video_url",modellist.get(position).getVideourl ());
                intent.putExtra ("video_name",modellist.get(position).getTitle());
                startActivity (intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

    private void initRecyclerview() {

        {

            loadingBar.show();
            modellist.clear();
            HashMap<String,String> params = new HashMap<>();
            params.put ("language_id",lan_id);
            params.put("user_id", sessionMangement.getUserDetails().get(KEY_ID));

            module.postRequest(URL_VIDEO_LIST, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e ("video_list", response);
                    loadingBar.dismiss ( );
                    try {
                        JSONObject object = new JSONObject (response);
                        boolean resp = object.getBoolean ("response");
                        if (resp) {
                            JSONArray data = object.getJSONArray ("data");

                            for (int i = 0; i < data.length ( ); i++) {
                                ShowVideoModel model = new ShowVideoModel();
                                JSONObject obj = data.getJSONObject (i);
                                model.setLan_id (obj.getString ("id"));
                                model.setTitle (obj.getString ("title"));
                                model.setDescription (obj.getString ("description"));
                                model.setVideourl (obj.getString ("url"));
                                modellist.add (model);
                            }
                        }


                        if (modellist.size ( ) > 0) {
                            adapter = new ShowVideoAdapter ( ShowVedioActivity.this, modellist);
                            rec_vedios.setAdapter (adapter);
                        }



                    } catch (Exception ex) {
                        ex.printStackTrace ( );
                        loadingBar.dismiss ( );
                    }
                }
            }, new Response.ErrorListener() {


                @Override
                public void onErrorResponse(VolleyError error) {
                    loadingBar.dismiss();
                    module.showVolleyError(error);
                }
            });

        }

    }
}
