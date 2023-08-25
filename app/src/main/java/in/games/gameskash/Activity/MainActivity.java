package in.games.gameskash.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.gameskash.Adapter.MenuAdapter;
import in.games.gameskash.Config.Module;
import in.games.gameskash.Fragment.AccountStatementtFragment;
import in.games.gameskash.Fragment.AllHistoryActivity;
import in.games.gameskash.Fragment.GameRateFragment;
//import in.games.GamesSattaBet.Fragment.HistoryFragment;
import in.games.gameskash.Fragment.HomeFragment;
import in.games.gameskash.Fragment.MyBidsFragment;
import in.games.gameskash.Fragment.NoticeboardFragment;
import in.games.gameskash.Fragment.NotificationFragment;
import in.games.gameskash.Fragment.SettingFragment;
import in.games.gameskash.Interfaces.GetAppSettingData;
import in.games.gameskash.Model.IndexResponse;
import in.games.gameskash.Model.MenuModel;
import in.games.gameskash.R;
import in.games.gameskash.Util.LoadingBar;
import in.games.gameskash.Util.RecyclerTouchListener;
import in.games.gameskash.Util.SessionMangement;

import static in.games.gameskash.Config.BaseUrls.URL_GET_STATUS;
import static in.games.gameskash.Config.BaseUrls.URL_UNSET_TOKE;
import static in.games.gameskash.Config.Constants.KEY_ID;
import static in.games.gameskash.Config.Constants.KEY_MOBILE;
import static in.games.gameskash.Config.Constants.KEY_NAME;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dcastalia.localappupdate.DownloadApk;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar, toolbar2;
    SessionMangement sessionMangement;
    TextView tv_title, tv_wallet, tv_title2, tv_mob;
    RecyclerView rec_menu;
    ImageView img_notification;
    ArrayList<MenuModel> mList;
    MenuAdapter menuAdapter;
