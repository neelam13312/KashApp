package in.games.GamesSattaBets.Fragment;

import static in.games.GamesSattaBets.Config.BaseUrls.URL_GET_STATUS;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_SET_STATUS;
import static in.games.GamesSattaBets.Config.Constants.KEY_ID;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.games.GamesSattaBets.Activity.MainActivity;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.SessionMangement;

public class SettingFragment extends Fragment {
    RadioButton rv_mpin,rv_pass;
    String type="",is_mpin="1",is_password="0",game_notifaction="1",main_notification="1"
            ,starline_notification="1";
    String android_id;
    LoadingBar loadingBar;
    SessionMangement sessionMangement;
    Module module;
    Switch main,game,starline;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate (R.layout.fragment_setting, container, false);
        ((MainActivity)getActivity()).setTitles("Setting");
        initview(view);
        getStatus();

        rv_mpin.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                type="mpin";
                is_mpin="1";
                is_password="0";
                setStatus(type,is_password,is_mpin,game_notifaction,main_notification,starline_notification);
            }
        });

        rv_pass.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                type="password";
                is_password="1";
                // is_mpin="0";
                is_mpin="1";
                setStatus(type,is_password,is_mpin,game_notifaction,main_notification,starline_notification);

            }
        });


        main.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    main_notification="1";
                    setStatus(type,is_password,is_mpin,game_notifaction,main_notification,starline_notification);

                    // The toggle is enabled
                } else {
                    main_notification="0";
                    setStatus(type,is_password,is_mpin,game_notifaction,main_notification,starline_notification);

                    // The toggle is disabled
                }
            }
        });
        game.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    game_notifaction="1";
                    setStatus(type,is_password,is_mpin,game_notifaction,main_notification,starline_notification);

                    // The toggle is enabled
                } else {
                    game_notifaction="0";
                    setStatus(type,is_password,is_mpin,game_notifaction,main_notification,starline_notification);

                    // The toggle is disabled
                }
            }
        });

        starline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    starline_notification="1";
                    setStatus(type,is_password,is_mpin,game_notifaction,main_notification,starline_notification);

                    // The toggle is enabled
                } else {
                    starline_notification="0";
                    setStatus(type,is_password,is_mpin,game_notifaction,main_notification,starline_notification);

                    // The toggle is disabled
                }
            }
        });
        return view;
    }

    private void getStatus()
    {
        loadingBar.show();
        HashMap<String,String> params = new HashMap<>();
        params.put("device_id",sessionMangement.getDeviceId());

        module.postRequest(URL_GET_STATUS, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("URL_GET_STATUS",response);
                //add condition and show

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("response")){
                        JSONArray array = object.getJSONArray("data");

                        JSONObject obj = array.getJSONObject(0);
                        obj.getString ("is_mpin");
                        obj.getString ("is_password");
                        obj.getString ("main_notification");
                        obj.getString ("game_notifaction");
                        obj.getString ("starline_notification");

                        if( obj.getString ("is_mpin").equalsIgnoreCase ("1"))
                        {
                            rv_mpin.setChecked (true);
                            rv_pass.setChecked (false);
                        }
                        if( obj.getString ("is_password").equalsIgnoreCase ("1"))
                        {
                            rv_mpin.setChecked (false);
                            rv_pass.setChecked (true);
                        }
                        if( obj.getString ("main_notification").equalsIgnoreCase ("1"))
                        {
                            main.setChecked (true);
                        }
                        else {
                            main.setChecked (false);
                        }

                        if( obj.getString ("game_notifaction").equalsIgnoreCase ("1"))
                        {
                            game.setChecked (true);
                        }
                        else
                        {
                            game.setChecked (false);
                        }
                        if(obj.getString ("starline_notification").equalsIgnoreCase ("1"))
                        {
                            starline.setChecked (true);
                        }
                        else
                        {
                            starline.setChecked (false);
                        }
                    }
                    else {
                        main.setChecked (false);
                        starline.setChecked (false);
                        game.setChecked (false);
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

    private void setStatus(String type,String is_pass,String is_mpin,String game,String main,String starline)

    {

//       String android_id = Settings.Secure.getString(getContext().getContentResolver(),
//                Settings.Secure.ANDROID_ID);
        loadingBar.show();
        HashMap<String,String> params = new HashMap<>();
        params.put("type",type);
        params.put("is_password",is_pass);
        params.put("is_mpin",is_mpin);
        params.put("game_notifaction",game);
        params.put("main_notification",main);
        params.put("starline_notification",starline);
        params.put("user_id",sessionMangement.getUserDetails ().get (KEY_ID));
        params.put("device_id",sessionMangement.getDeviceId());
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

    private void initview(View view) {
        rv_mpin=view.findViewById (R.id.rv_mpin);
        rv_pass=view.findViewById (R.id.rv_pass);
        sessionMangement=new SessionMangement (getActivity ());
        module=new Module (getActivity ());
        loadingBar=new LoadingBar (getActivity ());
        main=view.findViewById (R.id.main);
        game=view.findViewById (R.id.game);
        starline=view.findViewById (R.id.starline);
    }

}