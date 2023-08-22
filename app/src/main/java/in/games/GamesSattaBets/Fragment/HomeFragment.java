package in.games.GamesSattaBets.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dcastalia.localappupdate.DownloadApk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import in.games.GamesSattaBets.Activity.MainActivity;
import in.games.GamesSattaBets.Adapter.MatkaAdapter;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Interfaces.GetAppSettingData;
import in.games.GamesSattaBets.Model.IndexResponse;
import in.games.GamesSattaBets.Model.MatkaModel;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.ConnectivityReceiver;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.SessionMangement;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_Matka;
import static in.games.GamesSattaBets.Activity.SplashActivity.max_bet_amount;
import static in.games.GamesSattaBets.Activity.SplashActivity.min_bet_amount;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private final String TAG= HomeFragment.class.getSimpleName();
SwipeRefreshLayout swipe;
LinearLayout lin_home,lin_whatsapp,lin_whatsapp2,lin_play;
CircleImageView civ_logo;
LinearLayout lin_satarline,lin_newUpdate,linTelegram;
LoadingBar loadingBar;
TextView tv_whatsapp,tv_whatsapp2,tv_msg,jackpot_message;
Button btn_starline,btn_jackpot,btn_addFund,btn_withdrawFund;
RelativeLayout rel_matka;
RecyclerView rec_matka;
ArrayList<MatkaModel> mList;
MatkaAdapter matkaAdapter;
Module module;
String running_text;
float version_code ;
SessionMangement sessionMangement;
public String app_link="",home_text="",message="",whatsapp_msg="",whatsapp_no2="",whatsapp_no="",withdraw_no="",new_app_link="";
int ver_code;
String is_notification,download_link="";
int is_download,is_forced;
    String telegramLink="",status="";


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initView(root);

        if (ConnectivityReceiver.isConnected())
        {
            
            //                module.checkDeviceLogin();

            allMatkaGames ();
        }else {
            module.noInternet ();
        }
        //
        backPress(root);
        jackpot_message = root.findViewById(R.id.jackpot_message);

        module.getConfigData(new GetAppSettingData() {
            @Override
            public void getAppSettingData(IndexResponse model) {
                Log.e("running_text", "getAppSettingData: "+model.getHome_text() );
                tv_msg.setText (model.getHome_text());
                telegramLink=model.getApp_link();

                tv_whatsapp.setText (model.getMobile());
                running_text = model.getHome_text();
                home_text =model.getHome_text();
                message = model.getMessage();
                whatsapp_no = model.getMobile();
                withdraw_no = model.getWithdraw_no();
                jackpot_message.setText(Html.fromHtml(model.getHome_text())+"                       ");
                jackpot_message.setSelected(true);
                max_bet_amount = Integer.parseInt(module.checkNullNumber(model.getMax_bet_amount()));
                min_bet_amount = Integer.parseInt(module.checkNullNumber(model.getMin_bet_amt()));

                ver_code = Integer.parseInt(model.getVersion());
                app_link = model.getApp_link();
                new_app_link=model.getNew_app_link();
                is_notification=model.getIs_notification ();
                is_forced=Integer.parseInt (model.getIs_forced ());
                is_download=Integer.parseInt (model.getIs_in_update ());
                download_link=model.getDownload_link ();

                try {
                    PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                    version_code = pInfo.versionCode;
                    Log.e("Homefragment",""+ver_code+" - "+version_code);
                    if(version_code!=ver_code)
                    {
                        if(is_notification.equals("1"))
                        {
                            if (is_forced==2){
                                lin_newUpdate.setVisibility(View.VISIBLE);
                            }else {
                                lin_newUpdate.setVisibility(View.GONE);
                            }
                        }else {
                            lin_newUpdate.setVisibility(View.GONE);
                        }
                    }else{
                        lin_newUpdate.setVisibility(View.GONE);
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        module.getWalletAmount("main");

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing())
                {
                    if (ConnectivityReceiver.isConnected())
                    {   
                        ((MainActivity)getActivity()).callCommonApi();
                        allMatkaGames ();
                    }else {
                        module.noInternet ();
                    }
                    module.getWalletAmount("main");
                    swipe.setRefreshing(false);
                }
            }
        });

        return root;
    }

    public void backPress(View view)
    {
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity(),R.style.RightButtonsDialog);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure want to exit?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            //((MainActivity) getActivity()).finish();
                            getActivity().finishAffinity();

                        }
                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    final AlertDialog dialog=builder.create();
                    dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                        }
                    });
                    dialog.show();
                    return true;
                }
                return false;
            }
        });

    }

    private void initView(View root) {
        mList = new ArrayList<>();
        swipe = root.findViewById(R.id.swipe);
        lin_home = root.findViewById(R.id.lin_home);
        lin_satarline= root.findViewById(R.id.lin_satarline);
        lin_whatsapp = root.findViewById(R.id.lin_whatsapp);
        lin_play = root.findViewById(R.id.lin_play);
        civ_logo = root.findViewById(R.id.civ_logo);
        tv_whatsapp = root.findViewById(R.id.tv_whatsapp);
        tv_whatsapp.setOnClickListener (this);
        tv_msg = root.findViewById(R.id.tv_msg);
        btn_starline = root.findViewById(R.id.btn_starline);
        btn_jackpot = root.findViewById(R.id.btn_jackpot);
        rel_matka = root.findViewById(R.id.rel_matka);
        rec_matka = root.findViewById(R.id.rec_matka);
        btn_addFund = root.findViewById(R.id.btn_addFund);
        btn_withdrawFund = root.findViewById(R.id.btn_withdrawFund);
        lin_newUpdate = root.findViewById(R.id.lin_newUpdate);
        linTelegram = root.findViewById(R.id.linTelegram);
        linTelegram.setOnClickListener(this);
        btn_jackpot.setOnClickListener(this);
        btn_starline.setOnClickListener(this);
        lin_whatsapp.setOnClickListener(this);
        tv_whatsapp.setOnClickListener(this);
        lin_newUpdate.setOnClickListener(this);

        btn_withdrawFund.setOnClickListener(this);
        btn_addFund.setOnClickListener(this);
        lin_satarline.setOnClickListener(this);
        module=new Module (getContext ());
        loadingBar=new LoadingBar (getContext ());
        sessionMangement =new SessionMangement (getContext ());


        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Dialog dialog = new Dialog (getActivity());
                    dialog.requestWindowFeature (Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable (new ColorDrawable(0));
                    dialog.setContentView (R.layout.dialog_exit);
                    dialog.show();

                    TextView tv_msg;
                    RelativeLayout rel_ok, rel_close;

                    tv_msg = dialog.findViewById (R.id.tv_msg);
                    rel_ok = dialog.findViewById (R.id.rel_ok);
                    rel_close = dialog.findViewById (R.id.rel_close);

                    tv_msg.setText ("Are you sure want to exit?");

                    rel_ok.setOnClickListener (new View.OnClickListener ( ) {
                        @Override
                        public void onClick(View v) {
                           getActivity().finishAffinity();
                        }
                    });
                    rel_close.setOnClickListener (new View.OnClickListener ( ) {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss ( );
                        }
                    });

                    dialog.setCanceledOnTouchOutside (false);
                    return true;
                }
                return false;
            }
        });


    }
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        Fragment fragment = null;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        switch (v.getId())
      {
          case R.id.btn_jackpot:
              fragment = new JackpotGameFragment();

              fragmentManager.beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();

              break;

          case R.id.btn_starline:
               fragment = new HomeFragment();

              fragmentManager.beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();

              break;

          case R.id.btn_addFund:
              Intent intent=new Intent (getActivity (),AddFundFragment.class);
              startActivity (intent);
//              fragment = new AddFundFragment();
              break;

          case R.id.btn_withdrawFund:
              Intent intentq=new Intent (getActivity (),WithdrawalFundFragment.class);
              startActivity (intentq);
              //fragment = new WithdrawalFundFragment();
              break;
//
          case R.id.lin_satarline:
             // fragment = new StarlineGamesFragment ();
              Intent intetq=new Intent (getActivity (),StarlineGamesFragment.class);
              startActivity (intetq);
              break;

          case R.id.lin_whatsapp:
          case R.id.tv_whatsapp:
              showWhatsappDailoge(tv_whatsapp.getText ().toString ());
              break;
          case R.id.linTelegram:
                  module.intentT0TelegramId(telegramLink);

              break;

              
          case R.id.lin_newUpdate:
              if(is_download==1) {
                  //browser
                  Intent intent2 = new Intent(Intent.ACTION_VIEW);
                  intent2.setData(Uri.parse(download_link));
                  startActivity(intent2);
              }
              else{
                  inappUpdate();
              }
              break;




    }

    }

    private void showWhatsappDailoge(String num) {
        //Toast.makeText (getActivity (), ""+num, Toast.LENGTH_SHORT).show ( );
        final Dialog dialog = new Dialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();

        dialog.setContentView (R.layout.dailoge_whatsappcall);
        LinearLayout lin_d_call = dialog.findViewById(R.id.lin_d_call);
        LinearLayout lin_d_whasapp2 = dialog.findViewById(R.id.lin_d_whasapp2);
        ImageView iv_close = dialog.findViewById(R.id.iv_close);


        dialog.setCanceledOnTouchOutside (true);
        dialog.show ( );

        iv_close.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
         dialog.dismiss();
            }
        });
  lin_d_call.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                //module.whatsapp(whatsapp_no,"Hi! Admin");
                module.makePhoneCalls(num);
                dialog.dismiss ();

            }
        });

        lin_d_whasapp2.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                module.whatsapp(num,"Hi! Admin");
                dialog.dismiss ();
            }
        });



}


    private void allMatkaGames() {
        mList.clear();
        loadingBar.show ();
        HashMap<String,String> params = new HashMap<> (  );
        module.getRequest (URL_Matka, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                Log.e (TAG, "onResponse: "+response );


                try
                {
                    mList.clear();
                    JSONArray datay=new JSONArray(response);
                    for(int i=0; i<datay.length();i++) {

                        JSONObject jsonObject = datay.getJSONObject(i);
                        MatkaModel matkasObjects = new MatkaModel();
                        matkasObjects.setId(jsonObject.getString("id"));
                        matkasObjects.setName(jsonObject.getString("name"));
                        matkasObjects.setStart_time(jsonObject.getString("start_time"));
                        matkasObjects.setEnd_time(jsonObject.getString("end_time"));
                        matkasObjects.setStarting_num(jsonObject.getString("starting_num"));
                        matkasObjects.setNumber(jsonObject.getString("number"));
                        matkasObjects.setEnd_num(jsonObject.getString("end_num"));
//                            matkasObjects.setCreated_at(jsonObject.getString("created_at"));
//                            matkasObjects.setUpdated_at(jsonObject.getString("updated_at"));
                        matkasObjects.setStatus(jsonObject.getString("status"));
                        matkasObjects.setOpen_result_time(jsonObject.getString("open_result_time"));
                        matkasObjects.setClose_result_time(jsonObject.getString("close_result_time"));
                        matkasObjects.setColor (jsonObject.getString ("highlight"));
                        matkasObjects.setIs_market_open (jsonObject.getString ("is_market_open"));
                        matkasObjects.setIs_market_open_nextday (jsonObject.getString ("is_market_open_nextday"));
                        matkasObjects.setIs_market_open_nextday2 (jsonObject.getString ("is_market_open_nextday2"));
                        // matkasObjects.setLoader(jsonObject.getString("loader"));
                        // matkasObjects.setText(jsonObject.getString("text"));
                        //matkasObjects.setText_status(jsonObject.getString("text_status"));
                        mList.add(matkasObjects);
                    }
                    if(mList.size()>0)
                    {
                        rec_matka.setLayoutManager(new GridLayoutManager (getActivity (),1));
                        matkaAdapter = new MatkaAdapter(getActivity(),mList);
                        rec_matka.setAdapter(matkaAdapter);
                        matkaAdapter.notifyDataSetChanged ();
                    }
                    loadingBar.dismiss();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    loadingBar.dismiss();
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                module.showVolleyError(error);
                loadingBar.dismiss ();
            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();
        allMatkaGames ();
        module.getWalletAmount("main");
    }

    private void inappUpdate() {
        try {
            Log.e("app updating", "updating");
            module.customToast("Updating App...");
            Log.e("updating",new_app_link);
            DownloadApk downloadApk = new DownloadApk(getActivity());
            downloadApk.startDownloadingApk(new_app_link);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }


}