//    FloatingActionButton fab;
    LinearLayout lin_notification, lin_wallet;
    Module module;
    TextView tv_name, tv_ver;
    LoadingBar loadingBar;
    public static int starline_id;
    ImageView img_back, img_back2;
    RelativeLayout tool_img_logo;
    BottomNavigationView bottomNavigation;
    RelativeLayout rel_title;
    String Chart_link = "";
    float version_code;
    int ver_code;
    int is_download, is_forced;
    public String new_app_link = "", app_link;
    String is_notification = "", download_link = "", chat_msg = "", chat_mobile = "";
    public static String chat_status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
      //  getMobileData();
        module.getConfigData(new GetAppSettingData() {
            @Override
            public void getAppSettingData(IndexResponse model) {

                chat_msg = model.getChat_msg();
                chat_mobile = model.getChat_mobile();
                chat_status = model.getChat_status();
                ver_code = Integer.parseInt(model.getVersion());
                app_link = model.getApp_link();
                new_app_link = model.getNew_app_link();
                is_notification = model.getIs_notification();
                is_forced = Integer.parseInt(model.getIs_forced());
                is_download = Integer.parseInt(model.getIs_in_update());
                download_link = model.getDownload_link();

                try {
                    PackageInfo pInfo = MainActivity.this.getPackageManager().getPackageInfo(MainActivity.this.getPackageName(), 0);
                    version_code = pInfo.versionCode;
                    Log.e("Homefragment", "" + ver_code + " - " + version_code);
                    if (version_code != ver_code) {
                        if (is_notification.equals("1")) {
                            showUpdateDialog();
                        }
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                getMenu();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(R.color.colorAccent));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        module = new Module(MainActivity.this);
        loadingBar = new LoadingBar(MainActivity.this);
        mList = new ArrayList<>();
        tv_name = findViewById(R.id.tv_name);
        tv_mob = findViewById(R.id.tv_mob);
        img_back = findViewById(R.id.img_back);
        tv_title = findViewById(R.id.tv_title);
        tool_img_logo = findViewById(R.id.tool_img_logo);
//        fab = findViewById(R.id.fab);
        drawer = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        rec_menu = findViewById(R.id.rec_menu);
        tv_ver = findViewById(R.id.tv_ver);

        lin_notification = findViewById(R.id.lin_notification);
        tv_wallet = findViewById(R.id.tv_wallet);
        lin_wallet = findViewById(R.id.lin_wallet);
        img_notification = findViewById(R.id.img_notification);
        rel_title = findViewById(R.id.rel_title);
        img_notification.setOnClickListener(this);
        sessionMangement = new SessionMangement(this);
        sessionMangement.setnumValue("", "", "", "", "", "", "");

        tv_name.setText(sessionMangement.getUserDetails().get(KEY_NAME));
        tv_mob.setText(sessionMangement.getUserDetails().get(KEY_MOBILE));
        PackageManager pm = getApplicationContext().getPackageManager();
        String pkgName = getApplicationContext().getPackageName();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String ver = pkgInfo.versionName;

        tv_ver.setText("App Version : " + ver);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigation);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment selectedFragment = new HomeFragment();
//                getSupportFragmentManager().beginTransaction().add(R.id.frame,
//                        selectedFragment).addToBackStack(null).commit();
//            }
//        });
        module.getConfigData(new GetAppSettingData() {
            @Override
            public void getAppSettingData(IndexResponse model) {
                starline_id = Integer.parseInt(model.getStarline_id());
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        module.getWalletAmount("main");
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.frame, fragment).addToBackStack(null).commit();

        toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, R.string.drawer_open, R.string.drawer_close);
        toggle.getDrawerArrowDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        setTitles(MainActivity.this.getResources().getString(R.string.app_name));
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame);
                String frgmentName = fragment.getClass().getSimpleName();
                Log.e("fragment", frgmentName);
//               module.checkDeviceLogin();

                if (frgmentName.contains("HomeFragment")) {
                    rel_title.setVisibility(View.GONE);
                    tool_img_logo.setVisibility(View.VISIBLE);
                    img_back.setVisibility(View.GONE);
                    tv_title.setVisibility(View.GONE);
                    getSupportActionBar().setDisplayShowHomeEnabled(false);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    setTitles(MainActivity.this.getResources().getString(R.string.app_name));
                    toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
                    toggle.getDrawerArrowDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                    drawer.setDrawerListener(toggle);
                    toggle.syncState();
                    getSupportActionBar().setHomeButtonEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_sort_24);
//                    getSupportActionBar().setBackgroundDrawable(R.drawable.bg_circle_red);
                } else {
                    rel_title.setVisibility(View.VISIBLE);
                    tv_title.setVisibility(View.VISIBLE);
                    tool_img_logo.setVisibility(View.GONE);
                    img_back.setVisibility(View.VISIBLE);
                    toggle.setDrawerIndicatorEnabled(false);
                    toggle.syncState();


                    if (frgmentName.contains("FundFragment") || frgmentName.contains("MyBidsFragment")) {
                        rel_title.setVisibility(View.VISIBLE);
                        img_back.setVisibility(View.GONE);
                        tv_title.setVisibility(View.VISIBLE);
                        toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
                        toggle.getDrawerArrowDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                        drawer.setDrawerListener(toggle);
                        toggle.syncState();
                        getSupportActionBar().setHomeButtonEnabled(true);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_sort_24);
                    }
                }
            }
        });


        rec_menu.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//        getMenu();
        rec_menu.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, rec_menu, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String title = mList.get(position).getTitle();
                Fragment fm = null;
                Bundle args = new Bundle();

                switch (title) {
                    case "Home":
                        Fragment selectedFragment = new HomeFragment();
                        getSupportFragmentManager().beginTransaction().add(R.id.frame,
                                selectedFragment).addToBackStack(null).commit();
                        break;

                    case "My Profile":
                        fm = new AccountStatementtFragment();
                        break;

//                    case "Alert":
//                        Intent intents = new Intent(MainActivity.this, NotificationFragment.class);
//                        startActivity(intents);
//                        break;
//                    case "My Bids":
//                        fm = new MyBidsFragment();
//                        break;
//                    case "MPIN":
//                        fm = new GenerateMPIN_Fragment();
//                        break;
//
////                        case "My History":
////                            fm=new HistoryFragment ();
////                            break;
//                    case "Funds":
//                        fm = new FundFragment();
//                        break;
//                    case "Videos":
//                        fm = new VedioLanguageFragment();
//                        break;

                    case "Account Statement":
                        fm = new AccountStatementtFragment();
                        break;

//                    case "Support":
//                        if (chat_status.equals("0")) {
//                            Intent in_support = new Intent(MainActivity.this, SupportActivity.class);
//                            startActivity(in_support);
//                        } else if (chat_status.equals("1")) {
//                            Intent support = new Intent(MainActivity.this, WhatsappChatSupportActivity.class);
//                            startActivity(support);
//                        } else {
//                            module.whatsapp(chat_mobile, chat_msg);
//                        }
//
//                        break;

                    case "Notification":
                        Intent intent = new Intent(MainActivity.this, NotificationFragment.class);
                        startActivity(intent);

                        break;
//                        case "How to Play":
//                            fm = new HowtoPlayFragment ();
//                            break;
                    case "Game Rates":
                        fm = new GameRateFragment();
                        break;

                        case "Notice Board /Rules":
                            fm = new NoticeboardFragment();
                            break;

                    case "Setting":
                        fm = new SettingFragment();
                        break;
//                    case "Charts":
//                        Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(Chart_link));
//                        startActivity(intent1);
//
//                        break;
//                    case "Share Application":
//
//                        String message = "I'm loving Satta Bets App\n\nDownload App now\n\nFrom:-\n" + share_link;
//                        Intent share = new Intent(Intent.ACTION_SEND);
//                        share.setType("text/plain");
//                        share.putExtra(Intent.EXTRA_TEXT, message);
//
//                        startActivity(Intent.createChooser(share, "Title of the dialog the system will open"));
//
//                        break;
                    case "Logout":
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.RightButtonsDialog);
                        builder.setTitle("Logout");
                        builder.setMessage("Are you sure you want to logout ?");
                        builder.setCancelable(false);
                        builder.create();
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                 // getStatus();
//                                    unSetToken();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        final AlertDialog dialog = builder.create();
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                                dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                            }
                        });
                        dialog.show();
                        break;

                }

                if (fm != null) {
                    setTitles(title);
                    args.putString("title", title);
                    fm.setArguments(args);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame, fm).addToBackStack(null).commit();

                }

                drawer.closeDrawer(GravityCompat.START);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    public void callCommonApi() {
        module.getConfigData(new GetAppSettingData() {
            @Override
            public void getAppSettingData(IndexResponse model) {
                chat_status = model.getChat_status();
                Log.e("fvghj", chat_msg);
                getMenu();
            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigation =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.bottom_my_bids:
                          Intent  intent=new Intent ( MainActivity.this, AllHistoryActivity.class );
                            intent.putExtra ("name","mList.get(position).getName()");
                            intent.putExtra ("type","matka");
                            intent.putExtra ("matka_id","0");
                            startActivity(intent);

                            break;
                   case R.id.bottom_profile:
                       Intent intent_bank = new Intent(MainActivity.this, AddBankAccountActivity.class);
                       startActivity(intent_bank);

                            break;

                        case R.id.bottom_passbook:
                            selectedFragment = new AccountStatementtFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame,
                                    selectedFragment).addToBackStack(null).commit();
                            //selectedFragment = new PassbookFragment();
                            break;

                        case R.id.bottom_funds:
                            selectedFragment = new MyBidsFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame,
                                    selectedFragment).addToBackStack(null).commit();
                            break;
//                        case R.id.bottom_share:
//                            String message = "I'm loving Satta Bets App\n\nDownload App now\n\nFrom:-\n" + share_link;
//                            Intent share = new Intent(Intent.ACTION_SEND);
//                            share.setType("text/plain");
//                            share.putExtra(Intent.EXTRA_TEXT, message);
//
//                            startActivity(Intent.createChooser(share, "Title of the dialog the system will open"));
//                            break;

                        case R.id.bottom_support:

                            if (chat_status.equals("0")) {
                                Intent in_support = new Intent(MainActivity.this, SupportActivity.class);
                                startActivity(in_support);
                            } else if (chat_status.equals("1")) {
                                Intent support = new Intent(MainActivity.this, WhatsappChatSupportActivity.class);
                                startActivity(support);
                            } else {
                                module.whatsapp(chat_mobile, chat_msg);
                            }
                            break;
                    }

                    return true;
                }
            };

    private void getStatus() {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("token", sessionMangement.getToken());
        params.put("device_id", sessionMangement.getDeviceId());
        Log.e("wsderf", params.toString());


        module.postRequest(URL_GET_STATUS, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("URL_GET_STATUS", response);
                try {
                    loadingBar.dismiss();
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("response")) {
                        JSONArray array = object.getJSONArray("data");

                        JSONObject obj = array.getJSONObject(0);
                        String is_mpin = obj.getString("is_mpin");
                        String is_pass = obj.getString("is_password");

                        if (SplashActivity.sessionCountDownTimer != null) {
                            SplashActivity.sessionCountDownTimer.cancel();
                        }

                        unSetToken();
//                        Intent intent= null;
//                        if(is_mpin.equalsIgnoreCase ("1"))
//                        {
////                            sessionMangement.logoutSession ();
//                            if ( sessionMangement.isLoggedInSuccess()) {
//                                intent = new Intent(MainActivity.this, MpinLoginActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                finish();
//                            }else {
//                                intent = new Intent(MainActivity.this,LoginActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                finish();
//                            }
////                            intent = new Intent(MainActivity.this,MpinLoginActivity.class);
////                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                            startActivity(intent);
////                            finish();
//                        }
//                        else
//                        {
////                            sessionMangement.logoutSession ();
//                            intent = new Intent(MainActivity.this,LoginActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            finish();
//                        }
                    } else {

//                         module.errorToast (MainActivity,"Something Went Wrong");
                    }

                } catch (JSONException e) {
                    loadingBar.dismiss();
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                module.VolleyErrorMessage(error);
            }
        });
    }

    private void unSetToken() {
        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("mobileno", sessionMangement.getUserDetails().get(KEY_MOBILE));
        params.put("user_id", sessionMangement.getUserDetails().get(KEY_ID));
        module.postRequest(URL_UNSET_TOKE, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("URL_UNSET_TOKE", response);
                try {
                    JSONObject object = new JSONObject(response);
                    loadingBar.dismiss();
                    if (object.getBoolean("responce")) {
                        Intent intent = new Intent(MainActivity.this, NewLoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                         module.errorToast (MainActivity.this,"Something Went Wrong");
                    }

                } catch (JSONException e) {
                    loadingBar.dismiss();
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                module.VolleyErrorMessage(error);
            }
        });
    }

    void getMenu() {
        mList.clear ();
        mList.add (new MenuModel (  "Home",R.drawable.home));
        mList.add (new MenuModel (  "My Profile",R.drawable.profile));
        mList.add (new MenuModel (  "My History",R.drawable.history));
        mList.add (new MenuModel (  "Top Winner",R.drawable.wallet));
        mList.add (new MenuModel (  "Account Statement",R.drawable.statement));
        mList.add (new MenuModel (  "Notification",R.drawable.notification));
        mList.add (new MenuModel (  "Game Rates",R.drawable.rate));
        mList.add (new MenuModel (  "Notice Board /Rules",R.drawable.notice));
        mList.add (new MenuModel (  "Setting",R.drawable.settings));
        mList.add (new MenuModel (  "Logout",R.drawable.signout));

        if (mList.size()>0) {
            menuAdapter = new MenuAdapter(MainActivity.this, mList);
            rec_menu.setAdapter(menuAdapter);
        }

    }

    public void setTitles(String str) {
        tv_title.setText(str);
        tv_title.setSelected(true);
    }

    public void setWallet_Amount(String wallet) {
        try {
            tv_wallet.setText(wallet);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public String getWallet()
    {
        String s = tv_wallet.getText().toString().trim();
        return s;
    }

    @Override
    public void onClick(View v) {
        if(v.getId ()==R.id.img_notification){

            Intent intent=new Intent (MainActivity.this,NotificationFragment.class);
            startActivity (intent);
        }
    }

//   public void getMobileData()
//   {
//       HashMap<String,String> params = new HashMap<String, String> ( );
//       module.postRequest (URL_CHART, params, new Response.Listener<String> ( ) {
//           @Override
//           public void onResponse(String response) {
//               Log.e ("URL_CHART", "onResponse: " + response.toString ( ));
//               try {
//                   JSONObject jsonObject = new JSONObject (response);
//                   Chart_link= jsonObject.getString ("link");
//
//               } catch (JSONException e) {
//                   e.printStackTrace ( );
//               }
//           }
//       }, new Response.ErrorListener ( ) {
//           @Override
//           public void onErrorResponse(VolleyError error) {
//               String msg = module.VolleyErrorMessage (error);
//               if (!msg.isEmpty ( )) {
//                   module.errorToast (MainActivity.this,"" + msg);
//               }
//           }
//       });
//   }
    void showUpdateDialog()
    {
        final Dialog dialog=new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogue_updation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable (0));
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width= WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        Button btnCancel = dialog.findViewById(R.id.cancel);
        Button btnUpdate = dialog.findViewById(R.id.update);

        if(is_forced==0)
        {
         btnCancel.setText ("Update Later");

        }
        else if (is_forced == 1)
        {
            btnCancel.setText ("No Thanks");

        }


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(is_forced==0)
                {
                    dialog.dismiss();

                }else if(is_forced==1){
                    dialog.dismiss();
                    finishAffinity ();
                }
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(is_download==1) {
                    //browser
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(download_link));
                    startActivity(intent);
                }
                else{
                    inappUpdate();
                }
            }
        });

        dialog.setCanceledOnTouchOutside (false);
        dialog.setCancelable (false);
        dialog.show();
        if (is_forced==2){
            dialog.dismiss();
        }
    }

    private void inappUpdate() {
        try {
            Log.e("app updating", "updating");
            module.customToast("Updating App...");
            Log.e("updating",new_app_link);
            DownloadApk downloadApk = new DownloadApk(MainActivity.this);
            downloadApk.startDownloadingApk(new_app_link);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

}