package in.games.GamesSattaBets.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.GamesSattaBets.Activity.MainActivity;
import in.games.GamesSattaBets.Adapter.NotificationAdapter;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Model.NotifyModel;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.SessionMangement;

import static in.games.GamesSattaBets.Config.BaseUrls.URL_GET_NOTIFICATIONS;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_NOTIFICATIONS;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_SET_NOTIFICATIONS_STATUS;
import static in.games.GamesSattaBets.Config.Constants.KEY_ID;

public class NotificationFragment extends AppCompatActivity {
    RecyclerView rec_notification;
    NotificationAdapter notificationAdapter;
    LoadingBar loadingBar;
    Module module;
    SessionMangement sessionMangement;
    Switch aSwitch;
    SwipeRefreshLayout swipe;
    boolean flag=false;
    String user_id;
    TextView txtSwitch,tv_title;
    ImageView img_back;

    ArrayList<NotifyModel> nList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notification);

        initview();
        rec_notification.setLayoutManager (new LinearLayoutManager(NotificationFragment.this));
//        notificationData ( );

        getNotifications();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing())
                {
                    getNotifications();
                    swipe.setRefreshing(false);
                }
            }
        });


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(aSwitch.isChecked())
                {
                    if(flag){
                        setStatus("1");
                    }

                }
                else {
                    setStatus("0");
                }
            }
        });

//        notifications();

    }




    private void initview() {
        sessionMangement = new SessionMangement(NotificationFragment.this);
        loadingBar = new LoadingBar(NotificationFragment.this);
        module = new Module(NotificationFragment.this);
        rec_notification=findViewById (R.id.rec_notification);
        aSwitch =findViewById(R.id.notification_switch);
        txtSwitch =findViewById(R.id.text_notification);
        user_id=sessionMangement.getUserDetails().get(KEY_ID);
        swipe =findViewById(R.id.swipe);
        tv_title=findViewById (R.id.tv_title);
        tv_title.setText ("Alert");
        img_back=findViewById (R.id.img_back);

        nList=new ArrayList<> (  );
        img_back.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent (NotificationFragment.this,MainActivity.class );
                startActivity (intent);
            }
        });
    }
//    private void notificationData() {
//
//        nList.add(new NotificationModel ());
//        nList.add(new NotificationModel ());
//
//        notificationAdapter = new NotificationAdapter (getActivity (), nList);
//        notificationAdapter.notifyDataSetChanged();
//        rec_notification.setAdapter(notificationAdapter);
//    }

    private void notifications(){
        nList.clear();
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",sessionMangement.getUserDetails().get(KEY_ID));
        module.postRequest(URL_NOTIFICATIONS, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e( "notifications: ",response.toString() );
                try{
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success"))
                    {
                        module.successToast(object.getString("data"));
                    }else {
//                      module.errorToast(object.getString("data"));
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                }
                loadingBar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                module.showVolleyError(error);
                loadingBar.dismiss();
            }
        });

    }


    private void getNotifications(){
        nList.clear();
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",user_id);

        module.postRequest(URL_GET_NOTIFICATIONS, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get_notifi",response.toString());

                try {
                    nList.clear ();
                    JSONObject object = new JSONObject(response);
                    boolean resp=object.getBoolean("responce");
                    if(resp){
//                        if(!aSwitch.isChecked())
                            aSwitch.setChecked(true);

                            txtSwitch.setText("ON");
                         JSONArray jsonArray=object.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            NotifyModel matkasObjects=new NotifyModel();
                            matkasObjects.setNotification_id(jsonObject1.getString("notification_id"));
                            matkasObjects.setNotification(jsonObject1.getString("notification"));
                            matkasObjects.setTime(jsonObject1.getString("time"));
                            matkasObjects.setTitle(jsonObject1.getString("title"));

                            nList.add(matkasObjects);
                        }
                        if(nList.size()>0){
                            if(rec_notification.getVisibility()==View.GONE){
                                rec_notification.setVisibility(View.VISIBLE);
                            }

                            notificationAdapter=new NotificationAdapter(NotificationFragment.this,nList);
                            rec_notification.setAdapter(notificationAdapter);
                            notificationAdapter.notifyDataSetChanged();
                        }

                        loadingBar.dismiss();
                    }else{
//                        if(aSwitch.isChecked())
                            aSwitch.setChecked(false);

                        flag=true;
                        txtSwitch.setText("OFF");
                        rec_notification.setVisibility(View.GONE);
                        loadingBar.dismiss();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
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

    public void setStatus(final String st)
    {
        loadingBar.show();

        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",user_id);
        params.put("status",st);
        module.postRequest(URL_SET_NOTIFICATIONS_STATUS, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("status",response.toString());
                loadingBar.dismiss();
                try{
                    JSONObject object = new JSONObject(response.toString());
                    boolean resp=object.getBoolean("responce");
                    if(resp){
//                            common.showToast("Notification Enable.");
                        getNotifications();
                    }else{
//                     common.showToast(response.getString("error"));
                        if(aSwitch.isChecked())
                            aSwitch.setChecked(false);

                        txtSwitch.setText("OFF");
                        rec_notification.setVisibility(View.GONE);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
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