package in.games.GamesSattaBets.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.games.GamesSattaBets.Adapter.SelectGameAdapter;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Model.SelectGameModel;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.LoadingBar;


public class SelectGameActivity extends AppCompatActivity {
    RecyclerView rec_selectGame;
    ArrayList<SelectGameModel> sList, starline_list;
    SelectGameAdapter selectGameAdapter;
    SwipeRefreshLayout swipe;
    Module module;
    LoadingBar loadingBar;
    ImageView img_back;
    TextView tv_title,tv_wallet;
    LinearLayout lin_wallet;
    String market_status="",matka_name = "", game_id, game_name, matka_id = "",
            start_time = "", end_time = "", s_num = "", num = "", e_num = "", type = "",is_market_open_nextday="",is_market_open_nextday2="";
    Context context = SelectGameActivity.this;
    public SelectGameActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game);

        module = new Module (context);
        loadingBar = new LoadingBar (context);

        matka_name = getIntent ( ).getStringExtra ("matka_name");
        matka_id = getIntent ( ).getStringExtra ("m_id");
        start_time = getIntent ( ).getStringExtra ("start_time");
        end_time = getIntent ( ).getStringExtra ("end_time");
        s_num = getIntent ( ).getStringExtra ("s_num");
        num = getIntent ( ).getStringExtra ("num");
        e_num = getIntent ( ).getStringExtra ("e_num");
        type = getIntent ( ).getStringExtra ("type");
        market_status=getIntent ().getStringExtra ("market_status");
        is_market_open_nextday=getIntent ().getStringExtra ("is_market_open_nextday");
        is_market_open_nextday2=getIntent ().getStringExtra ("is_market_open_nextday2");
        module.getWalletAmount("game");

        sList = new ArrayList<> ( );
        rec_selectGame = findViewById (R.id.rec_selectGame);
        swipe = findViewById (R.id.swipe);
        tv_title = findViewById(R.id.tv_title);
        img_back = findViewById(R.id.img_back);
        tv_wallet = findViewById(R.id.tv_wallet);
        lin_wallet = findViewById(R.id.lin_wallet);

        if (Integer.parseInt(matka_id)<20) {
            setGameTitle(matka_name);
        } else if (Integer.parseInt(matka_id)>20 && Integer.parseInt(matka_id)<100){
            setGameTitle("Starline Games"+"("+matka_name+")");
        }else {
            setGameTitle(matka_name);
        }

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.game_frame);
                if(fragment.getClass().getSimpleName()!=null){
                    String frgmentName = fragment.getClass().getSimpleName();
                    module.sessionOut();
                    module.checkDeviceLogin();
                    Log.e("fragment",frgmentName);
                    tv_title.setVisibility(View.VISIBLE);
                    img_back.setVisibility(View.VISIBLE);
                    lin_wallet.setVisibility(View.VISIBLE);
                    if (frgmentName.contains("SelectGameFragment"))
                    {
                        img_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });
                    }else {
                        img_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                            }
                        });
                    }
                    module.getWalletAmount("game");
                }
            }
        });

        initControl();
    }

    @Override
    protected void onResume() {
        super.onResume ( );
        initControl ( );
    }

    public void initControl() {
        if (getSupportFragmentManager ( ).getBackStackEntryCount ( ) == 0) {
            OpenFragment ( );
            loadingBar.dismiss ( );

        }
    }
    public void setGameTitle(String title){
        tv_title.setText(title);
        tv_title.setSelected(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    public String getGameWallet()
    {
        String s = tv_wallet.getText().toString().trim();
        return s;
    }
    public void setGameWalletAmount(String wallet) {
        try {
            tv_wallet.setText(wallet);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    public void OpenFragment() {


        Bundle bundle = new Bundle();
        Fragment fragment = new SelectGameFragment();
        bundle.putString("matka_name", getIntent ( ).getStringExtra ("matka_name"));
        bundle.putString ("m_id",getIntent ( ).getStringExtra ("m_id"));
        bundle.putString("s_num",getIntent ( ).getStringExtra ("s_num"));
        bundle.putString("num",getIntent ( ).getStringExtra ("num"));
        bundle.putString("e_num",getIntent ( ).getStringExtra ("e_num"));
        bundle.putString("end_time",getIntent ( ).getStringExtra ("end_time"));
        bundle.putString("start_time",getIntent ( ).getStringExtra ("start_time"));
        bundle.putString("type",getIntent ( ).getStringExtra ("type"));
        bundle.putString ("market_status",getIntent ().getStringExtra ("market_status"));
        bundle.putString ("is_market_open_nextday",getIntent ().getStringExtra ("is_market_open_nextday"));
        bundle.putString ("is_market_open_nextday2",getIntent ().getStringExtra ("is_market_open_nextday2"));



        Log.e("sxdfgbh",getIntent ().getStringExtra ("is_market_open_nextday"));
        Log.e("sxdfgbh",getIntent ().getStringExtra ("is_market_open_nextday2"));

        Log.e("SelectGameActivity",bundle.toString());
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add (R.id.game_frame,fragment).addToBackStack(null).commit();


    }
    }
