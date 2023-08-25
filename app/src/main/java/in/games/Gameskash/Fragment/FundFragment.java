package in.games.Gameskash.Fragment;

import static in.games.Gameskash.Config.BaseUrls.URL_REGISTER;
import static in.games.Gameskash.Config.Constants.KEY_ACCOUNNO;
import static in.games.Gameskash.Config.Constants.KEY_ID;
import static in.games.Gameskash.Config.Constants.KEY_IFSC;
import static in.games.Gameskash.Config.Constants.KEY_MOBILE;
import static in.games.Gameskash.Config.Constants.KEY_PAYTM;
import static in.games.Gameskash.Config.Constants.KEY_PHONEPAY;
import static in.games.Gameskash.Config.Constants.KEY_TEZ;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.games.Gameskash.Activity.AddBankAccountActivity;
import in.games.Gameskash.Activity.MainActivity;
import in.games.Gameskash.Adapter.FundAdapter;
import in.games.Gameskash.Config.Module;
import in.games.Gameskash.Model.FundModel;
import in.games.Gameskash.R;
import in.games.Gameskash.Util.LoadingBar;
import in.games.Gameskash.Util.RecyclerTouchListener;
import in.games.Gameskash.Util.SessionMangement;

public class FundFragment extends Fragment implements View.OnClickListener {
LinearLayout lin_addFund,lin_withdrawFund;
RecyclerView rec_fund;
FundAdapter adapter;
ArrayList<FundModel> modellist;
SessionMangement sessionMangement;
LoadingBar loadingBar;
public static String my_error="";
Module module;
String ifsc_code,mobile, paytm_number, account_number, phonePay_number, googlePay_number, address, city, pincode, bank_name, acc_holder_name;

    public FundFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fund, container, false);
        ((MainActivity)getActivity()).setTitles("Fund");
        my_error="";
        initView(view);
        initRecyclerview();
        return view;
    }


    private void initView(View view) {
        loadingBar = new LoadingBar(getContext());
        module = new Module(getContext());
        sessionMangement = new SessionMangement (getActivity ( ));

        mobile = sessionMangement.getUserDetails ( ).get (KEY_MOBILE);
        googlePay_number = sessionMangement.getUserDetails ( ).get (KEY_TEZ);
        phonePay_number = sessionMangement.getUserDetails ( ).get (KEY_PHONEPAY);
        account_number = sessionMangement.getUserDetails ( ).get (KEY_ACCOUNNO);
        paytm_number = sessionMangement.getUserDetails ( ).get (KEY_PAYTM);
        ifsc_code = sessionMangement.getUserDetails ( ).get (KEY_IFSC);

        lin_addFund = view.findViewById(R.id.lin_addFund);
        lin_withdrawFund = view.findViewById(R.id.lin_withdrawFund);
        rec_fund= view.findViewById(R.id.rec_fund);
        rec_fund.setHasFixedSize(true);
       rec_fund.setLayoutManager(new GridLayoutManager(getActivity (),2));
        lin_withdrawFund.setOnClickListener(this);
        lin_addFund.setOnClickListener(this);
    }
    private void initRecyclerview() {

        modellist= new ArrayList<>();
        modellist.add(new FundModel(R.drawable.ic_baseline_add_24,"Add Fund"));
        modellist.add(new FundModel(R.drawable.withdraw_fund,"Withdraw Funds"));
        modellist.add(new FundModel(R.drawable.historybook,"Fund Request History"));
        modellist.add(new FundModel(R.drawable.statement,"Account Statements"));
        modellist.add(new FundModel(R.drawable.bank,"Add Bank Account"));

        adapter= new FundAdapter(getActivity(),modellist);
        rec_fund.setAdapter(adapter);

        GridLayoutManager glm = new GridLayoutManager(getActivity(), 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
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

        rec_fund.setLayoutManager (glm);
        rec_fund.addOnItemTouchListener (new RecyclerTouchListener (getActivity ( ), rec_fund, new RecyclerTouchListener.OnItemClickListener ( ) {
            @Override
            public void onItemClick(View view, int position) {
                FundModel model = modellist.get(position);
                String title = modellist.get(position).getTitle();
                Fragment fragment = null;
                Bundle args = new Bundle();
                Intent intent = null;

                switch (title) {

                    case "Add Fund":
                         intent=new Intent (getActivity (),AddFundFragment.class);
                        break;

                    case "Withdraw Funds":
                         intent = new Intent (getActivity (),WithdrawalFundFragment.class);
                        break;

                    case "Fund Request History":
                         intent = new Intent(getActivity(),WithdrawFundHistoryFragment.class);
                        intent.putExtra("name","a");
                        intent.putExtra ("status","");
                        break;

                    case "Account Statements":
                        intent = new Intent(getActivity(),WithdrawFundHistoryFragment.class);
                        intent.putExtra("name","w");
                        intent.putExtra ("status","");
                        break;

                    case "Add Bank Account":
                       // changeBankDetail();
//                        fragment = new ProfileFragment();
                        Intent in_add= new Intent(getActivity(), AddBankAccountActivity.class);
                        startActivity(in_add);
                        break;
                }
//             fragment.setArguments(bundle)
                if (fragment!=null){
                   FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();

                }
                if (intent!=null){
                    startActivity (intent);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    private void storeBankDetails(final String accno, final String bankname, final String ifsc, final String hod_name, final String mailid,String bra) {

        loadingBar.show ( );
        HashMap<String, String> params = new HashMap<> ( );
        params.put ("key", "3");
//        params.put("mobile",mailid);
        params.put ("mobile", mobile);
        params.put ("user_id", sessionMangement.getUserDetails ( ).get (KEY_ID));
        params.put ("accountno", accno);
        params.put ("bankname", bankname);
        params.put ("ifsc", ifsc);
        params.put ("accountholder", hod_name);

        module.postRequest (URL_REGISTER, params, new Response.Listener<String> ( ) {
            @Override
            public void onResponse(String response) {
                loadingBar.dismiss ( );
                try {
                    JSONObject jsonObject = new JSONObject (response);
                    boolean resp = jsonObject.getBoolean ("responce");
                    if (resp) {
                        sessionMangement.updateAccSection (accno, bankname, ifsc, hod_name,bra);
//                        common.showToast(""+response.getString("message"));
                         module.successToast (getContext(),"" + jsonObject.getString ("message"));
                        onStart ( );
                    } else {
                        module.errorToast (getContext(),"" + jsonObject.getString ("error"));
                    }

                } catch (Exception ex) {
                    ex.printStackTrace ( );
                    module.showToast ("wrong");
                }
            }
        }, new Response.ErrorListener ( ) {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss ( );
                module.showVolleyError (error);
            }
        });

    }


    @Override
    public void onClick(View v) {
        Fragment fragment = null;

        switch (v.getId())
        {

            case R.id.lin_addFund:
                Intent intent=new Intent (getActivity (),AddFundFragment.class);
                startActivity (intent);
//                fragment = new AddFundFragment();
                break;
            case R.id.lin_withdrawFund:
                Intent intentq=new Intent (getActivity (),WithdrawalFundFragment.class);
                startActivity (intentq);
                break;
        }
        if (fragment!=null)
        {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume ( );
        my_error="";
        module.getWalletAmount("main");
    }

}