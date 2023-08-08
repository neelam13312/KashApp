package in.games.GamesSattaBets.Fragment;

import static in.games.GamesSattaBets.Config.BaseUrls.URL_videoLanguage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.GamesSattaBets.Activity.MainActivity;
import in.games.GamesSattaBets.Activity.ShowVedioActivity;
import in.games.GamesSattaBets.Adapter.VedioLanguageAdapter;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Model.VedioLanguageModel;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.RecyclerTouchListener;
import in.games.GamesSattaBets.Util.SessionMangement;


public class VedioLanguageFragment extends Fragment {

    RecyclerView rec_vedio;
    VedioLanguageAdapter adapter;
    ArrayList<VedioLanguageModel> modellist;
    SessionMangement sessionMangement;
    LoadingBar loadingBar;
    Module module;

    public VedioLanguageFragment() {
        // Required empty public constructor
    }

    public static VedioLanguageFragment newInstance(String param1, String param2) {
        VedioLanguageFragment fragment = new VedioLanguageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_vedio, container, false);
        ((MainActivity)getActivity()).setTitles("Video Language");
        initView(view);
        initRecyclerview();
        return view;
    }


    private void initView(View view) {
        loadingBar = new LoadingBar(getContext());
        sessionMangement = new SessionMangement (getActivity ( ));
        modellist= new ArrayList<>();
        module=new Module (getActivity ());
        rec_vedio= view.findViewById(R.id.rec_vedio);
        rec_vedio.setHasFixedSize(true);
        rec_vedio.setLayoutManager(new GridLayoutManager(getActivity (),2));

    }
    private void initRecyclerview() {

            loadingBar.show();
            modellist.clear();
            HashMap<String,String> params = new HashMap<>();
            module.postRequest(URL_videoLanguage, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e ("video_langa", response);
                    loadingBar.dismiss ( );
                    try {
                        JSONObject object = new JSONObject (response);
                        boolean resp = object.getBoolean ("response");
                        if (resp) {
                            JSONArray data = object.getJSONArray ("data");

                            for (int i = 0; i < data.length ( ); i++) {
                                VedioLanguageModel model = new VedioLanguageModel ( );
                                JSONObject obj = data.getJSONObject (i);
                                model.setLan_id (obj.getString ("id"));
                                model.setTitle (obj.getString ("title"));
                                model.setSubtitile (obj.getString ("subtitile"));
                                modellist.add (model);

                            }
                            if (modellist.size ( ) > 0) {
                                adapter = new VedioLanguageAdapter (getActivity ( ), modellist);
                                rec_vedio.setAdapter (adapter);
                            }
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

//        modellist.add(new VedioLanguageModel("English"));
//        modellist.add(new VedioLanguageModel("Hindi"));
//        modellist.add(new VedioLanguageModel("Telugu"));
//        modellist.add(new VedioLanguageModel("Kananada"));



        GridLayoutManager glm = new GridLayoutManager(getActivity(), 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
//                if (position % 4 == 2) {
//                    return 2;
//                }
                switch (position %5) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        return 1;
                    case 4:
//                    case 2:
                        return 2;
                    default:
                        //never gonna happen
                        return  -1 ;
                }
            }
        });

        rec_vedio.setLayoutManager (glm);





        rec_vedio.addOnItemTouchListener (new RecyclerTouchListener(getActivity ( ), rec_vedio, new RecyclerTouchListener.OnItemClickListener ( ) {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent (getActivity (), ShowVedioActivity.class);
                intent.putExtra ("title",modellist.get(position).getTitle ());
                intent.putExtra ("lan_id",modellist.get(position).getLan_id ());
                startActivity (intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));





    }

}