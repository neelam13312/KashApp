package in.games.Gameskash.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import in.games.Gameskash.Activity.MainActivity;
import in.games.Gameskash.Config.Module;
import in.games.Gameskash.R;
import in.games.Gameskash.Util.LoadingBar;
import in.games.Gameskash.Util.SessionMangement;

public class AccountStatementtFragment extends Fragment implements View.OnClickListener {
RelativeLayout lin_withdrawFundHistory,lin_addFundHistory;
LoadingBar loadingBar;
Module module;
SessionMangement sessionMangement;
    public AccountStatementtFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_account_statementt, container, false);
        ((MainActivity)getActivity()).setTitles("Account Statement");

        loadingBar = new LoadingBar(getActivity());
        sessionMangement = new SessionMangement(getActivity());
        module = new Module(getActivity());
        lin_withdrawFundHistory = root.findViewById(R.id.lin_withdrawFundHistory);
        lin_addFundHistory = root.findViewById(R.id.lin_addFundHistory);
        lin_addFundHistory.setOnClickListener(this);
        lin_withdrawFundHistory.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        Fragment fm = null;
        Bundle args = new Bundle();
        switch (v.getId())
        {
            case R.id.lin_withdrawFundHistory:
               Intent intent = new Intent(getActivity(),WithdrawFundHistoryFragment.class);
                intent.putExtra("name","w");
                intent.putExtra ("status","");
                startActivity(intent);
                break;
            case R.id.lin_addFundHistory:
                Intent intent1 = new Intent(getActivity(),WithdrawFundHistoryFragment.class);
                intent1.putExtra("name","a");
                intent1.putExtra ("status","");
                startActivity(intent1);
                break;
        }

        if (fm!=null)
        {
            fm.setArguments(args);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame,fm).addToBackStack(null).commit();

        }
    }




}